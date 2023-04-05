package y2022.d12

data class Pos(val row: Int, val col: Int, val height: Int = Int.MIN_VALUE, var steps: Int = Int.MAX_VALUE) {

    fun candidates(map: MutableMap<Pos, Int>): List<Pos> {
        val c = mutableSetOf<Pos>()
        map.keys.firstOrNull { it == Pos(row - 1, col) }?.let { c.add(it) }
        map.keys.firstOrNull { it == Pos(row + 1, col) }?.let { c.add(it) }
        map.keys.firstOrNull { it == Pos(row, col + 1) }?.let { c.add(it) }
        map.keys.firstOrNull { it == Pos(row, col - 1) }?.let { c.add(it) }

        return c.filter {
            map[it]!! > map[this]!! + 1 && it.height >= height - 1
        }.onEach { map[it] = map[this]!! + 1 }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Pos
        if (row != other.row) return false
        if (col != other.col) return false
        return true
    }

    override fun hashCode(): Int {
        var result = row
        result = 31 * result + col
        return result
    }
}

fun day(data: List<String>) {
    println("2022-12-12")

    lateinit var start: Pos
    lateinit var end: Pos
    val map = mutableMapOf<Pos, Int>()
    data.forEachIndexed { row, line ->
        line.forEachIndexed { col, ch ->
            when (ch) {
                'S' -> {
                    start = Pos(row, col, 0)
                    map[start] = Int.MAX_VALUE
                }

                'E' -> {
                    end = Pos(row, col, 'z' - 'a')
                    map[end] = 0
                }

                else -> map[Pos(row, col, ch - 'a')] = Int.MAX_VALUE
            }
        }
    }

    val queue = mutableListOf<Pos>()
    queue.add(end)
    while (queue.isNotEmpty()) {
        val tree = queue.removeLast()
        if (tree != start) {
            queue.addAll(tree.candidates(map))
        }
    }

    println("1: ${map[start]!!}")
    println("2: ${map.filter { it.key.height == 0 }.minOf { it.value }}")
}
