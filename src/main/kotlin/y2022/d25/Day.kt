package y2022.d25


fun String.snafuToLong(): Long {
    var index = 0
    var v = 0L
    while (index < length) {
        if (this[index].isDigit()) v = 5 * v + this[index].digitToInt()
        else if (this[index] == '-') v = 5 * v - 1
        else if (this[index] == '=') v = 5 * v - 2
        index++
    }
    return v
}

fun Long.toSnafu():String {
    val digits = arrayOf('=', '-','0', '1', '2')
    val res = mutableListOf<Char>()
    var left = this
    var carry = 0
    while (left > 0) {
        var digit:Int = (left % 5 + carry).toInt()
        carry = if (digit > 2) {
            digit -= 5
            1
        } else 0
        res.add(0, digits[(digit + 2)])
        left /= 5
    }
    if (carry == 1){
        res.add(0,digits[3])
    }
    return String(res.toCharArray())
}

fun day(data: List<String>) {
    println("2022-12-25")

    var res1 = 0L

    data.forEach { line ->
//        if(line != line.snafuToLong().toSnafu()) {throw IllegalArgumentException()}
        res1 += line.snafuToLong()
    }

    println("1: ${res1.toSnafu()}")
}
