package y2022.d7

abstract class Obj(val name: String) {
    abstract val size: Int
}

class File(name: String, override val size: Int) : Obj(name)

class Dir(name: String, val parent: Dir?) : Obj(name) {
    val contents = mutableListOf<Obj>()
    override val size: Int
        get() = contents.sumOf { it.size }

    fun cdTo(n: String) = (contents.find { it.name == n } ?: Dir(n, this)) as Dir

    fun summarise(dirs: MutableList<Dir>) {
        contents.forEach {
            if (it is Dir) {
                dirs.add(it)
                it.summarise(dirs)
            }
        }
    }
}

fun day(data: List<String>) {
    println("2022-12-07")

    val tree = Dir("/", null)
    var curTree = tree
    data.forEach { line ->
        when {
            line.startsWith("$ cd") -> {
                val name = line.substringAfterLast(' ')
                curTree = when (name) {
                    "/" -> {
                        tree
                    }

                    ".." -> {
                        curTree.parent!!
                    }

                    else -> {
                        curTree.cdTo(name)
                    }
                }

            }

            line.startsWith("$ ls") -> {
                // Do nothing
            }

            line.startsWith("dir ") -> {
                curTree.contents.add(Dir(line.substringAfterLast(' '), curTree))
            }

            else -> {
                val file = line.split(' ')
                curTree.contents.add(File(file[1], file[0].toInt()))
            }
        }
    }

    val dirs = mutableListOf<Dir>()
    tree.summarise(dirs)
    val res1 = dirs.filter { it.size < 100000 }.sumOf { it.size }

    val sizeLeft = 70000000 - tree.size
    val res2 = dirs.filter { sizeLeft + it.size >= 30000000 }.sortedBy { it.size }[0].size

    println("1: $res1")
    println("2: $res2")
}
