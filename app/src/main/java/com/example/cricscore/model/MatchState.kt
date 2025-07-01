package com.example.cricscore.model

import androidx.compose.runtime.*
import com.example.cricscore.model.data.PlayerStats

class MatchState(
    val totalOvers: Int,
    striker: String,
    nonStriker: String
) {
    var runs by mutableIntStateOf(0)
    var wickets by mutableIntStateOf(0)
    var currentOver by mutableIntStateOf(0)
    var currentBall by mutableIntStateOf(0)

    var strikerName by mutableStateOf(striker)
    var strikerRuns by mutableIntStateOf(0)
    var strikerBalls by mutableIntStateOf(0)

    var nonStrikerName by mutableStateOf(nonStriker)
    var nonStrikerRuns by mutableIntStateOf(0)
    var nonStrikerBalls by mutableIntStateOf(0)

    var isStrikerOnStrike by mutableStateOf(true)

    var perBallResults = mutableStateListOf<String>()
    var overBallHistory = mutableStateListOf<List<String>>()

    var isExpectingNbRuns = false
    var isExpectingFreeHit = false



    private val playerStats = mutableStateListOf<PlayerStats>()

    data class Snapshot(
        val runs: Int,
        val wickets: Int,
        val over: Int,
        val ball: Int,
        val striker: Triple<String, Int, Int>,
        val nonStriker: Triple<String, Int, Int>,
        val isStrikerOnStrike: Boolean,
        val overState: List<String>,
        val playerStats: List<PlayerStats>
    )

    private val history = mutableStateListOf<Snapshot>()


    fun saveSnapshot() {
        history.add(
            Snapshot(
                runs, wickets, currentOver, currentBall,
                Triple(strikerName, strikerRuns, strikerBalls),
                Triple(nonStrikerName, nonStrikerRuns, nonStrikerBalls),
                isStrikerOnStrike,
                perBallResults.toList(),
                playerStats.map { it.copy() }
            )
        )
    }

    fun undo() {
        if (history.isNotEmpty()) {
            val last = history.removeLast()
            runs = last.runs
            wickets = last.wickets
            currentOver = last.over
            currentBall = last.ball
            strikerName = last.striker.first
            strikerRuns = last.striker.second
            strikerBalls = last.striker.third
            nonStrikerName = last.nonStriker.first
            nonStrikerRuns = last.nonStriker.second
            nonStrikerBalls = last.nonStriker.third
            isStrikerOnStrike = last.isStrikerOnStrike
            perBallResults.clear()
            perBallResults.addAll(last.overState)

            playerStats.clear()
            playerStats.addAll(last.playerStats)
        }
    }

    fun incrementBall() {
        val last = perBallResults.lastOrNull() ?: ""
        if (last !in listOf("Wd", "Nb")) {
            currentBall++
            if (currentBall == 6) {
                currentOver++
                currentBall = 0
                overBallHistory.add(perBallResults.toList())
                perBallResults.clear()
                isStrikerOnStrike = !isStrikerOnStrike
            }
        }
    }

    fun handleWicket(
        outType: String,
        askNewBatsman: ((String) -> Unit) -> Unit
    ) {
        saveSnapshot()
        wickets++
        perBallResults.add("W")

        if (isStrikerOnStrike) {
            strikerBalls++
            updatePlayerStats(strikerName, strikerRuns, strikerBalls, true,outType)
            askNewBatsman { name ->
                strikerName = name
                strikerRuns = 0
                strikerBalls = 0
                isStrikerOnStrike = true
            }
        } else {
            nonStrikerBalls++
            updatePlayerStats(nonStrikerName, nonStrikerRuns, nonStrikerBalls, true,outType)
            askNewBatsman { name ->
                nonStrikerName = name
                nonStrikerRuns = 0
                nonStrikerBalls = 0
                isStrikerOnStrike = false
            }
        }

        incrementBall()
    }

    fun handleRunOut(run: Int, outName: String, newName: String, strikerNow: Boolean) {
        saveSnapshot()
        wickets++
        runs += run
        perBallResults.add("W")

        if (outName == strikerName) {
            strikerRuns += run
            strikerBalls++
            updatePlayerStats(strikerName, strikerRuns, strikerBalls, true, outType = "Run Out")
            strikerName = newName
            strikerRuns = 0
            strikerBalls = 0
        } else {
            nonStrikerRuns += run
            nonStrikerBalls++
            updatePlayerStats(nonStrikerName, nonStrikerRuns, nonStrikerBalls, true,outType = "Run Out")
            nonStrikerName = newName
            nonStrikerRuns = 0
            nonStrikerBalls = 0
        }

        isStrikerOnStrike = strikerNow
        incrementBall()
    }

    fun handleRun(label: String) {
        saveSnapshot()

        when {
            label == "No Ball" -> {
                runs++
                perBallResults.add("Nb")
                isExpectingNbRuns = true  // Await user input for runs scored on no-ball
            }

            isExpectingNbRuns -> {
                val run = label.toIntOrNull() ?: 0
                runs += run
                if (isStrikerOnStrike) {
                    strikerRuns += run

                } else {
                    nonStrikerRuns += run

                }
                perBallResults.add("$run(nb)")
                isExpectingNbRuns = false
                isExpectingFreeHit = true  // Now expect Free Hit ball next
            }

            isExpectingFreeHit -> {
                val run = label.toIntOrNull() ?: 0
                runs += run
                if (isStrikerOnStrike) {
                    strikerRuns += run
                    strikerBalls++
                } else {
                    nonStrikerRuns += run
                    nonStrikerBalls++
                }
                perBallResults.add("$run(fh)")
                if (run % 2 != 0) isStrikerOnStrike = !isStrikerOnStrike
                isExpectingFreeHit = false
                incrementBall()
            }

            label == "Wide" -> {
                runs++
                perBallResults.add("Wd")
            }

            else -> {
                val run = label.toIntOrNull() ?: 0
                runs += run
                if (isStrikerOnStrike) {
                    strikerRuns += run
                    strikerBalls++
                } else {
                    nonStrikerRuns += run
                    nonStrikerBalls++
                }
                perBallResults.add(label)
                if (run % 2 != 0) isStrikerOnStrike = !isStrikerOnStrike
                incrementBall()
            }
        }
    }


    private fun updatePlayerStats(name: String, runs: Int, balls: Int, isOut: Boolean, outType: String) {
        val existing = playerStats.indexOfFirst { it.name == name }
        if (existing >= 0) {
            playerStats[existing] = PlayerStats(name, runs, balls, isOut, outType)
        } else {
            playerStats.add(PlayerStats(name, runs, balls, isOut,outType))
        }
    }

    fun getBattingStats(): List<PlayerStats> {

        updatePlayerStats(strikerName, strikerRuns, strikerBalls, false, outType = "")
        updatePlayerStats(nonStrikerName, nonStrikerRuns, nonStrikerBalls, false, outType = "")
        return playerStats.toList()
    }

    fun getOverWiseResults(): List<OverBreakdown> {
        val allOvers = overBallHistory.toMutableList()
        if (perBallResults.isNotEmpty()) {
            allOvers.add(perBallResults.toList())
        }
        return allOvers.mapIndexed { index, balls ->
            val runs = balls.sumOf { it.toIntOrNull() ?: if (it == "4") 4 else if (it == "6") 6 else 0 }
            val wkts = balls.count { it in listOf("W", "Run Out", "Bowled", "Catch Out", "LBW", "Hit Wicket") }
            OverBreakdown(index + 1, runs, wkts, balls)
        }
    }

    fun resetForNextInnings() {
        runs = 0
        wickets = 0
        currentOver = 0
        currentBall = 0

        strikerRuns = 0
        strikerBalls = 0
        nonStrikerRuns = 0
        nonStrikerBalls = 0

        isStrikerOnStrike = true

        perBallResults.clear()
        overBallHistory.clear()
        history.clear()
        playerStats.clear()
    }
}
