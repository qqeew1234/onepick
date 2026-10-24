package per.yun.onepick.domain.model

import java.time.Instant

data class SearchLog(
    val id: String,
    val query: String,
    val timestamp: Instant = Instant.now(),
    val latencyMs: Double,
    val heroProductId: String?,
    val heroProductName: String?,
    val matchedTags: Set<String>
)

data class PopularKeyword(
    val keyword: String,
    val count: Long,
    val averageLatencyMs: Double
)
