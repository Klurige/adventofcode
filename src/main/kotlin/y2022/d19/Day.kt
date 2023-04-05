package y2022.d19

enum class Minerals { ORE, OBSIDIAN, CLAY, GEODE }
data class Robot(
    val cost: Map<Minerals, Int>
)

private data class Blueprint(val line: String) {
    val index: Int = line.substringBefore(':').substringAfterLast(' ').toInt()
    val robotCosts = mutableMapOf<Minerals, Robot>()
    val maxCost = mutableMapOf<Minerals, Int>()

    init {
        val parts = line.substringAfter(':').split('.')
        parts.forEach { item ->
            val it = item.trim()
            val r = it.split(' ')
            if (r.size > 4) {
                val rType = when (r[1]) {
                    "ore" -> Minerals.ORE
                    "obsidian" -> Minerals.OBSIDIAN
                    "clay" -> Minerals.CLAY
                    "geode" -> Minerals.GEODE
                    else -> throw NullPointerException()
                }
                val rCost = mutableMapOf<Minerals, Int>()
                rCost[Minerals.ORE] = r[4].toInt()

                if (r.size > 6) {
                    when (r[8]) {
                        "obsidian" -> rCost[Minerals.OBSIDIAN] = r[7].toInt()
                        "clay" -> rCost[Minerals.CLAY] = r[7].toInt()
                        else -> throw NullPointerException()
                    }

                }
                robotCosts[rType] = Robot(rCost)
            }
        }

        Minerals.values().forEach { mineral ->
            maxCost[mineral] = robotCosts.maxOf {
                (it.value.cost[mineral] ?: 0)
            }
        }
        maxCost[Minerals.GEODE] = Int.MAX_VALUE
    }
}

private fun excavate(blueprint: Blueprint, maxMinutes: Int): Int {
    val queue = ArrayDeque<State>()
    queue.addLast(State().copy(time = maxMinutes))

    var maxGeodes = Int.MIN_VALUE

    val visited = hashSetOf<State>()

    while (queue.isNotEmpty()) {
        var currState = queue.removeFirst()
        if (visited.contains(currState))
            continue
        visited.add(currState)

        val currentMinute = currState.time
        val nextMinute = currentMinute - 1

        if (currState.time <= 0) {
            maxGeodes = maxOf(maxGeodes, currState.geodes)
            continue
        }

        // Remove excess robots
        if (currState.oreRobot >= blueprint.maxCost[Minerals.ORE]!!) {
            currState = currState.copy(oreRobot = blueprint.maxCost[Minerals.ORE]!!)
        }
        if (currState.clayRobot >= blueprint.maxCost[Minerals.CLAY]!!) {
            currState = currState.copy(
                clayRobot = blueprint.maxCost[Minerals.CLAY]!!
            )
        }
        if (currState.obsidianRobot >= blueprint.maxCost[Minerals.OBSIDIAN]!!) {
            currState = currState.copy(
                obsidianRobot = blueprint.maxCost[Minerals.OBSIDIAN]!!
            )
        }

        // Remove excess resources.
        if (currState.ore >= currentMinute * blueprint.maxCost[Minerals.ORE]!! - currState.oreRobot * nextMinute
        ) {
            currState = currState.copy(
                ore = currentMinute * blueprint.maxCost[Minerals.ORE]!! - currState.oreRobot * nextMinute
            )
        }
        if (currState.clay >= currentMinute * blueprint.maxCost[Minerals.CLAY]!! - currState.clayRobot * nextMinute
        ) {
            currState = currState.copy(
                clay = currentMinute * blueprint.maxCost[Minerals.CLAY]!! - currState.clayRobot * nextMinute
            )
        }
        if (currState.obsidian >= currentMinute * blueprint.maxCost[Minerals.OBSIDIAN]!! - currState.obsidianRobot * nextMinute){
            currState = currState.copy(
                obsidian = currentMinute * blueprint.maxCost[Minerals.OBSIDIAN]!! - currState.obsidianRobot * (nextMinute)
            )
        }

        // Add state for not building robots.
        queue.addLast(
            currState.copy(
                ore = currState.ore + currState.oreRobot,
                clay = currState.clay + currState.clayRobot,
                obsidian = currState.obsidian + currState.obsidianRobot,
                geodes = currState.geodes + currState.geodeRobot,
                time = nextMinute
            )
        )

        // Add states for every new robot possible.
        if (currState.ore >= blueprint.robotCosts[Minerals.ORE]!!.cost[Minerals.ORE]!!) {
            queue.addLast(
                currState.copy(
                    ore = currState.ore - blueprint.robotCosts[Minerals.ORE]!!.cost[Minerals.ORE]!! + currState.oreRobot,
                    clay = currState.clay + currState.clayRobot,
                    obsidian = currState.obsidian + currState.obsidianRobot,
                    geodes = currState.geodes + currState.geodeRobot,
                    oreRobot = currState.oreRobot + 1,
                    time = nextMinute
                )
            )
        }

        if (currState.ore >= blueprint.robotCosts[Minerals.CLAY]!!.cost[Minerals.ORE]!!) {
            queue.addLast(
                currState.copy(
                    ore = currState.ore - blueprint.robotCosts[Minerals.CLAY]!!.cost[Minerals.ORE]!! + currState.oreRobot,
                    clay = currState.clay + currState.clayRobot,
                    obsidian = currState.obsidian + currState.obsidianRobot,
                    geodes = currState.geodes + currState.geodeRobot,
                    clayRobot = currState.clayRobot + 1,
                    time = nextMinute
                )
            )
        }

        if (currState.ore >= blueprint.robotCosts[Minerals.OBSIDIAN]!!.cost[Minerals.ORE]!! && currState.clay >= blueprint.robotCosts[Minerals.OBSIDIAN]!!.cost[Minerals.CLAY]!!) {
            queue.addLast(
                currState.copy(
                    ore = currState.ore - blueprint.robotCosts[Minerals.OBSIDIAN]!!.cost[Minerals.ORE]!! + currState.oreRobot,
                    clay = currState.clay - blueprint.robotCosts[Minerals.OBSIDIAN]!!.cost[Minerals.CLAY]!! + currState.clayRobot,
                    obsidian = currState.obsidian + currState.obsidianRobot,
                    geodes = currState.geodes + currState.geodeRobot,
                    obsidianRobot = currState.obsidianRobot + 1,
                    time = nextMinute
                )
            )
        }

        if (currState.ore >= blueprint.robotCosts[Minerals.GEODE]!!.cost[Minerals.ORE]!! && currState.obsidian >= blueprint.robotCosts[Minerals.GEODE]!!.cost[Minerals.OBSIDIAN]!!) {
            queue.addLast(
                currState.copy(
                    ore = currState.ore - blueprint.robotCosts[Minerals.GEODE]!!.cost[Minerals.ORE]!! + currState.oreRobot,
                    obsidian = currState.obsidian - blueprint.robotCosts[Minerals.GEODE]!!.cost[Minerals.OBSIDIAN]!! + currState.obsidianRobot,
                    clay = currState.clay + currState.clayRobot,
                    geodes = currState.geodes + currState.geodeRobot,
                    geodeRobot = currState.geodeRobot + 1,
                    time = nextMinute
                )
            )
        }
    }
    return maxGeodes
}

private data class State(
    var ore: Int = 0,
    var clay: Int = 0,
    var obsidian: Int = 0,
    var geodes: Int = 0,
    var oreRobot: Int = 1,
    var clayRobot: Int = 0,
    var obsidianRobot: Int = 0,
    var geodeRobot: Int = 0,
    var time: Int = 24
)

fun part1(data: List<String>) {
    val blueprints = mutableListOf<Blueprint>()
    data.forEach { line ->
        blueprints.add(Blueprint(line))
    }
    var part1 = 0

    blueprints.forEach { blueprint ->
        part1 += blueprint.index * excavate(blueprint, 24)
    }
    println("Part1: $part1")

}

fun part2(data: List<String>) {
    val blueprints = mutableListOf<Blueprint>()
    data.take(3).forEach { line ->
        blueprints.add(Blueprint(line))
    }
    var part2 = 1
    blueprints.forEach { blueprint ->
        part2 *= excavate(blueprint, 32)
    }

    println("Part2: $part2")

}

fun day(data: List<String>) {
    println("2022-12-19")
    part1(data)
    part2(data)
}
