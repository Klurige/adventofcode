package y2021.d11

import kotlin.math.max
import kotlin.math.min

val octo = mutableListOf<IntArray>()
var flashes = 0

var isPart1 = false
var isPart2 = false

fun day(data: List<String>) {
    data.forEach { line ->
        octo.add(line.toCharArray().map { it.code - '0'.code }.toIntArray())
    }

    run loop@{
        var counter = 0
        while (true) {
            counter++
            var numZeros = 0
            octo.indices.forEach { row ->
                octo[row].indices.forEach { col ->
                    if (octo[row][col] == 0) {
                        numZeros++
                    }
                    octo[row][col]++
                }
            }
            if (numZeros == octo.size * octo[0].size) {
                println("All zeroes at ${counter - 1}")
                isPart2 = true
            }

            octo.indices.forEach { row ->
                octo[row].indices.forEach { col ->
                    if (octo[row][col] > 9) {
                        flash(row, col)
                    }
                }
            }
            if (counter >= 100) {
                isPart1 = true
                if (isPart2) return@loop
            }
        }
    }

    println("$flashes flashes")

}

fun flash(row: Int, col: Int) {
    if (!isPart1) flashes++
    octo[row][col] = 0
    (max(0, row - 1)..min(row + 1, octo.lastIndex)).forEach { r ->
        (max(0, col - 1)..min(col + 1, octo[r].lastIndex)).forEach { c ->
            if (octo[r][c] > 0) {
                octo[r][c]++
                if (octo[r][c] > 9) {
                    flash(r, c)
                }
            }
        }
    }
}