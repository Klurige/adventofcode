package y2016.d5

import java.math.BigInteger
import java.security.MessageDigest

fun md5(input: String): String {
    val md = MessageDigest.getInstance("MD5")
    return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0')
}

fun day(data: List<String>) {
    println("2016-12-05")

    val id = data[0]
    var index = 0

    var hash = md5("$id$index")
    val pwd1 = StringBuilder()
    val pwd2 = CharArray(8) { '_' }
    var pwd2Len = 0
    while (pwd1.length < 8 || pwd2Len < 8) {
        while (!hash.startsWith("00000")) {
            index++
            hash = md5("$id$index")
        }
        if (pwd1.length < 8) {
            pwd1.append("${hash[5]}")
        }
        val position = hash[5].code - '0'.code
        if (position < 8 && pwd2[position] == '_') {
            pwd2[position] = hash[6]
            pwd2Len++
        }
        index++
        hash = md5("$id$index")
    }

    println("1: $pwd1")
    println("2: ${String(pwd2)}")
}
