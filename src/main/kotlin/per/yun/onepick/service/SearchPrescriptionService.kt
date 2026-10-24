package per.yun.onepick.service

import org.springframework.stereotype.Service
import per.yun.onepick.domain.model.PopularKeyword
import per.yun.onepick.domain.model.PrescriptionResult
import per.yun.onepick.domain.model.SearchLog
import per.yun.onepick.engine.search.DeterministicSearchEngine
import per.yun.onepick.pipeline.AsyncDataPipeline

@Service
class SearchPrescriptionService(
    private val searchEngine: DeterministicSearchEngine,
    private val asyncPipeline: AsyncDataPipeline
) {

    fun prescribe(query: String): PrescriptionResult {
        val result = searchEngine.search(query)

        // 비동기 파이프라인으로 로그 전송 (검색 응답 레이턴시에 영향 없음)
        asyncPipeline.recordSearch(
            query = query,
            latencyMs = result.latencyMs,
            heroProductId = result.heroItem?.id,
            heroProductName = result.heroItem?.name,
            matchedTags = result.matchedTags
        )

        return result
    }

    fun getPopularKeywords(limit: Int = 10): List<PopularKeyword> {
        val keywords = asyncPipeline.getPopularKeywords(limit)
        if (keywords.isNotEmpty()) return keywords

        // 초기 추천 키워드 기본값
        return listOf(
            PopularKeyword("자취생 가성비 무선청소기", 1250, 1.45),
            PopularKeyword("출퇴근 노이즈캔슬링 이어폰", 980, 1.32),
            PopularKeyword("원룸 로봇청소기", 850, 1.55),
            PopularKeyword("개발자 4K 모니터", 740, 1.20),
            PopularKeyword("가성비 헤어드라이기", 620, 1.10)
        )
    }

    fun getRecentSearchLogs(): List<SearchLog> {
        return asyncPipeline.getRecentLogs()
    }
}
