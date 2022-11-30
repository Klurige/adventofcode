package y2015.d11

fun day(data: List<String>) {
    var input = "vzbxkghb"

    var isValid = false
    while(!isValid) {
        input = input.inc()
        isValid = validate(input)
    }
    println(input)

    isValid = false
    while(!isValid) {
        input = input.inc()
        isValid = validate(input)
    }
    println(input)
}

fun validate(pwd: String): Boolean {
    val straight = pwd.windowed(3).filter {
        it[2] == it[1] +1 && it[1] == it[0] + 1
    }
    if(straight.isEmpty()) return false

    val forbidden = pwd.count { a ->
        a == 'i' || a == 'o' || a == 'l'
    }
    if(forbidden > 0) return false

    val pairs = mutableListOf<Pair<String, Int>>()
    (0 until pwd.lastIndex).forEach { index ->
        if(pwd[index] == pwd[index+1]) {
            pairs.add(Pair("${pwd[index]}${pwd[index+1]}", index))
        }
    }

    if(pairs.size < 2) return false

    pairs.forEach { pair ->
        pairs.forEach { other ->
            if(pair.first == other.first && (pair.second == other.second-1 || pair.second == other.second+1)) return false
        }
    }
    return true
}

fun String.inc(): String {
    val data = toCharArray()
    data[data.lastIndex]++
    (data.lastIndex downTo 1).forEach { index ->
        if(data[index] > 'z') {
            val diff = data[index] - 'z'
            data[index-1] = data[index-1] + 1
            data[index] = 'a' + diff - 1
        } else {
            return data.joinToString("")
        }
    }
    return data.joinToString("")
}