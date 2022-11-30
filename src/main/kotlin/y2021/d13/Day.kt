package y2021.d13

fun day(data: List<String>) {
    val pts = mutableSetOf<Pair<Int, Int>>()
    val cmds = mutableListOf<String>()

    data.forEach { line ->
        if (line.isNotEmpty()) {
            if (line[0].isDigit()) {
                val coords = line.split(',').map { it.toInt() }
                pts.add(Pair(coords[0], coords[1]))
            } else {
                cmds.add(line)
            }
        }
    }

    var numDots = 0
    cmds.forEach { line ->
        val cmd = line.split(' ')[2].split('=')
        val crease = cmd[1].toInt()
        if (cmd[0] == "x") {
            val toFold = pts.filter { pt ->
                pt.first >= crease
            }
            toFold.forEach { pt ->
                pts.add(Pair(2 * crease - pt.first, pt.second))
            }
            pts.removeAll { pt ->
                toFold.contains(pt)
            }
        } else {
            val toFold = pts.filter { pt ->
                pt.second >= crease
            }
            toFold.forEach { pt ->
                val newPt = Pair(pt.first, 2 * crease - pt.second)
                pts.add(newPt)
            }
            pts.removeAll { pt ->
                toFold.contains(pt)
            }
        }
        if (numDots == 0) {
            numDots = pts.size
        }
    }

    val width = 1 + pts.maxByOrNull { it.first }!!.first
    val height = 1 + pts.maxByOrNull { it.second }!!.second

    (0..height).forEach { y ->
        val ptsOnRow = pts.filter { pt ->
            pt.second == y
        }
        val row = StringBuilder()
        (0..width).forEach { x ->
            val ptInCol = ptsOnRow.firstOrNull { pt ->
                pt.first == x
            }
            if (ptInCol == null) {
                row.append(' ')
            } else {
                row.append("█")
            }
        }
        println(row.toString())
    }

    println(numDots)
}

