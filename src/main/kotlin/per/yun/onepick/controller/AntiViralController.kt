package per.yun.onepick.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import per.yun.onepick.domain.model.Product
import per.yun.onepick.engine.antiviral.AntiViralEngine
import per.yun.onepick.engine.antiviral.ViralAuditReport
import per.yun.onepick.engine.search.DeterministicSearchEngine

data class TextAnalysisRequest(
    val text: String
)

data class ProductAuditRequest(
    val name: String,
    val brand: String,
    val sampleReviews: List<String> = emptyList(),
    val promotionalBlogCount: Int = 0,
    val pureCommunityMentions: Int = 100,
    val complaintRate: Double = 1.0,
    val dailyReviewCounts: List<Int> = emptyList(),
    val baselineAverage: Double = 10.0
)

@RestController
@RequestMapping("/api/v1/antiviral")
@CrossOrigin(origins = ["*"])
class AntiViralController(
    private val antiViralEngine: AntiViralEngine,
    private val searchEngine: DeterministicSearchEngine
) {

    @PostMapping("/analyze-text")
    fun analyzeText(@RequestBody request: TextAnalysisRequest): ResponseEntity<Map<String, Any>> {
        val violations = antiViralEngine.analyzeText(request.text)
        val isClean = violations.isEmpty()
        return ResponseEntity.ok(
            mapOf(
                "isClean" to isClean,
                "violations" to violations,
                "verdict" to if (isClean) "광고/협찬 패턴 미감지 (Clean)" else "광고성/위장 패턴 감지 (Filtered)"
            )
        )
    }

    @PostMapping("/audit-product")
    fun auditProduct(@RequestBody request: ProductAuditRequest): ResponseEntity<ViralAuditReport> {
        val dummyProduct = Product(
            id = "TEMP-${System.currentTimeMillis()}",
            name = request.name,
            brand = request.brand,
            category = "기타",
            price = 100000,
            imageUrl = "",
            affiliateUrl = "",
            communityOrganicMentions = request.pureCommunityMentions,
            promotionalBlogCount = request.promotionalBlogCount,
            complaintRate = request.complaintRate
        )

        val report = antiViralEngine.auditProduct(
            product = dummyProduct,
            recentReviewSamples = request.sampleReviews,
            recentDailyCounts = request.dailyReviewCounts,
            baselineDailyAverage = request.baselineAverage
        )
        return ResponseEntity.ok(report)
    }

    @GetMapping("/quarantine-list")
    fun getQuarantinedList(): ResponseEntity<List<Product>> {
        return ResponseEntity.ok(searchEngine.getQuarantinedProducts())
    }
}
