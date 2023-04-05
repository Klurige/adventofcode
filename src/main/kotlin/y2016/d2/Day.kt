package y2016.d2

fun day(data: List<String>) {
    println("2016-12-02")
    val keypad = listOf(listOf(1, 2, 3), listOf(4, 5, 6), listOf(7, 8, 9))
    var row = 1
    var col = 1

    print("1: ")
    data.forEach { line ->
        line.forEach { step ->
            when (step) {
                'U' -> {
                    if (row > 0) row--
                }

                'D' -> {
                    if (row < 2) row++
                }

                'L' -> {
                    if (col > 0) col--
                }

                'R' -> {
                    if (col < 2) col++
                }
            }
        }
        print("${keypad[row][col]}")
    }
    println()

    val keypad2 = listOf(
        listOf(' ', ' ', '1', ' ', ' '),
        listOf(' ', '2', '3', '4', ' '),
        listOf('5', '6', '7', '8', '9'),
        listOf(' ', 'A', 'B', 'C', ' '),
        listOf(' ', ' ', 'D', ' ', ' ')
    )
    row = 2
    col = 0
    print("2: ")
    data.forEach { line ->
        line.forEach { step ->
            when (step) {
                'U' -> {
                    if (row > 0 && keypad2[row - 1][col] != ' ') row--
                }

                'D' -> {
                    if (row < 4 && keypad2[row + 1][col] != ' ') row++
                }

                'L' -> {
                    if (col > 0 && keypad2[row][col - 1] != ' ') col--
                }

                'R' -> {
                    if (col < 4 && keypad2[row][col + 1] != ' ') col++
                }
            }
        }
        print("${keypad2[row][col]}")
    }
    println()

}
