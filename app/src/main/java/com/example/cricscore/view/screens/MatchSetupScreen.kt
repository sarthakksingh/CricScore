package com.example.cricscore.view.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cricscore.R


@Composable
fun MatchSetupScreen() {
    var overs by remember { mutableStateOf("") }
    var team1 by remember { mutableStateOf("") }
    var team2 by remember { mutableStateOf("") }
    var striker by remember { mutableStateOf("") }
    var nonStriker by remember { mutableStateOf("") }
    var numPlayers by remember { mutableStateOf("") }

    var battingFirst by remember { mutableStateOf("") }
    val bowlingTeam = if (battingFirst == team1) team2 else team1


    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.setup_background),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.55f)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Match Setup",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 16.dp)
            )


            TransparentPanel{
                Text(
                    text = "Overs",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, start = 4.dp, bottom = 4.dp)
                )

                OutlinedTextField(
                    value = overs,
                    onValueChange = { overs = it },
                    label = { Text("Enter Number of Overs") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }



            TransparentPanel {
                Text(
                    text = "Teams",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, start = 4.dp, bottom = 4.dp)
                )

                OutlinedTextField(
                    value = team1,
                    onValueChange = { team1 = it },
                    label = { Text("Team A") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = team2,
                    onValueChange = { team2 = it },
                    label = { Text("Team B") },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            TransparentPanel {
                Text(
                    text = "Who will bat first?",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .clip(RoundedCornerShape(25.dp))
                        .background(Color(0xFFE0E0E0)),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clickable { battingFirst = team1 }
                            .background(
                                if (battingFirst == team1) Color(0xFF007F0E) else Color.Transparent,
                                shape = RoundedCornerShape(25.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = team1.ifBlank { "Team A" },
                            color = if (battingFirst == team1) Color.White else Color.Black,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clickable { battingFirst = team2 }
                            .background(
                                if (battingFirst == team2) Color(0xFF007F0E) else Color.Transparent,
                                shape = RoundedCornerShape(25.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = team2.ifBlank { "Team B" },
                            color = if (battingFirst == team2) Color.White else Color.Black,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            TransparentPanel {
                Text(
                    text = "Opening Batters",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, start = 4.dp, bottom = 4.dp)
                )

                OutlinedTextField(
                    value = striker,
                    onValueChange = { striker = it },
                    label = { Text("Striker") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = nonStriker,
                    onValueChange = { nonStriker = it },
                    label = { Text("Non-Striker") },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            TransparentPanel {
                Text(
                    text = "Number Of Players",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, start = 4.dp, bottom = 4.dp)
                )

                OutlinedTextField(
                    value = numPlayers,
                    onValueChange = { numPlayers = it },
                    label = { Text("Total Players per Team") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { /*TODO*/},
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007F0E)),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Icon(Icons.Default.PlayArrow, contentDescription = null, tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Start Match", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}


@Composable
fun TransparentPanel(content: @Composable ColumnScope.() -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .shadow(
                elevation = 6.dp,
                shape = RoundedCornerShape(20.dp),
                ambientColor = Color.Black.copy(alpha = 0.1f),
                spotColor = Color.Black.copy(alpha = 0.1f)
            )
            .background(
                color = Color.White.copy(alpha = 0.75f),
                shape = RoundedCornerShape(20.dp)
            )
            .padding(16.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            content()
        }
    }
}



@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MatchSetupScreenPreview() {
    MaterialTheme {
        MatchSetupScreen()
    }
}


