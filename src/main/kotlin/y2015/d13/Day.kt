package y2015.d13

val placements: MutableSet<Pair<Array<Person>, Int>> = mutableSetOf()

fun day(data: List<String>) {
    val people = mutableSetOf<Person>()
    data.forEach { line ->
        val parts = line.split(' ')
        people.add(Person(parts[0]))
        val from = people.find { it.name == parts[0] }!!
        people.add(Person(parts.last().trim('.')))
        val to = people.find { it.name == parts.last().trim('.') }!!
        val factor = if (parts[2] == "gain") 1 else -1
        val happiness = factor * parts[3].toInt()
        from.addNeighbour(to, happiness)
    }

    permute(people.toTypedArray(), 0, people.size-1)

    val best1 = placements.sortedBy { it.second }
    println("Part1: ${best1.last().second}")

    val santa = Person("Santa")
    people.add(santa)
    val other = people.filter { it != santa }
    other.forEach { person ->
        santa.addNeighbour(person, 0)
        person.addNeighbour(santa, 0)
    }


    placements.clear()
    permute(people.toTypedArray(), 0, people.size-1)

    val best2 = placements.sortedBy { it.second }
    println("Part2: ${best2.last().second}")

}

fun swapPlaces(a: Array<Person>, l: Int, r: Int) {
    val ch = a[l]
    a[l] = a[r]
    a[r] = ch
}

fun permute(a: Array<Person>, l: Int, r: Int) {
    if (l == r) {
        val b = Array(a.size) { a[it] }
        var happiness = 0
        b.indices.forEach { index ->
            happiness += when(index) {
                0 -> {
                    b[index].getHappiness(b.last(), b[1])
                }
                b.lastIndex -> {
                    b[index].getHappiness(b[index-1], b.first())
                }
                else -> {
                    b[index].getHappiness(b[index-1],b[index+1])
                }
            }
        }
        placements.add(Pair(b, happiness))
    } else {
        (l..r).forEach { i ->
            swapPlaces(a, l, i)
            permute(a, l + 1, r)
            swapPlaces(a, l, i)
        }
    }
}

class Person(val name: String) {
    private val neighbours = mutableMapOf<Person, Int>()
    fun addNeighbour(person: Person, happiness: Int) {
        neighbours[person] = happiness
    }

    fun getHappiness(left: Person, right: Person): Int {
        return neighbours.filter { it.key == left || it.key == right}.entries.sumOf { it.value }
    }

    override fun toString(): String {
        return name
    }

    override fun equals(other: Any?): Boolean {
        if(other == null) return false
        if(other !is Person) return false
        return name == other.name
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }
}