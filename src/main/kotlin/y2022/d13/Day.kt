package y2022.d13


open class Entry {
    open operator fun compareTo(that: Entry): Int {
        throw IllegalAccessException()
    }
}

data class EntryValue(val v: Int) : Entry() {
    override fun toString(): String {
        return "$v"
    }

    override operator fun compareTo(that: Entry): Int {
        if (that !is EntryValue) throw IllegalArgumentException()
        return v.compareTo(that.v)
    }
}

data class EntryList(private val str: String, private val start: Int = 0) : Entry() {
    val data = mutableListOf<Entry>()
    private var leftOvers: Int = Int.MIN_VALUE

    init {
        var index = start
        var num = Int.MIN_VALUE
        if (str[index] != '[') {
            data.add(EntryValue(str.toInt()))
        } else {
            index++
            while (index < str.length && leftOvers < 0) {
                when (str[index]) {
                    ',' -> {
                        if (num != Int.MIN_VALUE) {
                            data.add(EntryValue(num))
                            num = Int.MIN_VALUE
                        }
                        index++
                    }

                    '[' -> {
                        val entry = EntryList(str, index)
                        data.add(entry)
                        index = entry.leftOvers
                    }

                    ']' -> {
                        if (num != Int.MIN_VALUE) {
                            data.add(EntryValue(num))
                            num = Int.MIN_VALUE
                        }
                        leftOvers = index + 1
                    }

                    in ('0'..'9') -> {
                        if (num == Int.MIN_VALUE) num = 0
                        num = num * 10 + str[index++].digitToInt()
                    }

                    else -> throw IllegalArgumentException()
                }
            }
        }
    }

    override fun toString(): String {
        val sb = StringBuilder()
        sb.append('[')
        data.forEachIndexed { index, entry ->
            sb.append(entry)
            if (index != data.lastIndex) sb.append(',')
        }
        sb.append(']')
        return sb.toString()
    }

    override operator fun compareTo(that: Entry): Int {
        var rightIndex = 0
        data.forEach { left ->
            val right = if (that is EntryList) {
                if (rightIndex <= that.data.lastIndex) {
                    that.data[rightIndex++]
                } else {
                    return 1
                }
            } else {
                that
            }
            if (left is EntryList) {
                if (right is EntryList) {
                    if (left < right) return -1
                    if (left > right) return 1
                } else {
                    val rightList = EntryList(right.toString())
                    if (left < rightList) return -1
                    if (left > rightList) return 1
                }
            } else {
                if (right is EntryValue) {
                    if (left < right) return -1
                    if (left > right) return 1
                } else {
                    val leftList = EntryList(left.toString())
                    if (leftList < right) return -1
                    if (leftList > right) return 1
                }
            }
        }
        return data.size.compareTo((that as EntryList).data.size)
    }
}

fun day(data: List<String>) {
    println("2022-12-13")

    var res1 = 0

    val entries = mutableListOf<Entry>()
    data.windowed(2, 3).forEachIndexed { index,  pair ->
        if (pair[0][0] != '#') {
            val entry1 = EntryList(pair[0])
            entries.add(entry1)
            val entry2 = EntryList(pair[1])
            entries.add(entry2)
            if (entry1 < entry2) res1 += index+1
        }
    }

    val divider1 = EntryList("[[2]]")
    val divider2 = EntryList("[[6]]")
    entries.add(divider1)
    entries.add(divider2)

    var isChanged = true
    while (isChanged) {
        isChanged = false
        repeat(entries.lastIndex) {
            if (entries[it] > entries[it + 1]) {
                isChanged = true
                val tmp = entries.removeAt(it + 1)
                entries.add(it, tmp)
            }
        }
    }

    println("1: $res1")
    println("2: ${(entries.indexOf(divider1) + 1) * (entries.indexOf(divider2) + 1)}")
}
