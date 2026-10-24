package per.yun.onepick.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import per.yun.onepick.domain.model.Product
import per.yun.onepick.engine.search.DeterministicSearchEngine
import per.yun.onepick.service.ProductCatalogService

@RestController
@RequestMapping("/api/v1/pipeline")
@CrossOrigin(origins = ["*"])
class PipelineController(
    private val searchEngine: DeterministicSearchEngine,
    private val catalogService: ProductCatalogService
) {

    @GetMapping("/catalog")
    fun getCatalog(): ResponseEntity<List<Product>> {
        return ResponseEntity.ok(searchEngine.getAllCleanProducts())
    }

    @PostMapping("/sync")
    fun reSync(): ResponseEntity<Map<String, Any>> {
        catalogService.initCatalog()
        val cleanCount = searchEngine.getAllCleanProducts().size
        val quarantinedCount = searchEngine.getQuarantinedProducts().size
        return ResponseEntity.ok(
            mapOf(
                "status" to "SUCCESS",
                "cleanProductsCount" to cleanCount,
                "quarantinedCount" to quarantinedCount,
                "message" to "비동기 파이프라인 동기화 및 안티 바이럴 검증 완료"
            )
        )
    }
}
