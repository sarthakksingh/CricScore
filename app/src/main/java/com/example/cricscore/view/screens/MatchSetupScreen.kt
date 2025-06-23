package com.example.cricscore.view.screens


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun MatchSetupScreen() {
    var overs by remember { mutableStateOf("") }
    //var isBatting by remember { mutableStateOf(true) }
    var batter1 by remember { mutableStateOf("") }
    var batter2 by remember { mutableStateOf("") }
    var totalPlayers by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("New Match", fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.align(Alignment.CenterHorizontally))
        Text("Match Setup", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)

        OutlinedTextField(
            value = overs,
            onValueChange = { overs = it },
            label = { Text("Overs") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

//        Text("Batting/Bowling", fontWeight = FontWeight.Medium)
//        Row(
//            horizontalArrangement = Arrangement.spacedBy(8.dp)
//        ) {
//            Button(
//                onClick = { isBatting = true },
//                colors = ButtonDefaults.buttonColors(
//                    containerColor = if (isBatting) Color(0xFFA5D6A7) else Color.LightGray,
//                    contentColor = if (isBatting) Color.White else Color.Black
//                ),
//                modifier = Modifier.weight(1f)
//            ) {
//                Text("Batting")
//            }
//            Button(
//                onClick = { isBatting = false },
//                colors = ButtonDefaults.buttonColors(
//                    containerColor = if (!isBatting) Color.Black else Color.LightGray,
//                    contentColor = if (!isBatting) Color.White else Color.Black
//                ),
//                modifier = Modifier.weight(1f)
//            ) {
//                Text("Bowling")
//            }
//        }

        Text("Opening Batters", fontWeight = FontWeight.Medium)

        OutlinedTextField(
            value = batter1,
            onValueChange = { batter1 = it },
            label = { Text("Batter 1") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = batter2,
            onValueChange = { batter2 = it },
            label = { Text("Batter 2") },
            modifier = Modifier.fillMaxWidth()
        )

        Text("Total Batting Players (Optional)", fontWeight = FontWeight.Medium)

        OutlinedTextField(
            value = totalPlayers,
            onValueChange = { totalPlayers = it },
            label = { Text("Number of Players") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { /* TODO */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32))
        ) {
            Text("Start Match", fontWeight = FontWeight.Bold)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MatchSetupPreview() {
    MatchSetupScreen()
}

