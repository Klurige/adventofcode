package y2022.d16

import kotlin.math.max

data class Cave(val valve: String = "", val flow: Int = 0, val tunnelNames: List<String> = emptyList()) {
    private lateinit var tunnels: List<Cave>
    val shortest = mutableMapOf<Cave, Int>()

    fun setTunnelsFromCave(caves: List<Cave>) {
        val t = mutableListOf<Cave>()
        tunnelNames.forEach { name ->
            t.add(caves.first { it.valve == name })
        }
        tunnels = t
    }

    /*
     * Dijkstra
     */
    fun shortestToAll(caves: List<Cave>) {
        val distances = mutableMapOf<Cave, Int>()
        caves.forEach { distances[it] = Int.MAX_VALUE }
        distances[this] = 0

        val known = mutableListOf<Cave>()
        val queue = mutableListOf<Cave>()
        queue.addAll(caves)
        while (queue.isNotEmpty()) {
            val cave = queue.reduce { cave1, cave2 ->
                if (distances[cave1]!! < distances[cave2]!!) cave1 else cave2
            }.also { queue.remove(it) }
            known.add(cave)

            cave.tunnels.forEach { tunnel ->
                if (distances[tunnel]!! > distances[cave]!! + 1) {
                    distances[tunnel] = distances[cave]!! + 1
                }
            }
        }
        distances.filter { it.key != this && it.key.flow > 0 }.forEach { shortest[it.key] = it.value }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Cave

        if (valve != other.valve) return false

        return true
    }

    override fun hashCode(): Int {
        return valve.hashCode()
    }

}

data class Step(
    val cave: Cave = Cave(),
    val distance: Int = 0,
    val minutesLeft: Int = Int.MAX_VALUE,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Step

        if (cave != other.cave) return false

        return true
    }

    override fun hashCode(): Int {
        return cave.hashCode()
    }
}

data class Path(val steps: List<Step>) {
    fun pressure(): Int = steps.sumOf { it.cave.flow * it.minutesLeft }

    private fun withOther(other: Path): Path = Path(listOf(steps, other.steps).flatten().groupBy { it.cave }.mapValues {
        it.value.reduce { acc, step ->
            if (acc.minutesLeft > step.minutesLeft) acc else step
        }
    }.values.toList())

    fun pressureWithOther(other: Path): Int = withOther(other).pressure()

}

fun part1(sofar: List<Step> = emptyList(), lastStep: Step, max: Int = Int.MIN_VALUE): Int {
    val path = mutableListOf<Step>()
    path.addAll(sofar)
    path.add(lastStep)
    val stepFlow = path.sumOf { it.cave.flow * it.minutesLeft }
    var currentMax = max(max, stepFlow)
    lastStep.cave.shortest.forEach { next ->
        if (path.minOf { it.minutesLeft } > 0 && path.none { it.cave == next.key } && lastStep.minutesLeft > next.value) {
            val nextStep = Step(next.key, next.value, lastStep.minutesLeft - 1 - next.value)
            currentMax = max(currentMax, part1(path, nextStep, currentMax))
        }
    }
    return currentMax
}

fun part2(
    sofar: List<Step> = emptyList(),
    lastStep: Step,
    max: Int = Int.MIN_VALUE,
    paths: MutableList<Path>
): Int {
    val path = mutableListOf<Step>()
    path.addAll(sofar)
    path.add(lastStep)
    val stepFlow = path.sumOf { it.cave.flow * it.minutesLeft }
    var currentMax = max(max, stepFlow)
    var isAdded = false
    lastStep.cave.shortest.forEach { next ->
        if (path.minOf { it.minutesLeft } > 0 && path.none { it.cave == next.key } && lastStep.minutesLeft > next.value) {
            isAdded = true
            val nextStep = Step(next.key, next.value, lastStep.minutesLeft - 1 - next.value)
            currentMax = max(currentMax, part2(path, nextStep, currentMax, paths))
        }
    }
    if (!isAdded) {
        paths.add(Path(path))
    }
    return currentMax
}

fun day(data: List<String>) {
    println("2022-12-16")


    val caves = mutableListOf<Cave>()
    data.forEach { line ->
        val parts = line.split(' ')
        val valve = parts[1]
        val flow = parts[4].split('=')[1].substringBeforeLast(';').toInt()
        val tunnels = mutableSetOf<String>()
        var index = parts.lastIndex
        while (!parts[index].startsWith("valve")) {
            tunnels.add(parts[index--].substringBefore(','))
        }
        caves.add(Cave(valve, flow, tunnels.toList()))
    }
    caves.forEach { it.setTunnelsFromCave(caves) }
    caves.forEach { it.shortestToAll(caves) }

    val start = caves.first { it.valve == "AA" }

    println("1: ${part1(lastStep = Step(start, 0, 30))}")

    val paths = mutableListOf<Path>()
    part2(lastStep = Step(start, 0, 26), paths = paths)


    val sorted = paths.sortedByDescending { it.pressure() }
    var max = paths.first().pressure()

    sorted.forEach { elfPath ->
        if (elfPath.pressure() > max / 2) {
            sorted.forEach { elephantPath ->
                val sum = elfPath.pressureWithOther(elephantPath)

                if (sum >= max) {
                    max = sum
                }
            }
        }
    }
    println("2: $max")
}
