package y2021.d6


@Suppress("unused")
fun day(data: List<String>) {
    val values = data.first().split(",").map { it.toInt() }

    val days = 256
    val fish = values.groupBy { it }.map { (k, v) -> k to v.size.toLong() }.toMap().toMutableMap()
    repeat(days) {
        val updates = fish.map { (age, amount) ->
            if (age == 0) (6 to amount) else (age - 1 to amount)
        } + (8 to (fish[0] ?: 0))
        fish.clear()
        updates.forEach { (age, amount) -> fish[age] = (fish[age] ?: 0) + amount }
    }

    println(fish.values.sum())
}
