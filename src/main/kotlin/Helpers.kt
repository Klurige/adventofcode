fun String.toListOfInts(delim:Char): List<Int> {
    return split(delim).map { it.trim().toInt() }
}
