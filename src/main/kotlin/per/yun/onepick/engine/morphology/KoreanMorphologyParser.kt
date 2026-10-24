package per.yun.onepick.engine.morphology

import org.apache.lucene.analysis.ko.KoreanTokenizer
import org.apache.lucene.analysis.ko.KoreanTokenizer.DecompoundMode
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute
import org.springframework.stereotype.Component
import java.io.StringReader

@Component
class KoreanMorphologyParser {

    private val stopWords = setOf(
        "추천", "사고", "싶어", "좋은", "뭐가", "알려줘", "비교", "구매", "골라", "어떤", "것이", "제일", "최고"
    )

    fun parse(text: String): List<String> {
        if (text.isBlank()) return emptyList()

        val tokens = mutableListOf<String>()
        val reader = StringReader(text)
        val tokenizer = KoreanTokenizer(
            KoreanTokenizer.DEFAULT_TOKEN_ATTRIBUTE_FACTORY,
            null,
            DecompoundMode.MIXED,
            false
        )
        try {
            tokenizer.setReader(reader)
            val termAttr = tokenizer.addAttribute(CharTermAttribute::class.java)
            tokenizer.reset()
            while (tokenizer.incrementToken()) {
                val term = termAttr.toString().trim().lowercase()
                if (term.length >= 2 && !stopWords.contains(term)) {
                    tokens.add(term)
                }
            }
            tokenizer.end()
        } finally {
            tokenizer.close()
        }

        // Also add space-separated words as fallback tokens
        val rawWords = text.split("\\s+".toRegex())
            .map { it.trim().lowercase() }
            .filter { it.length >= 2 && !stopWords.contains(it) }

        return (tokens + rawWords).distinct()
    }
}
