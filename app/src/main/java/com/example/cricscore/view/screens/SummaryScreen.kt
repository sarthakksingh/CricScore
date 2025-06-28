package com.example.cricscore.view.screens

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import com.example.cricscore.R
import com.example.cricscore.util.createSummaryPdf
import com.example.cricscore.view.utils.toShareText

/* ---------- DATA CLASSES ---------- */
data class PlayerStats(val name: String, val runs: Int, val balls: Int, val isOut: Boolean)
data class OverBreakdown(val overNumber: Int, val runs: Int, val wickets: Int, val perBall: List<String>)
data class InningsSummary(
    val teamName: String,
    val totalRuns: Int,
    val totalWkts: Int,
    val oversBowled: String,
    val runRate: String,
    val players: List<PlayerStats>,
    val overs: List<OverBreakdown>,
    val completed: Boolean
)

/* ---------- MAIN SUMMARY SCREEN ---------- */
@Composable
fun MatchSummaryScreen(
    inningsA: InningsSummary,
    inningsB: InningsSummary,
    backgroundRes: Int = R.drawable.summary_banner,
    onSave: () -> Unit = {},
    onBackToScoring: () -> Unit = {}
) {
    /* ---------- UI state ---------- */
    var selected by rememberSaveable { mutableStateOf(0) }
    val current = if (selected == 0) inningsA else inningsB
    var showPlayers by rememberSaveable { mutableStateOf(true) }
    var showOvers   by rememberSaveable { mutableStateOf(false) }
    val context = LocalContext.current

    /* ---------- Screen ---------- */
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
                .verticalScroll(rememberScrollState())
        ) {

            Spacer(Modifier.height(32.dp))
            Text("Match Summary", fontWeight = FontWeight.Bold, fontSize = 22.sp)
            Spacer(Modifier.height(12.dp))

            /* Toggle tiles */
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TeamToggleTile("Team A", selected == 0, Modifier.weight(1f)) { selected = 0 }
                TeamToggleTile("Team B", selected == 1, Modifier.weight(1f)) { selected = 1 }
            }

            Spacer(Modifier.height(14.dp))
            ScoreCard(current)
            Spacer(Modifier.height(18.dp))

            if (!current.completed) {
                Text(
                    "Haven’t batted yet.",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp,
                    color = Color.Red
                )
            } else {
                CollapsibleHeader("Player Stats", showPlayers) { showPlayers = !showPlayers }
                if (showPlayers) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        colors = CardDefaults.cardColors(Color.White.copy(alpha = 0.85f))
                    ) {
                        Column(Modifier.padding(12.dp)) {
                            current.players.forEach { PlayerRow(it) }
                        }
                    }
                    Spacer(Modifier.height(12.dp))
                }

                CollapsibleHeader("Over‑wise Scores", showOvers) { showOvers = !showOvers }
                if (showOvers) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        colors = CardDefaults.cardColors(Color.White.copy(alpha = 0.85f))
                    ) {
                        Column(Modifier.padding(12.dp)) {
                            current.overs.forEach { OverRow(it) }
                        }
                    }
                    Spacer(Modifier.height(12.dp))
                }
            }

            Spacer(Modifier.height(24.dp))

            /* Action buttons */
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(onClick = onSave, modifier = Modifier.weight(1f)) {
                    Text("Save Summary")
                }

                /* SHARE BUTTON */
                Button(
                    onClick = {
                        shareSummaryAsPdf(context, inningsA, inningsB)
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00897B))
                ) { Text("Share Summary") }
            }

            Spacer(Modifier.height(16.dp))
            Button(
                onClick = onBackToScoring,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4864F1))
            ) { Text("Go Back to Scoring") }

            Spacer(Modifier.height(32.dp))
        }
    }
}

/* ---------- SHARE HELPER ---------- */
private fun shareSummaryAsPdf(ctx: Context, innA: InningsSummary, innB: InningsSummary) {
    val summaryText = buildString {
        appendLine("MATCH SUMMARY")
        appendLine("------------------------------")
        appendLine(innA.toShareText())
        appendLine()
        if (innB.completed) appendLine(innB.toShareText())
        else appendLine("${innB.teamName} – yet to bat")
    }

    val pdfFile = createSummaryPdf(ctx, summaryText)
    val uri = FileProvider.getUriForFile(ctx, "${ctx.packageName}.fileprovider", pdfFile)

    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = "application/pdf"
        putExtra(Intent.EXTRA_STREAM, uri)
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }
    ctx.startActivity(Intent.createChooser(shareIntent, "Share match summary PDF"))
}

/* ---------- SMALL COMPOSABLES ---------- */
@Composable
private fun TeamToggleTile(text: String, selected: Boolean, modifier: Modifier = Modifier, onTap: () -> Unit) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = if (selected) Color(0xFF00695C) else Color.LightGray,
        modifier = modifier.height(40.dp).clickable { onTap() }
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(text, color = Color.White, fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
private fun ScoreCard(inn: InningsSummary) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(Color.White.copy(alpha = 0.9f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                Text(inn.teamName, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(inn.oversBowled, fontSize = 16.sp)
            }
            Spacer(Modifier.height(8.dp))
            Text("${inn.totalRuns}/${inn.totalWkts}", fontSize = 30.sp, fontWeight = FontWeight.ExtraBold)
            Spacer(Modifier.height(4.dp))
            Text("RR: ${inn.runRate}", fontSize = 14.sp)
        }
    }
}

@Composable
private fun CollapsibleHeader(title: String, expanded: Boolean, onToggle: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onToggle() }
            .background(Color(0xFFE0E0E0), RoundedCornerShape(6.dp))
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        Icon(
            if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
            contentDescription = null
        )
    }
    Spacer(Modifier.height(6.dp))
}

@Composable
private fun PlayerRow(p: PlayerStats) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) { Text(p.name); Text("${p.runs} (${p.balls})") }
}

@Composable
private fun OverRow(o: OverBreakdown) {
    Column(Modifier.fillMaxWidth()) {
        Text("Over ${o.overNumber}: ${o.runs}‑${o.wickets}")
        Spacer(Modifier.height(4.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            o.perBall.forEach {
                Surface(
                    shape = CircleShape,
                    color = Color(0xFF00695C),
                    modifier = Modifier.size(26.dp)
                ) { Box(Modifier.fillMaxSize(), Alignment.Center) { Text(it, color = Color.White, fontSize = 11.sp) } }
            }
        }
    }
}

/* ---------- PREVIEW ---------- */
@Preview(showSystemUi = true)
@Composable
fun SummaryPreviewFixed() {
    val innA = InningsSummary(
        teamName = "Team A", totalRuns = 120, totalWkts = 5,
        oversBowled = "10.0", runRate = "12.0",
        players = listOf(
            PlayerStats("Arjun Sharma", 45, 30, true),
            PlayerStats("Vikram Singh", 30, 25, true)
        ),
        overs = listOf(
            OverBreakdown(1,12,0,listOf("1","1","4","0","2","4")),
            OverBreakdown(2,8,1,listOf("1","W","1","2","0","4"))
        ),
        completed = true
    )
    val innB = InningsSummary(
        teamName = "Team B", totalRuns = 0, totalWkts = 0,
        oversBowled = "0.0", runRate = "0.0",
        players = emptyList(), overs = emptyList(),
        completed = false
    )
    MatchSummaryScreen(innA, innB)
}
