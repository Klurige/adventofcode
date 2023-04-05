package y2021.d8

@Suppress("unused")
fun day(data: List<String>) {
    val input = mutableListOf<Array<String>>()
    val output = mutableListOf<Array<String>>()

    data.forEach { line ->
            val inout = line.split('|')
            val indata = inout[0].trim().split(' ')
            val outdata = inout[1].trim().split(' ')
            input.add(indata.toTypedArray())
            output.add(outdata.toTypedArray())
        }

    var result = 0
    output.forEach { outArray ->
        result += outArray.count {
            (it.length == 2 || it.length == 3 || it.length == 4 || it.length == 7)
        }
    }
    println("Result part 1: $result")

    var tot = 0
    input.indices.forEach { index ->
        val wires = Array(10) { "" }
        wires[1] = input[index].first { it.length == 2 }
        wires[7] = input[index].first { it.length == 3 }
        wires[4] = input[index].first { it.length == 4 }
        wires[8] = input[index].first { it.length == 7 }
        wires[9] = input[index].first { it.length == 6 && it.containsSome(wires[4]) == wires[4].length }
        wires[6] = input[index].first { it.length == 6 && it.containsSome(wires[7]) != wires[7].length }
        wires[0] = input[index].first { it.length == 6 && it != wires[9] && it != wires[6] }
        wires[3] = input[index].first { it.length == 5 && it.containsSome(wires[7]) == wires[7].length }
        wires[5] = input[index].first { it.length == 5 && it != wires[3] && it.containsSome(wires[4]) == 3 }
        wires[2] = input[index].first { it.length == 5 && it != wires[3] && it.containsSome(wires[4]) == 2 }

        var res = 0
        output[index].forEach { digit ->
            wires.forEachIndexed { index, wiring ->
                if (digit.length == wiring.length && digit.containsSome(wiring) == wiring.length) {
                    res = res * 10 + index
                }
            }
        }
        tot += res
    }
    println("Result part2: $tot")
}

fun String.containsSome(str: String): Int {
    return str.toCharArray().count {
        this.contains(it)
    }

}
