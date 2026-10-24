package per.yun.onepick.domain.model

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class PrescriptiveSearchResponse(
    val query: String,
    val executionTimeMs: Double,
    val heroPick: HeroPickDto?,
    val alternatives: List<AlternativeDto> = emptyList(),
    val fallbackNotice: String? = null,
    val notice: String? = null
)

data class HeroPickDto(
    val productId: String,
    val productName: String,
    val brand: String,
    val thumbnailUrl: String,
    val currentPrice: Long,
    val priceCeiling: Long,
    val affiliateUrl: String,
    val prescriptiveReason: String,
    val nonTargetReason: String,
    val isOutOfStock: Boolean = false,
    val priceAnalysis: PriceAnalysisDto,
    val standardizedSpecs: StandardizedSpecsDto,
    val officialCatalog: OfficialCatalogDto
)

data class PriceAnalysisDto(
    val status: String, // ALL_TIME_LOW, FAIR_PRICE, PRICE_SPIKE
    val statusLabel: String, // 역대 최저가 수준, 안정적 적정가, 가격 일시 인상
    val minPrice90Days: Long,
    val avgPrice90Days: Long,
    val maxPrice90Days: Long,
    val chartPoints: List<PriceChartPointDto>
)

data class PriceChartPointDto(
    val date: String,
    val price: Long,
    val seller: String
)

data class StandardizedSpecsDto(
    val heroKeyMetrics: List<KeyMetricDto>,
    val verifiedPros: List<String>,
    val verifiedCons: List<String>,
    val specSections: List<SpecSectionDto>
)

data class KeyMetricDto(
    val key: String,
    val label: String,
    val value: String,
    val unit: String
)

data class SpecSectionDto(
    val sectionName: String,
    val items: List<SpecItemDto>
)

data class SpecItemDto(
    val label: String,
    val value: String
)

data class OfficialCatalogDto(
    val sourceCatalogUrl: String,
    val rawImages: List<String>
)

data class AlternativeDto(
    val type: String, // COST_EFFECTIVE, HIGH_END
    val typeLabel: String, // 가성비 대안, 상위 성능 대안
    val productId: String,
    val productName: String,
    val brand: String? = null,
    val thumbnailUrl: String? = null,
    val currentPrice: Long,
    val differentiatingReason: String,
    val affiliateUrl: String
)

data class FeedbackRequest(
    val productId: String,
    val isMatch: Boolean,
    val comment: String? = null
)

data class FeedbackResponse(
    val success: Boolean,
    val message: String
)
