package y2021.d21

import kotlin.math.max

fun day(data: List<String>) {
    val playerStartPositions = mutableSetOf<Int>()
    data.forEach { line ->
        val parts = line.split(' ')

        playerStartPositions.add(parts[4].toInt())
    }

    val die = Die()

    val players = listOf<Player>(Player(), Player())
    players.first().reset(playerStartPositions.first())
    players.last().reset(playerStartPositions.last())

    var score = 0
    while (score == 0) run roller@{
        players.forEach { player ->
            val steps = die.roll() + die.roll() + die.roll()
            player.move(steps)
            if (player.score >= 1000) {
                val loser = players.first { it.score < 1000 }
                score = loser.score * die.numRolls
                return@roller
            }
        }
    }
    println("Part1: $score")

    players.first().reset(playerStartPositions.first())
    players.last().reset(playerStartPositions.last())

    var p1Wins = 0L
    var p2Wins = 0L
    val rounds = mutableMapOf(Round(p1 = players.first(), p2 = players.last()) to 1L)
    while (rounds.isNotEmpty()) {
        val keys = rounds.keys
        val round = keys.first()
        val counter = rounds[round]!!
        rounds.remove(round)

        (1..3).forEach { d11 ->
            (1..3).forEach { d12 ->
                (1..3).forEach { d13 ->
                    val p1 = Player()
                    p1.reset(round.p1.position, round.p1.score)
                    p1.move(d11 + d12 + d13)
                    if (p1.score >= 21) {
                        p1Wins += counter
                    } else {
                        (1..3).forEach { d21 ->
                            (1..3).forEach { d22 ->
                                (1..3).forEach { d23 ->
                                    val p2 = Player()
                                    p2.reset(round.p2.position, round.p2.score)
                                    p2.move(d21 + d22 + d23)

                                    if (p2.score >= 21) {
                                        p2Wins += counter
                                    } else {
                                        val newRound = Round(p1, p2)
                                        if (newRound in rounds) {
                                            rounds[newRound] = rounds[newRound]!! + counter
                                        } else {
                                            rounds[newRound] = counter
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    println("Part2: ${max(p1Wins, p2Wins)}")
}


data class Round(val p1: Player, val p2: Player)

data class Die(var value: Int = 0, var numRolls: Int = 0) {
    fun roll(): Int {
        numRolls++
        value++
        while (value > 100) {
            value -= 100
        }
        return value
    }

}

data class Player(var position: Int = 0, var score: Int = 0) {
    fun move(steps: Int) {
        position = (position + steps)
        while (position >= 11) position -= 10
        score += position
    }

    fun reset(pos: Int, scr: Int = 0) {
        position = pos
        score = scr
    }
}
