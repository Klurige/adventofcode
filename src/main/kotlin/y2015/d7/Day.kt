package y2015.d7

val wires = mutableListOf<Triple<String, Connection, UShort?>>()

fun day(data: List<String>) {

    data.forEach { cmd ->
        val params = cmd.split(" -> ")
        wires.add(Triple(params[1], connection(params[0]), null))
    }

    while (wires.any { it.third == null }) {
        wires.indices.forEach {
            val result  = wires[it].second.calculate(wires[it].first)
            wires[it] = result
        }
    }
    val part1 = wires.first { it.first == "a" }.third
    println("Part1: $part1")

    wires.clear()
    data.forEach { cmd ->
        val params = cmd.split(" -> ")
        wires.add(Triple(params[1], connection(params[0]), null))
    }

    val  b = wires.indexOfFirst { it.first == "b" }
    wires[b] = Triple(wires[b].first, Connection.Value(part1!!), null)
    while (wires.any { it.third == null }) {
        wires.indices.forEach {
            val result  = wires[it].second.calculate(wires[it].first)
            wires[it] = result
        }
    }

    println("Part2: ${wires.first { it.first == "a" }.third}")

}

fun connection(conn: String): Connection {
    val parts = conn.split(' ')
    return when (parts.size) {
        1 -> {
            if (parts[0].isNumber()) {
                Connection.Value(parts[0].toUShort())
            } else {
                Connection.Wire(parts[0])
            }
        }
        2 -> {
            if (parts[0] == "NOT") {
                Connection.Not(parts[1])
            } else {
                println("Found: ${parts[0]}")
                Connection.Invalid(parts[0])
            }
        }
        3 -> {
            when (parts[1]) {
                "AND" -> Connection.And(parts[0], parts[2])
                "OR" -> Connection.Or(parts[0], parts[2])
                "LSHIFT" -> Connection.Lshift(parts[0], parts[2].toInt())
                "RSHIFT" -> Connection.Rshift(parts[0], parts[2].toInt())
                else -> Connection.Invalid("Not handled: ${parts[1]}")
            }

        }
        else -> {
            Connection.Invalid("Not handled: $parts")
        }
    }
}

fun String.isNumber(): Boolean {
    return all { it.isDigit() }
}

fun String.isUpper(): Boolean {
    return !(any { it.isLowerCase() })
}

sealed class Connection {
    override fun toString(): String {
        return this.javaClass.name
    }
    abstract fun calculate(into: String): Triple<String, Connection, UShort?>
    class Value(private val value: UShort) : Connection() {
        override fun calculate(into: String): Triple<String, Connection, UShort?> {
            return Triple(into, this, value)
        }
    }

    class And(private val a: String, private val b: String) : Connection() {
        override fun calculate(into: String): Triple<String, Connection, UShort?> {
            val p1 = if (a.isNumber()) {
                a.toUShort()
            } else {
                wires.first() { it.first == a }.third
            }
            val p2 = if (b.isNumber()) {
                b.toUShort()
            } else {
                wires.first() { it.first == b }.third
            }
            return if (p1 != null && p2 != null) {
                Triple(into, this, p1.and(p2))
            } else {
                Triple(into, this, null)
            }
        }
    }

    class Or(val a: String, val b: String) : Connection() {
        override fun calculate(into: String): Triple<String, Connection, UShort?> {
            val p1 = if(a.isNumber()) {
                a.toUShort()
            } else {
                wires.first() { it.first == a }.third
            }
            val p2 = if(b.isNumber()) {
                b.toUShort()
            } else {
                wires.first() { it.first == b }.third
            }
            return if (p1 != null && p2 != null) {
                Triple(into, this, p1.or(p2))
            } else {
                Triple(into, this, null)
            }
        }
    }

    class Lshift(val wire: String, val bits: Int) : Connection() {
        override fun calculate(into: String): Triple<String, Connection, UShort?> {
            val p1 = if(wire.isNumber()) {
                wire.toUShort()
            } else {
                wires.first() { it.first == wire }.third
            }
            return if(p1 != null) {
                Triple(into, this, p1.shl(bits))
            } else {
                Triple(into, this, null)
            }
        }
    }

    class Rshift(val wire: String, val bits: Int) : Connection() {
        override fun calculate(into: String): Triple<String, Connection, UShort?> {
            val p1 = if(wire.isNumber()) {
                wire.toUShort()
            } else {
                wires.first() { it.first == wire }.third
            }
            return if(p1 != null) {
                Triple(into, this, p1.shr(bits))
            } else {
                Triple(into, this, null)
            }
        }
    }

    class Not(val wire: String) : Connection() {
        override fun calculate(into: String): Triple<String, Connection, UShort?> {
            val p1 = if(wire.isNumber()) {
                wire.toUShort()
            } else {
                wires.first() { it.first == wire }.third
            }
            return if(p1 != null) {
                Triple(into, this, p1.inv())
            } else {
                Triple(into, this, null)
            }
        }
    }

    class Wire(private val wire: String) : Connection() {
        override fun calculate(into: String): Triple<String, Connection, UShort?> {
            val p1 = if(wire.isNumber()) {
                wire.toUShort()
            } else {
                wires.first() { it.first == wire }.third
            }
            return if(p1 != null) {
                Triple(into, this, p1)
            } else {
                Triple(into, this, null)
            }
        }
    }

    class Invalid(private val msg: String) : Connection() {
        override fun calculate(into: String): Triple<String, Connection, UShort?> {
            return Triple(into, Invalid("TODO"), 0u)
        }

        override fun toString(): String {
            return msg
        }
    }
}

fun UShort.shr(bits: Int): UShort {
    return this.toInt().shr(bits).toUShort()
}
fun UShort.shl(bits: Int): UShort {
    return this.toInt().shl(bits).toUShort()
}