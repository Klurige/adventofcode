import java.io.File
import java.lang.reflect.Modifier
import java.time.LocalDate
import java.time.Month
import kotlin.reflect.jvm.kotlinFunction
import kotlin.system.exitProcess
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

// Set to true to run all exercises.
// If false, only one will be executed, or if it doesn't exist - created.
// (Nothing will be created if RUN_ALL == true)
const val RUN_ALL = false

fun main(args: Array<String>) {
    if (args.size != 1) {
        println("test or input?")
        exitProcess(1)
    }

    var date: LocalDate? = null

    // Uncomment this and change date to run that specific date.
    date = LocalDate.parse("2022-12-01")

    @Suppress("KotlinConstantConditions")
    if (date == null) date = LocalDate.now()

    if (RUN_ALL) {
        (2015..LocalDate.now().year).forEach { year ->
            (1..25).forEach { day ->
                date = LocalDate.parse("$year-12-${String.format("%02d", day)}")
                executeExercise(date!!, args[0])

            }
        }
    } else {
        executeExercise(date!!, args[0])
    }
}

@OptIn(ExperimentalTime::class)
fun executeExercise(date: LocalDate, flavour: String) {
    val username = System.getProperty("user.name")

    runCatching {
        if (date.month != Month.DECEMBER || date.dayOfMonth > 25) {
            println("Date is not in advent.")
            throw NullPointerException()
        }
         val cl = ::main.javaClass.classLoader.loadClass("$username.y${date.year}.d${date.dayOfMonth}.DayKt")
        val m = cl.methods.find { it.name == "day" && Modifier.isStatic(it.modifiers) }
        m?.kotlinFunction ?: throw NullPointerException()
    }.onSuccess {
        println("Running ${date.year} - Day${date.dayOfMonth}")
        val data = mutableListOf<String>()
        runCatching {
            File("src/main/kotlin/$username/y${date.year}/d${date.dayOfMonth}/$flavour.txt").useLines { lines ->
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
        if (!RUN_ALL) {
            println("Couldn't find $username.y${date.year}.d${date.dayOfMonth}()")

            if (date.month == Month.DECEMBER && date.dayOfMonth <= 25) {
                val dir = File("src/main/kotlin/$username/", "y${date.year}/d${date.dayOfMonth}")
                dir.mkdirs()
                File(dir, "input.txt").createNewFile()
                File(dir, "test.txt").createNewFile()
                val src = File(dir, "Day.kt")
                src.createNewFile()
                src.printWriter().use {
                    it.println("package $username.y${date.year}.d${date.dayOfMonth}")
                    it.println("")
                    it.println("fun day(data: List<String>) {")
                    it.println("")
                    it.println("}")
                }
                println()
                println("Files for y${date.year}.d${date.dayOfMonth} are created. Time to implement solution!")
            }
        }
    }
}
