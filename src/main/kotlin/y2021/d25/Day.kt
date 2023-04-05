package y2021.d25


fun day(data: List<String>) {
    var rowsFirst = Array(data.size) { CharArray(0) }
    data.forEachIndexed { index, line ->
        rowsFirst[index] = line.toCharArray()
    }
    val rowsSecond = Array(rowsFirst[0].size) { CharArray(rowsFirst.size) }


    //printArea(rowsFirst, 0)

    var isMoves = true
    var stepCounter = 0
    while (isMoves) {
        stepCounter++
        isMoves = false
        rowsFirst.indices.forEach { row ->
            var isWritten = false
            rowsFirst[row].indices.forEach { c ->
                val col = rowsFirst[row].size - 1 - c
                if(rowsFirst[row][col] == '>') {
                    val destCol = if (col < rowsFirst[row].lastIndex) {
                        col + 1
                    } else {
                        0
                    }
                    if (rowsFirst[row][destCol] == '.') {
                        if(destCol == 0) isWritten = true
                        rowsSecond[col][row] = '.'
                        rowsSecond[destCol][row] = '>'
                        isMoves = true
                    } else {
                        rowsSecond[col][row] = rowsFirst[row][col]
                    }
                } else {
                    if(col > 0 || !isWritten) rowsSecond[col][row] = rowsFirst[row][col]
                }
            }
        }

        rowsSecond.indices.forEach { row ->
            var isWritten = false
            rowsSecond[row].indices.forEach { c ->
                val col = rowsSecond[row].size - 1 - c
                if(rowsSecond[row][col] == 'v') {
                    val destCol = if (col < rowsSecond[row].lastIndex) {
                        col + 1
                    } else {
                        0
                    }
                    if (rowsSecond[row][destCol] == '.') {
                        if(destCol == 0) isWritten = true
                        rowsFirst[col][row] = '.'
                        rowsFirst[destCol][row] = 'v'
                        isMoves = true
                    } else {
                        rowsFirst[col][row] = rowsSecond[row][col]
                    }
                } else {
                    if(col > 0 || !isWritten) rowsFirst[col][row] = rowsSecond[row][col]
                }
            }
        }

       // printArea(rowsFirst, stepCounter)

    }

    println("Part1: $stepCounter")
}

fun printArea(rows: Array<CharArray>, stepCounter: Int) {
    println("Step: $stepCounter")
    rows.forEach { row ->
        println(row.joinToString(""))
    }
    println()
}