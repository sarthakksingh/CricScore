package com.example.cricscore.model.data

import com.example.cricscore.model.InningsSummary

data class MatchRecord(
    val id: Int,
    val teamA: String,
    val teamB: String,
    val inningsA: InningsSummary,
    val inningsB: InningsSummary,
    val date: Long
)
