package y2015.d19

import kotlin.math.min


var medicine: String = ""
val replacements = mutableListOf<Pair<String, String>>()
var longest = 0

val molecules = mutableMapOf<String, Int>()
var fewest = 201

fun day(data: List<String>) {
    val first = data.partition { it.contains("=>") }

    first.first.forEach { replacement ->
        val parts = replacement.split(' ')
        replacements.add(Pair(parts[0], parts[2]))
        if (parts[2].length > longest) longest = parts[2].length
    }
    medicine = first.second.last()

    //val molecules = expand(medicine)
    //println("Part1: ${molecules.size}")

    molecules[medicine] = 0
    collapse()

    println("Part2: $fewest")
}

fun expand(med: String): Set<String> {
    val molecules = mutableSetOf<String>()
    med.indices.forEach { index ->
        val subs = mutableListOf<Triple<String, Int, Int>>()
        if (index > 0) {
            subs.add(Triple(med.substring(index - 1..index), index - 1, index))
        }
        if (index < med.lastIndex) {
            subs.add(Triple(med.substring(index..index + 1), index, index + 1))
        }
        subs.add(Triple("${med[index]}", index, index))
        subs.forEach { sub ->
            val repl = replacements.filter { it.first == sub.first }
            repl.forEach { r ->
                val left = med.substring(0 until sub.second)
                val mid = r.second
                val right = med.substring(sub.third + 1)
                molecules.add("${left}${mid}${right}")
            }
        }
    }
    return molecules
}

var numIterations = 0
tailrec fun collapse() {
    numIterations++
    if(numIterations > 10000) {
        println("${molecules.size}")
        numIterations = 0
    }
    if (molecules.isEmpty()) return
    val keys = molecules.keys
    val med = keys.sortedBy { it.length }.first()
    val steps = molecules[med]!!
    molecules.remove(med)
    if(steps > fewest) return
    if (med.isEmpty()) {
        if (steps < fewest) fewest = steps
    } else {
        if (steps < fewest) {
            (longest downTo 0).forEach { length ->
                val repl = replacements.filter { it.second.length == length }
                repl.forEach { r ->
                    med.indices.forEach { index ->
                        if (index + length - 1 <= med.lastIndex) {
                            val sub = med.substring(index until index + length)
                            if (sub == r.second) {
                                val left = if (med.substring(0, index) != "e") med.substring(0, index) else ""
                                val mid = if (r.first != "e") r.first else ""
                                val right =
                                    if (med.substring(index + 1 + sub.lastIndex) != "e") med.substring(index + 1 + sub.lastIndex) else ""
                                val shortened = "${left}${mid}${right}"
                                molecules[shortened] = min(steps + 1, molecules[shortened] ?: Int.MAX_VALUE)
                            }
                        }
                    }
                }
            }
        }
    }
    collapse()
}



