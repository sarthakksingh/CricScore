package com.example.cricscore.view.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun RunOutDialog(
    onDismiss: () -> Unit,
    onRunOutConfirmed: (run: Int, outName: String, newName: String, strikerNow: Boolean) -> Unit
) {
    var runTaken by remember { mutableStateOf("") }
    var whoOut by remember { mutableStateOf("") }
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
                OutlinedTextField(
                    value = whoOut,
                    onValueChange = { whoOut = it },
                    label = { Text("Who got out?") }
                )
                OutlinedTextField(
                    value = newBatsman,
                    onValueChange = { newBatsman = it },
                    label = { Text("New Batsman Name") }
                )
                Row {
                    Text("Who is on Strike?")
                    Spacer(modifier = Modifier.width(8.dp))
                    RadioButton(
                        selected = strikeNow,
                        onClick = { strikeNow = true }
                    )
                    Text("Striker")
                    Spacer(modifier = Modifier.width(8.dp))
                    RadioButton(
                        selected = !strikeNow,
                        onClick = { strikeNow = false }
                    )
                    Text("Non-Striker")
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                val run = runTaken.toIntOrNull() ?: 0
                onRunOutConfirmed(run, whoOut, newBatsman, strikeNow)
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