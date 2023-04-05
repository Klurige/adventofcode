package y2021.d20

var top = Int.MAX_VALUE
var bottom = Int.MIN_VALUE
var left = Int.MAX_VALUE
var right = Int.MIN_VALUE
var enhancement: String = ""

fun day(data: List<String>) {
    enhancement = data[0]
    val inputImage = mutableMapOf<Pixel, Boolean>()
    (2 until data.size).forEach { row ->
        data[row].forEachIndexed { col, char ->
            if (char == '#') inputImage[Pixel(row - 2, col)] = true
        }
    }

    println("Part1: ${enhance(2, inputImage)}")

    println("Part3: ${enhance(50, inputImage)}")

}

fun enhance(cycles: Int, image: Map<Pixel, Boolean>): Int {
    val inputImage = image.toMutableMap()
    adjustLimits(inputImage)

    repeat(cycles) {
        val outputImage = mutableMapOf<Pixel, Boolean>()
        val frame = 1
        (top - frame..bottom + frame).forEach { row ->
            (left - frame..right + frame).forEach { col ->
                val isOdd = if(enhancement.first() == '.')  false else  (it and 1) == 1
                val px = Pixel(row, col)
                val index = px.getIndex(inputImage, isOdd)
                val isLit = enhancement[index] == '#'
                if (isLit) outputImage[px] = true
            }
        }
        inputImage.clear()
        inputImage.putAll(outputImage)
        adjustLimits(inputImage)
    }
    return inputImage.size
}

fun adjustLimits(image: Map<Pixel, Boolean>) {
    image.forEach { pixel ->
        if (pixel.key.row < top) top = pixel.key.row
        if (pixel.key.row > bottom) bottom = pixel.key.row
        if (pixel.key.col < left) left = pixel.key.col
        if (pixel.key.col > right) right = pixel.key.col
    }

}

data class Pixel(val row: Int, val col: Int) {

    fun getIndex(image: Map<Pixel, Boolean>, isOdd: Boolean): Int {
        val binary = BooleanArray(9)
        var binaryIndex = 0
        (row - 1..row + 1).forEach { r ->
            (col - 1..col + 1).forEach { c ->
                val rIn = (r in top..bottom)
                val cIn = (c in left..right)
                if (rIn && cIn) {
                    binary[binaryIndex] = image.getOrDefault(Pixel(r,c), false)
                } else {
                    binary[binaryIndex] = isOdd
                }
                binaryIndex++
            }
        }

        var index = 0
        binary.forEach { isOne ->
            index *= 2
            if (isOne) index++
        }

        return index
    }
}