package y2015.d17


fun day(data: List<String>) {
    val containers = mutableListOf<Int>()
    data.forEach { line ->
        containers.add(line.toInt())
    }
    val arr = containers.toTypedArray()

    val combinations = combinations(arr)

    val ways = mutableMapOf<Array<Int>, Int>()
    combinations.forEach { comb ->
        val sum = comb.sum()
        if (sum == 150) ways[comb] = comb.size
    }
    println("Part1: ${ways.size}")

    val min = ways.minByOrNull { it.value }!!

    val numMin = ways.filter { it.value == min.value }
    println("Part2: ${numMin.size}")
}

fun combinations(arr: Array<Int>): List<Array<Int>> {
    val result = mutableListOf<Array<Int>>()
    repeat(arr.size + 1) {
        val part = combinationsOfLength(arr, it)
        result.addAll(part)
    }
    return result
}

fun combinationsOfLength(
    arr: Array<Int>,
    length: Int,
    data: Array<Int> = Array(length) { 0 },
    start: Int = 0,
    end: Int = arr.lastIndex,
    index: Int = 0,
    sofar: MutableList<Array<Int>> = mutableListOf()
): List<Array<Int>> {
    if (index == length) {
        sofar.add(data.copyOf())
        return sofar
    }

    var i = start
    while (i <= end && end - i + 1 >= length - index) {
        data[index] = arr[i]
        combinationsOfLength(
            arr = arr,
            data = data,
            start = i + 1,
            end = end,
            index = index + 1,
            length = length,
            sofar = sofar
        )
        i++
    }
    return sofar
}
