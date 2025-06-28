package com.example.cricscore.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ScoreSummaryCard(
    teamName: String,
    runs: Int,
    wickets: Int,
    overText: String,
    runRate: String,
    totalOvers: Int
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
            .padding(8.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(Color(0xFFFFFFFF).copy(alpha = 0.65f))
    ) {

        Text(
            text = teamName,
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 16.dp, top = 12.dp)
        )


        Column(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 16.dp)
        ) {
            Text(
                text = "$runs/$wickets",
                color = Color.Black,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 36.sp
            )


            Text(
                text = "RR: $runRate",
                color = Color.Black,
                fontWeight = FontWeight.Medium,
                fontSize = 15.sp,

                )
        }
        Text(
            text = "$overText/$totalOvers",
            color = Color.Gray,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(end = 16.dp, top = 12.dp)
        )
    }
}