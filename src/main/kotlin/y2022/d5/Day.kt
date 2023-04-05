package y2022.d5

// Convert data to stack and commands. Amount, src, dest.
fun initialise(data: List<String>): Pair<List<ArrayDeque<Char>>, List<Triple<Int, Int, Int>>> {
    val stacks = mutableListOf<ArrayDeque<Char>>()
    stacks.add(ArrayDeque()) // There is no stack 0. Temporarily used in part 2.
    data.first { it.startsWith(" 1") }.windowed(4, 4, true)
        .forEach { _ -> stacks.add(ArrayDeque()) }
    data.filter { !it.startsWith("move") }.forEach { line ->
        var lineIndex = 1
        var stackIndex = 1
        while (lineIndex < line.length) {
            val ch = line[lineIndex]
            if (ch != ' ') {
                stacks[stackIndex].addFirst(line[lineIndex])
            }
            lineIndex += 4
            stackIndex++
        }
    }
    val cmds = data.filter { it.startsWith("move") }.map { line ->
        val cmd = line.split(' ')
        Triple(cmd[1].toInt(), cmd[3].toInt(), cmd[5].toInt())
    }

    return Pair(stacks, cmds)
}

fun day(data: List<String>) {
    println("2022-12-05")

    val parsed1 = initialise(data)
    val stacks1 = parsed1.first
    parsed1.second.forEach { cmd ->
        repeat(cmd.first) {
            val ch = stacks1[cmd.second].removeLast()
            stacks1[cmd.third].addLast(ch)
        }
    }

    print("1: ")
    stacks1.filter { it.isNotEmpty() }.forEach { print(it.last()) }
    println()

    val parsed2 = initialise(data)
    val stacks2 = parsed2.first
    parsed2.second.forEach { cmd ->
        repeat(cmd.first) {
            val ch = stacks2[cmd.second].removeLast()
            stacks2[0].addLast(ch)
        }
        while (stacks2[0].isNotEmpty()) {
            val ch = stacks2[0].removeLast()
            stacks2[cmd.third].addLast(ch)

        }

    }

    println("2: ${String(stacks2.filter { it.isNotEmpty() }.map { it.last() }.toCharArray())}")
}
