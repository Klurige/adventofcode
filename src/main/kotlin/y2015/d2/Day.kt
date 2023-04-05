package y2015.d2

@Suppress("unused")
fun day(data: List<String>) {

    var paper = 0
    var ribbon = 0

    data.forEach { line ->
        val parcel = line.trim().split('x').map { it.toInt() }.sorted()
        val sides = listOf(parcel[0] * parcel[1], parcel[0] * parcel[2], parcel[1] * parcel[2])
        paper += 2 * sides.sum() + sides[0]
        ribbon += 2 * parcel[0] + 2 * parcel[1] + parcel[0] * parcel[1] * parcel[2]
    }

    println(paper)
    println(ribbon)
}