package com.example.cricscore.view.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun BattingStatCard(
    name: String,
    runs: Int,
    balls: Int,
    isOnStrike: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = if (isOnStrike) Color(0xFF007F0E) else Color(0xFF004C3F))
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(name, color = Color.White, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
            Text("$runs ($balls)", color = Color.White)
            if (isOnStrike) {
                Spacer(modifier = Modifier.width(8.dp))
                Text("*", color = Color.Yellow, fontWeight = FontWeight.Bold)
            }
        }
    }
}