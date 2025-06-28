package com.example.cricscore.view.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cricscore.R

@Composable
fun AboutScreen(
    description: String = defaultDescription()
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(32.dp))

        Text(
            "About CricScore",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(12.dp))

        Text(
            description,
            fontSize = 16.sp,
            lineHeight = 22.sp
        )

        Spacer(Modifier.height(24.dp))

        Text("Coded with ❤️ by", fontSize = 16.sp, fontWeight = FontWeight.Medium)
        Spacer(Modifier.height(12.dp))

        DeveloperCard(
            name = "Sarthak Singh",
            role = "Co‑Creator • B.Tech CSE (KIIT, Bhubaneswar)",
            imgRes = R.drawable.summary_banner      // put real resource
        )
        Spacer(Modifier.height(16.dp))
        DeveloperCard(
            name = "Harsh Singh",
            role = "Co‑Creator • B.Tech CSE (KIIT, Bhubaneswar)",
            imgRes = R.drawable.summary_banner        // put real resource
        )
    }
}

@Composable
private fun DeveloperCard(name: String, role: String, imgRes: Int) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface.copy(alpha = 0.9f))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(imgRes),
                contentDescription = name,
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(Modifier.width(16.dp))
            Column {
                Text(name, fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
                Text(role, fontSize = 14.sp)
            }
        }
    }
}

private fun defaultDescription() = """
CricScore is a lightweight yet powerful cricket‑scoring companion built with Jetpack Compose. 
It lets you set up matches in seconds, record every ball offline, generate instant PDF 
scorecards, and keep a searchable history of your games. Designed by cricket enthusiasts 
for clubs, college teams and backyard battles alike.
""".trimIndent()

@Preview(showBackground = true)
@Composable
fun AboutScreenPreview() {
    AboutScreen()
}
