package y2021.d2

@Suppress("unused")
fun day(data: List<String>) {
    var horizontal = 0
    var depth = 0
    var aim = 0

    data.forEach { line ->
        val params = line.split(" ")
        val direction = Pair(params[0], params[1].toInt())

        when (direction.first) {
            "forward" -> {
                horizontal += direction.second
                depth += aim * direction.second
            }
            "down" -> {
                aim += direction.second
            }
            "up" -> {
                aim -= direction.second
            }
            else -> println("Unknown param: $line")

        }
    }


    println("Horizontal: $horizontal")
    println("Depth: $depth")
    println("Mult: ${depth * horizontal}")
}