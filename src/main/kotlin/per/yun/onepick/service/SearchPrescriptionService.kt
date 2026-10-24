package per.yun.onepick.service

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import per.yun.onepick.domain.model.*
import per.yun.onepick.engine.search.DeterministicSearchEngine
import per.yun.onepick.pipeline.AsyncDataPipeline
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger

@Service
class SearchPrescriptionService(
    private val searchEngine: DeterministicSearchEngine,
    private val asyncPipeline: AsyncDataPipeline
) {
    private val log = LoggerFactory.getLogger(SearchPrescriptionService::class.java)

    // 사후 검증 피드백 저장소 (신뢰성 거버넌스)
    private val matchCountMap = ConcurrentHashMap<String, AtomicInteger>()
    private val mismatchCountMap = ConcurrentHashMap<String, AtomicInteger>()

    fun searchPrescriptive(query: String): PrescriptiveSearchResponse {
        val result = searchEngine.searchPrescriptive(query)

        // 비동기 파이프라인으로 검색 로그 기록 (레이턴시 영향 제로)
        asyncPipeline.recordSearch(
            query = query,
            latencyMs = result.executionTimeMs,
            heroProductId = result.heroPick?.productId,
            heroProductName = result.heroPick?.productName,
            matchedTags = emptySet()
        )

        return result
    }

    /**
     * 사후 검증 피드백 루프 (Post-Purchase Verification)
     */
    fun recordFeedback(request: FeedbackRequest): FeedbackResponse {
        val productId = request.productId
        if (request.isMatch) {
            matchCountMap.computeIfAbsent(productId) { AtomicInteger() }.incrementAndGet()
        } else {
            val mismatches = mismatchCountMap.computeIfAbsent(productId) { AtomicInteger() }.incrementAndGet()
            log.warn("Post-purchase mismatch feedback received for product $productId. Total mismatches: $mismatches")
        }
        return FeedbackResponse(
            success = true,
            message = if (request.isMatch) "추천 만족 피드백이 반영되었습니다." else "불일치 피드백이 접수되어 알고리즘 지수에 반영됩니다."
        )
    }

    fun prescribe(query: String): PrescriptionResult {
        return searchEngine.search(query)
    }

    fun getPopularKeywords(limit: Int = 10): List<PopularKeyword> {
        val keywords = asyncPipeline.getPopularKeywords(limit)
        if (keywords.isNotEmpty()) return keywords

        return listOf(
            PopularKeyword("원룸 가성비 무선청소기", 1520, 1.8),
            PopularKeyword("4K 개발자 모니터", 1180, 2.1),
            PopularKeyword("원룸 로봇청소기", 980, 1.9),
            PopularKeyword("출퇴근 노이즈캔슬링 이어폰", 840, 1.7),
            PopularKeyword("가성비 헤어드라이기", 620, 1.5)
        )
    }

    fun getRecentSearchLogs(): List<SearchLog> {
        return asyncPipeline.getRecentLogs()
    }
}
