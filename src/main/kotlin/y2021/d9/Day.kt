package y2021.d9

var heightMap: Array<IntArray>? = null
var maxX = 0
var maxY = 0

var basins = mutableListOf<Int>()

@Suppress("unused")
fun day(data: List<String>) {
    val input = mutableListOf<IntArray>()

    data.forEach { line ->
        val chars = line.toCharArray()
        val heights = chars.map { "$it".toInt() }
        input.add(heights.toIntArray())
    }

    heightMap = input.toTypedArray()

    maxY = heightMap!!.size - 1
    maxX = heightMap!!.first().size - 1

    var totRisk = 0
    heightMap!!.indices.forEach { y ->
        heightMap!![y].indices.forEach { x ->
            val riskLevel = risk(x, y)
            totRisk += riskLevel
        }
    }

    println("RiskLevel $totRisk")

    var sum = 1
    val largest = basins.maxOrNull() ?: 0
    basins.remove(largest)
    sum *= largest
    val second = basins.maxOrNull() ?: 0
    basins.remove(second)
    sum *= second
    val third = basins.maxOrNull() ?: 0
    sum *= third

    println("Sum is $sum")

}

fun risk(x: Int, y: Int): Int {
    val pos = heightMap!![y][x]
    var isLow = true
    val yMin = (y - 1).coerceAtLeast(0)
    val yMax = (y + 1).coerceAtMost(maxY)
    val xMin = (x - 1).coerceAtLeast(0)
    val xMax = (x + 1).coerceAtMost(maxX)
    (xMin..xMax).forEach { nx ->
        (yMin..yMax).forEach { ny ->
            if (nx != x || ny != y) {
                val neighbour = heightMap!![ny][nx]
                if (pos >= neighbour) isLow = false
            }
        }
    }
    return if (isLow) {
        basins.add(basinSize(x, y))
        pos + 1
    } else {
        0
    }
}

fun basinSize(x: Int, y: Int): Int {
    var size = 0
    if (heightMap!![y][x] < 9) {
        size++
        heightMap!![y][x] = 9
        if (x > 0) {
            size += basinSize(x - 1, y)
        }
        if (x < maxX) {
            size += basinSize((x + 1), y)
        }
        if (y > 0) {
            size += basinSize(x, y - 1)
        }
        if (y < maxY) {
            size += basinSize(x, y + 1)
        }
    }
    return size

}