package y2020.d3

val rowTrees = ArrayList<Long>()
val rowTreesMy = ArrayList<Long>()

@Suppress("unused")
fun day(data: List<String>) {
    val matrix = mutableListOf<CharArray>()

    data.forEach { line ->
        matrix.add(line.toCharArray())
    }

    val moves = listOf(listOf(1, 1), listOf(3, 1), listOf(5, 1), listOf(7, 1), listOf(1, 2))
    val slopes = mutableListOf<Int>()
    moves.forEach { mv ->
        var x = 0
        var trees = 0
        (mv[1] until matrix.size step mv[1]).forEach { y ->
            x += mv[0]
            if (x >= matrix[0].size) x -= matrix[0].size
            if (matrix[y][x] == '#') {
                trees++
                if (mv[1] == 2) {
                    rowTreesMy.add(trees.toLong())
                }
            }
        }
        println("Move ${mv[0]}, ${mv[1]} is $trees")
        slopes.add(trees)
    }

    var tot = 1.toBigInteger()
    slopes.forEach { slope ->
        tot = tot.multiply(slope.toBigInteger())
    }
    println("Trees: $tot")
}
