package per.yun.onepick.pipeline

import jakarta.annotation.PostConstruct
import jakarta.annotation.PreDestroy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import per.yun.onepick.domain.model.PopularKeyword
import per.yun.onepick.domain.model.Product
import per.yun.onepick.domain.model.SearchLog
import per.yun.onepick.engine.antiviral.AntiViralEngine
import per.yun.onepick.engine.search.DeterministicSearchEngine
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentLinkedDeque
import java.util.concurrent.atomic.AtomicLong

@Component
class AsyncDataPipeline(
    private val antiViralEngine: AntiViralEngine,
    private val searchEngine: DeterministicSearchEngine
) {
    private val log = LoggerFactory.getLogger(AsyncDataPipeline::class.java)
    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    // 검색 로그 큐 (비동기 처리)
    private val searchLogChannel = Channel<SearchLog>(capacity = 1000)
    private val recentLogs = ConcurrentLinkedDeque<SearchLog>()
    private val keywordCounts = ConcurrentHashMap<String, AtomicLong>()
    private val keywordTotalLatency = ConcurrentHashMap<String, Double>()

    private val maxLogsHistory = 100

    @PostConstruct
    fun startWorker() {
        scope.launch {
            for (logItem in searchLogChannel) {
                processSearchLog(logItem)
            }
        }
        log.info("AsyncDataPipeline worker started.")
    }

    @PreDestroy
    fun stopWorker() {
        searchLogChannel.close()
        scope.cancel()
        log.info("AsyncDataPipeline worker stopped.")
    }

    fun recordSearch(
        query: String,
        latencyMs: Double,
        heroProductId: String?,
        heroProductName: String?,
        matchedTags: Set<String>
    ) {
        val searchLog = SearchLog(
            id = UUID.randomUUID().toString(),
            query = query,
            latencyMs = latencyMs,
            heroProductId = heroProductId,
            heroProductName = heroProductName,
            matchedTags = matchedTags
        )
        // non-blocking trySend
        searchLogChannel.trySend(searchLog)
    }

    private fun processSearchLog(logItem: SearchLog) {
        recentLogs.addFirst(logItem)
        while (recentLogs.size > maxLogsHistory) {
            recentLogs.pollLast()
        }

        val normalized = logItem.query.trim().lowercase()
        if (normalized.isNotBlank()) {
            keywordCounts.computeIfAbsent(normalized) { AtomicLong(0) }.incrementAndGet()
            keywordTotalLatency.merge(normalized, logItem.latencyMs) { old, v -> old + v }
        }
    }

    fun getRecentLogs(): List<SearchLog> = recentLogs.toList()

    fun getPopularKeywords(limit: Int = 10): List<PopularKeyword> {
        return keywordCounts.entries
            .sortedByDescending { it.value.get() }
            .take(limit)
            .map { entry ->
                val count = entry.value.get()
                val totalLatency = keywordTotalLatency[entry.key] ?: 0.0
                val avgLatency = if (count > 0) totalLatency / count else 0.0
                PopularKeyword(
                    keyword = entry.key,
                    count = count,
                    averageLatencyMs = Math.round(avgLatency * 100.0) / 100.0
                )
            }
    }

    /**
     * 비동기 AI 파이프라인:
     * 신규/수집 상품에 대해 안티 바이럴 검증, AI 태깅, 1줄 선정 사유를 생성하고 검색 인덱스를 갱신합니다.
     */
    fun processAndIndexBatch(rawProducts: List<Product>, sampleReviews: Map<String, List<String>> = emptyMap()) {
        val auditedProducts = rawProducts.map { product ->
            val reviews = sampleReviews[product.id] ?: emptyList()
            val auditReport = antiViralEngine.auditProduct(product, reviews)

            if (auditReport.isQuarantined) {
                product.copy(
                    viralScore = auditReport.viralScore,
                    isQuarantined = true,
                    quarantineReason = auditReport.explanation
                )
            } else {
                // AI 선정 사유 보강 (필요시 기본값 유지)
                val enrichedReasons = product.oneLineReasons.toMutableMap()
                if (enrichedReasons.isEmpty()) {
                    enrichedReasons["원픽"] = "${product.brand} ${product.name} - 실구매 불만율 ${product.complaintRate}% 및 만족도 ${product.realBuyerSentimentScore}점 독보적 1위"
                }
                product.copy(
                    viralScore = 0.0,
                    isQuarantined = false,
                    quarantineReason = null,
                    oneLineReasons = enrichedReasons
                )
            }
        }

        searchEngine.updateCatalog(auditedProducts)
        log.info("Batch indexing completed. Total: ${auditedProducts.size}")
    }
}
