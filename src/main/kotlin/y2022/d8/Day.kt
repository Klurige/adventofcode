package y2022.d8

fun isVisible(f: Array<IntArray>, row: Int, col: Int): Boolean {
    if (row == 0 || row == f.lastIndex || col == 0 || col == f[row].lastIndex) return true
    val down = (row + 1..f.lastIndex).count { f[it][col] >= f[row][col] }
    val right = (col + 1..f[row].lastIndex).count { f[row][it] >= f[row][col] }
    val up = (row - 1 downTo 0).count { f[it][col] >= f[row][col] }
    val left = (col - 1 downTo 0).count { f[row][it] >= f[row][col] }
    return (down == 0 || up == 0 || left == 0 || right == 0)
}


fun scenicScore(f: Array<IntArray>, row: Int, col: Int): Int {
    var down = 0
    run down@{
        (row + 1..f.lastIndex).forEach {
            down++
            if (f[it][col] >= f[row][col]) {
                return@down
            }
        }
    }
    var up = 0
    run up@{
        (row - 1 downTo 0).forEach {
            up++
            if (f[it][col] >= f[row][col]) {
                return@up
            }
        }
    }

    var right = 0
    run right@{
        (col + 1..f[row].lastIndex).forEach {
            right++
            if (f[row][it] >= f[row][col]) {
                return@right
            }
        }
    }
    var left = 0
    run left@{
        (col - 1 downTo 0).forEach {
            left++
            if (f[row][it] >= f[row][col]) {
                return@left
            }
        }
    }
    return (up * down * left * right)
}

fun day(data: List<String>) {
    println("2022-12-08")

    var res1 = 0
    var res2 = 0

    val forest = Array(data.size) { IntArray(data[0].length) }
    data.forEachIndexed { row, line ->
        val trees = line.map { it.digitToInt() }
        trees.forEachIndexed { col, it ->
            forest[row][col] = it
        }
    }

    forest.forEachIndexed { row, _ ->
        forest[row].forEachIndexed { col, _ ->
            if (isVisible(forest, row, col)) {
                res1++
            }
            val score = scenicScore(forest, row, col)
            if (score > res2) res2 = score
        }
    }


    println("1: $res1")
    println("2: $res2")
}
