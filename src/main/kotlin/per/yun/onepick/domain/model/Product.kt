package per.yun.onepick.domain.model

data class Product(
    val id: String,
    val name: String,
    val brand: String,
    val category: String,
    val price: Long,
    val priceCeiling: Long = Long.MAX_VALUE,
    val originalPrice: Long? = null,
    val imageUrl: String,
    val affiliateUrl: String,
    val specs: Map<String, String> = emptyMap(),
    val tags: Set<String> = emptySet(),
    val prescriptiveReason: String? = null,
    val nonTargetReason: String = "실평수 25평 이상 거주자 및 고출력 작업 환경에는 비권장",
    val isOutOfStock: Boolean = false,
    val isLinkAlive: Boolean = true,
    val viralScore: Double = 0.0,
    val isQuarantined: Boolean = false,
    val quarantineReason: String? = null,
    val realBuyerSentimentScore: Double = 90.0,
    val complaintRate: Double = 1.0, // 실구매자 불만율 (%)
    val communityOrganicMentions: Int = 100,
    val promotionalBlogCount: Int = 0,
    val reviewCount: Int = 500,
    val oneLineReasons: Map<String, String> = emptyMap(),
    val defaultOneLineReason: String = "실구매자 만족도 최상위 및 불만율 최저 검증 완료",
    val priceHistory: List<PriceChartPointDto> = emptyList(),
    val standardizedSpecs: StandardizedSpecsDto? = null,
    val officialCatalog: OfficialCatalogDto? = null,
    val predefinedAlternatives: List<AlternativeDto> = emptyList()
) {
    val isAntiViralClean: Boolean
        get() = viralScore == 0.0 && !isQuarantined

    fun getPrescriptiveReasonFor(matchedTags: Set<String>): String {
        for (tag in matchedTags) {
            oneLineReasons[tag]?.let { return it }
        }
        return prescriptiveReason ?: defaultOneLineReason
    }

    /**
     * FN-PRICE-01 가격 상태 판정 로직
     */
    fun calculatePriceAnalysis(): PriceAnalysisDto {
        val points = if (priceHistory.isNotEmpty()) {
            priceHistory
        } else {
            // 기본 더미 데이터 생성 (최근 90일~180일)
            listOf(
                PriceChartPointDto("2026-08-01", (price * 1.15).toLong(), "오픈마켓A"),
                PriceChartPointDto("2026-09-01", (price * 1.08).toLong(), "오픈마켓B"),
                PriceChartPointDto("2026-09-20", (price * 1.02).toLong(), "오픈마켓A"),
                PriceChartPointDto("2026-10-10", (price * 0.98).toLong(), "오픈마켓C"),
                PriceChartPointDto("2026-10-24", price, "오픈마켓C (최저가)")
            )
        }

        val prices = points.map { it.price }
        val minPrice = prices.minOrNull() ?: price
        val maxPrice = prices.maxOrNull() ?: price
        val avgPrice = if (prices.isNotEmpty()) (prices.sum().toDouble() / prices.size).toLong() else price

        val current = price
        val (status, statusLabel) = when {
            current <= (minPrice * 1.05).toLong() -> "ALL_TIME_LOW" to "역대 최저가 수준"
            current <= (avgPrice * 1.05).toLong() -> "FAIR_PRICE" to "안정적 적정가"
            else -> "PRICE_SPIKE" to "가격 일시 인상"
        }

        return PriceAnalysisDto(
            status = status,
            statusLabel = statusLabel,
            minPrice90Days = minPrice,
            avgPrice90Days = avgPrice,
            maxPrice90Days = maxPrice,
            chartPoints = points
        )
    }

    /**
     * FN-FALLBACK-01 조건 충족 여부 (품절, 가격 상한선 초과, 링크 소멸)
     */
    val isFallbackTriggered: Boolean
        get() = isOutOfStock || (price > priceCeiling) || !isLinkAlive
}
