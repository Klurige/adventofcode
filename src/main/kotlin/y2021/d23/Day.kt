package y2021.d23

import java.util.*
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

const val isTest = true

var leastEnergy = Int.MAX_VALUE

fun day(data: List<String>) {
/*
    val cheat = Day23(data)
    val p1 =cheat.solvePart1()
    println("part1: $p1")
    val p2 = cheat.solvePart2()
    println("part2: $p2")
*/

    var b = initialBurrow(2, isTest)
/*
    b.print()
    println("${b.isComplete()}")

    // Kan optimeras. 3 steg färre med C, 4 steg färre med A (45272 istället för 45576)
    b = b.duplicateAndMove(8, 0)!!
    b = b.duplicateAndMove(8, 1)!!
    b = b.duplicateAndMove(8, 10)!!
    b = b.duplicateAndMove(8, 9)!!
    b = b.duplicateAndMove(2, 8)!!
    b = b.duplicateAndMove(2, 8)!!
    b = b.duplicateAndMove(2, 8)!! //
    b = b.duplicateAndMove(2, 7)!!
    b = b.duplicateAndMove(1, 2)!!
    b = b.duplicateAndMove(4, 2)!!
    b = b.duplicateAndMove(6, 1)!!
    b = b.duplicateAndMove(4, 5)!!
    b = b.duplicateAndMove(4, 2)!!
    b = b.duplicateAndMove(5, 4)!!
    b = b.duplicateAndMove(6, 4)!!
    b = b.duplicateAndMove(7, 4)!!
    b = b.duplicateAndMove(6, 4)!!
    b = b.duplicateAndMove(6, 2)!!
    b = b.duplicateAndMove(6, 8)!!
    b = b.duplicateAndMove(9, 6)!!
    b = b.duplicateAndMove(10, 6)!!
    b = b.duplicateAndMove(1, 6)!!
    b = b.duplicateAndMove(0, 6)!!

    println("${b.isComplete()}")
*/
    val stack = Stack<Burrow>()
    stack.push(initialBurrow(2, isTest))

    while(stack.isNotEmpty()) {
//        println("Stack: ${stack.size}")
        val burrow = stack.pop()
        (0..10).forEach { from ->
            (0..10).forEach { to ->
                if(from != to) {
                    val next = burrow.duplicateAndMove(from, to)
                    if(next != null) {
                        if(!next.isComplete()) {
                            if(next.energy < leastEnergy) {
                                stack.push(next)
                            }
                        } else {
                            if(next.energy < leastEnergy) leastEnergy = next.energy
                        }
                    }
                }
            }
        }
    }

    println("Least: $leastEnergy")
}

data class Burrow(
    val amphipods: MutableList<Amphipod> = mutableListOf(),
    val maxLevel: Int
) {
    var energy: Int = 0


    fun print() {
        println(" 01234567890 ")
        println("#############")
        val lvl0 = charArrayOf('#', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '#')
        amphipods.forEach { amphipod ->
            if (amphipod.lvl == 0) {
                lvl0[amphipod.pos + 1] = amphipod.getName()
            }
        }
        println(lvl0.joinToString(""))
        (1..maxLevel).forEach { lvl ->
            val a = if (lvl == 1) charArrayOf('#', '#', '#', '.', '#', '.', '#', '.', '#', '.', '#', '#', '#')
            else charArrayOf(' ', ' ', '#', '.', '#', '.', '#', '.', '#', '.', '#', ' ', ' ')
            amphipods.forEach { amphipod ->
                if (amphipod.lvl == lvl) {
                    a[amphipod.pos + 1] = amphipod.getName()
                }
            }
            println(a.joinToString(""))
        }
        println("  #########      Energy: $energy")
        println()
    }

    val hpos = intArrayOf(0, 1, 3, 5, 7, 9, 10)

    fun getAmphipodAt(pos: Int): Amphipod? {
        if (pos in hpos) {
            return amphipods.find { it.pos == pos }
        } else {
            run level@{
                (1..maxLevel).forEach { l ->
                    val a = amphipods.find { it.pos == pos && it.lvl == l }
                    if (a != null) {
                        return a
                    }
                }
            }
        }
        return null
    }

    fun getFreeRoomLevel(room: Int): Int {
        val inRoom = amphipods.filter {
            it.pos == room
        }
        if(inRoom.isEmpty()) return maxLevel
        val done = inRoom.filter { isDone(it) }
        if(done.isEmpty()) return 0
        return maxLevel - done.size
    }

    fun isDone(a: Amphipod): Boolean {
        if (a.lvl == 0) return false
        if (a.pos != a.room) return false

        (a.lvl + 1..maxLevel).forEach { l ->
            val b = amphipods.find { it.lvl == l && it.pos == a.room }
            if (b == null || a.getName() != b.getName()) return false
        }
        return true
    }
    /**
     * Create a duplicate of burrow and move amphipod in "from" to "to".
     */
    fun duplicateAndMove(from: Int, to: Int): Burrow? {
        val amphipod: Amphipod = getAmphipodAt(from) ?: return null
        val steps: Int
        var lvl = 0
        if (from in hpos) {
            if (to != amphipod.room) return null
            if(from < to) {
                if (amphipods.find { it.lvl == 0 && it.pos in (from + 1..to)} != null) return null
            } else {
                if (amphipods.find { it.lvl == 0 && it.pos in (to .. from-1)} != null) return null
            }
            lvl = getFreeRoomLevel(to)
            if (lvl < 1) return null
            steps = abs(from - amphipod.room) + lvl
        } else {
            if(isDone(amphipod)) return null
            if(amphipod is D) {
                if(to != amphipod.room) return null
            }
            (min(from,to)..max(from,to)).forEach { p ->
                val a = amphipods.find { it.pos == p && it.lvl == 0 }
                if (a != null && a != amphipod) return null
            }
            steps = if (to in hpos) {
                val dest = getAmphipodAt(to)
                if (dest != null) return null
                amphipod.lvl + abs(amphipod.pos - to)
            } else {
                if(to != amphipod.room) return null
                lvl = getFreeRoomLevel(to)
                if (lvl < 1) return null
                abs(from - amphipod.room) + lvl + amphipod.lvl
            }
        }
        val dup = Burrow(maxLevel = maxLevel)
        amphipods.forEach { a ->
            val copy = when (a) {
                is A -> A(lvl = a.lvl, pos = a.pos)
                is B -> B(lvl = a.lvl, pos = a.pos)
                is C -> C(lvl = a.lvl, pos = a.pos)
                is D -> D(lvl = a.lvl, pos = a.pos)
            }
            dup.amphipods.add(copy)
        }
        val a = dup.amphipods.find { (it.lvl == amphipod.lvl && it.pos == amphipod.pos) }!!
        a.lvl = lvl
        a.pos = to
        dup.energy = energy + steps * a.energy
        //dup.print()
        return dup

    }

    fun isComplete(): Boolean {
        if(amphipods.find { it.lvl == 0 } != null) return false
        (1..maxLevel).forEach { lvl ->
            val a = amphipods.filter { it.lvl == lvl && it.pos == it.room }
            if(a.size != 4) return false
        }
        return true
    }
}

sealed class Amphipod(
    val energy: Int,
    val room: Int,
    var lvl: Int,
    var pos: Int
) {
    abstract fun getName(): Char
}

class A(pos: Int, lvl: Int) : Amphipod(1, room = 2, lvl = lvl, pos = pos) {
    override fun getName() = 'A'
}

class B(pos: Int, lvl: Int) : Amphipod(10, room = 4, lvl = lvl, pos = pos) {
    override fun getName() = 'B'
}

class C(pos: Int, lvl: Int) : Amphipod(100, room = 6, lvl = lvl, pos = pos) {
    override fun getName() = 'C'
}

class D(pos: Int, lvl: Int) : Amphipod(1000, room = 8, lvl = lvl, pos = pos) {
    override fun getName() = 'D'
}


fun initialBurrow(part: Int, isTest: Boolean): Burrow {
    val burrow = Burrow(maxLevel = if (part == 1) 2 else 4)
    var lvl = 1
    if (isTest) {
        burrow.amphipods.add(B(lvl = lvl, pos = 2))
        burrow.amphipods.add(C(lvl = lvl, pos = 4))
        burrow.amphipods.add(B(lvl = lvl, pos = 6))
        burrow.amphipods.add(D(lvl = lvl, pos = 8))
        lvl++
        if (part == 2) {
            burrow.amphipods.add(D(lvl = lvl, pos = 2))
            burrow.amphipods.add(C(lvl = lvl, pos = 4))
            burrow.amphipods.add(B(lvl = lvl, pos = 6))
            burrow.amphipods.add(A(lvl = lvl, pos = 8))
            lvl++
            burrow.amphipods.add(D(lvl = lvl, pos = 2))
            burrow.amphipods.add(B(lvl = lvl, pos = 4))
            burrow.amphipods.add(A(lvl = lvl, pos = 6))
            burrow.amphipods.add(C(lvl = lvl, pos = 8))
            lvl++
        }
        burrow.amphipods.add(A(lvl = lvl, pos = 2))
        burrow.amphipods.add(D(lvl = lvl, pos = 4))
        burrow.amphipods.add(C(lvl = lvl, pos = 6))
        burrow.amphipods.add(A(lvl = lvl, pos = 8))
    } else {
        burrow.amphipods.add(D(lvl = lvl, pos = 2))
        burrow.amphipods.add(A(lvl = lvl, pos = 4))
        burrow.amphipods.add(B(lvl = lvl, pos = 6))
        burrow.amphipods.add(C(lvl = lvl, pos = 8))
        lvl++
        if (part == 2) {
            burrow.amphipods.add(D(lvl = lvl, pos = 2))
            burrow.amphipods.add(C(lvl = lvl, pos = 4))
            burrow.amphipods.add(B(lvl = lvl, pos = 6))
            burrow.amphipods.add(A(lvl = lvl, pos = 8))
            lvl++
            burrow.amphipods.add(D(lvl = lvl, pos = 2))
            burrow.amphipods.add(B(lvl = lvl, pos = 4))
            burrow.amphipods.add(A(lvl = lvl, pos = 6))
            burrow.amphipods.add(C(lvl = lvl, pos = 8))
            lvl++
        }
        burrow.amphipods.add(B(lvl = lvl, pos = 2))
        burrow.amphipods.add(A(lvl = lvl, pos = 4))
        burrow.amphipods.add(D(lvl = lvl, pos = 6))
        burrow.amphipods.add(C(lvl = lvl, pos = 8))
    }
    return burrow
}
class Day23(input: List<String>) {

    private val initialState = State.from(input)
    private val initialStateExtended = State.from(input.take(3) + "  #D#C#B#A#  " + "  #D#B#A#C#  " + input.takeLast(2))

    fun solvePart1() = organizeAmphipods(initialState)

    fun solvePart2() = organizeAmphipods(initialStateExtended)

    private fun organizeAmphipods(initialState: State): Int {
        val toVisit = PriorityQueue<StateWithCost>().apply { add(StateWithCost(initialState, 0)) }
        val visited = mutableSetOf<StateWithCost>()
        val currentCosts = mutableMapOf<State, Int>().withDefault { Int.MAX_VALUE }

        while (toVisit.isNotEmpty()) {
            val current = toVisit.poll().also { visited.add(it) }
            current.state.nextPossibleStates().forEach { next ->
                if (!visited.contains(next)) {
                    val newCost = current.cost + next.cost
                    if (newCost < currentCosts.getValue(next.state)) {
                        currentCosts[next.state] = newCost
                        toVisit.add(StateWithCost(next.state, newCost))
                    }
                }
            }
        }

        return currentCosts.keys.first { it.isFinished() }.let { currentCosts.getValue(it) }
    }

    private data class State(private val config: List<List<Char>>) {
        private val hallway = config[0]
        private val rooms = config.drop(1)
        private val destinationRooms = mapOf(
            'A' to Room('A', 2, rooms.map { row -> row[2] }),
            'B' to Room('B', 4, rooms.map { row -> row[4] }),
            'C' to Room('C', 6, rooms.map { row -> row[6] }),
            'D' to Room('D', 8, rooms.map { row -> row[8] })
        )
        private val multipliers = mapOf('A' to 1, 'B' to 10, 'C' to 100, 'D' to 1000)
        private val legalHallwayIndexes
            get() = listOf(0, 1, 3, 5, 7, 9, 10).filter { hallway[it] == '.' }

        fun isFinished() = destinationRooms.values.all { it.hasOnlyValidAmphipods() }

        fun nextPossibleStates(): List<StateWithCost> {
            return buildList {
                amphipodsInHallwayThatCanMove().forEach {
                    val room = destinationRooms.getValue(it.value)
                    if (hallwayPathIsClear(it.index, room.index)) {
                        val y = room.content.lastIndexOf('.') + 1
                        val cost = (abs(it.index - room.index) + y) * multipliers.getValue(it.value)
                        add(StateWithCost(State(
                            config.map { row -> row.toMutableList() }.apply {
                                get(0)[it.index] = '.'
                                get(y)[room.index] = it.value
                            }
                        ), cost))
                    }
                }
                roomsWithWrongAmphipods().forEach { room ->
                    val toMove = room.content.withIndex().first { it.value != '.' }
                    legalHallwayIndexes.forEach { index ->
                        if (hallwayPathIsClear(index, room.index)) {
                            val y = toMove.index + 1
                            val cost = (abs(room.index - index) + y) * multipliers.getValue(toMove.value)
                            add(StateWithCost(State(
                                config.map { row -> row.toMutableList() }.apply {
                                    get(y)[room.index] = '.'
                                    get(0)[index] = toMove.value
                                }
                            ), cost))
                        }
                    }
                }
            }
        }

        private fun amphipodsInHallwayThatCanMove(): List<IndexedValue<Char>> {
            return hallway.withIndex().filter {
                it.value.isLetter() && destinationRooms.getValue(it.value).isEmptyOrHasAllValidAmphipods()
            }
        }

        private fun roomsWithWrongAmphipods() = destinationRooms.values.filter { it.hasAmphipodsWithWrongType() }

        private fun hallwayPathIsClear(start: Int, end: Int): Boolean {
            return hallway.slice(
                when (start > end) {
                    true -> (start - 1) downTo end
                    false -> (start + 1)..end
                }
            ).all { it == '.' }
        }

        companion object {
            fun from(input: List<String>) = State(input.drop(1).dropLast(1).map { it.drop(1).dropLast(1).toList() })
        }
    }

    private class StateWithCost(val state: State, val cost: Int) : Comparable<StateWithCost> {
        override fun compareTo(other: StateWithCost) = cost.compareTo(other.cost)
    }

    private class Room(val char: Char, val index: Int, val content: List<Char>) {
        fun hasOnlyValidAmphipods() = content.all { it == char }

        fun isEmptyOrHasAllValidAmphipods() = content.all { it == '.' || it == char }

        fun hasAmphipodsWithWrongType() = !isEmptyOrHasAllValidAmphipods()
    }
}