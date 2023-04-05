package y2022.d18

import kotlin.math.max
import kotlin.math.min

fun day(data: List<String>) {
    println("2022-12-18")

    data class Cube(val x: Int, val y: Int, val z: Int) {
        override fun toString(): String {
            return "[$x,$y,$z]: ${neighbours.size}"
        }

        val neighbours = mutableSetOf<Cube>()
        lateinit var possibleNeighbours: MutableSet<Cube>
        fun getPossibleExisting(): Set<Cube> {
            if (!this::possibleNeighbours.isInitialized) {
                possibleNeighbours = mutableSetOf()
                possibleNeighbours.add(Cube(x - 1, y, z))
                possibleNeighbours.add(Cube(x + 1, y, z))
                possibleNeighbours.add(Cube(x, y - 1, z))
                possibleNeighbours.add(Cube(x, y + 1, z))
                possibleNeighbours.add(Cube(x, y, z - 1))
                possibleNeighbours.add(Cube(x, y, z + 1))
            }
            return possibleNeighbours
        }

        fun setNeighbours(cubes: MutableSet<Cube>) {
            getPossibleExisting().forEachIndexed { index, cube ->
                val old = cubes.filter { it == cube }
                if (old.isNotEmpty()) {
                    old[0].neighbours.add(this)
                    neighbours.add(old[0])
                }
            }
        }
    }

    var xMin = Int.MAX_VALUE
    var yMin = Int.MAX_VALUE
    var zMin = Int.MAX_VALUE
    var xMax = Int.MIN_VALUE
    var yMax = Int.MIN_VALUE
    var zMax = Int.MIN_VALUE

    val cubes = mutableSetOf<Cube>()
    data.forEach { line ->
        val c = line.split(',').map { it.trim().toInt() }
        xMin = min(xMin, c[0])
        yMin = min(yMin, c[1])
        zMin = min(zMin, c[2])
        xMax = max(xMax, c[0])
        yMax = max(yMax, c[1])
        zMax = max(yMax, c[2])

        val cube = Cube(c[0], c[1], c[2])

        cube.setNeighbours(cubes)
        cubes.add(cube)
    }
    var notConnected = 0
    cubes.forEach { cube ->
        notConnected += (6 - cube.neighbours.size)
    }
    println("1: $notConnected")

    // Add an empty space around lava, so it can be fully submerged.
    xMax+=2
    yMax+=2
    zMax+=2
    xMin-=2
    yMin-=2
    zMin-=2

    // Starting point known to be outside lava.
    val water = mutableSetOf(Cube(xMax, yMax, zMax))
    var cubeCounter = 0
    val maybe = mutableSetOf<Cube>()
    (xMax downTo xMin).forEach { x ->
        (yMax downTo yMin).forEach { y ->
            (zMax downTo zMin).forEach { z ->
                cubeCounter++
                val cube = Cube(x, y, z)
                if (!cubes.contains(cube)) {
                    val w = cube.getPossibleExisting().filter {
                        (it.x in xMin..xMax)
                                && (it.y in yMin..yMax)
                                && (it.z in zMin..zMax)
                    }.toMutableSet()
                    if(w.any { water.contains(it)}) {
                        water.add(cube)
                    } else {
                        maybe.add(cube)
                    }
                }
            }
        }
    }

    // Loop through all unfilled spaces and check for water.
    // Do this until no more water is added.
    val added = mutableListOf<Cube>()
    do {
        added.clear()
        maybe.forEach { cube ->
            cube.getPossibleExisting().forEach { n ->
                if (water.contains(n)) {
                    water.add(cube)
                    added.add(cube)
                }
            }
        }
        added.forEach { cube ->
            maybe.remove(cube)
        }
    }while(added.isNotEmpty())

    var res2 = 0
    water.forEach { cube ->
        val neigh = cube.getPossibleExisting()
        neigh.forEach { n ->
            if(cubes.contains(n)) {
                res2++
            }
        }
    }
    println("2: $res2")
}
