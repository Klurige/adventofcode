package y2015.d3

@Suppress("unused")
fun day(data: List<String>) {
    part1(data)
    part2(data)
}

fun part1(data:List<String>) {
    var santaEast = 0
    var santaNorth = 0

    val houses = mutableListOf<Triple<Int, Int, Int>>()
    houses.add(Triple(0, 0, 1))
    data.first().forEachIndexed { index, char ->
            when (char) {
                '<' -> {
                    santaEast--
                }
                '>' -> {
                    santaEast++
                }
                '^' -> {
                    santaNorth++
                }
                'v' -> {
                    santaNorth--
                }
            }
            val curHouse = houses.indexOfFirst { house ->
                house.first == santaEast && house.second == santaNorth
            }
            if (curHouse == -1) {
                houses.add(Triple(santaEast, santaNorth, 1))
            } else {
                houses[curHouse] =
                    Triple(houses[curHouse].first, houses[curHouse].second, houses[curHouse].third + 1)
            }

    }

    println("${houses.size}")

}
fun part2(data:List<String>) {
    var santaEast = 0
    var santaNorth = 0
    var roboEast = 0
    var roboNorth = 0

    val houses = mutableListOf<Triple<Int, Int, Int>>()
    houses.add(Triple(0, 0, 1))
    data.first().forEachIndexed { index, char ->
        if (index % 2 == 0) {
            when (char) {
                '<' -> {
                    santaEast--
                }
                '>' -> {
                    santaEast++
                }
                '^' -> {
                    santaNorth++
                }
                'v' -> {
                    santaNorth--
                }
            }
            val curHouse = houses.indexOfFirst { house ->
                house.first == santaEast && house.second == santaNorth
            }
            if (curHouse == -1) {
                houses.add(Triple(santaEast, santaNorth, 1))
            } else {
                houses[curHouse] =
                    Triple(houses[curHouse].first, houses[curHouse].second, houses[curHouse].third + 1)
            }
        } else {
            when (char) {
                '<' -> {
                    roboEast--
                }
                '>' -> {
                    roboEast++
                }
                '^' -> {
                    roboNorth++
                }
                'v' -> {
                    roboNorth--
                }
            }
            val curHouse = houses.indexOfFirst { house ->
                house.first == roboEast && house.second == roboNorth
            }
            if (curHouse == -1) {
                houses.add(Triple(roboEast, roboNorth, 1))
            } else {
                houses[curHouse] =
                    Triple(houses[curHouse].first, houses[curHouse].second, houses[curHouse].third + 1)
            }

        }
    }

    println("${houses.size}")

}