package y2016.d4

fun day(data: List<String>) {
    println("2016-12-04")
    var sum = 0
    data.forEach { line ->
        val name = line.substringBeforeLast('-')
        val id = line.substringAfterLast('-').substringBefore('[')
        val checksum = line.substringAfterLast('[').substringBefore(']')
        val letters = mutableMapOf<Char, Int>()
        name.forEach { ch ->
            if (ch != '-') {
                letters[ch] = letters[ch]?.plus(1) ?: 1
            }
        }
        val result = letters.toList().sortedBy { (key, _) -> key }.sortedByDescending { (_, value) -> value }.toMap().keys.toList()
            .subList(0, 5)
        if (checksum.toList() == result) {
            sum += id.toInt()

            val decrypted = mutableListOf<Char>()
            val key = id.toInt() % 26

            name.forEach { ch ->
                if (ch == '-') decrypted.add(' ')
                else {
                    val res = ch.lowercaseChar() + key
                        if (res > 'z') {
                            decrypted.add((res- 26).toChar())
                        }
                        else {
                            decrypted.add(res)
                        }

                }
            }
            if (decrypted.subList(0, 5).toCharArray().contentEquals("north".toCharArray())) {
                println("2: $decrypted $id")
            }
        }
    }
    println("1: $sum")

}

