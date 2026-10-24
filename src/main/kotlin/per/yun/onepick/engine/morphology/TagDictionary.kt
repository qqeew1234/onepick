package per.yun.onepick.engine.morphology

import org.springframework.stereotype.Component

@Component
class TagDictionary {

    private val morphemeToTagMap: Map<String, Set<String>> = mapOf(
        // 자취 / 1인가구
        "자취" to setOf("자취", "원룸"),
        "자취생" to setOf("자취", "원룸"),
        "자취방" to setOf("자취", "원룸"),
        "원룸" to setOf("자취", "원룸"),
        "1인" to setOf("자취", "원룸"),
        "1인가구" to setOf("자취", "원룸"),

        // 가격/가성비/하이엔드
        "가성" to setOf("가성비"),
        "가성비" to setOf("가성비"),
        "저렴" to setOf("가성비"),
        "싼" to setOf("가성비"),
        "싸" to setOf("가성비"),
        "보급" to setOf("가성비"),
        "보급형" to setOf("가성비"),
        "엔트리" to setOf("가성비"),
        "프리미엄" to setOf("하이엔드"),
        "하이엔드" to setOf("하이엔드"),
        "플래그십" to setOf("하이엔드"),
        "최고급" to setOf("하이엔드"),

        // 무선청소기
        "청소기" to setOf("무선청소기"),
        "무선청소기" to setOf("무선청소기"),
        "무선" to setOf("무선"),
        "핸디" to setOf("무선청소기", "핸디"),
        "흡입" to setOf("흡입력"),
        "흡입력" to setOf("흡입력"),
        "경량" to setOf("경량"),
        "가벼운" to setOf("경량"),
        "소음" to setOf("저소음"),
        "저소음" to setOf("저소음"),
        "조용한" to setOf("저소음"),

        // 로봇청소기
        "로봇청소기" to setOf("로봇청소기"),
        "로봇" to setOf("로봇청소기"),
        "로청" to setOf("로봇청소기"),
        "물걸레" to setOf("물걸레"),
        "올인원" to setOf("올인원"),

        // 헤어드라이어
        "드라이" to setOf("헤어드라이기"),
        "드라이기" to setOf("헤어드라이기"),
        "드라이어" to setOf("헤어드라이기"),
        "헤어드라이기" to setOf("헤어드라이기"),
        "헤어드라이어" to setOf("헤어드라이기"),
        "헤어" to setOf("헤어드라이기"),
        "바람" to setOf("강풍"),
        "모발" to setOf("손상방지"),

        // 무선이어폰
        "이어폰" to setOf("무선이어폰"),
        "무선이어폰" to setOf("무선이어폰"),
        "블루투스" to setOf("무선"),
        "노캔" to setOf("노이즈캔슬링"),
        "노이즈" to setOf("노이즈캔슬링"),
        "캔슬링" to setOf("노이즈캔슬링"),
        "노이즈캔슬링" to setOf("노이즈캔슬링"),
        "통화" to setOf("통화품질"),
        "음질" to setOf("고음질"),
        "출퇴근" to setOf("출퇴근", "노이즈캔슬링"),

        // 모니터
        "모니터" to setOf("모니터"),
        "게이밍" to setOf("게이밍"),
        "4k" to setOf("4K", "고해상도"),
        "고해상도" to setOf("4K", "고해상도"),
        "개발자" to setOf("개발자", "모니터", "고해상도"),
        "코딩" to setOf("개발자", "모니터"),
        "주사율" to setOf("게이밍"),
        "주사" to setOf("게이밍"),

        // 동적 대체 테스트용 태그
        "급상승" to setOf("단독급상승"),
        "단독급상승" to setOf("단독급상승")
    )

    fun mapToTags(morphemes: List<String>): Set<String> {
        val result = mutableSetOf<String>()
        for (morpheme in morphemes) {
            val lower = morpheme.lowercase()
            morphemeToTagMap[lower]?.let { result.addAll(it) }

            // Check substring or containment for compound tokens
            morphemeToTagMap.forEach { (key, tags) ->
                if (lower.contains(key) || key.contains(lower)) {
                    result.addAll(tags)
                }
            }
        }
        return result
    }
}
