package y2020.d4

data class Passport(val kv: List<Pair<String, String>>) {
    companion object {
        fun fromLines(lines: MutableList<String>): Passport {
            val kv = mutableListOf<Pair<String, String>>()
            lines.forEach { line ->
                val elements = line.split(' ', '\n')
                elements.forEach { element ->
                    val p = element.split(':')
                    if (p.size == 2) {
                        kv.add(Pair(p[0], p[1]))
                    } else {
                        println("More than 2 params: $element")
                    }
                }
            }
            if(kv.none { it.first == "cid" }) {
                kv.add(Pair("cid", "North Pole Credentials"))
            }

            if(kv.size != 8) {
                return NoPassport
            } else {
                var isOk = true
                kv.forEach {
                    when (it.first) {
                        "byr" -> {
                            val i = it.second.toInt()
                            if(i < 1920 || i > 2002) {
                                isOk = false
                            }
                        }
                        "iyr" -> {
                            val i = it.second.toInt()
                            if(i < 2010 || i > 2020) {
                                isOk = false
                            }
                        }
                        "eyr" -> {
                            val i = it.second.toInt()
                            if(i < 2020 || i > 2030) {
                                isOk = false
                            }
                        }
                        "hgt" -> {
                            val i = it.second.substring(0, it.second.lastIndex - 1).toIntOrNull()?:-1
                            val unit = it.second.substring(it.second.lastIndex - 1)
                            when(unit) {
                                "cm" ->  {
                                    if(i < 150 || i > 193) {
                                        isOk = false
                                    }
                                }
                                "in" ->  {
                                    if(i < 59 || i > 76) {
                                        isOk = false
                                    }
                                }
                                else -> {
                                    isOk = false
                                }
                            }

                        }
                        "hcl" -> {
                            if(!it.second.matches("^#[0-9a-f]{6}".toRegex())) {
                                isOk = false
                            }
                        }
                        "ecl" -> {
                            when(it.second) {
                                "amb","blu", "brn", "gry", "grn", "hzl", "oth" -> {}
                                else -> {
                                    isOk = false
                                }
                            }

                        }
                        "pid" -> {
                            if(!it.second.matches("^[0-9]{9}".toRegex())) {
                                isOk = false
                            }
                        }
                        "cid" -> {}
                        else -> isOk = false
                    }
                }
                return if(isOk) {
                    Passport(kv)
                } else {
                    NoPassport
                }
            }
        }
    }

    override fun toString(): String {
        return if (kv.isEmpty()) {
            "NoPassport"
        } else {
            "ValidPassport"
        }
    }

}

val NoPassport = Passport(emptyList())

fun day(data: List<String>) {
    println("2020-12-04")

    val lines = mutableListOf<String>()
    val passports = mutableListOf<Passport>()
    var numInvalid = 0
    data.forEach { line ->
        if (line != "") {
            lines.add(line)
        } else {
            val passport = Passport.fromLines(lines)
            lines.clear()
            if (passport != NoPassport) {
                passports.add(passport)
            } else {
                numInvalid++
            }
        }
    }
    //passports.forEachIndexed { index, passport ->
    //    println("Got passport: $index: $passport")
    //}
    println("Got ${passports.size} valid passports. and $numInvalid invalid.")
}
