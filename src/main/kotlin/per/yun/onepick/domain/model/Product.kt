package per.yun.onepick.domain.model

data class Product(
    val id: String,
    val name: String,
    val brand: String,
    val category: String,
    val price: Long,
    val originalPrice: Long? = null,
    val imageUrl: String,
    val affiliateUrl: String,
    val specs: Map<String, String> = emptyMap(),
    val tags: Set<String> = emptySet(),
    val viralScore: Double = 0.0,
    val isQuarantined: Boolean = false,
    val quarantineReason: String? = null,
    val realBuyerSentimentScore: Double = 90.0,
    val complaintRate: Double = 1.0, // 실구매자 불만율 (%)
    val communityOrganicMentions: Int = 100,
    val promotionalBlogCount: Int = 0,
    val reviewCount: Int = 500,
    val oneLineReasons: Map<String, String> = emptyMap(),
    val defaultOneLineReason: String = "실구매자 만족도 최상위 및 불만율 최저 검증 완료"
) {
    val isAntiViralClean: Boolean
        get() = viralScore == 0.0 && !isQuarantined

    fun getPrescriptiveReasonFor(matchedTags: Set<String>): String {
        for (tag in matchedTags) {
            oneLineReasons[tag]?.let { return it }
        }
        return defaultOneLineReason
    }
}
