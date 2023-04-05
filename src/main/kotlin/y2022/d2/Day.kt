package y2022.d2

fun day(data: List<String>) {
    println("2022-12-02")

    var score1 = 0
    var score2 = 0
    data.forEach { line ->
        when (line[0]) {
            'A' -> {
                when (line[2]) {
                    'X' -> {
                        // Rock, draw.
                        score1 += 1 + 3
                        // Loss, paper.
                        score2 += 0 + 3
                    }

                    'Y' -> {
                        // Paper, win.
                        score1 += 2 + 6
                        // Draw, rock.
                        score2 += 3 + 1
                    }

                    'Z' -> {
                        // Scissors, loss.
                        score1 += 3 + 0
                        // Win, scissors.
                        score2 += 6 + 2
                    }
                }
            }

            'B' -> {
                when (line[2]) {
                    'X' -> {
                        // Rock, loss.
                        score1 += 1 + 0
                        // Loss, rock.
                        score2 += 0 + 1
                    }

                    'Y' -> {
                        // Paper, draw.
                        score1 += 2 + 3
                        // Draw, paper.
                        score2 += 3 + 2
                    }

                    'Z' -> {
                        // Scissors, win.
                        score1 += 3 + 6
                        // Win, scissors.
                        score2 += 6 + 3
                    }
                }
            }

            'C' -> {
                when (line[2]) {
                    'X' -> {
                        // Rock, win.
                        score1 += 1 + 6
                        // Loss, paper.
                        score2 += 0 + 2
                    }

                    'Y' -> {
                        // Paper, loss.
                        score1 += 2 + 0
                        // Draw, scissors.
                        score2 += 3 + 3
                    }

                    'Z' -> {
                        // Scissors, draw.
                        score1 += 3 + 3
                        // Win, rock.
                        score2 += 6 + 1
                    }
                }
            }
        }
    }
    println("Score1: $score1.\nScore2: $score2.")
}
