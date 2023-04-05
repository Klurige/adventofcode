package y2022.d24

import kotlin.math.abs

enum class Direction {
    UP {
        override fun toString(): String {
            return "^"
        }
    },
    DOWN {
        override fun toString(): String {
            return "v"
        }
    },
    LEFT {
        override fun toString(): String {
            return "<"
        }
    },
    RIGHT {
        override fun toString(): String {
            return ">"
        }
    }
}

data class Position(val row: Int, val col: Int)
data class Blizzard(
    val pos: Position,
    val dir: Direction
) {
    override fun toString(): String {
        return dir.toString()
    }
}

var maxRow = 0
var maxCol = 0
var blizzardsRepeats = 0
var entryCol = 0
var exitCol = 0
val blizzardsMap = mutableMapOf<Int, List<Blizzard>>()

fun day(inputData: List<String>) {
    println("2022-12-24")

    val blizzards = mutableListOf<Blizzard>()
    maxRow = inputData.size - 1
    maxCol = inputData.first().length - 1
    blizzardsRepeats = lcm(maxRow-1, maxCol -1)

    entryCol = inputData.first().indexOfFirst { it == '.' }
    exitCol = inputData.last().indexOfFirst { it == '.' }
    inputData.forEachIndexed { lineNumber, line ->
        line.forEachIndexed { columnNumber, char ->
            when (char) {
                '<' -> {
                    blizzards.add(Blizzard(Position(lineNumber, columnNumber), Direction.LEFT))
                }

                '>' -> {
                    blizzards.add(Blizzard(Position(lineNumber, columnNumber), Direction.RIGHT))
                }

                '^' -> {
                    blizzards.add(Blizzard(Position(lineNumber, columnNumber), Direction.UP))
                }

                'v' -> {
                    blizzards.add(Blizzard(Position(lineNumber, columnNumber), Direction.DOWN))
                }
            }
        }
    }
    blizzardsMap[0] = blizzards
    //println("Part1: ${part1()}")
  println("Part2: ${part2()}")
}
fun lcm(n1: Int, n2: Int): Int {
    var gcd = 1

    var i = 1
    while (i <= n1 && i <= n2) {
        // Checks if i is factor of both integers
        if (n1 % i == 0 && n2 % i == 0)
            gcd = i
        ++i
    }

    return  n1 * n2 / gcd
}

fun advanceBlizzards(next: Int): List<Blizzard> {
    val normalised = next % blizzardsRepeats
    if (!blizzardsMap.containsKey(normalised)) {
        var prev = normalised - 1
        while (!blizzardsMap.containsKey(prev)) prev--
        var knownBlizzards = blizzardsMap[prev]!!
        while (prev < normalised) {
            prev++
            val newBlizzards = mutableListOf<Blizzard>()
            knownBlizzards.forEach { blizzard ->
                val nextPos = when (blizzard.dir) {
                    Direction.UP -> {
                        val row = if (blizzard.pos.row > 1) {
                            blizzard.pos.row - 1
                        } else {
                            maxRow - 1
                        }
                        Position(row, blizzard.pos.col)
                    }

                    Direction.DOWN -> {
                        val row = if (blizzard.pos.row < maxRow - 1) {
                            blizzard.pos.row + 1
                        } else {
                            1
                        }
                        Position(row, blizzard.pos.col)
                    }

                    Direction.LEFT -> {
                        val col = if (blizzard.pos.col > 1) {
                            blizzard.pos.col - 1
                        } else {
                            maxCol - 1
                        }
                        Position(blizzard.pos.row, col)
                    }

                    Direction.RIGHT -> {
                        val col = if (blizzard.pos.col < maxCol - 1) {
                            blizzard.pos.col + 1
                        } else {
                            1
                        }
                        Position(blizzard.pos.row, col)
                    }
                }
                newBlizzards.add(Blizzard(nextPos, blizzard.dir))
            }
            blizzardsMap[prev] = newBlizzards
            knownBlizzards = newBlizzards
        }
    }
    return blizzardsMap[normalised]!!
}

fun isWall(pos: Position): Boolean {
    if (pos.col <= 0 || pos.col >= maxCol) return true
    if (pos.row == 0 && pos.col != entryCol) return true
    if (pos.row == maxRow && pos.col != exitCol) return true
    return pos.row < 0 || pos.row > maxRow
}

fun part1(): Int {
    return calcSteps(1, Position(0, entryCol), Position(maxRow, exitCol)) - 1
}
fun part2(): Int {
    val journey1 = calcSteps(1, Position(0, entryCol), Position(maxRow, exitCol)) - 1
    val journey2 = calcSteps(journey1, Position(maxRow, exitCol), Position(0, entryCol)) - 1
    return  calcSteps(journey2, Position(0, entryCol), Position(maxRow, exitCol)) - 1
}

fun calcSteps(startStep:Int, startPos:Position, endPosition: Position): Int {
    val moves = mutableListOf<Pair<Position, Int>>()
    val visited = mutableSetOf<Pair<Position, Int>>()
    var minSteps = Int.MAX_VALUE
    moves.add(Pair(startPos, startStep))
    while (moves.isNotEmpty()) {
        val move = moves.minBy { manhattan(it.first, endPosition) }
        moves.remove(move)
        val (pos, step) = move
        if (pos == endPosition) {
            if (step + 1 < minSteps) {
                minSteps = step + 1
                moves.removeIf { it.second >= minSteps }
            }
        } else {
            if (step + manhattan(pos, endPosition) < minSteps) {
                val nextStep = step + 1
                val newBlizzards = advanceBlizzards(nextStep)
                val nextPositions = listOf(
                    Position(pos.row, pos.col - 1),
                    Position(pos.row, pos.col + 1),
                    Position(pos.row - 1, pos.col),
                    Position(pos.row + 1, pos.col)
                )
                nextPositions.forEach { nextPos ->
                    if (!isWall(nextPos)) {
                        if (!visited.contains(Pair(nextPos, nextStep))) {
                            if (newBlizzards.find { it.pos == nextPos } == null) {
                                visited.add(Pair(nextPos, nextStep))
                                moves.add(Pair(nextPos, nextStep))
                            }
                        }
                    }
                }
                if (!visited.contains(Pair(pos, nextStep))) {
                    if (newBlizzards.find { it.pos == pos } == null) {
                        visited.add(Pair(pos, nextStep))
                        moves.add(0, Pair(pos, nextStep))
                    }
                }

            }
        }
    }
    return minSteps
}

fun manhattan(first: Position, second:Position): Int {
    return abs(first.row - second.row )+ abs(first.col-second.col)

}

fun dump(blizzards: List<Blizzard>, pos: Position, step: Int) {
    println("Step $step")
    (0..maxCol).forEach {
        if (entryCol == it) {
            if (pos.row > 0) print('.') else print('E')
        } else {
            print('#')
        }
    }
    println()
    (1 until maxRow).forEach { rowNumber ->
        print('#')
        val rowBlizzards = blizzards.filter { it.pos.row == rowNumber }
        (1 until maxCol).forEach { colNumber ->
            if (pos.row == rowNumber && pos.col == colNumber) {
                if (rowBlizzards.filter { it.pos.col == colNumber }.isEmpty()) {
                    print('E')
                } else {
                    print('*')
                }
            } else
                rowBlizzards.filter { it.pos.col == colNumber }.also {
                    when (it.size) {
                        0 -> print('.')
                        1 -> print(it.first())
                        in 2..9 -> print(it.size)
                        else -> print('M')
                    }
                }
        }
        println('#')
    }
    (0..maxCol).forEach {
        if (exitCol == it) {
            if (pos.row < maxRow) print('.') else print('E')
        } else {
            print('#')
        }
    }
    println()
    println()
}
