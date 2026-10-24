package per.yun.onepick.controller

import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import per.yun.onepick.domain.model.*
import per.yun.onepick.service.SearchPrescriptionService

@RestController
@RequestMapping("/api/v1/search")
@CrossOrigin(origins = ["*"])
class SearchController(
    private val prescriptionService: SearchPrescriptionService
) {

    /**
     * spec_design.md 4.1 & 4.2 사양 준수 엔드포인트
     * GET /api/v1/search/prescriptive?q=...
     */
    @GetMapping("/prescriptive")
    fun searchPrescriptive(
        @RequestParam("q", defaultValue = "") query: String
    ): ResponseEntity<PrescriptiveSearchResponse> {
        val result = prescriptionService.searchPrescriptive(query)
        val headers = HttpHeaders().apply {
            add("X-Deterministic-Engine", "ONE-PICK-Sub-10ms")
            add("X-Response-Time-Ms", "${result.executionTimeMs}ms")
            add("X-Anti-Viral-Audited", "true")
        }
        return ResponseEntity.ok().headers(headers).body(result)
    }

    /**
     * 사후 검증 피드백 엔드포인트 (PRD 3. 신뢰성 거버넌스)
     */
    @PostMapping("/feedback")
    fun recordFeedback(
        @RequestBody request: FeedbackRequest
    ): ResponseEntity<FeedbackResponse> {
        val response = prescriptionService.recordFeedback(request)
        return ResponseEntity.ok(response)
    }

    /**
     * 기존 하위 호환 엔드포인트
     */
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
