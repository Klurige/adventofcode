package y2015.d9

fun day(data: List<String>) {
    val loc = mutableSetOf<City>()

    data.forEach { line ->
        val parts = line.split(' ')
        val c1 = City(parts[0])
        loc.add(c1)
        val c2 = City(parts[2])
        loc.add(c2)
        val city1 = loc.find { it == c1 }
        val city2 = loc.find { it == c2 }
        city1!!.addCity(city2!!, parts[4].toInt())
        city2.addCity(city1, parts[4].toInt())
    }

    var shortest = Int.MAX_VALUE
    var longest = 0
    loc.forEach{ city ->
        val s = city.shortest(setOf(city))
        if(s < shortest) shortest = s
        val l = city.longest(setOf(city))
        if(l > longest) longest = l
    }
    println("Done: $shortest $longest")
}

class City(val name: String) {
    private val cities = mutableSetOf<Pair<City, Int>>()

    fun addCity(city: City, distance: Int) = cities.add(Pair(city, distance))

    fun shortest(visited: Set<City>): Int {
        var dist = Int.MAX_VALUE
        cities.forEach{ city ->
            if(visited.find { it == city.first } == null) {
                val c = setOf(city.first) + visited
                val d = city.first.shortest(c) + city.second
                if(d < dist) dist = d
            }
        }
        return if(dist < Int.MAX_VALUE) dist else 0
    }

    fun longest(visited: Set<City>): Int {
        var dist = 0
        cities.forEach{ city ->
            if(visited.find { it == city.first } == null) {
                val c = setOf(city.first) + visited
                val d = city.first.longest(c) + city.second
                if(d > dist) dist = d
            }
        }
        return dist
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if(other == null) {
            return false
        }
        if(other !is City) {
            return false
        }
        if(name == other.name) {
            return true
        } else {
            return false
        }
    }

    override fun toString(): String  = name
}