package y2021.d22

import kotlin.math.max
import kotlin.math.min

fun day(data: List<String>) {
    val commands = mutableListOf<Command>()
    run collect@{
        data.forEach { line ->
            if (line.isEmpty()) return@collect
            val parts = line.split(' ')
            val cmd = parts[0] == "on"
            val xyz = parts[1].split(',')
            val xRange = xyz[0].split('=')[1].split("..")
            val x = (xRange[0].toInt()..xRange[1].toInt())
            val yRange = xyz[1].split('=')[1].split("..")
            val y = (yRange[0].toInt()..yRange[1].toInt())
            val zRange = xyz[2].split('=')[1].split("..")
            val z = (zRange[0].toInt()..zRange[1].toInt())
            commands.add(Command(cmd, x, y, z))
        }
    }

    val cubes = mutableMapOf<Cube, Boolean>()


    commands.forEach { cmd ->
        cmd.x.forEach { x ->
            if (x >= -50 && x <= 50) {
                cmd.y.forEach { y ->
                    if (y >= -50 && y <= 50) {

                        cmd.z.forEach { z ->
                            if (z >= -50 && z <= 50) {
                                cubes[Cube(x, y, z)] = cmd.cmd
                            }
                        }
                    }
                }
            }
        }
    }

    val part1 = cubes.count { it.value }

    println("Part1: $part1")

    val cubesOn = mutableMapOf<CubeRange, Long>()
    commands.forEach { cmd ->
        val cubeRange = CubeRange(cmd.x, cmd.y, cmd.z)
        if (cmd.cmd) {
            val newRanges = mutableListOf(cubeRange)
            cubesOn.forEach { (alreadyOn, _) ->
                val nr = alreadyOn.turnOff(cubeRange)
                nr.forEach { (r, _) ->
                    newRanges.add(r)
                }
            }
            cubesOn.clear()
            newRanges.forEach { nr ->
                cubesOn[nr] = nr.areOn()
            }
        } else {
            val updated = mutableMapOf<CubeRange, Long>()
            cubesOn.forEach { (onRange, _) ->
                val newRanges = onRange.turnOff(cubeRange)
                updated.putAll(newRanges)
            }
            cubesOn.clear()
            cubesOn.putAll(updated)
        }
    }


    val part2 = cubesOn.values.sum()

    println("Part2: $part2")
}


data class Command(val cmd: Boolean, val x: IntRange, val y: IntRange, val z: IntRange)

data class Cube(val x: Int, val y: Int, val z: Int)

data class CubeRange(val x: IntRange, val y: IntRange, val z: IntRange) {
    override fun toString(): String {
        return "[${x.first},${x.last}][${y.first},${y.last}][${z.first},${z.last}]: ${areOn()}"
    }
    fun areOn(): Long {
        val xSize =  if(!x.isEmpty()) (x.last - x.first + 1).toLong() else 0
        val ySize =  if(!y.isEmpty()) (y.last - y.first + 1).toLong() else 0
        val zSize =  if(!z.isEmpty()) (z.last - z.first + 1).toLong() else 0
        return xSize * ySize * zSize
    }

    fun turnOff(cubeRange: CubeRange): Map<CubeRange, Long> {
        val xSplit = x.nonOverlap(cubeRange.x)
        val ySplit = y.nonOverlap(cubeRange.y)
        val zSplit = z.nonOverlap(cubeRange.z)

        val left = mutableListOf(
            CubeRange(xSplit.first, y, z),
            CubeRange(xSplit.second, y, z),
            CubeRange(x.overlap(cubeRange.x), ySplit.first, z),
            CubeRange(x.overlap(cubeRange.x), ySplit.second, z),
            CubeRange(x.overlap(cubeRange.x), y.overlap(cubeRange.y), zSplit.first),
            CubeRange(x.overlap(cubeRange.x), y.overlap(cubeRange.y), zSplit.second)
        )

        val result = mutableMapOf<CubeRange, Long>()
        left.forEach { cr ->
            val on = cr.areOn()
            if(on > 0) result[cr] = on
        }
        return result
    }
}

fun IntRange.overlap(other: IntRange): IntRange {
    val min = max(this.first, other.first)
    val max = min(this.last, other.last)
    if (max >= min) return (min..max)
    return IntRange.EMPTY
}

fun IntRange.nonOverlap(other: IntRange): Pair<IntRange, IntRange> {
    val ov = overlap(other)

    if(ov.isEmpty()) return Pair(this, IntRange.EMPTY)

    val left = (first until ov.first)
    val right = (ov.last+1..last)

    return Pair(left, right)
}