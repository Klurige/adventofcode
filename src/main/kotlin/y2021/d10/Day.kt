package y2021.d10

@Suppress("unused")
fun day(data: List<String>) {
    val scoresMissing = mutableListOf<Long>()

    var scoreCorrupt = 0L

    data.forEach { line ->
        scoreCorrupt += findScore(line, true)
        val scoreMissing = findScore(line, false)
        if (scoreMissing > 0) {
            scoresMissing.add(scoreMissing)
        }
    }


    println("Corrupt score: $scoreCorrupt")

    scoresMissing.sort()
    val middle = scoresMissing.size / 2
    println("Missing score: ${scoresMissing[middle]}")
}

fun findScore(line: String, countCorrupt: Boolean): Long {
    var chars = line
    while (chars.isNotEmpty()) {
        var isDone = false
        chars.indices.forEach { index ->
            if (!isDone && index < chars.length - 1) {
                val ch = chars[index]
                if (isOpening(ch)) {
                    val ch1 = chars[index + 1]
                    if (ch1 == getClosing(ch)) {
                        val first = chars.substring(0, index)
                        val second = chars.substring(index + 2)
                        chars = first + second
                        isDone = true
                    }
                }
            }
        }
        if (!isDone) {
            val first = chars.toCharArray().firstOrNull {
                !isOpening(it)
            }
            if (first == null) {
                //println("Incomplete.")
                return if (countCorrupt) 0
                else {
                    var missing = ""
                    chars.forEach { ch ->
                        missing = "${getClosing(ch)}$missing"
                    }
                    var score = 0L
                    missing.forEach { ch ->
                        score = score * 5 + getScoreMissing(ch)
                    }
                    score
                }
            } else {
                //println("Corrupt.")
                return if (countCorrupt) getScoreCorrupt(first)
                else 0
            }
        }
    }
    return 0
}

fun getScoreMissing(ch: Char) = when (ch) {
    ')' -> 1L
    ']' -> 2L
    '}' -> 3L
    '>' -> 4L
    else -> throw IllegalArgumentException()
}

fun getScoreCorrupt(ch: Char) = when (ch) {
    ')' -> 3L
    ']' -> 57L
    '}' -> 1197L
    '>' -> 25137L
    else -> throw IllegalArgumentException()
}

fun isOpening(ch: Char): Boolean = ch == '(' || ch == '{' || ch == '[' || ch == '<'


fun getClosing(ch: Char) = when (ch) {
    '(' -> ')'
    '[' -> ']'
    '{' -> '}'
    '<' -> '>'
    else -> throw IllegalArgumentException("Not opening $ch")
}
