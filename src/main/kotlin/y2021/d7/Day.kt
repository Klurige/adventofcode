package y2021.d7

import kotlin.math.absoluteValue


@Suppress("unused")
fun day(data: List<String>) {
    val values = data.first().split(",").map { it.toInt() }
    val max = values.maxOrNull() ?: 0

    val best1 = (0..max).minOf { depth ->
        values.sumOf { crab ->
            (crab - depth).absoluteValue
        }
    }

    val depths2 = IntArray(max + 1)
    values.forEach { crab ->
        (depths2.indices).forEach { depth ->
            val distance = (crab - depth).absoluteValue
            depths2[depth] += (0..distance).sum()
        }
    }

    val best2 = depths2.minOrNull()!!

    println("$best1 $best2")
}
