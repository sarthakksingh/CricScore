package com.example.cricscore.model

import com.example.cricscore.model.data.PlayerStats


data class OverBreakdown(val overNumber: Int, val runs: Int, val wickets: Int, val perBall: List<String>)


data class InningsSummary(
    val teamName: String,
    val totalRuns: Int,
    val totalWkts: Int,
    val oversBowled: String,
    val runRate: String,
    val players: List<PlayerStats>,
    val overs: List<List<String>>,
    val completed: Boolean
)


data class MatchRecord(
    val id: Long,
    val date: String,
    //   val venue: String,
    val inningsA: InningsSummary,
    val inningsB: InningsSummary
)