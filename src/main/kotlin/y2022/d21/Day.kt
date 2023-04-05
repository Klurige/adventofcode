package y2022.d21


data class Monkey(val name: String, val operation: List<String> = emptyList()) {
    override fun toString(): String {
        val op1 = needs[0].second?.toString() ?: needs[0].first
        val op2 = needs[1].second?.toString() ?: needs[1].first
        return "$name [$op1 $operator $op2]"
    }

    fun calcResult() {
        if (result == null) {
            if (needs.all { it.second != null }) {
                result = when (operator) {
                    "+" -> {
                        needs[0].second!! + needs[1].second!!
                    }

                    "-" -> {
                        needs[0].second!! - needs[1].second!!
                    }

                    "*" -> {
                        needs[0].second!! * needs[1].second!!
                    }

                    "/" -> {
                        needs[0].second!! / needs[1].second!!
                    }

                    else -> throw NullPointerException()
                }
            }
        }
    }

    fun setOperand(key: String, result: Long) {
        if (needs.isNotEmpty()) {
            if (needs[0].first == key) {
                needs[0] = Pair(key, result)
            } else if (needs[1].first == key) {
                needs[1] = Pair(key, result)
            } else {
                throw NullPointerException()
            }
        }

    }

    fun missingValue(): Long? {
        val known = needs[0].second ?: needs[1].second
        if(known != null) {
            return when (operator) {
                "+" -> {
                    expected!! - known
                }

                "-" -> {
                    expected!! + known
                }

                "*" -> {
                    expected!! / known
                }

                "/" -> {
                    expected!! * known
                }

                else -> throw NullPointerException()
            }
        } else {
            return null
        }
    }
    var expected: Long? = null

    fun missingMonkey(): Monkey {
        val unknown = if(needs[0].second == null) {
            needs[0]
        } else {
            needs[1]
        }
        val monkey = Monkey(unknown.first)
        monkey.expected = missingValue()
        return monkey
    }



    var result: Long? = null
    val needs = mutableListOf<Pair<String, Long?>>()
    var operator = ""

    init {
        if (operation.size > 0) {
            if (operation.size == 1) {
                result = operation[0].toLong()
            } else {
                needs.add(Pair(operation[0], null))
                needs.add(Pair(operation[2], null))
                operator = operation[1]
            }
        }
    }
}

fun readData(data: List<String>): Pair<MutableMap<String, Monkey>, MutableMap<String, Long>> {
    val monkeys = mutableMapOf<String, Monkey>()
    val knownMonkeys = mutableMapOf<String, Long>()
    data.forEach { line ->
        if (line.isNotEmpty()) {
            val name = line.substringBefore(':')
            val operation = line.substringAfter(':').trim().split(' ')
            if (operation.size == 1) {
                knownMonkeys[name] = operation[0].toLong()
            } else {
                monkeys[name] = Monkey(name, operation)
            }
        }
    }
    return Pair(monkeys, knownMonkeys)
}

fun part1(data: List<String>): Long {
    val (monkeys, knownMonkeys) = readData(data)

    while (!knownMonkeys.containsKey("root")) {
        val doneMonkeys = mutableSetOf<String>()
        monkeys.forEach { monkey ->
            if (knownMonkeys.containsKey(monkey.value.needs[0].first)) {
                monkey.value.setOperand(monkey.value.needs[0].first, knownMonkeys[monkey.value.needs[0].first]!!)
            }
            if (knownMonkeys.containsKey(monkey.value.needs[1].first)) {
                monkey.value.setOperand(monkey.value.needs[1].first, knownMonkeys[monkey.value.needs[1].first]!!)
            }
            monkey.value.calcResult()
            if (monkey.value.result != null) {
                knownMonkeys[monkey.key] = monkey.value.result!!
                doneMonkeys.add(monkey.key)
            }

        }
        doneMonkeys.forEach {
            monkeys.remove(it)
        }
    }

    return knownMonkeys["root"]!!
}

fun part2(data: List<String>): Long {
    val (monkeys, knownMonkeys) = readData(data)

    knownMonkeys.remove("humn")
    monkeys["root"]!!.expected = 300L
    while (monkeys.isNotEmpty()) {
            val doneMonkeys = mutableSetOf<String>()
            monkeys.forEach { monkey ->
                if (knownMonkeys.containsKey(monkey.value.needs[0].first)) {
                    monkey.value.setOperand(monkey.value.needs[0].first, knownMonkeys[monkey.value.needs[0].first]!!)
                }
                if (knownMonkeys.containsKey(monkey.value.needs[1].first)) {
                    monkey.value.setOperand(monkey.value.needs[1].first, knownMonkeys[monkey.value.needs[1].first]!!)
                }
                monkey.value.calcResult()
                if (monkey.value.result != null) {
                    knownMonkeys[monkey.key] = monkey.value.result!!
                    doneMonkeys.add(monkey.key)
                } else if(monkey.value.expected != null) {
                    val v = monkey.value.missingValue()
                    if(v != null) {
                        val known = monkey.value.missingMonkey()
                        if(known.name == "humn") {
                            return v
                        } else {
                            monkeys[known.name]!!.expected = v
                        }
                        doneMonkeys.add(monkey.key)
                    }
                }
            }
            doneMonkeys.forEach {
                monkeys.remove(it)
            }
        }
    throw NullPointerException()
}

fun day(data: List<String>) {
    println("2022-12-21")

    println("1: ${part1(data)}")
    println("2: ${part2(data)}")
}
