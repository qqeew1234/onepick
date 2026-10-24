package per.yun.onepick.engine.search

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import per.yun.onepick.domain.model.PrescriptionBadgeType
import per.yun.onepick.domain.model.PrescriptionResult
import per.yun.onepick.domain.model.Product
import per.yun.onepick.domain.model.ProductSummary
import per.yun.onepick.engine.morphology.KoreanMorphologyParser
import per.yun.onepick.engine.morphology.TagDictionary
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArrayList

@Component
class DeterministicSearchEngine(
    private val morphologyParser: KoreanMorphologyParser,
    private val tagDictionary: TagDictionary
) {
    private val log = LoggerFactory.getLogger(DeterministicSearchEngine::class.java)

    // 인메모리 초고속 인덱스 (Sub-5ms 보장)
    private val productCatalog = ConcurrentHashMap<String, Product>()
    private val tagIndex = ConcurrentHashMap<String, CopyOnWriteArrayList<String>>()
    private val quarantinedProducts = ConcurrentHashMap<String, Product>()

    fun updateCatalog(products: List<Product>) {
        productCatalog.clear()
        tagIndex.clear()
        quarantinedProducts.clear()

        for (p in products) {
            if (p.isAntiViralClean) {
                productCatalog[p.id] = p
                for (tag in p.tags) {
                    val list = tagIndex.computeIfAbsent(tag.lowercase()) { CopyOnWriteArrayList() }
                    list.add(p.id)
                }
                // Category index
                tagIndex.computeIfAbsent(p.category.lowercase()) { CopyOnWriteArrayList() }.add(p.id)
            } else {
                quarantinedProducts[p.id] = p
            }
        }
        log.info("Deterministic catalog updated: ${productCatalog.size} clean products, ${quarantinedProducts.size} quarantined")
    }

    fun getAllCleanProducts(): List<Product> = productCatalog.values.toList()
    fun getQuarantinedProducts(): List<Product> = quarantinedProducts.values.toList()

    fun search(rawQuery: String): PrescriptionResult {
        val startNano = System.nanoTime()

        val query = rawQuery.trim()
        if (query.isBlank()) {
            return emptyResult(rawQuery, (System.nanoTime() - startNano) / 1_000_000.0)
        }

        // 1. 형태소 분석 (Nori 기반)
        val morphemes = morphologyParser.parse(query)

        // 2. 형태소-태그 매핑 테이블 조회
        val matchedTags = tagDictionary.mapToTags(morphemes).toMutableSet()

        // 형태소 자체도 태그로 병합
        for (m in morphemes) {
            matchedTags.add(m)
        }

        // 3. 후보 제품 추출 (완전 격리된 바이럴 0점 제품 중에서만 조회)
        val candidateScores = mutableMapOf<String, Double>()

        for (tag in matchedTags) {
            val productIds = tagIndex[tag.lowercase()] ?: emptyList()
            for (id in productIds) {
                val current = candidateScores.getOrDefault(id, 0.0)
                // 태그 일치 시 가중치 부여
                candidateScores[id] = current + 15.0
            }

            // 부분 일치 태그 탐색
            for ((indexedTag, ids) in tagIndex) {
                if (indexedTag.contains(tag) || tag.contains(indexedTag)) {
                    for (id in ids) {
                        val current = candidateScores.getOrDefault(id, 0.0)
                        candidateScores[id] = current + 8.0
                    }
                }
            }
        }

        // 만약 태그 매칭이 적은 경우 상품명/브랜드 직접 매칭 fallback
        if (candidateScores.isEmpty()) {
            for ((id, product) in productCatalog) {
                val matchScore = calculateDirectMatch(product, morphemes, query)
                if (matchScore > 0.0) {
                    candidateScores[id] = matchScore
                }
            }
        }

        if (candidateScores.isEmpty()) {
            val elapsedMs = (System.nanoTime() - startNano) / 1_000_000.0
            return emptyResult(rawQuery, elapsedMs, morphemes, matchedTags)
        }

        // 4. 점수 정규화 및 최종 랭킹 산출
        // 공식: (태그 매칭 가중치) + (실구매자 만족도 / 2) - (불만율 * 5) + (유기적 커뮤니티 언급 가산점)
        val rankedProducts = candidateScores.mapNotNull { (id, tagScore) ->
            val product = productCatalog[id] ?: return@mapNotNull null
            val organicBonus = (product.communityOrganicMentions.coerceAtMost(500) / 50.0)
            val complaintPenalty = product.complaintRate * 4.0
            val satisfactionScore = product.realBuyerSentimentScore
            val totalScore = tagScore + satisfactionScore - complaintPenalty + organicBonus
            product to totalScore
        }.sortedByDescending { it.second }

        val topProducts = rankedProducts.map { it.first }
        if (topProducts.isEmpty()) {
            val elapsedMs = (System.nanoTime() - startNano) / 1_000_000.0
            return emptyResult(rawQuery, elapsedMs, morphemes, matchedTags)
        }

        // 5. Hero Item (단 하나의 정답) 선정
        val heroProduct = topProducts.first()

        // 1줄 선정 사유 결정
        val prescriptiveReason = heroProduct.getPrescriptiveReasonFor(matchedTags)
        val heroSummary = ProductSummary.from(
            product = heroProduct,
            badgeType = PrescriptionBadgeType.HERO_PICK,
            keyReason = prescriptiveReason
        )

        // 6. 대안 추출 (선택 피로 방지를 위해 최대 2개만 선별)
        val alternatives = mutableListOf<ProductSummary>()
        val isQueryBudgetOriented = matchedTags.contains("가성비") || query.contains("싼") || query.contains("저렴")

        val remainingCandidates = topProducts.drop(1)
        if (remainingCandidates.isNotEmpty()) {
            // 대안 1: 가성비 대안 또는 프리미엄 대안 탐색
            val budgetAlt = remainingCandidates.find { it.price < heroProduct.price }
            val premiumAlt = remainingCandidates.find { it.price > heroProduct.price }

            if (isQueryBudgetOriented) {
                // 가성비 질의였으면 프리미엄 대안을 1순위 대안으로
                premiumAlt?.let {
                    alternatives.add(
                        ProductSummary.from(
                            it,
                            PrescriptionBadgeType.PREMIUM_PICK,
                            it.getPrescriptiveReasonFor(matchedTags)
                        )
                    )
                }
                budgetAlt?.let {
                    if (alternatives.size < 2 && it.id != alternatives.firstOrNull()?.id) {
                        alternatives.add(
                            ProductSummary.from(
                                it,
                                PrescriptionBadgeType.BUDGET_PICK,
                                it.getPrescriptiveReasonFor(matchedTags)
                            )
                        )
                    }
                }
            } else {
                // 일반 질의였으면 가성비 대안과 프리미엄 대안 구성
                budgetAlt?.let {
                    alternatives.add(
                        ProductSummary.from(
                            it,
                            PrescriptionBadgeType.BUDGET_PICK,
                            it.getPrescriptiveReasonFor(matchedTags)
                        )
                    )
                }
                premiumAlt?.let {
                    if (alternatives.size < 2 && it.id != alternatives.firstOrNull()?.id) {
                        alternatives.add(
                            ProductSummary.from(
                                it,
                                PrescriptionBadgeType.PREMIUM_PICK,
                                it.getPrescriptiveReasonFor(matchedTags)
                            )
                        )
                    }
                }
            }

            // 대안이 여전히 비어있다면 상위 후보 중 1~2개 추가 (최대 2개 제한)
            if (alternatives.isEmpty()) {
                remainingCandidates.take(2).forEach {
                    val badge = if (it.price < heroProduct.price) PrescriptionBadgeType.BUDGET_PICK else PrescriptionBadgeType.PREMIUM_PICK
                    alternatives.add(ProductSummary.from(it, badge, it.getPrescriptiveReasonFor(matchedTags)))
                }
            }
        }

        val elapsedMs = (System.nanoTime() - startNano) / 1_000_000.0

        return PrescriptionResult(
            query = rawQuery,
            heroItem = heroSummary,
            alternatives = alternatives.take(2),
            prescriptiveReason = prescriptiveReason,
            latencyMs = Math.round(elapsedMs * 100.0) / 100.0,
            parsedMorphemes = morphemes,
            matchedTags = matchedTags,
            totalCleanCandidates = candidateScores.size,
            isDeterministic = true,
            antiViralGuaranteed = true
        )
    }

    private fun calculateDirectMatch(product: Product, morphemes: List<String>, rawQuery: String): Double {
        var score = 0.0
        val targetText = "${product.name} ${product.brand} ${product.category} ${product.tags.joinToString(" ")}".lowercase()

        if (targetText.contains(rawQuery.lowercase())) {
            score += 30.0
        }
        for (m in morphemes) {
            if (targetText.contains(m.lowercase())) {
                score += 10.0
            }
        }
        return score
    }

    private fun emptyResult(
        query: String,
        latencyMs: Double,
        morphemes: List<String> = emptyList(),
        matchedTags: Set<String> = emptySet()
    ): PrescriptionResult {
        return PrescriptionResult(
            query = query,
            heroItem = null,
            alternatives = emptyList(),
            prescriptiveReason = "해당 검색어에 완벽히 부합하는 검증된 원픽 제품을 찾지 못했습니다.",
            latencyMs = Math.round(latencyMs * 100.0) / 100.0,
            parsedMorphemes = morphemes,
            matchedTags = matchedTags,
            totalCleanCandidates = 0,
            isDeterministic = true,
            antiViralGuaranteed = true
        )
    }
}
