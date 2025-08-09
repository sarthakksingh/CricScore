package com.example.cricscore.room


import androidx.room.Embedded
import androidx.room.Relation

data class MatchWithInnings(
    @Embedded val match: MatchEntity,
    @Relation(
        parentColumn = "matchId",
        entityColumn = "matchId",
        entity = InningsEntity::class
    )
    val innings: List<InningsEntity>
)

data class InningsWithPlayers(
    @Embedded val innings: InningsEntity,
    @Relation(
        parentColumn = "inningsId",
        entityColumn = "inningsId",
        entity = PlayerStatsEntity::class
    )
    val players: List<PlayerStatsEntity>
)
