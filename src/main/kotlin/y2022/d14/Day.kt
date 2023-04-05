package y2022.d14


enum class Contents(val ch: Char) { SAND('o'), ROCK('#') }
data class Point(val row: Int, val col: Int) {
    fun fallsTo(): List<Point> {
        return listOf(
            Point(row + 1, col),
            Point(row + 1, col - 1),
            Point(row + 1, col + 1)
        )
    }
}

fun day(data: List<String>) {
    println("2022-12-14")

    var res1 = 0
    var res2 = 0

    val cave = mutableMapOf<Point, Contents>()
    data.forEach { line ->
        val coords = line.split(" -> ").map {
            val a = it.split(",")
            val b = a.map { it.toInt() }
            Point(b[1], b[0])
        }
        if (coords.size == 1) {
            cave[coords[0]] = Contents.ROCK
        } else {
            coords.windowed(2, 1).forEach { cs ->
                val rMin = minOf(cs[0].row, cs[1].row)
                val rMax = maxOf(cs[0].row, cs[1].row)
                val cMin = minOf(cs[0].col, cs[1].col)
                val cMax = maxOf(cs[0].col, cs[1].col)
                (rMin..rMax).forEach { row ->
                    (cMin..cMax).forEach { col ->
                        cave[Point(row, col)] = Contents.ROCK
                    }
                }
            }
        }
    }
    val rMax = cave.keys.maxOf { it.row }
    while (!cave.containsKey(Point(0, 500))) {
        var sand = Point(0, 500)
        var atRest = false
        while (!atRest) {

            run pour@{
                sand.fallsTo().forEach { c ->
                    if (!cave.containsKey(c)) {
                        sand = c
                        if (sand.row >= rMax + 1) {
                            if(res1 == 0) res1 = res2
                            atRest = true
                            return@forEach
                        }
                        return@pour
                    }
                }

                cave[sand] = Contents.SAND
                res2++
                atRest = true
            }
        }
    }

    println("1: $res1")
    println("2: $res2")
}
