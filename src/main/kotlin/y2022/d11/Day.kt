package y2022.d11

import java.math.BigInteger

var commonDivisor: BigInteger = BigInteger.ONE

class Monkey(startingItems: List<Int>, private val operation: String, private val divisor: Int, private val ifTrue: Int, private val ifFalse: Int) {
    private val items = mutableListOf<BigInteger>()
    var inspected = 0

    init {
        startingItems.forEach {
            receive(it.toBigInteger())
        }
    }

    fun throwTo(reducer: Int): List<Pair<Int, BigInteger>> {
        val res = mutableListOf<Pair<Int, BigInteger>>()
        items.forEach { item ->
            val w = (worryLevel(item).divide(reducer.toBigInteger())).mod(commonDivisor)
            if (w.mod(divisor.toBigInteger()) == BigInteger.ZERO) {
                res.add(Pair(ifTrue, w))
            } else {
                res.add(Pair(ifFalse, w))
            }
        }
        inspected += items.size
        items.clear()
        return res
    }

    fun receive(item: BigInteger) {
        items.add(item)
    }

    private fun worryLevel(item: BigInteger): BigInteger {
        val p = operation.split(' ')
        val f = if (p[2] == "old") item else p[2].toBigInteger()
        return when (p[1]) {
            "*" -> item.times(f)
            "+" -> item.plus(f)
            "-" -> item.minus(f)
            else -> throw NullPointerException("Illegal op")
        }

    }
}

fun getMonkeys(data: List<String>): MutableList<Monkey> {
    val monkeys = mutableListOf<Monkey>()
    var dataIndex = 0
    while (dataIndex < data.size) {
        if (data[dataIndex].isEmpty()) dataIndex++ else {
            //val monkeyIndex = data[dataIndex++].substringAfter(' ').substringBefore(':').trim().toInt()
            dataIndex++
            val startingItems = data[dataIndex++].split(':')[1].split(',').map { it.trim().toInt() }
            val operation = data[dataIndex++].substringAfter('=').trim()
            val divisor = data[dataIndex++].substringAfterLast(' ').toInt()
            val ifTrue = data[dataIndex++].substringAfterLast(' ').toInt()
            val ifFalse = data[dataIndex++].substringAfterLast(' ').toInt()
            monkeys.add(Monkey(startingItems, operation, divisor, ifTrue, ifFalse))
            commonDivisor = commonDivisor.multiply(divisor.toBigInteger())
        }
    }
    return monkeys
}

fun day(data: List<String>) {
    println("2022-12-11")

    val monkeys1 = getMonkeys(data)

    repeat(20) {
        monkeys1.forEach { m ->
            val items = m.throwTo(3)
            items.forEach {
                monkeys1[it.first].receive(it.second)
            }
        }
    }

    val res1 = monkeys1
        .map { it.inspected.toBigInteger() }
        .sortedDescending()
        .subList(0,2)
        .reduce{acc, it -> acc.multiply(it)}
    println("1: $res1")

    val monkeys2 = getMonkeys(data)

    repeat(10000) {
        monkeys2.forEach { m ->
            val items = m.throwTo(1)
            items.forEach {
                monkeys2[it.first].receive(it.second)
            }
        }
    }

    val res2 = monkeys2
        .map { it.inspected.toBigInteger() }
        .sortedDescending()
        .subList(0,2)
        .reduce{acc, it -> acc.multiply(it)}
    println("1: $res2")
}
