package y2021.d14

val insertions = mutableSetOf<Pair<String, Char>>()

fun day(data: List<String>) {
    val iter = 1
    var input = ""
    data.forEach { line ->
        if (line.isNotEmpty()) {
            if (input.isEmpty()) {
                input = line
            } else {
                val parts = line.split(' ')
                insertions.add(Pair(parts.first(), parts.last()[0]))
            }
        }
    }

    var pairs = input.windowed(2).groupingBy { it }.eachCount().mapValues { it.value.toLong() }

    repeat(iter) {
        pairs = expand(pairs)
    }

    val freq = mutableMapOf<Char, Long>().withDefault { 0 }

    pairs.forEach { (pair, count) ->
        freq[pair[0]] = freq.getValue(pair[0]) + count
        freq[pair[1]] = freq.getValue(pair[1]) + count
    }
    freq[input.first()] = freq[input.first()]!! + 1
    freq[input.last()] = freq[input.last()]!! + 1

    val halves = freq.mapValues { it.value / 2 }
    val max = halves.maxByOrNull { it.value }
    val min = halves.minByOrNull { it.value }

    println("${max!!.value - min!!.value}")
}

fun expand(pairs: Map<String, Long>): Map<String, Long> {
    val expanded = mutableMapOf<String, Long>().withDefault { 0 }
    pairs.forEach { (pair, count) ->
        val key = insertions.find { it.first == pair }!!.second
        val p1 = "${pair[0]}$key"
        val p2 = "$key${pair[1]}"
        expanded.put(p1, expanded.getValue(p1) + count)
        expanded.put(p2, expanded.getValue(p2) + count)
    }
    return expanded
}