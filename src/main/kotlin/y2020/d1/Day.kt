package y2020.d1

@Suppress("unused")
fun day(data: List<String>) {
    val expenses = MutableList<Int>(0) { it }

    data.forEach { line ->
        expenses.add(line.toInt())
    }

    expenses.indices.forEach { index ->
        (index + 1 until expenses.size).forEach { index2 ->
            expenses.indices.forEach { index3 ->
                if (index != index3 && index3 != index2) {
                    if (expenses[index] + expenses[index2] + expenses[index3] == 2020) {
                        println("Result is ${expenses[index] * expenses[index2] * expenses[index3]}")
                        return
                    }
                }
            }
        }
    }
}