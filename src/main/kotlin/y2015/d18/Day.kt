package y2015.d18

import kotlin.math.max
import kotlin.math.min


var numRows = 100
var numCols  = 100

fun day(data: List<String>) {
    val lightsFirst = mutableSetOf<Pair<Int, Int>>()
    val lightsSecond = mutableSetOf<Pair<Int, Int>>()
    numRows = data.size
    numCols = data[0].length
    data.forEachIndexed { row, line ->
        line.forEachIndexed { col, c ->
            if(c == '#') lightsFirst.add(Pair(row, col))
        }
    }

    lightsFirst.add(Pair(0,0))
    lightsFirst.add(Pair(numRows-1,0))
    lightsFirst.add(Pair(0, numCols-1))
    lightsFirst.add(Pair(numRows-1, numCols-1))
    repeat(100) {
        //printLights(lightsFirst)
        lightsSecond.clear()
        repeat(numRows) { row ->
            repeat(numCols) { col ->
                val light = Pair(row,col)
                val numNeighbours = lightsFirst.filter { it.first in (max(0,row-1)..min(row+1, numRows-1)) && it.second in (max(0,col-1)..min(col+1, numRows-1))}.size
                if(lightsFirst.contains(light)) {
                    if(numNeighbours == 3 || numNeighbours == 4) {
                        lightsSecond.add(light)
                    }
                } else {
                    if(numNeighbours == 3) lightsSecond.add(light)
                }
            }
        }
        lightsFirst.clear()
        lightsFirst.addAll(lightsSecond)
        lightsFirst.add(Pair(0,0))
        lightsFirst.add(Pair(numRows-1,0))
        lightsFirst.add(Pair(0, numCols-1))
        lightsFirst.add(Pair(numRows-1, numCols-1))

    }
    //printLights(lightsFirst)

    println("NumLightsOn: ${lightsFirst.size}")
}

fun printLights(lights: Set<Pair<Int, Int>>) {
    repeat(numRows) { row ->
        repeat(numCols) { col ->
            if(lights.contains(Pair(row,col))) print('#') else print('.')
        }
        println()
    }
    println()
}