import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import kotlin.properties.Delegates
import kotlin.system.exitProcess

// Normally negative, which means RUN_TODAY or RUN_DATE.
// Set to 0 to run every task, or a specific year for all tasks from that year.
const val RUN_ALL = 2015

// Set to true to run today's exercise. (RUN_ALL takes precedence.)
const val RUN_TODAY = false

// Run exercise for date. (Only if RUN_ALL and RUN_TODAY are both false.)
const val RUN_DATE = "2022-12-19"

var isTest = true

fun main(args: Array<String>) {
    if (args.size != 1) {
        println("test or input?")
        exitProcess(1)
    }

    @Suppress("KotlinConstantConditions")
    when {
        RUN_ALL < 0 -> {
            val date = if (RUN_TODAY) {
                Clock.System.todayIn(TimeZone.UTC)
            } else {
                LocalDate.parse(RUN_DATE)
            }
            if (date.month != Month.DECEMBER || date.dayOfMonth > 25) {
                println("Date $date is not in advent.")
            } else {
                executeExercise(date, args[0])
            }
        }

        RUN_ALL == 0 -> {
            (2015..Clock.System.todayIn(TimeZone.UTC).year).forEach { year ->
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

