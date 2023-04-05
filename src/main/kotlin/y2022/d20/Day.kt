package y2022.d20


fun part1(data: List<String>): Int {
    val digits = data.withIndex().map {
        Pair(it.index, it.value.toInt())
    }.toMutableList()

    repeat(digits.size) { index ->
        val item = digits.withIndex().first { it.value.first == index }
        val newIndex = (item.index + item.value.second).mod(digits.lastIndex)
        digits.removeAt(item.index)
        digits.add(newIndex, item.value)
    }
    val zero = digits.withIndex().first { it.value.second == 0 }
    return setOf(1000, 2000, 3000).sumOf { digits[(zero.index + it) % digits.size].second }
}

fun part2(data: List<String>): Long {
    val key = 811589153L
    val digits = data.withIndex().map {
        Pair(it.index, it.value.toInt() * key)
    }.toMutableList()

    repeat(10) {
        repeat(digits.size) { index ->
            val item = digits.withIndex().first { it.value.first == index }
            val newIndex = (item.index + item.value.second).mod(digits.lastIndex).toLong()
            digits.removeAt(item.index)
            digits.add(newIndex.toInt(), item.value)
        }
    }
    val zero = digits.withIndex().first { it.value.second.toInt() == 0 }
    return setOf(1000, 2000, 3000).sumOf { digits[(zero.index + it) % digits.size].second }
}

fun day(data: List<String>) {
    println("2022-12-20")

    println("1: ${part1(data)}")
    println("2: ${part2(data)}")
}
