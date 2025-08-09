package com.example.cricscore.room

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface MatchDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMatch(match: MatchEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInnings(innings: InningsEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlayerStat(player: PlayerStatsEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlayerStats(players: List<PlayerStatsEntity>): List<Long>



    @Query("SELECT * FROM matches ORDER BY date DESC")
    suspend fun getAllMatches(): List<MatchEntity>

    @Transaction
    @Query("SELECT * FROM matches ORDER BY date DESC")
    suspend fun getAllMatchesWithInnings(): List<MatchWithInnings>

    @Transaction
    @Query("SELECT * FROM matches WHERE matchId = :matchId LIMIT 1")
    suspend fun getMatchWithInningsById(matchId: Long): MatchWithInnings?

    @Query("SELECT * FROM matches WHERE matchId = :matchId LIMIT 1")
    suspend fun getMatchById(matchId: Long): MatchEntity?

    @Query("SELECT * FROM innings WHERE matchId = :matchId ORDER BY inningsId")
    suspend fun getInningsForMatch(matchId: Long): List<InningsEntity>

    @Transaction
    @Query("SELECT * FROM innings WHERE inningsId = :inningsId LIMIT 1")
    suspend fun getInningsWithPlayers(inningsId: Long): InningsWithPlayers?

    @Query("SELECT * FROM player_stats WHERE inningsId = :inningsId")
    suspend fun getPlayerStatsForInnings(inningsId: Long): List<PlayerStatsEntity>



    @Query("DELETE FROM innings WHERE matchId = -1 AND battingTeam = :team")
    suspend fun deleteOngoingInningsForTeam(team: String)


    @Query("DELETE FROM player_stats WHERE inningsId = :inningsId")
    suspend fun deleteOngoingPlayersForInnings(inningsId: Long)

    @Query("SELECT * FROM innings WHERE matchId = -1 LIMIT 1")
    suspend fun getOngoingInnings(): InningsEntity?

    @Query("DELETE FROM innings WHERE matchId = -1")
    suspend fun clearOngoingInnings()



    @Delete
    suspend fun deleteMatch(match: MatchEntity)

    @Query("DELETE FROM matches WHERE matchId = :matchId")
    suspend fun deleteMatchById(matchId: Long)

    @Query("DELETE FROM innings WHERE matchId = :matchId")
    suspend fun deleteInningsForMatch(matchId: Long)

    @Query("DELETE FROM player_stats WHERE inningsId = :inningsId")
    suspend fun deletePlayerStatsForInnings(inningsId: Long)
}
