package y2022.d3

fun itemToPrio(item: Char): Int {
    return if (item in 'a'..'z') {
        item - 'a' + 1
    } else {
        item - 'A' + 27
    }
}

fun day(data: List<String>) {
    println("2022-12-03")

    var itemPrio = 0
    var badgePrio = 0
    val elves = mutableListOf<Set<Char>>()
    data.forEach { line ->
        elves += line.toSet()
        val compartments = line.chunked(line.length / 2).map { it.toSet() }
        itemPrio += compartments[0].intersect(compartments[1]).sumOf { itemToPrio(it) }

        if (elves.size == 3) {
            badgePrio += elves[0].intersect(elves[1].intersect(elves[2])).sumOf { itemToPrio(it) }
            elves.clear()
        }
    }
    println("1: $itemPrio")
    println("2: $badgePrio")
}
