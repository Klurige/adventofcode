package y2016.d6

fun day(data: List<String>) {
    println("2016-12-06")
    val frequency: MutableList<MutableMap<Char, Int>> = mutableListOf()
    data.forEach { line ->
        line.forEachIndexed { index, ch ->
            if (frequency.size <= index) {
                frequency.add(mutableMapOf())
            }
            frequency[index][ch] = frequency[index][ch]?.plus(1) ?: 1
        }
    }

    print("1: ")
    frequency.forEach { histo ->
        print("${histo.maxBy {it.value}.key}")
    }
    println("")

    print("2: ")
    frequency.forEach { histo ->
        print("${histo.minBy {it.value}.key}")
    }
    println("")
}
