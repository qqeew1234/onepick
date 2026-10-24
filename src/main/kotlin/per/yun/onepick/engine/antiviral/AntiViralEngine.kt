package per.yun.onepick.engine.antiviral

import org.springframework.stereotype.Component
import per.yun.onepick.domain.model.Product

data class ViralAuditReport(
    val viralScore: Double,
    val isQuarantined: Boolean,
    val flags: List<String>,
    val explanation: String,
    val textPatternViolations: List<String>,
    val timeSeriesAnomalyDetected: Boolean,
    val crossValidationRatio: Double
)

@Component
class AntiViralEngine {

    // 1. 텍스트 패턴 분석용 키워드 및 정규식
    private val explicitSponsorPatterns = listOf(
        "협찬", "원고료", "지원받아", "제공받아", "제작지원", "소정의 수수료",
        "소정의 포인트", "업체로부터", "제품 지원", "무상 제공", "체험단"
    )

    private val disguisedFakePatterns = listOf(
        "내돈내산인 척", "내돈내산이지만", "내돈내산 솔직", "체험단 후기", "광고 포함"
    )

    private val botRepetitionRegex = Regex("(.)\\1{4,}") // e.g. ㅋㅋㅋㅋㅋ, ㅎㅎㅎㅎㅎ, 강추강추강추

    fun analyzeText(text: String): List<String> {
        val violations = mutableListOf<String>()
        val normalized = text.lowercase()

        for (pattern in explicitSponsorPatterns) {
            if (normalized.contains(pattern)) {
                violations.add("협찬/지원성 문구 탐지: '$pattern'")
            }
        }

        for (pattern in disguisedFakePatterns) {
            if (normalized.contains(pattern)) {
                violations.add("위장형 리뷰 의심 문구 탐지: '$pattern'")
            }
        }

        if (botRepetitionRegex.containsMatchIn(text)) {
            violations.add("매크로성 비정상 반복 텍스트 탐지")
        }

        return violations
    }

    // 2. 시계열 이상 탐지 (최근 리뷰/언급량이 기준치 대비 비정상 급증했는지 검사)
    fun detectTimeSeriesAnomaly(recentDailyCounts: List<Int>, baselineDailyAverage: Double): Boolean {
        if (recentDailyCounts.isEmpty() || baselineDailyAverage <= 0.0) return false
        val recentMax = recentDailyCounts.maxOrNull() ?: 0
        // 평균 대비 3.5배 이상 폭증한 경우 시계열 바이럴 어뷰징 의심
        return (recentMax.toDouble() / baselineDailyAverage) >= 3.5
    }

    // 3. 교차 검증 (순수 커뮤니티 언급량 vs 상업성 블로그/SNS 발행량)
    fun calculateCrossValidationRatio(pureCommunityMentions: Int, promotionalBlogCount: Int): Double {
        if (promotionalBlogCount == 0) return 1.0
        val total = pureCommunityMentions + promotionalBlogCount
        return pureCommunityMentions.toDouble() / total.toDouble()
    }

    // 종합 상품 바이럴 감사 (Zero-Tolerance Audit)
    fun auditProduct(
        product: Product,
        recentReviewSamples: List<String> = emptyList(),
        recentDailyCounts: List<Int> = emptyList(),
        baselineDailyAverage: Double = 10.0
    ): ViralAuditReport {
        val flags = mutableListOf<String>()
        val textViolations = mutableListOf<String>()

        // 텍스트 패턴 검증
        for (sample in recentReviewSamples) {
            val sampleViolations = analyzeText(sample)
            if (sampleViolations.isNotEmpty()) {
                textViolations.addAll(sampleViolations)
            }
        }
        if (textViolations.isNotEmpty()) {
            flags.add("리뷰 텍스트 내 광고/협찬/어뷰징 패턴 발견 (${textViolations.size}건)")
        }

        // 시계열 이상치 검증
        val isAnomaly = detectTimeSeriesAnomaly(recentDailyCounts, baselineDailyAverage)
        if (isAnomaly) {
            flags.add("단기간 내 비정상적 리뷰/언급량 폭증 감지 (시계열 어뷰징 의심)")
        }

        // 교차 검증 (순수 커뮤니티 비율이 40% 미만이고 협찬 블로그 수가 많으면 감점)
        val cvRatio = calculateCrossValidationRatio(
            product.communityOrganicMentions,
            product.promotionalBlogCount
        )
        if (product.promotionalBlogCount > 20 && cvRatio < 0.4) {
            flags.add("순수 커뮤니티 언급 대비 상업성 블로그 비율 과다 (신뢰도 결여)")
        }

        // 실구매자 불만율 과다 여부 (불만율 5.0% 이상이면 원픽 기준 탈락)
        if (product.complaintRate >= 5.0) {
            flags.add("실구매자 불만율 ${product.complaintRate}% (임계치 5.0% 초과)")
        }

        // 기존 product에 바이럴 점수나 격리 사유가 설정되어 있는 경우
        if (product.viralScore > 0.0) {
            flags.add("기등록 바이럴 스코어: ${product.viralScore}")
        }
        if (product.isQuarantined && !product.quarantineReason.isNullOrBlank()) {
            flags.add("격리 상태: ${product.quarantineReason}")
        }

        val totalViralScore = when {
            flags.isEmpty() -> 0.0
            else -> {
                var score = 0.0
                if (textViolations.isNotEmpty()) score += 40.0
                if (isAnomaly) score += 30.0
                if (cvRatio < 0.4 && product.promotionalBlogCount > 20) score += 25.0
                if (product.viralScore > 0.0) score += product.viralScore
                score.coerceAtMost(100.0)
            }
        }

        val isQuarantined = totalViralScore > 0.0 || product.isQuarantined

        val explanation = if (isQuarantined) {
            "🚨 [바이럴 격리 조치] 감지된 사유: ${flags.joinToString(", ")}"
        } else {
            "✅ [Zero-Tolerance Clean] 바이럴 스코어 0.0 - 광고 및 어뷰징 완전 배제 통과"
        }

        return ViralAuditReport(
            viralScore = totalViralScore,
            isQuarantined = isQuarantined,
            flags = flags,
            explanation = explanation,
            textPatternViolations = textViolations.distinct(),
            timeSeriesAnomalyDetected = isAnomaly,
            crossValidationRatio = cvRatio
        )
    }
}
