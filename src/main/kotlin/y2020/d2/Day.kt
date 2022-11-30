package y2020.d2

@Suppress("unused")
fun day(data: List<String>) {
    var valid1 = 0
    var valid2 = 0
    data.forEach { line ->
            val e = line.split(':')
            val r = e[0].trim().split(' ')
            val range = r[0].split('-').map { it.toInt() }
            val key = r[1].trim()[0]
            val password = e[1].trim().toCharArray()
            val num = password.count {char ->
                char == key
            }
            if(num >= range[0] && num <= range[1]) {
                valid1++
            }

            if(password[range[0]-1] == key && password[range[1] - 1] != key
                ||password[range[1]-1] == key && password[range[0] - 1] != key ) {
                valid2++
            }
        }

    println("Found $valid1 and $valid2 valid passwords")
}