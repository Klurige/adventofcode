package y2022.d9

fun day(data: List<String>) {
    println("2022-12-09")

    var res1 = 0
    var res2 = 0
    val bridge = Array(1000) { Array<Pair<Boolean, Boolean>>(1000) { Pair(false, false) } }

    var row = 500
    var col = 500

    val rope1 = mutableListOf(
        Pair(row, col),
        Pair(row, col)
    )
    val rope2 = mutableListOf(
        Pair(row, col),
        Pair(row, col),
        Pair(row, col),
        Pair(row, col),
        Pair(row, col),
        Pair(row, col),
        Pair(row, col),
        Pair(row, col),
        Pair(row, col),
        Pair(row, col)
    )
    data.forEach { line ->
        val m = line.split(' ')
        repeat(m[1].toInt()) {
            row = rope1.last().first
            col = rope1.last().second
            when (m[0]) {
                "R" -> col++
                "L" -> col--
                "U" -> row--
                "D" -> row++
            }
            var newPos = Pair(row, col)

            run rope1@{
                (rope1.lastIndex downTo 0).forEach { index ->
                    rope1[index] = newPos
                    if (index > 0) {
                        if (!adjacent(rope1[index - 1], rope1[index])) {
                            newPos = moveTo(rope1[index - 1], rope1[index])
                        } else {
                            return@rope1
                        }
                    }
                }
            }

            newPos = Pair(row, col)
            run rope2@{
                (rope2.lastIndex downTo 0).forEach { index ->
                    rope2[index] = newPos
                    if (index > 0) {
                        if (!adjacent(rope2[index - 1], rope2[index])) {
                            newPos = moveTo(rope2[index - 1], rope2[index])
                        } else {
                            return@rope2
                        }
                    }
                }
            }

            bridge[rope1.first().first][rope1.first().second] =
                Pair(true, bridge[rope1.first().first][rope1.first().second].second)
            bridge[rope2.first().first][rope2.first().second] =
                Pair(bridge[rope2.first().first][rope2.first().second].first, true)
        }
    }

    bridge.forEach { r ->
        r.forEach { c ->
            if (c.first) res1++
            if (c.second) res2++
        }
    }

    println("11: $res1")
    println("12: $res2")
}

fun moveTo(k1: Pair<Int, Int>, k2: Pair<Int, Int>): Pair<Int, Int> {
    var row = k1.first
    var col = k1.second

    if (k1.first == k2.first) {
        if (k1.second < k2.second - 1) {
            col = k1.second + 1
        }
        if (k1.second > k2.second + 1) {
            col = k1.second - 1
        }
    } else if (k1.second == k2.second) {
        if (k1.first < k2.first - 1) {
            row = k1.first + 1
        }
        if (k1.first > k2.first + 1) {
            row = k1.first - 1
        }
    } else {
        if (k1.first < k2.first) {
            row = k1.first + 1
        }
        if (k1.first > k2.first) {
            row = k1.first - 1
        }
        if (k1.second < k2.second) {
            col = k1.second + 1
        }
        if (k1.second > k2.second) {
            col = k1.second - 1
        }
    }
    return Pair(row, col)
}

fun adjacent(head: Pair<Int, Int>, tail: Pair<Int, Int>): Boolean {
    return (head.first - 1..head.first + 1).contains(tail.first) &&
            (head.second - 1..head.second + 1).contains(tail.second)
}
