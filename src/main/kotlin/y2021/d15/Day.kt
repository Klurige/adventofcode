package y2021.d15

lateinit var maze: Array<Array<Point>>

fun day(data: List<String>) {
    val map = mutableListOf<IntArray>()

    data.forEach { line ->
        map.add(line.toList().map { it.code - '0'.code }.toIntArray())
    }
    var isUpdated: Boolean

    maze = Array(map.size) { row ->
        map[row].mapIndexed { col, risk ->
            Point(row, col, risk, Int.MAX_VALUE)
        }.toTypedArray()
    }

    maze[0][0] = Point(0, 0, 0, 0)

    isUpdated = true
    while (isUpdated) {
        isUpdated = false
        maze.forEach { row ->
            row.forEach { pt ->
                val pts = pt.getNeighbours(maze.last().last())
                pts.forEach { n ->
                    val newRisk = pt.totRisk + n.risk
                    if (newRisk < n.totRisk) {
                        n.totRisk = newRisk
                        isUpdated = true
                    }
                }
            }
        }
    }
    println("Part1: ${maze.last().last().totRisk}")

    maze = Array(map.size * 5) {
        Array(5 * map[0].size) { Point() }
    }

    map.indices.forEach { row ->
        map[row].indices.forEach { col ->
            (0..4).forEach { mr ->
                (0..4).forEach { mc ->
                    val r = mr * map.size + row
                    val c = mc * map[row].size + col
                    var risk = map[row][col] + mc + mr
                    while (risk > 9) risk -= 9
                    maze[mr * map.size + row][mc * map[row].size + col] = Point(r, c, risk, Int.MAX_VALUE)
                }
            }
        }
    }
    maze[0][0] = Point(0, 0, 0, 0)

    isUpdated = true
    while (isUpdated) {
        isUpdated = false
        maze.forEach { row ->
            row.forEach { pt ->
                val pts = pt.getNeighbours(maze.last().last())
                pts.forEach { n ->
                    val newRisk = pt.totRisk + n.risk
                    if (newRisk < n.totRisk) {
                        n.totRisk = newRisk
                        isUpdated = true
                    }
                }
            }
        }
    }
    println("Part2: ${maze.last().last().totRisk}")

}

data class Point(val row: Int = -1, val col: Int = -1, val risk: Int = -1, var totRisk: Int = -1) {
    private lateinit var neighbours: List<Point>
    fun getNeighbours(end: Point): List<Point> {
        if (!this::neighbours.isInitialized) {
            val pts = mutableListOf<Point>()
            if (row > 0) {
                pts.add(maze[row - 1][col])
            }
            if (col < end.col) {
                pts.add(maze[row][col + 1])
            }
            if (row < end.row) {
                pts.add(maze[row + 1][col])
            }
            if (col > 0) {
                pts.add(maze[row][col - 1])
            }
            neighbours = pts
        }
        return neighbours
    }

}
