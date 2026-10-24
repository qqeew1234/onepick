package per.yun.onepick.controller

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import per.yun.onepick.domain.model.Product
import per.yun.onepick.domain.model.PriceChartPointDto
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@SpringBootTest
@AutoConfigureMockMvc
class PrescriptiveSearchTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `test prescriptive search API endpoint according to spec`() {
        mockMvc.get("/api/v1/search/prescriptive?q=원룸 가성비 무선청소기")
            .andExpect {
                status { isOk() }
                header { exists("X-Deterministic-Engine") }
                header { exists("X-Response-Time-Ms") }
                jsonPath("$.heroPick") { exists() }
                jsonPath("$.heroPick.productId") { value("PRD-VC-2026-001") }
                jsonPath("$.heroPick.brand") { value("테크라이프") }
                jsonPath("$.heroPick.prescriptiveReason") { isNotEmpty() }
                jsonPath("$.heroPick.nonTargetReason") { isNotEmpty() }
                jsonPath("$.heroPick.priceAnalysis.status") { isNotEmpty() }
                jsonPath("$.heroPick.standardizedSpecs.heroKeyMetrics") { isArray() }
                jsonPath("$.heroPick.standardizedSpecs.verifiedPros") { isArray() }
                jsonPath("$.heroPick.standardizedSpecs.verifiedCons") { isArray() }
                jsonPath("$.alternatives") { isArray() }
            }
    }

    @Test
    fun `test top picks showcase endpoint returns verified products`() {
        mockMvc.get("/api/v1/search/top-picks")
            .andExpect {
                status { isOk() }
                header { exists("X-Deterministic-Engine") }
                header { exists("X-Total-Count") }
                jsonPath("$") { isArray() }
                jsonPath("$.length()") { value(org.hamcrest.Matchers.greaterThanOrEqualTo(5)) }
                jsonPath("$[0].productId") { exists() }
                jsonPath("$[0].productName") { exists() }
                jsonPath("$[0].prescriptiveReason") { exists() }
                jsonPath("$[0].priceStatus") { exists() }
            }
    }

    @Test
    fun `test post-purchase verification feedback endpoint`() {
        mockMvc.post("/api/v1/search/feedback") {
            contentType = MediaType.APPLICATION_JSON
            content = """{"productId":"PRD-VC-2026-001","isMatch":true,"comment":"원룸에서 정말 가볍고 좋아요"}"""
        }.andExpect {
            status { isOk() }
            jsonPath("$.success") { value(true) }
        }
    }

    @Test
    fun `test price status calculation logic FN-PRICE-01`() {
        // 1. ALL_TIME_LOW: P_current <= P_min90 * 1.05
        val p1 = Product(
            id = "T1",
            name = "Test 1",
            brand = "Brand",
            category = "Test",
            price = 100000,
            imageUrl = "",
            affiliateUrl = "",
            priceHistory = listOf(
                PriceChartPointDto("2026-08-01", 120000, "A"),
                PriceChartPointDto("2026-09-01", 110000, "B"),
                PriceChartPointDto("2026-10-01", 100000, "C")
            )
        )
        val analysis1 = p1.calculatePriceAnalysis()
        assertEquals("ALL_TIME_LOW", analysis1.status)
        assertEquals("역대 최저가 수준", analysis1.statusLabel)

        // 2. FAIR_PRICE: P_min90 * 1.05 < P_current <= P_avg90 * 1.05
        val p2 = Product(
            id = "T2",
            name = "Test 2",
            brand = "Brand",
            category = "Test",
            price = 115000,
            imageUrl = "",
            affiliateUrl = "",
            priceHistory = listOf(
                PriceChartPointDto("2026-08-01", 130000, "A"),
                PriceChartPointDto("2026-09-01", 130000, "B"),
                PriceChartPointDto("2026-10-01", 100000, "C")
            )
        )
        val analysis2 = p2.calculatePriceAnalysis()
        assertEquals("FAIR_PRICE", analysis2.status)
        assertEquals("안정적 적정가", analysis2.statusLabel)

        // 3. PRICE_SPIKE: P_current > P_avg90 * 1.05
        val p3 = Product(
            id = "T3",
            name = "Test 3",
            brand = "Brand",
            category = "Test",
            price = 150000,
            imageUrl = "",
            affiliateUrl = "",
            priceHistory = listOf(
                PriceChartPointDto("2026-08-01", 100000, "A"),
                PriceChartPointDto("2026-09-01", 100000, "B"),
                PriceChartPointDto("2026-10-01", 100000, "C")
            )
        )
        val analysis3 = p3.calculatePriceAnalysis()
        assertEquals("PRICE_SPIKE", analysis3.status)
        assertEquals("가격 일시 인상", analysis3.statusLabel)
    }

    @Test
    fun `test dynamic fallback promotion FN-FALLBACK-01`() {
        // "단독급상승"으로 검색 시 상한선 초과 상품 대신 대안 상품이 Hero Pick으로 승격되어야 함
        mockMvc.get("/api/v1/search/prescriptive?q=단독급상승")
            .andExpect {
                status { isOk() }
                jsonPath("$.heroPick") { exists() }
                jsonPath("$.heroPick.productId") { value("PRD-VC-2026-001") } // 승격된 상품
                jsonPath("$.fallbackNotice") { isNotEmpty() }
            }
    }

    @Test
    fun `test edge case EC-01 no matching tag`() {
        mockMvc.get("/api/v1/search/prescriptive?q=우주선로켓엔진부품")
            .andExpect {
                status { isOk() }
                jsonPath("$.heroPick") { doesNotExist() }
                jsonPath("$.notice") { isNotEmpty() }
            }
    }
}
