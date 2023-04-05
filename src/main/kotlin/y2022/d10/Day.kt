package y2022.d10

fun day(data: List<String>) {
    println("2022-12-10")

    println("2:")
    
    var res1 = 0
    var pc = 0
    var xDiff = Int.MAX_VALUE
    var x = 1
    var line = 0
    var readInterval = 20
    var clock = 0
    while (pc < data.size) {
        clock++
        if (clock == readInterval && clock <= 220) {
            readInterval += 40
            res1 += clock * x
        }

        line++
        if((line-2..line).contains(x)) {
            print('#')
        } else {
            print('.')
        }

        if (clock % 40 == 0) {
            println()
            line = 0
        }
        if (xDiff == Int.MAX_VALUE) {
            data[pc++].apply {
                if (startsWith("addx")) {
                    xDiff = substringAfterLast(' ').toInt()
                }
            }
        } else {
            x += xDiff
            xDiff = Int.MAX_VALUE
        }
    }

    println()
    println("1: $res1")
}
