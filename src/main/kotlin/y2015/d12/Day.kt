package y2015.d12

var sum = 0

fun day(data: List<String>) {

    var number = 0
    var sign = 1
    data.forEach { line ->
        sum = 0
        val values = objects(line, 0, true)
        println("$line has sum ${values.first.sum()}")

    }

}

fun objects(line: String, start: Int, isFilter:Boolean): Pair<List<Int>, Int> {
    var index = start
    val values = mutableListOf(mutableListOf<Int>())
    var sign = 1
    var number = 0
    var isRed = false
    while (index < line.length) {
        when(line[index]) {
            '[' -> {
                val sub = objects(line, index+1, isFilter)
                values.add(sub.first.toMutableList())
                index = sub.second
            }
            ']' -> {
                if(number != 0) {
                    values.last().add(sign*number)
                }
                return Pair(values.flatten(), index + 1)
            }
            '{' -> {
                val sub = objects(line, index+1, isFilter)
                values.last().add(sub.first.sum())
                index = sub.second
            }
            '}' -> {
                if(number != 0) {
                    values.last().add(sign*number)
                }
                return if(isRed && isFilter) {
                    Pair(listOf(0), index+1)
                } else {
                    Pair(listOf(values.flatten().sum()), index + 1)
                }
            }
            '-' -> {
                sign = -1
                index++
            }
            '0', '1', '2', '3', '4', '5','6','7','8','9' -> {
                number = 10*number + line[index].code - '0'.code
                index++
            }
            'r' -> {
                if (line[index + 1] == 'e' && line[index + 2] == 'd') {
                    isRed = true
                    index += 3
                } else {
                    index++
                }
            }
            else -> {
                if(number != 0) {
                    values.last().add(sign*number)
                    number = 0
                    sign = 1
                }
                index++
            }
        }
    }
    return if(isRed && isFilter) {
        Pair(listOf(0), index+1)
    } else {
        Pair(listOf(values.flatten().sum()), index + 1)
    }
}

