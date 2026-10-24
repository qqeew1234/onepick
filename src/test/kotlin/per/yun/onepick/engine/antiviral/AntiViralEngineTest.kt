package per.yun.onepick.engine.antiviral

import org.junit.jupiter.api.Test
import per.yun.onepick.domain.model.Product
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class AntiViralEngineTest {

    private val antiViralEngine = AntiViralEngine()

    @Test
    fun `test sponsored text detection`() {
        val review = "본 포스팅은 업체로부터 소정의 원고료를 지원받아 작성되었습니다."
        val violations = antiViralEngine.analyzeText(review)
        assertTrue(violations.isNotEmpty())
        assertTrue(violations.any { it.contains("원고료") || it.contains("지원받아") })
    }

    @Test
    fun `test disguised fake naedon naesan detection`() {
        val review = "내돈내산인 척 작성하는 솔직후기입니다."
        val violations = antiViralEngine.analyzeText(review)
        assertTrue(violations.isNotEmpty())
    }

    @Test
    fun `test clean review passing`() {
        val review = "한 달째 자취방에서 쓰고 있는데 소음도 적고 가벼워서 만족스럽습니다."
        val violations = antiViralEngine.analyzeText(review)
        assertTrue(violations.isEmpty())
    }

    @Test
    fun `test time series anomaly detection`() {
        // 평소 일평균 10건인데 최근 50건으로 폭증
        val dailyCounts = listOf(8, 12, 9, 52)
        val isAnomaly = antiViralEngine.detectTimeSeriesAnomaly(dailyCounts, 10.0)
        assertTrue(isAnomaly)

        // 정상 범위
        val normalCounts = listOf(8, 11, 10, 14)
        val isNormal = antiViralEngine.detectTimeSeriesAnomaly(normalCounts, 10.0)
        assertFalse(isNormal)
    }

    @Test
    fun `test product quarantine on viral violations`() {
        val suspiciousProduct = Product(
            id = "TEST-VIRAL",
            name = "바이럴 의심 상품",
            brand = "브랜드A",
            category = "무선청소기",
            price = 100000,
            imageUrl = "",
            affiliateUrl = "",
            communityOrganicMentions = 10,
            promotionalBlogCount = 150, // 협찬 블로그 폭증
            complaintRate = 6.5
        )

        val report = antiViralEngine.auditProduct(
            product = suspiciousProduct,
            recentReviewSamples = listOf("체험단 후기입니다 강추강추강추"),
            recentDailyCounts = listOf(5, 5, 80),
            baselineDailyAverage = 5.0
        )

        assertTrue(report.isQuarantined)
        assertTrue(report.viralScore > 0.0)
        println("Quarantine report: ${report.explanation}")
    }
}
