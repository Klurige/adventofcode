package y2022.d6


fun day(data: List<String>) {
    println("2022-12-06")

    var index = 4
    data[0].windowed(4, 1) {
        if (index > 0 && it.toList().distinct().size == 4) {
            println("1: $index")
            index = Int.MIN_VALUE // Only get the first answer.
        }
        index++
    }
    
    index = 14;
    data[0].windowed(14, 1) {
        if (index > 0 && it.toList().distinct().size == 14) {
            println("2: $index")
            index = Int.MIN_VALUE // Only get the first answer.
        }
        index++
    }
}


