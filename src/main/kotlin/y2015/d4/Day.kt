package y2015.d4

import java.security.MessageDigest
import kotlin.text.Charsets.UTF_8

@Suppress("unused")
fun day(data: List<String>) {
    val input = "bgvyzdsv"

    val part1 = calculate(input, "00000")
    val part2 = calculate(input, "000000")

    println("Part 1: $part1")
    println("Part 2: $part2")
}

fun calculate(input: String, prefix: String): Int {
    var extra = 1
    while (true) {
        val result = md5("${input}$extra").toHex()
        if (result.startsWith(prefix)) {
            return extra
        }
        extra++
    }

}

fun md5(str: String): ByteArray = MessageDigest.getInstance("MD5").digest(str.toByteArray(UTF_8))
fun ByteArray.toHex() = joinToString(separator = "") { byte -> "%02x".format(byte) }
