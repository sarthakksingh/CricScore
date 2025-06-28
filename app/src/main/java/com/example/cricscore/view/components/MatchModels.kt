package com.example.cricscore.view.components

import com.example.cricscore.view.screens.PlayerStats

data class PlayerStats(val name: String, val runs: Int, val balls: Int, val isOut: Boolean)
data class OverBreakdown(val overNumber: Int, val runs: Int, val wickets: Int, val perBall: List<String>)
data class InningsSummary(
    val teamName: String,
    val totalRuns: Int,
    val totalWkts: Int,
    val oversBowled: String,
    val runRate: String,
    val players: List<PlayerStats>,
    val overs: List<OverBreakdown>,
    val completed: Boolean
)

/* One record per finished match */
data class MatchRecord(
    val id: Long,
    val date: String,
 //   val venue: String,
    val inningsA: InningsSummary,
    val inningsB: InningsSummary
)