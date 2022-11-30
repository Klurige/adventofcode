package y2021.d12


var numPaths = 0
val allCaves = mutableListOf<Cave>()

fun day(data: List<String>) {
    run loop@{
        data.forEach { line ->
            if (line.isEmpty()) {
                return@loop
            }
            val params = line.split('-')
            var cave0 = allCaves.firstOrNull { it.name == params[0] }
            if (cave0 == null) {
                cave0 = Cave(params[0])
                allCaves.add(cave0)
            }
            var cave1 = allCaves.firstOrNull { it.name == params[1] }
            if (cave1 == null) {
                cave1 = Cave(params[1])
                allCaves.add(cave1)
            }
            if (cave0.name != "end") {
                if (cave1.name != "start") {
                    cave0.addTunnel(cave1)
                }
            }
            if (cave1.name != "end") {
                if (cave0.name != "start") {
                    cave1.addTunnel(cave0)
                }
            }
        }
    }

    val start = allCaves.first { it.name == "start" }
    numPaths = 0
    start.getAllPathsToEnd(allCaves, emptyList(), true)
    println("part1: $numPaths")
    numPaths = 0
    start.getAllPathsToEnd(allCaves, emptyList(), false)
    println("part2: $numPaths")
}

class Cave(val name: String) {
    override fun toString(): String {
        return name
    }

    private val tunnels = mutableListOf<Cave>()

    fun addTunnel(cave: Cave) {
        tunnels.add(cave)
    }

    fun getAllPathsToEnd(caves: List<Cave>, sofar: List<Cave>, isRevisited: Boolean) {
        val path = listOf(sofar, listOf(this)).flatten()
        if (name == "end") {
            numPaths++
        } else {
            tunnels.forEach { cave ->
                if(cave.name[0].isUpperCase()) {
                    cave.getAllPathsToEnd(caves, path, isRevisited)
                } else {
                    if(sofar.firstOrNull { visited ->
                            visited.name == cave.name
                        } == null) {
                        cave.getAllPathsToEnd(caves, path, isRevisited)
                    } else {
                        if(!isRevisited) {
                            cave.getAllPathsToEnd(caves, path, true)
                        }
                    }
                }
            }
        }
    }
}