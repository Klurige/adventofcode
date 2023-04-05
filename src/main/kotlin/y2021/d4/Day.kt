package y2021.d4

@Suppress("unused")
fun day(data: List<String>) {

    val boards = mutableListOf<Board>()
    data.forEachIndexed { index, line ->
        if (index > 0) {
            if (line == "") {
                boards.add(Board(5, 5))
            } else {
                val row = line.trimStart().split("\\s+".toRegex())
                boards.last().addRow(row)
            }
        }
    }
    var order = 0
    val line = data.first()
    val numbers = line.split(',')
    numbers.forEach { number ->
        boards.forEach { board ->
            val isBingo = board.drawNumber(number.toInt())

            if (isBingo) {
                val score = board.getScore()
                if (board.bingoOrder < 0) {
                    println("Order: $order Score: ${score * number.toInt()}")
                    board.bingoOrder = order++
                    if (order == boards.size) {
                        return
                    }
                }
            }
        }
    }
}


data class Board(val rows: Int, val columns: Int) {
    var bingoOrder = -1
    private val array = Array(rows) {
        Array(columns) {
            Pair(false, it)
        }
    }
    private var currentRow = 0

    fun addRow(values: List<String>) {
        values.forEachIndexed { index, i ->
            array[currentRow][index] = Pair(false, i.toInt())
        }
        currentRow++
    }

    fun drawNumber(number: Int): Boolean {
        array.forEachIndexed { rowIndex, row ->
            row.forEachIndexed { colIndex, pair ->
                if (pair.second == number) {
                    return markNumber(rowIndex, colIndex)
                }
            }
        }
        return false
    }

    private fun markNumber(row: Int, col: Int): Boolean {
        array[row][col] = Pair(true, array[row][col].second)

        var isBingo = true
        (0 until 5).forEach { r ->
            if (!array[r][col].first) {
                isBingo = false
            }
        }
        if (!isBingo) {
            isBingo = true
            (0 until 5).forEach { c ->
                if (!array[row][c].first) {
                    isBingo = false
                }
            }

        }

        return isBingo
    }

    fun getScore(): Int {
        var sum = 0
        array.forEach { row ->
            row.forEach { num ->
                if (!num.first) {
                    sum += num.second
                }
            }
        }
        return sum
    }
}