package com.lolinsight.presentation.history

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lolinsight.domain.model.GameAnalysis
import com.lolinsight.ui.theme.AllyBlue
import com.lolinsight.ui.theme.AllyBlueDark
import com.lolinsight.ui.theme.AllyBlueLight
import com.lolinsight.ui.theme.CardBackground
import com.lolinsight.ui.theme.CardBorder
import com.lolinsight.ui.theme.EnemyRed
import com.lolinsight.ui.theme.EnemyRedDark
import com.lolinsight.ui.theme.EnemyRedLight
import com.lolinsight.ui.theme.LolDark
import com.lolinsight.ui.theme.LolGold
import com.lolinsight.ui.theme.LolSurface
import com.lolinsight.ui.theme.TextMuted
import com.lolinsight.ui.theme.TextPrimary
import com.lolinsight.ui.theme.TextSecondary
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    onNavigateBack: () -> Unit,
    onNavigateToAnalysis: (Long) -> Unit,
    viewModel: HistoryViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.FRENCH)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LolDark)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            TopAppBar(
                title = {
                    Text(
                        text = "Historique",
                        color = LolGold,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Retour", tint = LolGold)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = LolSurface)
            )

            when {
                uiState.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = LolGold)
                    }
                }
                uiState.analyses.isEmpty() -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.History,
                                contentDescription = null,
                                tint = TextMuted,
                                modifier = Modifier.size(64.dp)
                            )
                            Text(
                                text = "Aucune analyse",
                                color = TextSecondary,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "Vos analyses sauvegardées apparaîtront ici.",
                                color = TextMuted,
                                textAlign = TextAlign.Center,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize().padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        item {
                            Text(
                                text = "${uiState.analyses.size} analyse(s) sauvegardée(s)",
                                color = TextSecondary,
                                fontSize = 13.sp,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                        }

                        items(
                            items = uiState.analyses,
                            key = { it.id }
                        ) { analysis ->
                            HistoryItem(
                                analysis = analysis,
                                formattedDate = dateFormat.format(analysis.timestamp),
                                onClick = { onNavigateToAnalysis(analysis.id) },
                                onDelete = { viewModel.deleteAnalysis(analysis.id) }
                            )
                        }

                        item {
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun HistoryItem(
    analysis: GameAnalysis,
    formattedDate: String,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = formattedDate,
                    color = TextMuted,
                    fontSize = 11.sp
                )

                // Teams preview
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Ally team
                    TeamPreview(
                        champions = analysis.allyTeam,
                        label = "Alliés",
                        color = AllyBlue,
                        modifier = Modifier.weight(1f)
                    )

                    // Enemy team
                    TeamPreview(
                        champions = analysis.enemyTeam,
                        label = "Ennemis",
                        color = EnemyRed,
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    StatBadge(
                        label = "${analysis.threats.filter { it.threatLevel >= 4 }.size} menaces",
                        color = EnemyRed
                    )
                    StatBadge(
                        label = "${analysis.allySynergies.size} synergies",
                        color = AllyBlue
                    )
                    StatBadge(
                        label = "${analysis.matchups.size} matchups",
                        color = LolGold
                    )
                }
            }

            IconButton(
                onClick = onDelete,
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Supprimer",
                    tint = EnemyRed.copy(alpha = 0.6f),
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

@Composable
private fun TeamPreview(
    champions: List<String>,
    label: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    val bgColor = if (color == AllyBlue) AllyBlueDark else EnemyRedDark
    val textColor = if (color == AllyBlue) AllyBlueLight else EnemyRedLight

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(bgColor.copy(alpha = 0.2f))
            .border(1.dp, color.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = label,
            color = textColor,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold
        )
        if (champions.isEmpty()) {
            Text(text = "—", color = TextMuted, fontSize = 11.sp)
        } else {
            champions.take(3).forEach { champion ->
                Text(
                    text = champion,
                    color = TextPrimary,
                    fontSize = 11.sp
                )
            }
            if (champions.size > 3) {
                Text(
                    text = "+${champions.size - 3} autres",
                    color = TextMuted,
                    fontSize = 10.sp
                )
            }
        }
    }
}

@Composable
private fun StatBadge(label: String, color: androidx.compose.ui.graphics.Color) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(color.copy(alpha = 0.1f))
            .padding(horizontal = 6.dp, vertical = 2.dp)
    ) {
        Text(
            text = label,
            color = color,
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium
        )
    }
}
