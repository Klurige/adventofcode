package y2022.d22

import isTest

data class FlatPos(val row: Int, val col: Int) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FlatPos

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

data class CubePos(val row: Int, val col: Int, val side: Int)

fun perform(pos: FlatPos, move: Move, map: MutableMap<FlatPos, Char>): FlatPos {
    var oldPos = pos
    when (move) {
        is Steps -> {
            var dir = map[oldPos]!!
            val maxCol = map.filter { it.key.row == pos.row }.maxOf { it.key.col }
            val minCol = map.filter { it.key.row == pos.row }.minOf { it.key.col }
            val maxRow = map.filter { it.key.col == pos.col }.maxOf { it.key.row }
            val minRow = map.filter { it.key.col == pos.col }.minOf { it.key.row }
            repeat(move.num) {
                when (dir) {
                    '>' -> {
                        val newPos = FlatPos(pos.row, if (oldPos.col == maxCol) minCol else oldPos.col + 1)
                        if (map[newPos] != '#') {
                            oldPos = newPos
                            map[oldPos] = dir
                        }
                    }

                    '<' -> {
                        val newPos = FlatPos(pos.row, if (oldPos.col == minCol) maxCol else oldPos.col - 1)
                        if (map[newPos] != '#') {
                            oldPos = newPos
                            map[oldPos] = dir
                        }
                    }

                    'v' -> {
                        val newPos = FlatPos(if (oldPos.row == maxRow) minRow else oldPos.row + 1, pos.col)
                        if (map[newPos] != '#') {
                            oldPos = newPos
                            map[oldPos] = dir
                        }
                    }

                    '^' -> {
                        val newPos = FlatPos(if (oldPos.row == minRow) maxRow else oldPos.row - 1, pos.col)
                        if (map[newPos] != '#') {
                            oldPos = newPos
                            map[oldPos] = dir
                        }
                    }

                    else -> TODO()
                }
            }
        }

        is Turn -> {
            val newDir = when (map[pos]!!) {
                '>' -> {
                    if (move.dir == 'R') 'v' else '^'
                }

                '<' -> {
                    if (move.dir == 'R') '^' else 'v'
                }

                'v' -> {
                    if (move.dir == 'R') '<' else '>'
                }

                '^' -> {
                    if (move.dir == 'R') '>' else '<'
                }

                else -> TODO()
            }
            map[pos] = newDir
        }
    }
    return oldPos
}

fun performCube(
    cubePos: CubePos,
    move: Move,
    cube: MutableMap<CubePos, FlatPos>,
    map: MutableMap<FlatPos, Char>
): CubePos {
    var oldPos = cubePos
    when (move) {
        is Steps -> {
            repeat(move.num) {
                var dir = map[cube[oldPos]!!]!!
                val newPos: CubePos = if(isTest) {
                    val length = 4
                    when (dir) {
                        '>' -> {
                            if (oldPos.col < length) CubePos(oldPos.row, oldPos.col + 1, oldPos.side)
                            else {
                                when (oldPos.side) {
                                    1 -> CubePos(length-oldPos.row+1, length, 6).also { dir = '<' }
                                    2 -> CubePos(oldPos.row, 1, 3).also { dir = '>' }
                                    3 -> CubePos(oldPos.row, 1, 4).also { dir = '>' }
                                    4 -> CubePos(1, length-oldPos.row+1, 6).also { dir = 'v' }
                                    5 -> CubePos(oldPos.row, 1, 6).also { dir = '>' }
                                    6 -> CubePos(length-oldPos.row+1, length, 1).also { dir = '<' }
                                    else -> TODO()
                                }
                            }
                        }

                        '<' -> {
                            if (oldPos.col > 1) CubePos(oldPos.row, oldPos.col - 1, oldPos.side)
                            else {
                                when (oldPos.side) {
                                    1 -> CubePos(1, oldPos.row, 3).also { dir = 'v' }
                                    2 -> CubePos(length, length-oldPos.row+1, 6).also { dir = '^' }
                                    3 -> CubePos(oldPos.row, length, 2).also { dir = '<' }
                                    4 -> CubePos(oldPos.row, length, 3).also { dir = '<' }
                                    5 -> CubePos(length, length-oldPos.row+1, 3).also { dir = '^' }
                                    6 -> CubePos(oldPos.row, length, 5).also { dir = '<' }
                                    else -> TODO()
                                }
                            }
                        }

                        'v' -> {
                            if (oldPos.row < length) CubePos(oldPos.row + 1, oldPos.col, oldPos.side)
                            else {
                                when (oldPos.side) {
                                    1 -> CubePos(1, oldPos.col, 4).also { dir = 'v' }
                                    2 -> CubePos(length, length-oldPos.col+1, 5).also { dir = '^' }
                                    3 -> CubePos(oldPos.row, 1, 5).also { dir = '>' }
                                    4 -> CubePos(1, oldPos.col, 5).also { dir = 'v' }
                                    5 -> CubePos(length, length-oldPos.col+1, 2).also { dir = '^' }
                                    6 -> CubePos(oldPos.row, 1, 2).also { dir = '>' }
                                    else -> TODO()
                                }
                            }
                        }

                        '^' -> {
                            if (oldPos.row > 1) CubePos(oldPos.row - 1, oldPos.col, oldPos.side)
                            else {
                                when (oldPos.side) {
                                    1 -> CubePos(1, oldPos.col, 2).also { dir = 'v' }
                                    2 -> CubePos(1, oldPos.col, 1).also { dir = 'v' }
                                    3 -> CubePos(oldPos.col, 1, 1).also { dir = '>' }
                                    4 -> CubePos(length, oldPos.col, 1).also { dir = '^' }
                                    5 -> CubePos(length, oldPos.col, 4).also { dir = '^' }
                                    6 -> CubePos(oldPos.row, length, 4).also { dir = '<' }
                                    else -> TODO()
                                }
                            }
                        }

                        else -> TODO()
                    }
                } else {
                    val length = 50
                    when (dir) {
                        '>' -> {
                            if (oldPos.col < length) CubePos(oldPos.row, oldPos.col + 1, oldPos.side)
                            else {
                                when (oldPos.side) {
                                    1 -> CubePos(oldPos.row, 1, 2).also { dir = '>' }
                                    2 -> CubePos(length-oldPos.row+1, length, 5).also { dir = '<' }
                                    3 -> CubePos(length, oldPos.row, 2).also { dir = '^' }
                                    4 -> CubePos(oldPos.row, 1, 5).also { dir = '>' }
                                    5 -> CubePos(length-oldPos.row+1, length, 2).also { dir = '<' }
                                    6 -> CubePos(length, oldPos.row, 5).also { dir = '^' }
                                    else -> TODO()
                                }
                            }
                        }

                        '<' -> {
                            if (oldPos.col > 1) CubePos(oldPos.row, oldPos.col - 1, oldPos.side)
                            else {
                                when (oldPos.side) {
                                    1 -> CubePos(length - oldPos.row +1, 1, 4).also { dir = '>' }
                                    2 -> CubePos(oldPos.row, length, 1).also { dir = '<' }
                                    3 -> CubePos(1, oldPos.row, 4).also { dir = 'v' }
                                    4 -> CubePos(length - oldPos.row +1, 1, 1).also { dir = '>' }
                                    5 -> CubePos(oldPos.row, length, 4).also { dir = '<' }
                                    6 -> CubePos(1,oldPos.row, 1).also { dir = 'v' }
                                    else -> TODO()
                                }
                            }
                        }

                        'v' -> {
                            if (oldPos.row < length) CubePos(oldPos.row + 1, oldPos.col, oldPos.side)
                            else {
                                when (oldPos.side) {
                                    1 -> CubePos(1, oldPos.col, 3).also { dir = 'v' }
                                    2 -> CubePos(oldPos.col, length, 3).also { dir = '<' }
                                    3 -> CubePos(1, oldPos.col, 5).also { dir = 'v' }
                                    4 -> CubePos(1, oldPos.col, 6).also { dir = 'v' }
                                    5 -> CubePos(oldPos.col, length, 6).also { dir = '<' }
                                    6 -> CubePos(1, oldPos.col,2).also { dir = 'v' }
                                    else -> TODO()
                                }
                            }
                        }

                        '^' -> {
                            if (oldPos.row > 1) CubePos(oldPos.row - 1, oldPos.col, oldPos.side)
                            else {
                                when (oldPos.side) {
                                    1 -> CubePos(oldPos.col, 1, 6).also { dir = '>' }
                                    2 -> CubePos(length, oldPos.col, 6).also { dir = '^' }
                                    3 -> CubePos(length, oldPos.col, 1).also { dir = '^' }
                                    4 -> CubePos(oldPos.col, 1, 3).also { dir = '>' }
                                    5 -> CubePos(length, oldPos.col, 3).also { dir = '^' }
                                    6 -> CubePos(length, oldPos.col, 4).also { dir = '^' }
                                    else -> TODO()
                                }
                            }
                        }

                        else -> TODO()
                    }
                }
                if (map[cube[newPos]!!]!! != '#') {
                    oldPos = newPos
                    map[cube[oldPos]!!] = dir
                }
            }
        }

        is Turn -> {
            val newDir = when (map[cube[cubePos]!!]!!) {
                '>' -> {
                    if (move.dir == 'R') 'v' else '^'
                }

                '<' -> {
                    if (move.dir == 'R') '^' else 'v'
                }

                'v' -> {
                    if (move.dir == 'R') '<' else '>'
                }

                '^' -> {
                    if (move.dir == 'R') '>' else '<'
                }

                else -> TODO()
            }
            map[cube[cubePos]!!] = newDir
        }
    }
    return oldPos
}

sealed class Move()
class Steps(val num: Int) : Move()
class Turn(val dir: Char) : Move()

fun dirToValue(ch: Char): Int = when (ch) {
    '>' -> 0
    '<' -> 2
    'v' -> 1
    '^' -> 3
    else -> TODO()
}

fun day(data: List<String>) {
    println("2022-12-22")

    val map = mutableMapOf<FlatPos, Char>()
    val path = data.last()
    data.forEachIndexed { row, line ->
        if (line.isNotEmpty() && !line.contains("[0-9]".toRegex())) {
            line.forEachIndexed { col, ch ->
                if (ch != ' ') {
                    map[FlatPos(row + 1, col + 1)] = ch
                }
            }
        }
    }

    val moves = mutableListOf<Move>()
    var steps = 0
    repeat(path.length) { index ->
        if (path[index].isDigit()) steps = steps * 10 + path[index].digitToInt()
        else {
            moves.add(Steps(steps))
            steps = 0
            moves.add(Turn(path[index]))
        }
    }
    if (steps != 0) moves.add(Steps(steps))

    val start = map.filter { it.key.row == 1 }.minBy { it.key.col }
    var pos = FlatPos(row = start.key.row, col = start.key.col)
    map[pos] = '>'
    moves.forEach { move ->
        pos = perform(pos, move, map)
        //    dump(map)
    }
    val dirVal = dirToValue(map[pos]!!)
    println("1: ${1000 * (pos.row) + 4 * (pos.col) + dirVal}")

    map.filterValues { it != '#' && it != '.' }.forEach { (flatPos, _) ->
        map[flatPos] = '.'
    }

    val cube = mutableMapOf<CubePos, FlatPos>()
    map.forEach {
        if(isTest){
            val length = 4
            when {
                it.key.row in (1..length) && it.key.col in (2*length + 1..3 * length) -> {
                    cube[CubePos(it.key.row, it.key.col - 2 * length, 1)] = it.key
                }

                it.key.row in (length + 1..2 * length) && it.key.col in (1..length) -> {
                    cube[CubePos(it.key.row - length, it.key.col, 2)] = it.key
                }

                it.key.row in (length + 1..2 * length) && it.key.col in (length + 1..2 * length) -> {
                    cube[CubePos(it.key.row - length, it.key.col - length, 3)] = it.key
                }

                it.key.row in (length + 1..2 * length) && it.key.col in (2* length +1..3* length) -> {
                    cube[CubePos(it.key.row - length, it.key.col - 2*length, 4)] = it.key
                }

                it.key.row in (2 * length + 1..3 * length) && it.key.col in (2 *length + 1..3 * length) -> {
                    cube[CubePos(it.key.row - 2 * length, it.key.col - 2* length, 5)] = it.key
                }

                it.key.row in (2 * length + 1..3 * length) && it.key.col in (3* length + 1..4* length) -> {
                    cube[CubePos(it.key.row - 2 * length, it.key.col - 3*length, 6)] = it.key
                }
            }
        } else {
            val length = 50
            when {
                it.key.row in (1..length) && it.key.col in (length + 1..2 * length) -> {
                    cube[CubePos(it.key.row, it.key.col - length, 1)] = it.key
                }

                it.key.row in (1..length) && it.key.col in (2 * length + 1..3 * length) -> {
                    cube[CubePos(it.key.row, it.key.col - 2 * length, 2)] = it.key
                }

                it.key.row in (length + 1..2 * length) && it.key.col in (length + 1..2 * length) -> {
                    cube[CubePos(it.key.row - length, it.key.col - length, 3)] = it.key
                }

                it.key.row in (2 * length + 1..3 * length) && it.key.col in (1..length) -> {
                    cube[CubePos(it.key.row - 2 * length, it.key.col, 4)] = it.key
                }

                it.key.row in (2 * length + 1..3 * length) && it.key.col in (length + 1..2 * length) -> {
                    cube[CubePos(it.key.row - 2 * length, it.key.col - length, 5)] = it.key
                }

                it.key.row in (3 * length + 1..4 * length) && it.key.col in (1..length) -> {
                    cube[CubePos(it.key.row - 3 * length, it.key.col, 6)] = it.key
                }
            }
        }
    }
    var cubePos = CubePos(row = 1, col = 1, side = 1)
    map[cube[cubePos]!!] = '>'
    moves.forEach { move ->
        cubePos = performCube(cubePos, move, cube, map)
    }

    println("2: ${1000 * (cube[cubePos]!!.row) + 4 * (cube[cubePos]!!.col) + dirToValue(map[cube[cubePos]!!]!!)}")
}


fun dump(map: Map<FlatPos, Char>) {
    val maxRow = map.maxOf { it.key.row }
    val maxCol = map.maxOf { it.key.col }
    (0..maxRow).forEach { row ->
        (0..maxCol).forEach { col ->
            if (map.containsKey(FlatPos(row, col))) {
                print(map[FlatPos(row, col)])
            } else print(' ')
        }
        println()
    }
    println()
}
