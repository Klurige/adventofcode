package y2015.d20

fun giftsForHouse(house:Int): Long {
    var gifts = 0L
    (house downTo 1).forEach { elf ->
        if((house % elf) == 0) {
            gifts += elf
        }
    }
    return gifts
}
fun day(data: List<String>) {
    var isDone = false
    var house = 1
    var gifts = 0L
    while(!isDone) {
        gifts = giftsForHouse(house)
        if (gifts >= 3400000L) isDone = true
        else
            house*=2
    }
    println("House number $house receives $gifts gifts, which is enough.")
    isDone = false
    while(!isDone) {
        gifts = giftsForHouse(house)
        if (gifts < 3400000L) isDone = true
        else
            house--
    }
    println("House number $house receives $gifts gifts, which is too few.")
}
