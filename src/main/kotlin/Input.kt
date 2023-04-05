import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.onDownload
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.statement.HttpResponse
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.todayIn
import java.io.File
import java.util.MissingResourceException
import java.util.ResourceBundle

fun downloadInput(date: LocalDate): String {
    val dir = File("input/", "y${date.year}/d${date.dayOfMonth}")
    val file = File(dir, "input.txt")
    if (!file.exists()) {
        val session = try {
            ResourceBundle.getBundle("settings").getString("session")
        } catch (e: MissingResourceException) {
            println("Couldn't find session cookie. Please look at https://github.com/wimglenn/advent-of-code-wim/issues/1 and set 'session=cookie' in")
            println("located in src/main/resources/settings.properties.")
            println()
            println("Alternatively, copy input data into a file named ${file.canonicalPath}")
            return file.canonicalPath
        }
        dir.mkdirs()
        if (file.createNewFile()) {
            val url = "https://adventofcode.com/${date.year}/day/${date.dayOfMonth}/input"
            runBlocking {
                val client = HttpClient(CIO) {
                    install(Logging) {
                        level = LogLevel.INFO
                    }
                }
                val response: HttpResponse = client.get(url) {
                    header("Cookie", "session=$session")
                    onDownload { bytesSentTotal, contentLength ->
                        println("Received $bytesSentTotal bytes from $contentLength")
                    }
                }

                client.close()
                when (response.status.value) {
                    in (200..299) -> {
                        println("Successful response!")
                        val responseBody: ByteArray = response.body()
                        file.writeBytes(responseBody)
                        println("File saved to ${file.path}")
                    }

                    in (400..499) -> {
                        println("Does not exist. Please double-check your session cookie.")
                        file.delete()
                    }

                    else -> {
                        println("Another error: ${response.status.value}")
                        file.delete()
                    }
                }
            }
        } else {
            if (!file.exists()) {
                println("Failed to create input file: ${file.canonicalPath}.")
            }
        }
    }
    return file.canonicalPath
}