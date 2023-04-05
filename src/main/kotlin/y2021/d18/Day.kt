package y2021.d18

import java.util.*

fun reduce(line: String): String {
    var isDone = false
    var l = line
    while (!isDone) {
        val lExpanded = expand(l)
        val lSplit = split(lExpanded)
        isDone = lSplit == l
        l = lSplit
    }
    return l
}

fun split(line: String): String {
    line.indices.forEach { index ->
        if(line[index] != '(' && line[index] != ')') {
            if(line[index].code > '9'.code) {
                val v = line[index].code - '0'.code
                val v1 = v / 2
                val v2 = (v+1) / 2
                val a = (v1 + '0'.code).toChar()
                val b = (v2 + '0'.code).toChar()
                val left = line.substring(0, index)
                val right = line.substring(index+1)
                return "${left}(${a}${b})${right}"
            }
        }
    }
    return line
}

fun expand(line: String): String {
    var after = line
    var before: String
    do {
        before = after
        after = expandInternal(before)
    } while(after != before)
    return after
}

fun expandInternal(line: String): String {
    val brackets = Stack<Char>()

    line.indices.forEach { index ->
        when (line[index]) {
            '(' -> {
                brackets.push('(')
            }
            ')' -> {
                brackets.pop()
                if(brackets.size >= 4) {
                    val x = line[index - 2].code - '0'.code
                    val y = line[index - 1].code - '0'.code
                    var left = line.substring(0, index - 3)
                    val lastDigit = left.indexOfLast { it != ')' && it != '(' }
                    if(lastDigit >= 0) {
                        val chars = left.toCharArray()
                        val newChar = chars[lastDigit].code + x
                        if(newChar > 255) TODO()
                        chars[lastDigit] = chars[lastDigit] + x
                        left = chars.joinToString("")
                    }
                    var right = line.substring(index + 1)
                    val firstDigit = right.indexOfFirst { it != ')' && it != '(' }

                    if(firstDigit >= 0) {
                        val chars = right.toCharArray()
                        val newChar = chars[firstDigit].code + y
                        if(newChar > 255) TODO()
                        chars[firstDigit] = chars[firstDigit] + y
                        right = chars.joinToString("")
                    }
                    return "${left}0${right}"
                }
            }
        }
    }
    return line
}

fun magnitude(sum: String): Int {
    val stack = Stack<Int>()
    sum.forEach { ch ->
        when(ch) {
            '(' -> stack.push(0)
            ')' -> {
                val a = stack.pop()
                val b = stack.pop()
                val s = 3*b+ 2*a
                stack.pop()
                stack.push(s)
            }
            else -> {
                val a = ch.code - '0'.code
                stack.push(a)
            }
        }
    }
return stack.pop()
}


fun day(data: List<String>) {
    val numbers = mutableListOf<String>()
    data.forEach { line ->
        val a = line.replace(",", "")
        val b = a.replace("[", "(")
        val c = b.replace("]", ")")
        numbers.add(reduce(c))
    }

    var sum = numbers[0]
    (1 until numbers.size).forEach {  number ->
        sum = "($sum${numbers[number]})"
        sum = reduce(sum)
    }
    val m = magnitude(sum)
    println("Part1: $sum -> $m")

    var max = Int.MIN_VALUE
    numbers.indices.forEach { first ->
        numbers.indices.forEach { second ->
            if(first != second) {
                var sum = "(${numbers[first]}${numbers[second]})"
                sum = reduce(sum)
                val n = magnitude(sum)
                if(n > max) max = n
            }
        }
    }

    println("Part2: $max")
}