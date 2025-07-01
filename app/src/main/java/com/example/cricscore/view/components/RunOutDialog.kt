package com.example.cricscore.view.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp

@Composable
fun RunOutDialog(
    strikerName: String,
    nonStrikerName: String,
    onDismiss: () -> Unit,
    onRunOutConfirmed: (run: Int, outName: String, newName: String, strikerNow: Boolean) -> Unit
) {
    var runTaken by remember { mutableStateOf("") }
    var selectedOutBatter by remember { mutableStateOf(strikerName) }
    var newBatsman by remember { mutableStateOf("") }
    var strikeNow by remember { mutableStateOf(true) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Run Out Details") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = runTaken,
                    onValueChange = { runTaken = it },
                    label = { Text("Runs Taken (0,1,2)") }
                )

                Text("Who got out?")
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    RadioButton(
                        selected = selectedOutBatter == strikerName,
                        onClick = { selectedOutBatter = strikerName }
                    )
                    Text(strikerName)

                    RadioButton(
                        selected = selectedOutBatter == nonStrikerName,
                        onClick = { selectedOutBatter = nonStrikerName }
                    )
                    Text(nonStrikerName)
                }

                OutlinedTextField(
                    value = newBatsman,
                    onValueChange = { newBatsman = it },
                    label = { Text("New Batsman Name") }
                )

                Text("Who is on Strike?")
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    RadioButton(
                        selected = strikeNow,
                        onClick = { strikeNow = true }
                    )
                    Text("New Batsman")

                    RadioButton(
                        selected = !strikeNow,
                        onClick = { strikeNow = false }
                    )
                    Text("Other Player")
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                val run = runTaken.toIntOrNull() ?: 0
                onRunOutConfirmed(run, selectedOutBatter, newBatsman, strikeNow)
            }) {
                Text("Confirm")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
