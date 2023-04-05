package y2015.d6

@Suppress("unused")
fun day(data: List<String>) {

    val onoffLights = Array(1000) { BooleanArray(1000) }
    val brightLights = Array(1000) { IntArray(1000) }
    data.forEach { line ->
        val s = line.split(" ")

        when {
            line.startsWith("toggle") -> {
                val start = s[1].split(',').map { it.toInt() }
                val end = s[3].split(',').map { it.toInt() }
                (start[0]..end[0]).forEach { x ->
                    (start[1]..end[1]).forEach { y ->
                        onoffLights[x][y] = !onoffLights[x][y]
                        brightLights[x][y] += 2
                    }
                }
            }
            line.startsWith("turn off") -> {
                val start = s[2].split(',').map { it.toInt() }
                val end = s[4].split(',').map { it.toInt() }
                (start[0]..end[0]).forEach { x ->
                    (start[1]..end[1]).forEach { y ->
                        onoffLights[x][y] = false
                        brightLights[x][y]--
                        if (brightLights[x][y] < 0) brightLights[x][y] = 0
                    }
                }
            }
            line.startsWith("turn on") -> {
                val start = s[2].split(',').map { it.toInt() }
                val end = s[4].split(',').map { it.toInt() }
                (start[0]..end[0]).forEach { x ->
                    (start[1]..end[1]).forEach { y ->
                        onoffLights[x][y] = true
                        brightLights[x][y]++
                    }
                }
            }
        }
    }

    var lit = 0
    onoffLights.indices.forEach { x ->
        onoffLights[x].indices.forEach { y ->
            if(onoffLights[x][y]) lit++
        }
    }
    var brightness = 0
    brightLights.indices.forEach { x ->
        brightLights[x].indices.forEach { y ->
            brightness += brightLights[x][y]
        }
    }

    println("Num lit: $lit")
    println("Brightness: $brightness")
}
