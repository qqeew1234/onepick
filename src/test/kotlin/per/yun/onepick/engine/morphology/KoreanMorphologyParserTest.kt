package per.yun.onepick.engine.morphology

import org.junit.jupiter.api.Test
import kotlin.test.assertTrue

class KoreanMorphologyParserTest {

    private val parser = KoreanMorphologyParser()

    @Test
    fun `test korean morphology parsing`() {
        val tokens = parser.parse("자취생 가성비 좋은 무선청소기 추천해줘")
        println("Parsed tokens: $tokens")
        assertTrue(tokens.contains("자취") || tokens.contains("자취생"))
        assertTrue(tokens.contains("가성") || tokens.contains("가성비"))
        assertTrue(tokens.contains("무선") || tokens.contains("청소기") || tokens.contains("무선청소기"))
    }
}
