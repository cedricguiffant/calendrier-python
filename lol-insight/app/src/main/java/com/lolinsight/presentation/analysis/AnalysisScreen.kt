package com.lolinsight.presentation.analysis

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lolinsight.domain.model.GameAnalysis
import com.lolinsight.domain.model.Matchup
import com.lolinsight.domain.model.MatchupDifficulty
import com.lolinsight.domain.model.Synergy
import com.lolinsight.domain.model.SynergyStrength
import com.lolinsight.domain.model.Threat
import com.lolinsight.ui.theme.AllyBlue
import com.lolinsight.ui.theme.AllyBlueDark
import com.lolinsight.ui.theme.AllyBlueLight
import com.lolinsight.ui.theme.CardBackground
import com.lolinsight.ui.theme.CardBorder
import com.lolinsight.ui.theme.DifficultyEasy
import com.lolinsight.ui.theme.DifficultyHard
import com.lolinsight.ui.theme.DifficultyMedium
import com.lolinsight.ui.theme.EnemyRed
import com.lolinsight.ui.theme.EnemyRedDark
import com.lolinsight.ui.theme.EnemyRedLight
import com.lolinsight.ui.theme.LolDark
import com.lolinsight.ui.theme.LolGold
import com.lolinsight.ui.theme.LolGoldLight
import com.lolinsight.ui.theme.LolSurface
import com.lolinsight.ui.theme.LolSurfaceVariant
import com.lolinsight.ui.theme.TextMuted
import com.lolinsight.ui.theme.TextPrimary
import com.lolinsight.ui.theme.TextSecondary
import com.lolinsight.ui.theme.ThreatCritical
import com.lolinsight.ui.theme.ThreatHigh
import com.lolinsight.ui.theme.ThreatLow
import com.lolinsight.ui.theme.ThreatMedium

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalysisScreen(
    analysisId: Long,
    onNavigateBack: () -> Unit,
    onNavigateToCamera: () -> Unit,
    viewModel: AnalysisViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(analysisId) {
        viewModel.loadAnalysis(analysisId)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LolDark)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            TopAppBar(
                title = {
                    Text(
                        text = "Analyse de Composition",
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

            when (val state = uiState) {
                is AnalysisUiState.Loading -> LoadingState()
                is AnalysisUiState.Error -> ErrorState(
                    message = state.message,
                    onRetry = { viewModel.loadAnalysis(analysisId) },
                    onNewCapture = onNavigateToCamera
                )
                is AnalysisUiState.Success -> AnalysisContent(
                    analysis = state.analysis,
                    onNewCapture = onNavigateToCamera
                )
            }
        }
    }
}

@Composable
private fun LoadingState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CircularProgressIndicator(color = LolGold, modifier = Modifier.size(56.dp))
            Text(
                text = "Chargement de l'analyse...",
                color = LolGold,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun ErrorState(
    message: String,
    onRetry: () -> Unit,
    onNewCapture: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = null,
                tint = EnemyRed,
                modifier = Modifier.size(64.dp)
            )
            Text(
                text = "Erreur d'analyse",
                color = EnemyRed,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = message,
                color = TextSecondary,
                textAlign = TextAlign.Center
            )
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(
                    onClick = onRetry,
                    colors = ButtonDefaults.buttonColors(containerColor = LolGold)
                ) {
                    Text("Réessayer", color = LolDark)
                }
                Button(
                    onClick = onNewCapture,
                    colors = ButtonDefaults.buttonColors(containerColor = AllyBlue)
                ) {
                    Icon(Icons.Default.CameraAlt, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Nouvelle photo")
                }
            }
        }
    }
}

@Composable
private fun AnalysisContent(
    analysis: GameAnalysis,
    onNewCapture: () -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Teams Section
        TeamsSection(
            allyTeam = analysis.allyTeam,
            enemyTeam = analysis.enemyTeam
        )

        // General Advice
        if (analysis.generalAdvice.isNotEmpty()) {
            ExpandableSection(
                title = "Conseils Généraux",
                icon = Icons.Default.Info,
                iconColor = LolGold,
                defaultExpanded = true
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    analysis.generalAdvice.forEach { advice ->
                        AdviceItem(text = advice, color = LolGold)
                    }
                }
            }
        }

        // Top Threats
        if (analysis.threats.isNotEmpty()) {
            ExpandableSection(
                title = "Menaces Prioritaires",
                icon = Icons.Default.Warning,
                iconColor = EnemyRed,
                defaultExpanded = true
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    analysis.threats.take(5).forEach { threat ->
                        ThreatCard(threat = threat)
                    }
                }
            }
        }

        // Matchups
        val notableMatchups = analysis.matchups.filter {
            it.difficulty != MatchupDifficulty.MEDIUM || it.tips.isNotEmpty()
        }
        if (notableMatchups.isNotEmpty()) {
            ExpandableSection(
                title = "Matchups",
                icon = Icons.Default.Security,
                iconColor = AllyBlue,
                defaultExpanded = false
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    notableMatchups.take(8).forEach { matchup ->
                        MatchupCard(matchup = matchup)
                    }
                }
            }
        }

        // Ally Synergies
        if (analysis.allySynergies.isNotEmpty()) {
            ExpandableSection(
                title = "Synergies Alliées",
                icon = Icons.Default.Groups,
                iconColor = AllyBlue,
                defaultExpanded = true
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    analysis.allySynergies.forEach { synergy ->
                        SynergyCard(synergy = synergy, isAlly = true)
                    }
                }
            }
        }

        // Enemy Synergies
        if (analysis.enemySynergies.isNotEmpty()) {
            ExpandableSection(
                title = "Synergies Ennemies",
                icon = Icons.Default.Groups,
                iconColor = EnemyRed,
                defaultExpanded = false
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    analysis.enemySynergies.forEach { synergy ->
                        SynergyCard(synergy = synergy, isAlly = false)
                    }
                }
            }
        }

        // Early Game Advice
        if (analysis.earlyGameAdvice.isNotEmpty()) {
            ExpandableSection(
                title = "Conseils Early Game",
                icon = Icons.Default.Lightbulb,
                iconColor = DifficultyEasy,
                defaultExpanded = false
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    analysis.earlyGameAdvice.forEach { advice ->
                        AdviceItem(text = advice, color = DifficultyEasy)
                    }
                }
            }
        }

        // Mid Game Advice
        if (analysis.midGameAdvice.isNotEmpty()) {
            ExpandableSection(
                title = "Conseils Mid Game",
                icon = Icons.Default.Psychology,
                iconColor = DifficultyMedium,
                defaultExpanded = false
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    analysis.midGameAdvice.forEach { advice ->
                        AdviceItem(text = advice, color = DifficultyMedium)
                    }
                }
            }
        }

        // Teamfight Advice
        if (analysis.teamfightAdvice.isNotEmpty()) {
            ExpandableSection(
                title = "Conseils Teamfight",
                icon = Icons.Default.Shield,
                iconColor = EnemyRed,
                defaultExpanded = false
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    analysis.teamfightAdvice.forEach { advice ->
                        AdviceItem(text = advice, color = EnemyRed)
                    }
                }
            }
        }

        // Item Recommendations
        if (analysis.itemRecommendations.isNotEmpty()) {
            ExpandableSection(
                title = "Objets Recommandés",
                icon = Icons.Default.ShoppingCart,
                iconColor = LolGold,
                defaultExpanded = false
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    analysis.itemRecommendations.forEach { rec ->
                        AdviceItem(text = rec, color = LolGoldLight)
                    }
                }
            }
        }

        // New Capture Button
        Button(
            onClick = onNewCapture,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            colors = ButtonDefaults.buttonColors(containerColor = LolGold),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(Icons.Default.CameraAlt, contentDescription = null, tint = LolDark)
            Spacer(Modifier.width(8.dp))
            Text("Nouvelle analyse", color = LolDark, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun TeamsSection(
    allyTeam: List<String>,
    enemyTeam: List<String>
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Compositions",
                color = LolGold,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Ally Team
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(AllyBlueDark.copy(alpha = 0.3f))
                        .border(1.dp, AllyBlue.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(AllyBlue)
                        )
                        Text(
                            text = "Alliés",
                            color = AllyBlueLight,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                    if (allyTeam.isEmpty()) {
                        Text(
                            text = "Non détectés",
                            color = TextMuted,
                            fontSize = 12.sp,
                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                        )
                    } else {
                        allyTeam.forEach { champion ->
                            ChampionNameTag(name = champion, color = AllyBlue)
                        }
                    }
                }

                // Enemy Team
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(EnemyRedDark.copy(alpha = 0.3f))
                        .border(1.dp, EnemyRed.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(EnemyRed)
                        )
                        Text(
                            text = "Ennemis",
                            color = EnemyRedLight,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                    if (enemyTeam.isEmpty()) {
                        Text(
                            text = "Non détectés",
                            color = TextMuted,
                            fontSize = 12.sp,
                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                        )
                    } else {
                        enemyTeam.forEach { champion ->
                            ChampionNameTag(name = champion, color = EnemyRed)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ChampionNameTag(name: String, color: Color) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
                .background(color.copy(alpha = 0.2f))
                .border(1.dp, color.copy(alpha = 0.5f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = name.take(2).uppercase(),
                color = color,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Text(
            text = name,
            color = TextPrimary,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun ExpandableSection(
    title: String,
    icon: ImageVector,
    iconColor: Color,
    defaultExpanded: Boolean = false,
    content: @Composable () -> Unit
) {
    var expanded by remember { mutableStateOf(defaultExpanded) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = title,
                    color = TextPrimary,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = if (expanded) "Réduire" else "Développer",
                    tint = TextSecondary,
                    modifier = Modifier.size(20.dp)
                )
            }

            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp)) {
                    Divider(color = CardBorder, modifier = Modifier.padding(bottom = 12.dp))
                    content()
                }
            }
        }
    }
}

@Composable
private fun ThreatCard(threat: Threat) {
    val threatColor = when (threat.threatLevel) {
        1, 2 -> ThreatLow
        3 -> ThreatMedium
        4 -> ThreatHigh
        5 -> ThreatCritical
        else -> ThreatMedium
    }
    val threatLabel = when (threat.threatLevel) {
        1 -> "Faible"
        2 -> "Bas"
        3 -> "Modéré"
        4 -> "Élevé"
        5 -> "CRITIQUE"
        else -> "Modéré"
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = LolSurfaceVariant),
        shape = RoundedCornerShape(10.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, threatColor.copy(alpha = 0.4f))
    ) {
        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(threatColor.copy(alpha = 0.2f))
                            .border(1.dp, threatColor, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = threat.champion.take(2).uppercase(),
                            color = threatColor,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Column {
                        Text(
                            text = threat.champion,
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Text(
                            text = "Dangereux dès ${threat.dangerousFrom}",
                            color = TextMuted,
                            fontSize = 11.sp
                        )
                    }
                }

                // Threat level badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(threatColor.copy(alpha = 0.2f))
                        .border(1.dp, threatColor, RoundedCornerShape(6.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = threatLabel,
                        color = threatColor,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Threat bar
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(text = "Menace", color = TextMuted, fontSize = 11.sp, modifier = Modifier.width(50.dp))
                LinearProgressIndicator(
                    progress = threat.threatLevel / 5f,
                    modifier = Modifier
                        .weight(1f)
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = threatColor,
                    trackColor = LolSurface
                )
                repeat(5) { i ->
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(
                                if (i < threat.threatLevel) threatColor else LolSurface
                            )
                    )
                }
            }

            Text(
                text = threat.reason,
                color = TextSecondary,
                fontSize = 12.sp,
                lineHeight = 17.sp
            )

            // Counter tip
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(AllyBlue.copy(alpha = 0.1f))
                    .border(1.dp, AllyBlue.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                    .padding(10.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Shield,
                    contentDescription = null,
                    tint = AllyBlue,
                    modifier = Modifier.size(14.dp)
                )
                Text(
                    text = threat.counterTip,
                    color = AllyBlueLight,
                    fontSize = 12.sp,
                    lineHeight = 16.sp
                )
            }
        }
    }
}

@Composable
private fun MatchupCard(matchup: Matchup) {
    val difficultyColor = when (matchup.difficulty) {
        MatchupDifficulty.EASY -> DifficultyEasy
        MatchupDifficulty.MEDIUM -> DifficultyMedium
        MatchupDifficulty.HARD -> DifficultyHard
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = LolSurfaceVariant),
        shape = RoundedCornerShape(10.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, difficultyColor.copy(alpha = 0.3f))
    ) {
        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = matchup.champion,
                        color = AllyBlue,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    Text(text = "vs", color = TextMuted, fontSize = 12.sp)
                    Text(
                        text = matchup.opponent,
                        color = EnemyRed,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(difficultyColor.copy(alpha = 0.15f))
                        .border(1.dp, difficultyColor, RoundedCornerShape(6.dp))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = matchup.difficulty.label(),
                        color = difficultyColor,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            if (matchup.tips.isNotEmpty()) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    matchup.tips.take(3).forEach { tip ->
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(text = "•", color = difficultyColor, fontSize = 12.sp)
                            Text(text = tip, color = TextSecondary, fontSize = 12.sp, lineHeight = 16.sp)
                        }
                    }
                }
            }

            if (matchup.keyItems.isNotEmpty()) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = null,
                        tint = LolGold,
                        modifier = Modifier.size(12.dp)
                    )
                    Text(
                        text = matchup.keyItems.joinToString(", "),
                        color = LolGoldLight,
                        fontSize = 11.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun SynergyCard(synergy: Synergy, isAlly: Boolean) {
    val accentColor = if (isAlly) AllyBlue else EnemyRed
    val strengthColor = when (synergy.strength) {
        SynergyStrength.LOW -> TextMuted
        SynergyStrength.MEDIUM -> DifficultyMedium
        SynergyStrength.HIGH -> DifficultyEasy
        SynergyStrength.EXCEPTIONAL -> LolGold
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = LolSurfaceVariant),
        shape = RoundedCornerShape(10.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, strengthColor.copy(alpha = 0.4f))
    ) {
        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    synergy.champions.forEachIndexed { index, champion ->
                        if (index > 0) {
                            Text(text = "+", color = accentColor, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                        Text(
                            text = champion,
                            color = accentColor,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    repeat(4) { i ->
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = if (i < synergy.strength.ordinal + 1) strengthColor else TextMuted,
                            modifier = Modifier.size(12.dp)
                        )
                    }
                }
            }

            Text(
                text = synergy.description,
                color = TextSecondary,
                fontSize = 12.sp,
                lineHeight = 16.sp
            )

            // Combo
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(strengthColor.copy(alpha = 0.1f))
                    .border(1.dp, strengthColor.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                    .padding(10.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Lightbulb,
                    contentDescription = null,
                    tint = strengthColor,
                    modifier = Modifier.size(14.dp)
                )
                Text(
                    text = synergy.combo,
                    color = strengthColor,
                    fontSize = 12.sp,
                    lineHeight = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
private fun AdviceItem(text: String, color: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(color.copy(alpha = 0.08f))
            .padding(10.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(6.dp)
                .clip(CircleShape)
                .background(color)
                .align(Alignment.Top)
                .padding(top = 4.dp)
        )
        Text(
            text = text,
            color = TextSecondary,
            fontSize = 13.sp,
            lineHeight = 18.sp
        )
    }
}
