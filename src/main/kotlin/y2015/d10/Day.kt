package y2015.d10

fun day(data: List<String>) {

    var input = "1113222113"

    val numIterations = 50

    repeat(numIterations) {
        var msg = input
        val output = StringBuilder()
        while (msg.isNotEmpty()) {
            val leftOvers = msg.trimStart(msg[0])
            val end = msg.length - leftOvers.length
            val chunk = msg.substring(0, end)
            output.append(chunk.length).append(chunk[0])
            msg = leftOvers
        }
        input = output.toString()
        if(it == 39) {
            println("Part1: ${input.length}")
        }

    }
    println("Part2: ${input.length}")
}
