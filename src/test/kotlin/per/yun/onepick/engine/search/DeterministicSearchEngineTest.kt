package per.yun.onepick.engine.search

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import per.yun.onepick.domain.model.Product
import per.yun.onepick.engine.morphology.KoreanMorphologyParser
import per.yun.onepick.engine.morphology.TagDictionary
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class DeterministicSearchEngineTest {

    private val morphologyParser = KoreanMorphologyParser()
    private val tagDictionary = TagDictionary()
    private lateinit var searchEngine: DeterministicSearchEngine

    @BeforeEach
    fun setUp() {
        searchEngine = DeterministicSearchEngine(morphologyParser, tagDictionary)

        val sampleProducts = listOf(
            Product(
                id = "VC-001",
                name = "차이슨 i10 가성비 무선청소기",
                brand = "디베아",
                category = "무선청소기",
                price = 149000,
                imageUrl = "http://example.com/p1.jpg",
                affiliateUrl = "http://affiliate.com/p1",
                tags = setOf("무선청소기", "청소기", "가성비", "자취", "원룸"),
                viralScore = 0.0,
                isQuarantined = false,
                realBuyerSentimentScore = 96.0,
                complaintRate = 0.8,
                oneLineReasons = mapOf(
                    "자취" to "원룸/자취생 흡입력과 가성비 종결자, 실구매자 불만율 0.8% 최저"
                )
            ),
            Product(
                id = "VC-002",
                name = "V12 플러피 무선청소기",
                brand = "다이슨",
                category = "무선청소기",
                price = 799000,
                imageUrl = "http://example.com/p2.jpg",
                affiliateUrl = "http://affiliate.com/p2",
                tags = setOf("무선청소기", "청소기", "하이엔드", "프리미엄"),
                viralScore = 0.0,
                isQuarantined = false,
                realBuyerSentimentScore = 94.0,
                complaintRate = 1.2,
                oneLineReasons = mapOf(
                    "하이엔드" to "프리미엄 미세먼지 시각화 레이저 플러피 종결"
                )
            ),
            Product(
                id = "VC-VIRAL",
                name = "광고 도배 청소기",
                brand = "바이럴브랜드",
                category = "무선청소기",
                price = 99000,
                imageUrl = "http://example.com/viral.jpg",
                affiliateUrl = "http://affiliate.com/viral",
                tags = setOf("무선청소기", "자취", "가성비"),
                viralScore = 90.0,
                isQuarantined = true,
                quarantineReason = "체험단 100건 및 광고 도배 탐지"
            )
        )
        searchEngine.updateCatalog(sampleProducts)
    }

    @Test
    fun `test sub-10ms response guarantee and hero item prescription`() {
        val result = searchEngine.search("자취생 가성비 무선청소기")

        // 1. 초저지연 (Sub-10ms) 보장 검증
        println("Search Latency: ${result.latencyMs} ms")
        assertTrue(result.latencyMs < 10.0, "Latency must be strictly under 10ms, but was ${result.latencyMs}ms")

        // 2. 단 하나의 Hero Item 제시 (Prescriptive)
        assertNotNull(result.heroItem)
        assertEquals("VC-001", result.heroItem?.id)
        assertEquals("디베아", result.heroItem?.brand)

        // 3. 1줄 선정 사유 존재
        assertTrue(result.prescriptiveReason.isNotBlank())
        println("Prescriptive Reason: ${result.prescriptiveReason}")

        // 4. 대안은 최대 2개로 제한 (총 결과 1~3개)
        assertTrue(result.alternatives.size <= 2)

        // 5. 바이럴 제품은 검색 결과에 절대 포함되지 않음 (Zero-Tolerance)
        val allReturnedIds = listOfNotNull(result.heroItem?.id) + result.alternatives.map { it.id }
        assertTrue(!allReturnedIds.contains("VC-VIRAL"), "Quarantined viral product must never be included")
    }

    @Test
    fun `test 100 consecutive searches sub-10ms benchmark`() {
        var maxLatency = 0.0
        val times = mutableListOf<Double>()

        for (i in 1..100) {
            val result = searchEngine.search("가성비 무선청소기")
            times.add(result.latencyMs)
            if (result.latencyMs > maxLatency) {
                maxLatency = result.latencyMs
            }
        }

        val avgLatency = times.average()
        println("100 iterations - Average Latency: ${String.format("%.3f", avgLatency)}ms, Max Latency: ${maxLatency}ms")
        assertTrue(avgLatency < 5.0, "Average latency should be Sub-5ms in-memory")
    }
}
