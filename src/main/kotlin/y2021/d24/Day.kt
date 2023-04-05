package y2021.d24

fun day(data: List<String>) {
    val part2 = true
    val blocks = data.chunked(18)
    val result = MutableList(14) { -1 }
    val buffer = ArrayDeque<Pair<Int, Int>>()
    fun List<String>.lastOf(command: String) = last { it.startsWith(command) }.split(" ").last().toInt()
    val best = if (part2) 1 else 9
    blocks.forEachIndexed { index, instructions ->
        if ("div z 26" in instructions) {
            val offset = instructions.lastOf("add x")
            val (lastIndex, lastOffset) = buffer.removeFirst()
            val difference = offset + lastOffset
            if (difference >= 0) {
                result[lastIndex] = if (part2) best else best - difference
                result[index] = if (part2) best + difference else best
            } else {
                result[lastIndex] = if (part2) best - difference else best
                result[index] = if (part2) best else best + difference
            }
        } else buffer.addFirst(index to instructions.lastOf("add y"))
    }

    println("${result.joinToString("").toLong()}")
}
