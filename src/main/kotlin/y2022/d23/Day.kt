package y2022.d23

enum class Direction { N, S, W, E, NE, NW, SE, SW }

data class Pos(val row: Int, val col: Int) {
    private fun getAdjacent(direction: Direction): Pos {
        return when (direction) {
            Direction.N -> Pos(row - 1, col)
            Direction.S -> Pos(row + 1, col)
            Direction.W -> Pos(row, col - 1)
            Direction.E -> Pos(row, col + 1)
            Direction.NE -> Pos(row - 1, col + 1)
            Direction.NW -> Pos(row - 1, col - 1)
            Direction.SE -> Pos(row + 1, col + 1)
            Direction.SW -> Pos(row + 1, col - 1)
        }
    }

    fun getProposal(elves: Set<Pos>, directions: List<Direction>): Pos? {
        val candidates = Direction.values().map { Pair(getAdjacent(it), it) }.filter { a ->
            elves.contains(a.first)
        }.toSet()
        if (candidates.isEmpty()) return null
        directions.forEach { dir ->
            val proposal = when (dir) {
                Direction.N -> candidates.filter { setOf(Direction.N, Direction.NE, Direction.NW).contains(it.second) }
                Direction.S -> candidates.filter { setOf(Direction.S, Direction.SE, Direction.SW).contains(it.second) }
                Direction.W -> candidates.filter { setOf(Direction.W, Direction.SW, Direction.NW).contains(it.second) }
                Direction.E -> candidates.filter { setOf(Direction.E, Direction.NE, Direction.SE).contains(it.second) }
                else -> throw IllegalArgumentException()
            }
            if (proposal.isEmpty()) return getAdjacent(dir)
        }
        return null
    }
}

fun day(data: List<String>) {
    println("2022-12-23")

    val elves = mutableSetOf<Pos>()
    data.forEachIndexed { row, line ->
        line.forEachIndexed { col, c ->
            if (c == '#') elves.add(Pos(row, col))
        }
    }

    val proposalList = mutableListOf(Direction.N, Direction.S, Direction.W, Direction.E)
    var count = 0
    while (true) {
        count++
        if (count == 10) {
            val r = elves.maxOf { it.row } - elves.minOf { it.row } + 1
            val c = elves.maxOf { it.col } - elves.minOf { it.col } + 1
            println("1: ${r * c - elves.size}")

        }
        val proposals = mutableListOf<Pair<Pos, Pos>>()
        elves.forEach { elf ->
            elf.getProposal(elves, proposalList)?.let { proposals.add(Pair(elf, it)) }
        }

        proposals.forEach { proposal ->
            if (proposals.filter { proposal.second == it.second }.size == 1) {
                elves.add(proposal.second)
                elves.remove(proposal.first)
            }
        }
        if (proposals.isEmpty()) {
            println("2: $count")
            return
        }
        proposalList.add(proposalList.removeAt(0))
    }
}