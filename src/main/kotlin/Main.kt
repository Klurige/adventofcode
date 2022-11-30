import java.io.File
import java.lang.reflect.Modifier
import java.time.LocalDate
import java.time.Month
import kotlin.reflect.jvm.kotlinFunction
import kotlin.system.exitProcess
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

// Normally negative, which means RUN_TODAY or RUN_DATE.
// Set to 0 to run every task, or a specific year for all tasks from that year.
const val RUN_ALL = -1

// Set to true to run today's exercise. (RUN_ALL takes precedence.)
const val RUN_TODAY = true

// Run exercise for date. (Only if RUN_ALL and RUN_TODAY are both false.)
const val RUN_DATE = "2022-12-01"

fun main(args: Array<String>) {
    if (args.size != 1) {
        println("test or input?")
        exitProcess(1)
    }

    when {
        RUN_ALL < 0 -> {
            val date = if (RUN_TODAY) {
                LocalDate.now()
            } else {
                LocalDate.parse(RUN_DATE)
            }
            if (date.month != Month.DECEMBER || date.dayOfMonth > 25) {
                println("Date $date is not in advent.")
            } else {
                executeExercise(date!!, args[0])
            }
        }

        RUN_ALL == 0 -> {
            (2015..LocalDate.now().year).forEach { year ->
                (1..25).forEach { day ->
                    executeExercise(LocalDate.parse("$year-12-${String.format("%02d", day)}"), args[0])
                }
            }
        }

        else -> {
            (1..25).forEach { day ->
                executeExercise(LocalDate.parse("$RUN_ALL-12-${String.format("%02d", day)}"), args[0])
            }
        }
    }
}

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
            File("src/main/kotlin/y${date.year}/d${date.dayOfMonth}/$flavour.txt").useLines { lines ->
                lines.forEach {
                    data.add(it)
                }
            }
        }

        val elapsed = measureTime {
            it.call(data)
        }

        println()
        println("Execution took $elapsed")
        println()
    }.onFailure {
        if (RUN_ALL < 0) {
            println("Couldn't find y${date.year}.d${date.dayOfMonth}()")

            if (date.month == Month.DECEMBER && date.dayOfMonth <= 25) {
                val dir = File("src/main/kotlin/", "y${date.year}/d${date.dayOfMonth}")
                dir.mkdirs()
                File(dir, "input.txt").createNewFile()
                File(dir, "test.txt").createNewFile()
                val src = File(dir, "Day.kt")
                src.createNewFile()
                src.printWriter().use {
                    it.println("package y${date.year}.d${date.dayOfMonth}")
                    it.println("")
                    it.println("fun day(data: List<String>) {")
                    it.println("    println(\"${date}\")")
                    it.println("}")
                }
                println()
                println("Files for y${date.year}.d${date.dayOfMonth} are created. Time to implement solution!")
            }
        }
    }
}
