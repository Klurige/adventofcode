package y2021.d5

import kotlin.math.max
import kotlin.math.min

@Suppress("unused")
fun day(data: List<String>) {

    val lines = mutableListOf<Line>()
    var minX = Int.MAX_VALUE
    var minY = Int.MAX_VALUE
    var maxX = Int.MIN_VALUE
    var maxY = Int.MIN_VALUE
    data.forEach { line ->
        val elements = line.trim().split(' ')
        lines.add(Line(Point.fromString(elements.first()), Point.fromString(elements.last())))
        if (minX > lines.last().pt0.x) {
            minX = lines.last().pt0.x
        }
        if (minX > lines.last().pt1.x) {
            minX = lines.last().pt1.x
        }
        if (maxX < lines.last().pt0.x) {
            maxX = lines.last().pt0.x
        }
        if (maxX < lines.last().pt1.x) {
            maxX = lines.last().pt1.x
        }
        if (minY > lines.last().pt0.y) {
            minY = lines.last().pt0.y
        }
        if (minY > lines.last().pt1.y) {
            minY = lines.last().pt1.y
        }
        if (maxY < lines.last().pt0.y) {
            maxY = lines.last().pt0.y
        }
        if (maxY < lines.last().pt1.y) {
            maxY = lines.last().pt1.y
        }
    }

    val area = Array(1000) {
        IntArray(1000)
    }

    lines.forEach { line ->
        val allPoints = line.allPoints()
        allPoints.forEach { pt ->
            area[pt.x][pt.y]++
        }
    }

    var overlaps = 0
    area.forEach { column ->
        column.forEach {
            if (it > 1) {
                overlaps++
            }
        }
    }
    println("Overlaps: $overlaps")
}

data class Point(val x: Int, val y: Int) {
    companion object {
        fun fromString(coords: String): Point {
            val c = coords.trim().split(',')
            return Point(c.first().toInt(), c.last().toInt())
        }
    }
}

data class Line(val pt0: Point, val pt1: Point) {
    fun allPoints(): List<Point> {
        val pts = mutableListOf<Point>()
        if (pt0.x == pt1.x) {
            (min(pt0.y, pt1.y)..max(pt0.y, pt1.y)).forEach {
                pts.add(Point(pt0.x, it))
            }
        } else if (pt0.y == pt1.y) {
            (min(pt0.x, pt1.x)..max(pt0.x, pt1.x)).forEach {
                pts.add(Point(it, pt0.y))
            }
        } else {
            val ptStart: Point
            val ptEnd: Point
            if (pt0.x < pt1.x) {
                ptStart = pt0
                ptEnd = pt1
            } else {
                ptStart = pt1
                ptEnd = pt0
            }

            val dir = if (ptStart.y > ptEnd.y) {
                -1
            } else {
                1
            }

            (ptStart.x..ptEnd.x).forEachIndexed { index, x ->
                pts.add(Point(x, ptStart.y + index * dir))
            }

        }
        return pts
    }
}