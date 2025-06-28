package com.example.cricscore.view.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun NewBatsmanDialog(
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var batsmanName by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("New Batsman") },
        text = {
            OutlinedTextField(
                value = batsmanName,
                onValueChange = { batsmanName = it },
                label = { Text("Enter new batsman name") }
            )
        },
        confirmButton = {
            Button(onClick = { onConfirm(batsmanName) }) { Text("Confirm") }
        },
        dismissButton = {
            Button(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
