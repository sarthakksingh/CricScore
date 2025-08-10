package com.example.cricscore.viewModel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.cricscore.model.MatchState
import com.example.cricscore.room.CricketDatabase
import com.example.cricscore.room.InningsEntity
import com.example.cricscore.room.MatchEntity
import com.example.cricscore.room.PlayerStatsEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

enum class MatchPhase { FIRST, SECOND, COMPLETE }

class MatchViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = CricketDatabase.getDatabase(application).matchDao()

    var matchState: MatchState? = null
        private set

    var phase: MatchPhase = MatchPhase.FIRST
    var totalOvers: Int = 0
    var strikerName: String = ""
    var nonStrikerName: String = ""
    var battingTeam: String = "Team A"
    var targetScore: Int = 0
    var numPlayersPerTeam by mutableIntStateOf(11)

    private val _matchHistory = MutableLiveData<List<MatchEntity>>()
    val matchHistory: LiveData<List<MatchEntity>> get() = _matchHistory


    private val _matchDetails = MutableLiveData<MatchEntity?>()
    val matchDetails: LiveData<MatchEntity?> get() = _matchDetails

    private val _ongoingMatch = MutableLiveData<Pair<String, MatchState>?>()
    val ongoingMatch: LiveData<Pair<String, MatchState>?> get() = _ongoingMatch

    fun initMatchState(overs: Int, striker: String, nonStriker: String) {
        if (matchState == null) {
            matchState = MatchState(overs, striker, nonStriker)
        }
    }

    fun updateLiveInnings(matchState: MatchState, battingTeam: String) {
        this.matchState = matchState
        this.battingTeam = battingTeam

        viewModelScope.launch(Dispatchers.IO) {

            dao.deleteOngoingInningsForTeam(battingTeam)


            val inningsId = dao.insertInnings(
                InningsEntity(
                    matchId = -1L,
                    battingTeam = battingTeam,
                    runs = matchState.runs,
                    wickets = matchState.wickets,
                    overs = matchState.currentOver,
                    totalOvers = totalOvers,
                    balls = matchState.currentBall,
                    target = if (phase == MatchPhase.SECOND) targetScore else null
                )
            )

            dao.deleteOngoingPlayersForInnings(inningsId)
            matchState.getBattingStats().forEach { player ->
                dao.insertPlayerStat(
                    PlayerStatsEntity(
                        inningsId = inningsId,
                        name = player.name,
                        runs = player.runs,
                        balls = player.balls,
                        isOut = player.isOut,
                        outType = player.outType
                    )
                )
            }
        }
    }


    fun saveMatchToDb(
        teamA: String,
        teamB: String,
        winner: String?,
        inningsList: List<Pair<String, MatchState>>
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val matchId = dao.insertMatch(
                MatchEntity(
                    teamA = teamA,
                    teamB = teamB,
                    totalOvers = totalOvers,
                    date = System.currentTimeMillis(),
                    winner = winner
                )
            )

            inningsList.forEach { (battingTeam, state) ->
                val inningsId = dao.insertInnings(
                    InningsEntity(
                        matchId = matchId,
                        battingTeam = battingTeam,
                        runs = state.runs,
                        wickets = state.wickets,
                        overs = state.currentOver,
                        totalOvers = totalOvers,
                        balls = state.currentBall,
                        target = if (phase == MatchPhase.SECOND) targetScore else null
                    )
                )

                state.getBattingStats().forEach { player ->
                    dao.insertPlayerStat(
                        PlayerStatsEntity(
                            inningsId = inningsId,
                            name = player.name,
                            runs = player.runs,
                            balls = player.balls,
                            isOut = player.isOut,
                            outType = player.outType
                        )
                    )
                }
            }


            dao.clearOngoingInnings()
        }
    }

    fun loadMatchHistory() {
        viewModelScope.launch(Dispatchers.IO) {
            val matches = dao.getAllMatches()
            _matchHistory.postValue(matches)
        }
    }

    fun loadMatchDetails(matchId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val match = dao.getMatchById(matchId)
            _matchDetails.postValue(match)
        }
    }


    fun loadOngoingMatch() {
        viewModelScope.launch(Dispatchers.IO) {
            val innings = dao.getOngoingInnings()
            if (innings != null) {
                totalOvers = innings.totalOvers
                targetScore = innings.target ?: 0
                val stats = dao.getPlayerStatsForInnings(innings.inningsId)
                val resumedState = MatchState(
                    innings.totalOvers,
                    stats.firstOrNull()?.name ?: "",
                    stats.getOrNull(1)?.name ?: ""
                ).apply {
                    runs = innings.runs
                    wickets = innings.wickets
                    currentOver = innings.overs
                    currentBall = innings.balls
                    restoreBattingStats(stats)
                }
                _ongoingMatch.postValue(innings.battingTeam to resumedState)
            } else {
                _ongoingMatch.postValue(null)
            }
        }
    }
}
