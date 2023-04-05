package y2022.d15

import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

fun IntRange.join(other: IntRange): IntRange {
    if (contains(other.first) || contains(other.last)) {
        return (Integer.min(first, other.first)..Math.max(last, other.last))
    }
    if(first == other.last+1) {
        return (other.first..last)
    }
    if(last == other.first -1) {
        return (first..other.last)
    }
    return IntRange.EMPTY
}

data class Coord(val x: Int, val y: Int) {
    override fun toString(): String {
        return "($x,$y)"
    }

    fun distance(other: Coord): Int {
        return (abs(x - other.x) + abs(y - other.y))

    }
}

fun day(data: List<String>) {
    println("2022-12-15")

    val map = mutableMapOf<Coord, Coord>()

    data.forEach { line ->
        val pos = line.split(' ')
        val sensor =
            Coord(pos[2].split('=')[1].substringBefore(',').toInt(), pos[3].split('=')[1].substringBefore(':').toInt())
        val beacon = Coord(pos[8].split('=')[1].substringBefore(',').toInt(), pos[9].split('=')[1].toInt())
        map[sensor] = beacon
    }
    val y = 2000000
    val allPos = mutableSetOf<Coord>()
    map.forEach { (sensor, beacon) ->
        val pos = mutableSetOf<Coord>()
        val distance = sensor.distance(beacon) - abs(y - sensor.y)
        (sensor.x - distance..sensor.x + distance).forEach { x ->
            pos.add(Coord(x, y))
        }

        allPos.addAll(pos)
    }
    println("1: ${allPos.filter { !map.containsValue(it) }.size}")

    val max = 4000000
    val ranges = mutableMapOf<Int, MutableSet<IntRange>>()
    map.forEach { (sensor, beacon) ->
        (0..max).forEach { y ->
            val distance = sensor.distance(beacon) - abs(y - sensor.y)
            if (distance > 0) {
                val range = (max(sensor.x - distance, 0)..min(sensor.x + distance, max))
                if (!ranges.containsKey(y)) {
                    ranges[y] = mutableSetOf(range)
                } else {
                    ranges[y]!!.add(range)
                    var existing = ranges[y]!!.sortedWith(compareBy({ it.first }, { it.last }))
                    var isDone = false
                    var index = 0
                    while (!isDone) {
                        if (index == existing.lastIndex) isDone = true else {
                            val r = existing[index].join(existing[index + 1])
                            if (r != IntRange.EMPTY) {
                                with(mutableListOf<IntRange>()) {
                                    addAll(existing.subList(0, index))
                                    add(r)
                                    addAll(existing.subList(index + 2, existing.size))
                                    existing = this
                                }
                                index = 0
                            } else {
                                index++
                            }
                        }
                    }
                    ranges[y] = existing.toMutableSet()
                }
            }
        }
    }

    val r = ranges.filter { it.value.size != 1 }
    val y2 = r.keys.first()
    val x2 = (r[y2]!!.first().last + 1).toBigInteger()
    val f = 4000000
    val freq = x2.times(f.toBigInteger()).plus(y2.toBigInteger())
    println("2: ${freq}")
}
