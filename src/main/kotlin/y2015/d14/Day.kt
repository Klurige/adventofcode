package y2015.d14

fun day(data: List<String>) {
    val r = mutableSetOf<Reindeer>()
    data.forEach { line ->
        val parts = line.split(' ')
        val reindeer = Reindeer(name = parts[0], speed = parts[3].toInt() , moving = parts[6].toInt(), resting = parts[13].toInt())
        r.add(reindeer)
    }
    (0 until 2503).forEach { s ->
        r.forEach { reindeer ->
            reindeer.movement(s)
        }

        val sorted = r.sortedBy { it.distance }
        val best = sorted.last().distance
        r.filter {it.distance == best}.forEach { it.points++ }
    }

    r.forEach { reindeer ->
        println(reindeer)
    }
}

data class Reindeer(val name: String, val speed: Int, val moving: Int, val resting: Int, var distance: Int = 0, var points: Int = 0) {
    fun movement(sec: Int) {
        val rest = sec % (moving+resting)
        if(rest < moving) {
            distance += speed
        }
    }

    override fun toString(): String {
        return "$name has travelled $distance km and gained $points points."
    }
}
