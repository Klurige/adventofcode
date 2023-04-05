package y2015.d1

@Suppress("unused")
fun day(data: List<String>) {
    var floor = 0
    var entering = 0
    data.first().forEachIndexed { index, char ->
            when (char) {
                '(' -> floor++
                ')' -> floor--
            }
            if (floor == -1 && entering == 0) entering =index + 1
        }
    println("Floor: $floor")
    println("Entering basement: $entering")
}