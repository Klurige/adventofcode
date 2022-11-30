package y2021.d16

import java.lang.IllegalArgumentException

var version = 0
fun day(data: List<String>) {
    data.forEach { line ->
        version = 0
        val msgBuilder = StringBuilder()
        line.toCharArray().forEach { ch ->
            msgBuilder.append("$ch".toInt(16).toString(2).padStart(4, '0'))
        }
        val msg = msgBuilder.toString()

        val res = decode(msg, 0)
        println("Version: $version Res: $res")

    }
}

fun decode(msg: String, start: Int): Packet {
    var next = start
    val ver = msg.substring(next, next + 3).binaryToLong().toInt()
    version += ver
    next += 3
    val type = msg.substring(next, next + 3).binaryToLong().toInt()
    next += 3
    when (type) {
        0 -> { // sum
            val packets = getPackets(msg, next)
            val value = packets.sumOf { (it as Packet.Literal).value }
            return Packet.Literal(value, packets.first().start, packets.last().end)

        }
        1 -> { // product
            val packets = getPackets(msg, next)
            val value = packets.fold(1L) { res, packet ->
                res * (packet as Packet.Literal).value
            }
            return Packet.Literal(value, packets.first().start, packets.last().end)
        }
        2 -> { // minimum
            val packets = getPackets(msg, next)
            return packets.reduce { a,b ->
                val v = (a as Packet.Literal).value.coerceAtMost((b as Packet.Literal).value)
                Packet.Literal(v, packets.first().start, packets.last().end)
            }
        }
        3 -> { // maximum
            val packets = getPackets(msg, next)
            return packets.reduce { a,b ->
                val v = (a as Packet.Literal).value.coerceAtLeast((b as Packet.Literal).value)
                Packet.Literal(v, packets.first().start, packets.last().end)
            }
        }
        4 -> { // literal
            return literal(msg, next)
        }
        5 -> { // greater than
            val packets = getPackets(msg, next)
            if (packets.size != 2) throw IllegalArgumentException("Requires exactly two packets.")
            val value = if ((packets[0] as Packet.Literal).value > (packets[1] as Packet.Literal).value) {
                1L
            } else 0L
            return Packet.Literal(value, packets.first().start, packets.last().end)
        }
        6 -> { // less than
            val packets = getPackets(msg, next)
            if (packets.size != 2) throw IllegalArgumentException("Requires exactly two packets.")
            val value = if ((packets[0] as Packet.Literal).value < (packets[1] as Packet.Literal).value) {
                1L
            } else 0L
            return Packet.Literal(value, packets.first().start, packets.last().end)
        }
        7 -> { // equal
            val packets = getPackets(msg, next)
            if (packets.size != 2) throw IllegalArgumentException("Requires exactly two packets.")
            val value = if ((packets[0] as Packet.Literal).value == (packets[1] as Packet.Literal).value) {
                1L
            } else 0L
            return Packet.Literal(value, packets.first().start, packets.last().end)
        }
        else -> {
            val packets = getPackets(msg, next)
            return Packet.Empty(packets.first().start, packets.last().end)
        }
    }
}

fun getPackets(msg:String, start: Int): List<Packet> {
    var next = start
    val packets = mutableListOf<Packet>()
    if (msg[next] == '0') {
        next++
        var numberOfBitsInSubPackets = msg.substring(next, next + 15).binaryToLong().toInt()
        next += 15
        while(numberOfBitsInSubPackets > 0) {
            val pkt = decode(msg, next)
            packets.add(pkt)
            numberOfBitsInSubPackets -= (pkt.end - next + 1)
            next = pkt.end + 1
        }
    } else {
        next++
        var numberOfSubPackets = msg.substring(next, next + 11).binaryToLong().toInt()
        next += 11
        while(numberOfSubPackets > 0) {
            val pkt = decode(msg, next)
            packets.add(pkt)
            numberOfSubPackets--
            next = pkt.end + 1
        }
    }
    return packets
}

fun literal(msg: String, start: Int): Packet.Literal {
    val res = StringBuilder()
    var first = start - 5
    do {
        first += 5
        res.append(msg.substring(first + 1, first + 5))
    } while (msg[first] == '1')
    return Packet.Literal(res.toString().binaryToLong(), start-6, first + 4)
}

fun String.binaryToLong(): Long {
    var res: Long = 0
    forEach { bit ->
        res *= 2
        if (bit == '1') res += 1
    }
    return res
}

sealed class Packet(val start: Int, val end: Int) {
    class Literal(val value: Long, start: Int, end: Int) : Packet(start, end) {
        override fun toString(): String {
            return "$value"
        }
    }
    class Empty(start: Int, end: Int) : Packet(start, end) {
        override fun toString(): String {
            return "."
        }
    }
}