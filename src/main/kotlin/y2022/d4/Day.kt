package y2022.d4

fun day(data: List<String>) {
    println("2022-12-04")
    var fully = 0
    var partial = 0
    data.forEach { line ->
        val cleaners = line.split(',')
        val area1 = cleaners[0].split('-').map { it.toInt() }
        val area2 = cleaners[1].split('-').map { it.toInt() }
        
        if ((area1[0] >= area2[0] && area1[1] <= area2[1]) || (area2[0] >= area1[0] && area2[1] <= area1[1])) {
            fully++
        }

        val range1 = IntRange(area1[0], area1[1])
        val range2 = IntRange(area2[0], area2[1])
        if (range1.intersect(range2).isNotEmpty()) {
            partial++
        }

    }
    println("1: $fully")
    println("2: $partial")
}
