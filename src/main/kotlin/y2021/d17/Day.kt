package y2021.d17

fun day(data: List<String>) {
    lateinit var target: Rect
    data.forEach { line ->
        val parts = line.split(", ")
        val yrange = parts.last().split('=')[1]
        val xrange = parts.first().split('=')[1]
        target = Rect(xrange, yrange)
    }

    val probe = Probe()
    val passedTarget = Rect("${target.xmax+1}..10000", "-10000..10000")
    val belowTarget = Rect("0..10000", "-10000..${target.ymin-1}")
    var best = 0
    var numHits = 0
    (1..2* target.xmax).forEach { x ->
        (target.ymin..-target.ymin+1).forEach { y ->
            probe.set(x, y)
            while (!probe.isHit(passedTarget) && !probe.isHit(belowTarget) && !probe.isHit(target)) {
                probe.step()
            }
            if (probe.isHit(target)) {
                numHits++
                if (probe.peak > best) best = probe.peak
            }

        }
    }

    println(best)
    println(numHits)
}

class Probe {
    var x = 0
    var y = 0
    var vX = 0
    var vY = 0
    var peak = 0

    fun set(velocityX: Int, velocityY: Int) {
        x = 0
        y = 0
        vX = velocityX
        vY = velocityY
        peak = 0
    }

    fun step() {
        x += vX
        y += vY
        if (vX > 0) vX--
        else if (vX < 0) vX++
        vY--

        if (y > peak) peak = y
    }

    fun isHit(target: Rect): Boolean {
        return target.contains(x, y)
    }

    override fun toString(): String {
        return "[$x, $y]"
    }
}

class Rect(xrange: String, yrange: String) {
    private val xmin: Int
    val xmax: Int
    val ymin: Int
    val ymax: Int

    init {
        val xparts = xrange.split("..")
        xmin = xparts.first().toInt()
        xmax = xparts.last().toInt()
        val yparts = yrange.split("..")
        ymin = yparts.first().toInt()
        ymax = yparts.last().toInt()
    }

    fun contains(x: Int, y: Int): Boolean {
        return (x in xmin..xmax) && (y in ymin..ymax)
    }

    override fun toString(): String {
        return "with: $xmin -> $xmax height: $ymin -> $ymax"
    }
}