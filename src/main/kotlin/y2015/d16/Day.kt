package y2015.d16

fun day(data: List<String>) {
    var sues = data.map { Sue(it) }.toSet()
    val props = setOf(
        Pair("children", 3),
        Pair("cats", 7),
        Pair("samoyeds", 2),
        Pair("pomeranians", 3),
        Pair("akitas", 0),
        Pair("vizslas", 0),
        Pair("goldfish", 5),
        Pair("trees", 3),
        Pair("cars", 2),
        Pair("perfumes", 1)
    )
    props.forEach { prop ->
        sues = sues.filter { sue ->
            sue.hasProperty(prop)
        }.toSet()
    }
    println("Part1: Sue: ${sues.first().index}")
}

class Sue(data: String) {
    val index: Int
    private val properties = mutableSetOf<Pair<String, Int>>()

    init {
        val name = data.substringBefore(':')
        index = name.split(' ')[1].toInt()

        val props = data.substringAfter(':').replace(" ", "").split(",")
        props.forEach { prop ->
            val p = prop.split(':')
            properties.add(Pair(p[0], p[1].toInt()))
        }
    }
    fun hasProperty(prop: Pair<String, Int>): Boolean {
        val found = properties.find { p ->
            p.first == prop.first
        } ?: return true
        return when(prop.first) {
            "cats", "trees" -> found.second > prop.second
            "pomeranians" ,"goldfish" -> found.second < prop.second
            else ->found.second == prop.second
        }
    }
}
