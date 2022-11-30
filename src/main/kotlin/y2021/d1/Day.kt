package y2021.d1

@Suppress("unused")
fun day(data: List<String>) {
    val numbers = data.map { line ->
        line.toInt()
    }.toList()

    val result = numbers.windowed(3).map { (a, b, c) ->
        a + b + c
    }.windowed(2).count { (a, b) ->
        a < b
    }

    println("Found $result increases")

    val increases = numbers.windowed(3).map { it.sum() }
        .windowed(2).count { (a, b) ->
            a < b
        }

    println("Found $increases increases")

    var inc = 0
    var prevSum = 0
    (0 until numbers.size - 2).forEach { index ->
        val thisSum = numbers[index] + numbers[index + 1] + numbers[index + 2]
        if (prevSum != 0) {
            if (thisSum > prevSum) inc++
        }
        prevSum = thisSum
    }

    println("Found $inc increases")
}