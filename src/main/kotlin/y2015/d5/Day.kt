package y2015.d5

import kotlin.math.absoluteValue

@Suppress("unused")
fun day(data: List<String>) {

    var nice1Counter = 0
    var nice2Counter = 0
    data.forEach { line ->
            val isNice1 = checkIfNice1(line)
            if (isNice1) {
                nice1Counter++
            }
            val isNice2 = checkIfNice2(line)
            if (isNice2) {
                nice2Counter++
            }
        }

    println("Nices: $nice1Counter $nice2Counter")
}

fun checkIfNice2(str: String): Boolean {
    val chars = str.toCharArray()
    val pairs = mutableListOf<Pair<Int, String>>()
    (0 until chars.size - 1).forEach { index ->
        pairs.add(Pair(index, "${chars[index]}${chars[index + 1]}"))
    }
    var c1 = false
    pairs.forEach { pair ->
        if (!c1) {
            val matches = pairs.filter {
                it.second == pair.second && (it.first - pair.first).absoluteValue > 1
            }
            c1 = matches.isNotEmpty()
        }
    }
    var c2 = false
    (0 until chars.size - 2).forEach { index ->
        if (chars[index] == chars[index + 2]) c2 = true
    }
    return c1 && c2
}

fun checkIfNice1(str: String): Boolean {
    val bad = listOf("ab", "cd", "pq", "xy")
    bad.forEach {
        if (str.contains(it)) return false
    }
    val chars = str.toCharArray()
    var vowels = 0
    chars.forEach {
        when (it) {
            'a', 'e', 'i', 'o', 'u' -> vowels++
        }
    }
    if (vowels < 3) return false
    var foundOne = false
    (0 until chars.size - 1).forEach { index ->
        if (chars[index] == chars[index + 1]) foundOne = true
    }

    return foundOne
}