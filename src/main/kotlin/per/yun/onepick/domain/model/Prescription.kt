package per.yun.onepick.domain.model

import java.text.NumberFormat
import java.util.Locale

enum class PrescriptionBadgeType(val label: String, val badgeColor: String) {
    HERO_PICK("원픽 정답 (HERO)", "#2563EB"),
    BUDGET_PICK("가성비 대안 (BUDGET)", "#059669"),
    PREMIUM_PICK("하이엔드 대안 (PREMIUM)", "#7C3AED")
}

data class ProductSummary(
    val id: String,
    val name: String,
    val brand: String,
    val category: String,
    val price: Long,
    val formattedPrice: String,
    val originalPrice: Long? = null,
    val discountRate: Int? = null,
    val imageUrl: String,
    val affiliateUrl: String,
    val specs: Map<String, String>,
    val tags: List<String>,
    val badgeType: PrescriptionBadgeType,
    val sentimentScore: Double,
    val complaintRate: Double,
    val cleanBadge: Boolean = true,
    val keyReason: String
) {
    companion object {
        private val formatter = NumberFormat.getNumberInstance(Locale.KOREA)

        fun from(product: Product, badgeType: PrescriptionBadgeType, keyReason: String): ProductSummary {
            val discount = if (product.originalPrice != null && product.originalPrice > product.price) {
                (((product.originalPrice - product.price).toDouble() / product.originalPrice) * 100).toInt()
            } else null

            return ProductSummary(
                id = product.id,
                name = product.name,
                brand = product.brand,
                category = product.category,
                price = product.price,
                formattedPrice = "${formatter.format(product.price)}원",
                originalPrice = product.originalPrice,
                discountRate = discount,
                imageUrl = product.imageUrl,
                affiliateUrl = product.affiliateUrl,
                specs = product.specs,
                tags = product.tags.toList(),
                badgeType = badgeType,
                sentimentScore = product.realBuyerSentimentScore,
                complaintRate = product.complaintRate,
                cleanBadge = product.isAntiViralClean,
                keyReason = keyReason
            )
        }
    }
}

data class PrescriptionResult(
    val query: String,
    val heroItem: ProductSummary?,
    val alternatives: List<ProductSummary> = emptyList(),
    val prescriptiveReason: String,
    val latencyMs: Double,
    val parsedMorphemes: List<String> = emptyList(),
    val matchedTags: Set<String> = emptySet(),
    val totalCleanCandidates: Int = 0,
    val isDeterministic: Boolean = true,
    val antiViralGuaranteed: Boolean = true
)
