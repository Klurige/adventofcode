package y2016.d3


fun isTriangle(sides: List<Int>): Boolean =
    (sides[0] + sides[1] > sides[2]) && (sides[0] + sides[2] > sides[1]) && (sides[1] + sides[2] > sides[0])

fun part1(data: List<String>): Int {
    var tot = 0
    data.forEach { line ->
        val sides = line.trim().split("\\s+".toRegex()).map { it.toInt() }
        if (isTriangle(sides)) tot++
    }
    return tot
}

fun part2(data: List<String>): Int {
    var tot = 0
    var counter = 0
    val multiSides = Array(3) { IntArray(3) }
    data.forEach { line ->
        val sides = line.trim().split("\\s+".toRegex()).map { it.toInt() }
        multiSides[counter][0] = sides[0]
        multiSides[counter][1] = sides[1]
        multiSides[counter][2] = sides[2]
        if (counter == 2) {
            repeat(3) { col ->
                val s = mutableListOf<Int>()
                repeat(3) { row ->
                    s.add(multiSides[row][col])
                }
                if (isTriangle(s)) tot++
            }
            counter = 0
        } else {
            counter++
        }
    }
    return tot
}

fun day(data: List<String>) {
    println("2016-12-03")

    println("1: ${part1(data)}")
    println("2: ${part2(data)}")
}
