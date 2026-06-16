package com.lolinsight.presentation.manual

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lolinsight.ui.theme.AllyBlue
import com.lolinsight.ui.theme.EnemyRed
import com.lolinsight.ui.theme.LolDark
import com.lolinsight.ui.theme.LolGold
import com.lolinsight.ui.theme.LolSurface
import com.lolinsight.ui.theme.TextPrimary
import com.lolinsight.ui.theme.TextSecondary
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManualInputScreen(
    onNavigateBack: () -> Unit,
    onNavigateToAnalysis: (Long) -> Unit,
    viewModel: ManualInputViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val allySlots by viewModel.allySlots.collectAsState()
    val enemySlots by viewModel.enemySlots.collectAsState()

    // État du bottom sheet de sélection
    var showPicker by remember { mutableStateOf(false) }
    var pickingForAlly by remember { mutableStateOf(true) }
    var pickingIndex by remember { mutableStateOf(0) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    LaunchedEffect(uiState) {
        if (uiState is ManualInputUiState.Success) {
            onNavigateToAnalysis((uiState as ManualInputUiState.Success).analysisId)
        }
    }

    // Champions déjà sélectionnés (pour griser)
    val selectedChampions = (allySlots.filterNotNull() + enemySlots.filterNotNull()).toSet()

    Box(modifier = Modifier.fillMaxSize().background(LolDark)) {
        Column(modifier = Modifier.fillMaxSize()) {

            TopAppBar(
                title = {
                    Text("Saisie manuelle", color = TextPrimary, fontWeight = FontWeight.SemiBold)
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Retour", tint = LolGold)
                    }
                },
                actions = {
                    TextButton(onClick = { viewModel.clearAll() }) {
                        Text("Réinitialiser", color = LolGold)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = LolSurface)
            )

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item { Spacer(Modifier.height(8.dp)) }

                // Section Alliés
                item {
                    Text(
                        text = "ALLIÉS",
                        color = AllyBlue,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        letterSpacing = 2.sp
                    )
                }
                items(5) { index ->
                    ChampionSlot(
                        champion = allySlots[index],
                        label = "Allié ${index + 1}",
                        isAlly = true,
                        onClick = {
                            pickingForAlly = true
                            pickingIndex = index
                            showPicker = true
                        },
                        onClear = { viewModel.setAlly(index, null) }
                    )
                }

                item { Spacer(Modifier.height(8.dp)) }

                // Section Ennemis
                item {
                    Text(
                        text = "ENNEMIS",
                        color = EnemyRed,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        letterSpacing = 2.sp
                    )
                }
                items(5) { index ->
                    ChampionSlot(
                        champion = enemySlots[index],
                        label = "Ennemi ${index + 1}",
                        isAlly = false,
                        onClick = {
                            pickingForAlly = false
                            pickingIndex = index
                            showPicker = true
                        },
                        onClear = { viewModel.setEnemy(index, null) }
                    )
                }

                item { Spacer(Modifier.height(16.dp)) }
            }

            // Bouton analyser
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(LolSurface)
                    .padding(16.dp)
            ) {
                if (uiState is ManualInputUiState.Error) {
                    Text(
                        text = (uiState as ManualInputUiState.Error).message,
                        color = EnemyRed,
                        fontSize = 13.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                    )
                }

                Button(
                    onClick = { viewModel.analyze() },
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = LolGold),
                    enabled = uiState !is ManualInputUiState.Analyzing,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    if (uiState is ManualInputUiState.Analyzing) {
                        CircularProgressIndicator(
                            color = LolDark,
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp
                        )
                        Spacer(Modifier.width(8.dp))
                        Text("Analyse en cours...", color = LolDark, fontWeight = FontWeight.Bold)
                    } else {
                        Text(
                            text = "ANALYSER LA COMPOSITION",
                            color = LolDark,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            letterSpacing = 1.sp
                        )
                    }
                }
            }
        }
    }

    // Bottom Sheet — sélection de champion
    if (showPicker) {
        ModalBottomSheet(
            onDismissRequest = { showPicker = false },
            sheetState = sheetState,
            containerColor = LolSurface
        ) {
            ChampionPickerSheet(
                isAlly = pickingForAlly,
                alreadySelected = selectedChampions,
                onSelect = { champion ->
                    if (pickingForAlly) viewModel.setAlly(pickingIndex, champion)
                    else viewModel.setEnemy(pickingIndex, champion)
                    scope.launch { sheetState.hide() }.invokeOnCompletion { showPicker = false }
                }
            )
        }
    }
}

@Composable
private fun ChampionSlot(
    champion: String?,
    label: String,
    isAlly: Boolean,
    onClick: () -> Unit,
    onClear: () -> Unit
) {
    val borderColor = if (champion != null) {
        if (isAlly) AllyBlue else EnemyRed
    } else {
        Color.White.copy(alpha = 0.15f)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = if (champion != null)
                (if (isAlly) AllyBlue else EnemyRed).copy(alpha = 0.12f)
            else Color.White.copy(alpha = 0.04f)
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, borderColor, RoundedCornerShape(8.dp))
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                tint = if (champion != null) (if (isAlly) AllyBlue else EnemyRed) else TextSecondary,
                modifier = Modifier.size(20.dp)
            )
            Spacer(Modifier.width(12.dp))
            Text(
                text = champion ?: label,
                color = if (champion != null) TextPrimary else TextSecondary,
                fontWeight = if (champion != null) FontWeight.SemiBold else FontWeight.Normal,
                fontSize = 15.sp,
                modifier = Modifier.weight(1f)
            )
            if (champion != null) {
                IconButton(
                    onClick = onClear,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Effacer",
                        tint = TextSecondary,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun ChampionPickerSheet(
    isAlly: Boolean,
    alreadySelected: Set<String>,
    onSelect: (String) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    val accentColor = if (isAlly) AllyBlue else EnemyRed
    val title = if (isAlly) "Choisir un allié" else "Choisir un ennemi"

    val filtered = remember(searchQuery) {
        ManualInputViewModel.ALL_CHAMPIONS.filter {
            it.contains(searchQuery, ignoreCase = true)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = title,
            color = accentColor,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Rechercher un champion...", color = TextSecondary) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = TextSecondary) },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { searchQuery = "" }) {
                        Icon(Icons.Default.Clear, contentDescription = "Effacer", tint = TextSecondary)
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary,
                focusedBorderColor = accentColor,
                unfocusedBorderColor = Color.White.copy(alpha = 0.3f),
                cursorColor = accentColor,
                focusedContainerColor = LolDark,
                unfocusedContainerColor = LolDark
            ),
            singleLine = true
        )

        Spacer(Modifier.height(8.dp))

        LazyColumn(
            modifier = Modifier.height(380.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(filtered) { champion ->
                val isDisabled = alreadySelected.contains(champion)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(6.dp))
                        .background(
                            if (isDisabled) Color.White.copy(alpha = 0.03f)
                            else Color.White.copy(alpha = 0.06f)
                        )
                        .clickable(enabled = !isDisabled) { onSelect(champion) }
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = champion,
                        color = if (isDisabled) TextSecondary.copy(alpha = 0.4f) else TextPrimary,
                        fontSize = 15.sp,
                        fontWeight = if (!isDisabled) FontWeight.Medium else FontWeight.Normal,
                        modifier = Modifier.weight(1f)
                    )
                    if (isDisabled) {
                        Text(
                            text = "Sélectionné",
                            color = TextSecondary.copy(alpha = 0.4f),
                            fontSize = 11.sp
                        )
                    }
                }
            }
            item { Spacer(Modifier.height(32.dp)) }
        }
    }
}
