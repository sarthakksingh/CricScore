package com.example.cricscore.view.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun WicketDialog(
    onDismiss: () -> Unit,
    onOutTypeSelected: (String) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Select type of Wicket") },
        text = {
            Column {
                listOf("Catch Out", "Bowled", "Run Out", "LBW", "Hit Wicket").forEach { type ->
                    TextButton(onClick = { onOutTypeSelected(type) }) {
                        Text(type)
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = {}
    )
}
