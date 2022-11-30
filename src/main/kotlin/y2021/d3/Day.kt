package y2021.d3

import kotlin.math.pow

@Suppress("unused")
fun day(data: List<String>) {
    val numChars = 12
    var oxygen = 0
    var co2 = 0

    var factor = 2.0.pow(numChars - 1).toInt()

    var oxfilter = ""

    (0 until numChars).forEach { filterIndex ->
        val oneVector = mutableListOf<Int>()
        val zeroVector = mutableListOf<Int>()
        (0 until numChars).forEach { _ ->
            oneVector.add(0)
            zeroVector.add(0)
        }
        data.forEach { line ->
                if (filterIndex == 0) {
                    if (line[filterIndex] == '1') {
                        oneVector[filterIndex]++
                    } else {
                        zeroVector[filterIndex]++
                    }
                } else {
                    if (line.startsWith(oxfilter)) {
                        if (line[filterIndex] == '1') {
                            oneVector[filterIndex]++
                        } else {
                            zeroVector[filterIndex]++
                        }
                    }
                }
            }

        if (oneVector[filterIndex] >= zeroVector[filterIndex]) {
            oxfilter = "${oxfilter}1"
            oxygen += factor
        } else if (oneVector[filterIndex] < zeroVector[filterIndex]) {
            oxfilter = "${oxfilter}0"
        }

        factor /= 2
    }

    factor = 2.0.pow(numChars - 1).toInt()

    oxfilter = ""

    (0 until numChars).forEach { filterIndex ->
        val oneVector = mutableListOf<Int>()
        val zeroVector = mutableListOf<Int>()
        (0 until numChars).forEach { _ ->
            oneVector.add(0)
            zeroVector.add(0)
        }
        var numHits = 0
        data.forEach { line ->
                if (filterIndex == 0) {
                    numHits = 2
                    if (line[filterIndex] == '1') {
                        oneVector[filterIndex]++
                    } else {
                        zeroVector[filterIndex]++
                    }
                } else {
                    if (line.startsWith(oxfilter)) {
                        numHits++
                        if (line[filterIndex] == '1') {
                            oneVector[filterIndex]++
                        } else {
                            zeroVector[filterIndex]++
                        }
                    }
                }
            }
        if (oneVector[filterIndex] >= zeroVector[filterIndex]) {
            if (numHits > 1) {
                oxfilter = "${oxfilter}0"
            } else {
                oxfilter = "${oxfilter}1"
                co2 += factor
            }
        } else {
            if (numHits > 1) {
                oxfilter = "${oxfilter}1"
                co2 += factor
            } else {
                oxfilter = "${oxfilter}0"
            }
        }


        factor /= 2
    }

    println("oxygen: $oxygen")
    println("co2 $co2")
    println("filter: $oxfilter")
    println("mulitplied ${oxygen * co2}")
}