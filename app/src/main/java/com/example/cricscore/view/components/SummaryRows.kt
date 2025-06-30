package com.example.cricscore.view.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cricscore.model.OverBreakdown
import com.example.cricscore.model.data.PlayerStats

@Composable
 fun PlayerRow(p: PlayerStats) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) { Text(p.name); Text("${p.runs} (${p.balls})") }
}

@Composable
fun OverRow(overNumber: Int, deliveries: List<String>) {
    val runs = deliveries.sumOf {
        when (it) {
            "1", "2", "3", "4", "6" -> it.toInt()
            "Wd", "Nb" -> 1
            else -> 0
        }
    }

    val wickets = deliveries.count { it in listOf("W", "Run Out", "Catch Out", "Bowled", "LBW", "Hit Wicket") }

    Column(Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Text("Over $overNumber: $runs-$wickets", fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(4.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            deliveries.forEach {
                Surface(
                    shape = CircleShape,
                    color = Color(0xFF00695C),
                    modifier = Modifier.size(26.dp)
                ) {
                    Box(Modifier.fillMaxSize(), Alignment.Center) {
                        Text(it, color = Color.White, fontSize = 11.sp)
                    }
                }
            }
        }
    }
}
