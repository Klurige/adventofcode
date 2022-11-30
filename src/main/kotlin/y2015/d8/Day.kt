package y2015.d8

fun day(data: List<String>) {

    var numCode = 0
    var numDecoded = 0
    var numEncoded = 0
    data.forEach { line ->
        numCode += line.length
        numDecoded += decode(line).length
        numEncoded += encode(line).length
    }
    println(numCode - numDecoded)
    println(numEncoded - numCode)
}

fun encode(msg:String):String {
    val chars = msg.toCharArray()
    var index = 0
    val data = StringBuilder()
    data.append('"')
    while (index < chars.size) {
        when (chars[index]) {
            '"' -> {
                data.append('\\')
                data.append(chars[index])
                index++
            }
            '\\' -> {
                data.append('\\')
                data.append(chars[index])
                index++
            }
            else -> data.append(chars[index++])
        }
    }
    data.append('"')
    return data.toString()
}

fun decode(msg: String): String {
    val chars = msg.toCharArray()
    var index = 0
    val data = StringBuilder()
    while (index < chars.size) {
        when (chars[index]) {
            '"' -> index++
            '\\' -> {
                index++
                when (chars[index]) {
                    '"' ->  data.append(chars[index++])
                    '\\' -> data.append(chars[index++])
                    'x' -> {
                        index++
                        val hex = "${chars[index++]}${chars[index++]}"
                        val v = hex.toInt(16)
                        data.append(v.toChar())
                    }
                    else -> println("Unsupported: $chars[index]")
                }
            }
            else -> data.append(chars[index++])
        }
    }
    return data.toString()
}