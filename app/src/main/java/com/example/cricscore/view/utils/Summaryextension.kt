package com.example.cricscore.view.utils

import com.example.cricscore.model.InningsSummary


fun InningsSummary.toShareText(): String {
    val header = "$teamName  –  $totalRuns/$totalWkts  in  $oversBowled overs  (RR $runRate)"
    val playersBlock = players.joinToString("\n") {
        "${it.name.padEnd(18, ' ')}  ${it.runs} (${it.balls})"
    }
    return "$header\n\n$playersBlock"
}