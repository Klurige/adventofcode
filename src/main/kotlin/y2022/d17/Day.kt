package y2022.d17

import java.util.BitSet

fun BitSet.toByte(): Byte {
    var b = 1
    var acc = 0
    repeat(length()) {
        if (get(it)) acc += b
        b *= 2
    }
    return acc.toByte()
}

data class Pos(val row: Int, val col: Int)

abstract class Rock(private val shape: Set<Pos>) {
    private var position = Pos(0, 0)
    fun reset(top: Int) {
        position = Pos(top + 4, 2)
    }

    private fun top(): Int {
        return if (shape.isNotEmpty())
            shape.maxOf { it.row } + position.row
        else Int.MIN_VALUE
    }

    private fun bottom(): Int {
        return if (shape.isNotEmpty())
            shape.minOf { it.row } + position.row
        else Int.MIN_VALUE
    }

    private fun atRest(room: List<BitSet>): Boolean {
        shape.forEach { pos ->
            val s = Pos(pos.row + position.row - 1, pos.col + position.col)
            if (room.lastIndex >= s.row) {
                if (room.isEmpty()) return true
                if (room[s.row].get(s.col)) return true
            }
        }
        return false
    }

    fun move(jet: Char, room: List<BitSet>) {
        if (jet == '<') {
            val edge = shape.minOf { it.col } + position.col
            if (edge > 0) {
                var isBlocked = false
                shape.forEach { pos ->
                    val s = Pos(pos.row + position.row, pos.col + position.col - 1)
                    if (s.row >= 0) {
                        if (room.lastIndex >= s.row) {
                            if (room[s.row].get(s.col)) isBlocked = true
                        }
                    }
                }
                if (!isBlocked) position = Pos(position.row, position.col - 1)
            }
        } else {
            val edge = shape.maxOf { it.col } + position.col
            if (edge < 6) {
                var isBlocked = false
                shape.forEach { pos ->
                    val s = Pos(pos.row + position.row, pos.col + position.col + 1)
                    if (s.row >= 0) {
                        if (room.lastIndex >= s.row) {
                            if (room[s.row].get(s.col)) isBlocked = true
                        }
                    }
                }
                if (!isBlocked) position = Pos(position.row, position.col + 1)
            }
        }
    }

    fun drop(room: List<BitSet>): Boolean {
        if (atRest(room)) return false
        position = Pos(position.row - 1, position.col)
        return true
    }

    fun solidify(room: MutableList<BitSet>) {
        (bottom()..top()).forEach { row ->
            val s = shape.filter { it.row == row - position.row }.map { it.col + position.col }
            val bitset = BitSet(7)
            s.forEach { col ->
                bitset.set(col)
            }
            if (room.lastIndex >= row) room[row].or(bitset)
            else room.add(bitset)
        }
    }
}

object Rock1 : Rock(setOf(Pos(0, 0), Pos(0, 1), Pos(0, 2), Pos(0, 3)))
object Rock2 : Rock(setOf(Pos(0, 1), Pos(1, 0), Pos(1, 1), Pos(1, 2), Pos(2, 1)))
object Rock3 : Rock(setOf(Pos(0, 0), Pos(0, 1), Pos(0, 2), Pos(1, 2), Pos(2, 2)))
object Rock4 : Rock(setOf(Pos(0, 0), Pos(1, 0), Pos(2, 0), Pos(3, 0)))
object Rock5 : Rock(setOf(Pos(0, 0), Pos(1, 0), Pos(0, 1), Pos(1, 1)))
val rocks = listOf(Rock1, Rock2, Rock3, Rock4, Rock5)

data class State(val formation: ByteArray, val rockIndex: Int, val jetIndex: Int, val rocksDropped: Long, val roomSize: Long) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as State

        if (!formation.contentEquals(other.formation)) return false
        if (rockIndex != other.rockIndex) return false
        if (jetIndex != other.jetIndex) return false

        return true
    }

    override fun hashCode(): Int {
        var result = formation.contentHashCode()
        result = 31 * result + rockIndex
        result = 31 * result + jetIndex
        return result
    }
}

fun dropRocks(numRocks: Long, data: List<String>): Long {
    var rockIndex = 0
    val room = mutableListOf<BitSet>()
    val states = mutableSetOf<State>()
    val jets = data[0]
    var rocksDropped = 0L
    var isSearching = false
    var jetIndex = 0
    var cycleHeight = 0L
    while (rocksDropped < numRocks) {
        val rock = rocks[rockIndex++]
        if (rockIndex == rocks.size) rockIndex = 0
        rock.reset(room.lastIndex)
        var atRest = false
        while (!atRest) {
            val jet = jets[jetIndex++]
            if (jetIndex == jets.length) jetIndex = 0
            if (!isSearching && jetIndex == 0) {
                isSearching = true
            }
            rock.move(jet, room)
            atRest = !rock.drop(room)
        }
        rock.solidify(room)
        if (isSearching && cycleHeight == 0L) {
            val formation = room.subList(room.size - 10, room.size).map { it.toByte() }.toByteArray()
            val state = State(formation, rockIndex, jetIndex, rocksDropped, room.size.toLong())
            if (states.contains(state)) {
                val prevState: State = states.first { it == state }
                val cyclePeriod = state.rocksDropped - prevState.rocksDropped
                val remainingCycles = (numRocks - state.rocksDropped) / cyclePeriod
                rocksDropped += remainingCycles * cyclePeriod
                cycleHeight = (state.roomSize - prevState.roomSize) * remainingCycles
            } else {
                states.add(state)
            }
        }

        rocksDropped++
    }
    return room.size + cycleHeight
}

fun day(data: List<String>) {
    println("2022-12-17")

    println("1: ${dropRocks(2022,data)}")
    println("2: ${dropRocks(1000000000000, data)}")
}
