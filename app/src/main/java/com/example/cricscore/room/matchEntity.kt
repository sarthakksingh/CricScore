package com.example.cricscore.room


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "matches")
data class MatchEntity(
    @PrimaryKey(autoGenerate = true) val matchId: Long = 0,
    val teamA: String,
    val teamB: String,
    val totalOvers: Int,
    val date: Long,
    val winner: String? = null
)

@Entity(tableName = "innings")
data class InningsEntity(
    @PrimaryKey(autoGenerate = true) val inningsId: Long = 0,
    val matchId: Long,
    val battingTeam: String,
    val runs: Int,
    val wickets: Int,
    val overs: Int,
    val totalOvers: Int,
    val balls: Int,
    val target: Int? = null
)

@Entity(tableName = "player_stats")
data class PlayerStatsEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val inningsId: Long,
    val name: String,
    val runs: Int,
    val balls: Int,
    val isOut: Boolean,
    val outType: String
)
