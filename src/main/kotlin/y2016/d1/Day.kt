package y2016.d1

import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

fun day(data: List<String>) {
    println("2016-12-01")
    val map = mutableListOf<Pair<Int, Int>>()

    val steps = data[0].split(",")
    var dir = 0
    var distX = 0
    var distY = 0
    var isFound = false
    steps.forEach { step ->
        val prevPos = Pair(distX, distY)
        val s = step.trim()
        if (s[0] == 'L') {
            dir += 90
        } else {
            dir -= 90
        }
        if (dir > 360) dir -= 360
        if (dir < 0) dir += 360
        val dist = s.substring(1).toInt()
        when (dir) {
            0, 360 -> distX += dist
            90 -> distY += dist
            180 -> distX -= dist
            270 -> distY -= dist
        }

        if (!isFound) {
            for (x in min(distX, prevPos.first)..max(distX, prevPos.first)) {
                for (y in min(distY, prevPos.second)..max(distY, prevPos.second)) {
                    if(!(x == prevPos.first && y == prevPos.second)) {
                        val visited = map.find { pos ->
                            pos.first == x && pos.second == y
                        }
                        if (visited != null) {
                            isFound = true
                            println(
                                "First position visited twice is $visited, which is ${
                                    abs(visited.first) + abs(
                                        visited.second
                                    )
                                } away."
                            )
                        } else {
                            map.add(Pair(x, y))
                        }
                    }
                }
            }
        }
    }

    println("Distance is $distX, $distY, which is ${abs(distX) + abs(distY)} away.")
}
