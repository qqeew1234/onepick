package per.yun.onepick.controller

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

@SpringBootTest
@AutoConfigureMockMvc
class SearchControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `test legacy search API endpoint`() {
        mockMvc.get("/api/v1/search?q=원룸 가성비 무선청소기")
            .andExpect {
                status { isOk() }
                header { exists("X-Deterministic-Engine") }
                header { exists("X-Response-Time-Ms") }
                jsonPath("$.heroItem") { exists() }
                jsonPath("$.heroItem.name") { value("슬림 파워 무선청소기 V10 Lite") }
                jsonPath("$.prescriptiveReason") { isNotEmpty() }
                jsonPath("$.antiViralGuaranteed") { value(true) }
            }
    }

    @Test
    fun `test popular keywords endpoint`() {
        mockMvc.get("/api/v1/search/popular")
            .andExpect {
                status { isOk() }
                jsonPath("$") { isArray() }
            }
    }
}
