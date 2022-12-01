package y2022.d1

fun day(data: List<String>) {
    println("2022-12-01")

    val elvesMap = mutableMapOf<Int, Int>()
    var elf = 1
    elvesMap[elf] = 0

    data.forEach { line ->
        if (line.isEmpty()) {
            elf++
            elvesMap[elf] = 0
        } else {
            elvesMap[elf] = elvesMap[elf]!! + line.toInt()
        }
    }

    val elves = elvesMap.values.sortedDescending()

    println("1: ${elves[0]}")
    println("2: ${elves[0] + elves[1] + elves[2]}")
}
