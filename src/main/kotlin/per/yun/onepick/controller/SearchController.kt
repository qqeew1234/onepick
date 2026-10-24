package per.yun.onepick.controller

import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import per.yun.onepick.domain.model.PopularKeyword
import per.yun.onepick.domain.model.PrescriptionResult
import per.yun.onepick.domain.model.SearchLog
import per.yun.onepick.service.SearchPrescriptionService

@RestController
@RequestMapping("/api/v1/search")
@CrossOrigin(origins = ["*"])
class SearchController(
    private val prescriptionService: SearchPrescriptionService
) {

    @GetMapping
    fun search(@RequestParam("q", defaultValue = "") query: String): ResponseEntity<PrescriptionResult> {
        val result = prescriptionService.prescribe(query)
        val headers = HttpHeaders().apply {
            add("X-Deterministic-Engine", "ONE-PICK-Sub-10ms")
            add("X-Response-Time-Ms", "${result.latencyMs}ms")
            add("X-Anti-Viral-Audited", "true")
        }
        return ResponseEntity.ok().headers(headers).body(result)
    }

    @GetMapping("/popular")
    fun getPopularKeywords(): ResponseEntity<List<PopularKeyword>> {
        return ResponseEntity.ok(prescriptionService.getPopularKeywords())
    }

    @GetMapping("/logs")
    fun getRecentLogs(): ResponseEntity<List<SearchLog>> {
        return ResponseEntity.ok(prescriptionService.getRecentSearchLogs())
    }
}
