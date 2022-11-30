package y2021.d19

fun day(data: List<String>) {
    val scanners = mutableListOf<Scanner>()
    data.forEach { line ->
        if (line.isNotEmpty()) {
            if (line.startsWith("---")) {
                val parts = line.split(' ')
                val index = parts[2].toInt()
                scanners.add(Scanner(index))
            } else {
                val parts = line.split(',')
                val beacon = Beacon(Point(parts[0].toInt(), parts[1].toInt(), parts[2].toInt()))
                scanners.last().addBeacon(beacon)
            }
        }
    }

    scanners[0].setPosition(Point(0, 0, 0))
    scanners[0].tryMapping(scanners[0])

    var numDone = scanners.count { it.isPlaced }
    while (numDone < scanners.size) {
        scanners.forEach { first ->
            scanners.forEach { second ->
                if(first != second && first.isPlaced && !second.isPlaced)
                    second.tryMapping(first)
            }
        }
        numDone = scanners.count { it.isPlaced }
        println("Done: $numDone / ${scanners.size}")
    }

    val beacons = mutableSetOf<Point>()
    scanners.forEach { scanner ->

        beacons.addAll(scanner.getAbsoluteBeacons())
    }

    println("Part1: ${beacons.size} beacons.")

    var maxDistance = 0

    scanners.forEach { first ->
        scanners.forEach { second ->
            val distance = first.getDistanceTo(second)
            if(distance > maxDistance) maxDistance = distance
        }
    }

    println("Part2: $maxDistance")

}

data class Point(val x: Int, val y: Int, val z: Int) {
    override fun toString(): String {
        return "$x,$y,$z"
    }
}

class Scanner(val index: Int) {
    private val beacons = mutableListOf<Beacon>()
    private var position = Point(0, 0, 0)
    private var orientation = intArrayOf(0, 0, 0)
    var isPlaced = false

    fun tryMapping(other: Scanner) {
        (0 until 360 step 90).forEach { rotX ->
            (0 until 360 step 90).forEach { rotY ->
                (0 until 360 step 90).forEach { rotZ ->
                    if (!(rotX == 180 && rotY == 180 && rotZ == 180)) {
                        setOrientation(rotX, rotY, rotZ)
                        beacons.forEach { thisBeacon ->
                            other.beacons.forEach { otherBeacon ->
                                setPosition(other.position)
                                val x = otherBeacon.getPosition().x - thisBeacon.getPosition().x + other.position.x
                                val y = otherBeacon.getPosition().y - thisBeacon.getPosition().y + other.position.y
                                val z = otherBeacon.getPosition().z - thisBeacon.getPosition().z  + other.position.z
                                setPosition(Point(x, y, z))
                                if(thisBeacon.getPosition() != otherBeacon.getPosition()) {
                                    TODO()
                                }
                                val hit = beacons.count { tb ->
                                    val found = other.beacons.find { ob ->
                                        tb.getPosition() == ob.getPosition()
                                    }
                                    found != null
                                }
                                if (hit == 0) {
                                    TODO()
                                }
                                if (hit >= 12) {
                                    isPlaced = true
                                    return
                                }
                            }
                        }
                    }
                }
            }
        }
        }

    fun getDistanceTo(other: Scanner): Int {
        return position.x - other.position.x + position.y - other.position.y + position.z - other.position.z
    }

    fun getAbsoluteBeacons(): List<Point> {
        return beacons.map { beacon ->
            beacon.getPosition()
        }
    }

    fun setPosition(pos: Point) {
        position = pos
        beacons.forEach { beacon ->
            beacon.adjust(position, orientation)
        }
    }

    fun setOrientation(x: Int, y: Int, z: Int) {
        orientation = intArrayOf(x, y, z)
        beacons.forEach { beacon ->
            beacon.adjust(position, orientation)
        }
    }

    fun addBeacon(beacon: Beacon) {
        beacon.adjust(position, orientation)
        beacons.add(beacon)
    }

    override fun toString(): String {
        return "$index[${beacons.size}]"
    }

    fun print(beacon: Int = -1) {
        println("--- scanner $index --- [$position] {${orientation[0]},${orientation[1]},${orientation[2]}} $isPlaced")
        if (beacon == -1) {
            beacons.forEach { b ->
                println(b)
            }
        } else {
            println(beacons[beacon])
        }
    }
}

class Beacon(private val position: Point) {
    var adjusted = Point(0, 0, 0)

    fun adjust(pos: Point, orientation: IntArray) {
        var x = position.x
        var y = position.y
        var z = position.z
        when (orientation[2]) {
            90 -> {
                val t = x
                x = -y
                y = t
            }
            180 -> {
                x = -x
                y = -y
            }
            270 -> {
                val t = x
                x = y
                y = -t
            }
        }
        when (orientation[1]) {
            90 -> {
                val t = x
                x = -z
                z = t
            }
            180 -> {
                x = -x
                z = -z
            }
            270 -> {
                val t = x
                x = z
                z = -t
            }
        }
        when (orientation[0]) {
            90 -> {
                val t = y
                y = -z
                z = t
            }
            180 -> {
                y = -y
                z = -z
            }
            270 -> {
                val t = y
                y = z
                z = -t
            }
        }

        adjusted = Point(pos.x + x, pos.y + y, pos.z + z)
    }

    fun getPosition(): Point {
        return adjusted
    }

    override fun toString(): String {
        return "${getPosition()}"
    }
}