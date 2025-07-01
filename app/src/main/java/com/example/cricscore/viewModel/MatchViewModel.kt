package com.example.cricscore.viewModel

import android.annotation.SuppressLint
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.example.cricscore.model.InningsSummary
import com.example.cricscore.model.MatchRecord
import com.example.cricscore.model.MatchState
import java.text.SimpleDateFormat
import java.util.*

enum class MatchPhase { FIRST, SECOND, COMPLETE }

class MatchViewModel : ViewModel() {


    var totalOvers by mutableIntStateOf(0)
    var numPlayersPerTeam by mutableIntStateOf(11)
    var teamAName by mutableStateOf("Team A")
    var teamBName by mutableStateOf("Team B")
    var strikerName by mutableStateOf("")
    var nonStrikerName by mutableStateOf("")



    var phase by mutableStateOf(MatchPhase.FIRST)


    var targetScore by mutableIntStateOf(0)


    var battingTeam by mutableStateOf("Team A")


    var inningsA by mutableStateOf<InningsSummary?>(null)
    var inningsB by mutableStateOf<InningsSummary?>(null)


    var selectedMatch by mutableStateOf<MatchRecord?>(null)


    val matchList = mutableStateListOf<MatchRecord>()
    var matchState: MatchState? = null

    fun initMatchState(overs: Int, striker: String, nonStriker: String) {
        if (matchState == null) {
            matchState = MatchState(overs, striker, nonStriker)
        }
    }

    fun resetMatchState() {
        matchState = null
    }

    fun resetMatch() {
        phase = MatchPhase.FIRST
        targetScore = 0
        battingTeam = "Team A"
        inningsA = null
        inningsB = null
    }


    @SuppressLint("DefaultLocale")
    fun updateLiveInnings(matchState: MatchState, teamName: String) {
        val overs = "${matchState.currentOver}.${matchState.currentBall}"
        val rr = if (matchState.currentOver + matchState.currentBall / 6.0 > 0)
            String.format("%.2f", matchState.runs / (matchState.currentOver + matchState.currentBall / 6.0))
        else "0.00"

        val summary = InningsSummary(
            teamName = teamName,
            totalRuns = matchState.runs,
            totalWkts = matchState.wickets,
            oversBowled = overs,
            runRate = rr,
            players = matchState.getBattingStats(),
            overs = matchState.getOverWiseResults().map { it.perBall },
            completed = true
        )

        when (phase) {
            MatchPhase.FIRST -> inningsA = summary
            MatchPhase.SECOND -> inningsB = summary
            else -> {}
        }
    }

    fun addMatchToHistory() {
        if (inningsA != null && inningsB != null) {
            val id = System.currentTimeMillis()
            val date = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
            val match = MatchRecord(id, date, inningsA!!, inningsB!!)
            matchList.add(match)
        }
    }

    fun selectMatch(match: MatchRecord) {
        selectedMatch = match
    }

    fun clearSelectedMatch() {
        selectedMatch = null
    }

    fun removeMatch(match: MatchRecord) {
        matchList.remove(match)
    }

    fun clearAllMatches() {
        matchList.clear()
    }
}
