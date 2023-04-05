import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import java.io.File
import java.lang.reflect.Modifier
import kotlin.reflect.jvm.kotlinFunction
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

@OptIn(ExperimentalTime::class)
fun executeExercise(date: LocalDate, flavour: String) {
    runCatching {
        val cl = ::main.javaClass.classLoader.loadClass("y${date.year}.d${date.dayOfMonth}.DayKt")
        val m = cl.methods.find { it.name == "day" && Modifier.isStatic(it.modifiers) }
        m?.kotlinFunction ?: throw NullPointerException()
    }.onSuccess {
        println("Running ${date.year} - Day${date.dayOfMonth}")
        val data = mutableListOf<String>()
        runCatching {
            val inputFilename = if (flavour == "test") {
                isTest = true
                "src/main/kotlin/y${date.year}/d${date.dayOfMonth}/test.txt"
            } else {
                isTest = false
                downloadInput(date)
            }

            File(inputFilename).useLines { lines ->
                lines.forEach {
                    data.add(it)
                }
            }
        }.onFailure {
            println("**** Couldn't get input ****")
        }

        val elapsed = measureTime {
            it.call(data)
        }

        println()
        println("Execution took $elapsed")
        println()
    }.onFailure {
        @Suppress("KotlinConstantConditions")
        if (RUN_ALL < 0) {
            println("Couldn't find y${date.year}.d${date.dayOfMonth}()")

            if (date.month == Month.DECEMBER && date.dayOfMonth <= 25) {
                val dir = File("src/main/kotlin/", "y${date.year}/d${date.dayOfMonth}")
                dir.mkdirs()
                File(dir, "test.txt").createNewFile()
                downloadInput(date)
                val src = File(dir, "Day.kt")
                src.createNewFile()
                src.printWriter().use {
                    it.println("package y${date.year}.d${date.dayOfMonth}")
                    it.println("")
                    it.println("")
                    it.println("fun day(data: List<String>) {")
                    it.println("    println(\"${date}\")")
                    it.println("")
                    it.println("    var res1 = 0")
                    it.println("    var res2 = 0")
                    it.println("")
                    it.println("    data.forEach { line ->")
                    it.println("")
                    it.println("    }")
                    it.println("")
                    it.println("    println(\"1: \$res1\")")
                    it.println("    println(\"2: \$res2\")")
                    it.println("}")
                }
                println()
                println("Files for y${date.year}.d${date.dayOfMonth} are created. Time to implement solution!")
            }
        }
    }
}