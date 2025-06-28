package com.example.cricscore.view.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cricscore.R
import com.example.cricscore.view.components.MatchRecord
import com.example.cricscore.view.components.InningsSummary
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.material3.CardDefaults

@Composable
fun HistoryScreen(
    matches: List<MatchRecord>,
    onMatchSelected: (MatchRecord) -> Unit,
    onDelete: (MatchRecord) -> Unit,
    backgroundRes: Int = R.drawable.history_bg
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(backgroundRes),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize().alpha(0.55f)
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Spacer(Modifier.height(32.dp)) // Top spacer
            Text("Match History", fontSize = 22.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(12.dp))
            LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                items(matches) { match ->
                    MatchCard(
                        record = match,
                        onClick = onMatchSelected,
                        onDelete = onDelete
                    )
                }
            }
        }
    }
}

@Composable
private fun MatchCard(
    record: MatchRecord,
    onClick: (MatchRecord) -> Unit,
    onDelete: (MatchRecord) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
            .clickable { onClick(record) }
            .alpha(.95f),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Box(Modifier.fillMaxSize()) {
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(24.dp)
            ) {
                Text(record.date, fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
                Spacer(Modifier.height(12.dp))
                Text(
                    "${record.inningsA.teamName} ${record.inningsA.totalRuns}/${record.inningsA.totalWkts} vs " +
                            "${record.inningsB.teamName} ${record.inningsB.totalRuns}/${record.inningsB.totalWkts}",
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp
                )
            }
            IconButton(
                onClick = { showDialog = true },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
            ) {
                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
            }
            if (showDialog) {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    title = { Text("Delete Match?") },
                    text = { Text("Are you sure you want to delete this match?") },
                    confirmButton = {
                        TextButton(onClick = {
                            onDelete(record)
                            showDialog = false
                        }) { Text("Delete") }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDialog = false }) { Text("Cancel") }
                    }
                )
            }
        }
    }
}


/* ---------- PREVIEW WITH DUMMY DATA ---------- */
@Preview(showSystemUi = true)
@Composable
fun HistoryScreenPreview() {
    // Dummy data for preview
    val dummyInnings = InningsSummary(
        teamName = "Team A",
        totalRuns = 150,
        totalWkts = 7,
        oversBowled = "20.0",
        runRate = "7.5",
        players = emptyList(),
        overs = emptyList(),
        completed = true
    )

    val today = SimpleDateFormat("dd-MM-yyyy", Locale.US).format(Date())

    // Use remember for mutable state in preview to simulate deletion
    var matches by remember {
        mutableStateOf(
            listOf(
                MatchRecord(
                    id = 1,
                    date = today,
                    inningsA = dummyInnings,
                    inningsB = dummyInnings.copy(teamName = "Team B")
                ),
                MatchRecord(
                    id = 2,
                    date = today,
                    inningsA = dummyInnings.copy(teamName = "Team C"),
                    inningsB = dummyInnings.copy(teamName = "Team D")
                )
            )
        )
    }

    HistoryScreen(
        matches = matches,
        onMatchSelected = {},
        onDelete = { record -> matches = matches.filter { it.id != record.id } }
    )
}

