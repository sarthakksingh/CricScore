package com.example.cricscore.data

data class PlayerStats(
    val name: String,
    val runs: Int,
    val balls: Int,
    val isOut: Boolean,
    val outType: String)
