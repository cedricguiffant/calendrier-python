package com.lolinsight.presentation.manual

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lolinsight.domain.repository.AnalysisRepository
import com.lolinsight.domain.usecase.AnalyzeCompositionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

sealed class ManualInputUiState {
    object Idle : ManualInputUiState()
    object Analyzing : ManualInputUiState()
    data class Success(val analysisId: Long) : ManualInputUiState()
    data class Error(val message: String) : ManualInputUiState()
}

@HiltViewModel
class ManualInputViewModel @Inject constructor(
    private val analyzeCompositionUseCase: AnalyzeCompositionUseCase,
    private val analysisRepository: AnalysisRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<ManualInputUiState>(ManualInputUiState.Idle)
    val uiState: StateFlow<ManualInputUiState> = _uiState.asStateFlow()

    // 5 slots alliés + 5 slots ennemis, null = vide
    private val _allySlots = MutableStateFlow<List<String?>>(List(5) { null })
    val allySlots: StateFlow<List<String?>> = _allySlots.asStateFlow()

    private val _enemySlots = MutableStateFlow<List<String?>>(List(5) { null })
    val enemySlots: StateFlow<List<String?>> = _enemySlots.asStateFlow()

    fun setAlly(index: Int, champion: String?) {
        val updated = _allySlots.value.toMutableList()
        updated[index] = champion
        _allySlots.value = updated
    }

    fun setEnemy(index: Int, champion: String?) {
        val updated = _enemySlots.value.toMutableList()
        updated[index] = champion
        _enemySlots.value = updated
    }

    fun clearAll() {
        _allySlots.value = List(5) { null }
        _enemySlots.value = List(5) { null }
        _uiState.value = ManualInputUiState.Idle
    }

    fun analyze() {
        val allies = _allySlots.value.filterNotNull()
        val enemies = _enemySlots.value.filterNotNull()

        if (allies.isEmpty() && enemies.isEmpty()) {
            _uiState.value = ManualInputUiState.Error("Sélectionnez au moins un champion")
            return
        }

        _uiState.value = ManualInputUiState.Analyzing
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val analysis = analyzeCompositionUseCase(allies, enemies)
                val savedId = analysisRepository.saveAnalysis(analysis)
                withContext(Dispatchers.Main) {
                    _uiState.value = ManualInputUiState.Success(savedId)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _uiState.value = ManualInputUiState.Error("Erreur: ${e.message}")
                }
            }
        }
    }

    fun resetState() {
        _uiState.value = ManualInputUiState.Idle
    }

    companion object {
        // Liste complète de tous les champions LoL (juin 2026)
        val ALL_CHAMPIONS = listOf(
            "Aatrox", "Ahri", "Akali", "Akshan", "Alistar", "Amumu", "Anivia", "Annie",
            "Aphelios", "Ashe", "AurelionSol", "Azir", "Bard", "BelVeth", "Blitzcrank",
            "Brand", "Braum", "Briar", "Caitlyn", "Camille", "Cassiopeia", "ChoGath",
            "Corki", "Darius", "Diana", "DrMundo", "Draven", "Ekko", "Elise", "Evelynn",
            "Ezreal", "Fiddlesticks", "Fiora", "Fizz", "Galio", "Gangplank", "Garen",
            "Gnar", "Gragas", "Graves", "Gwen", "Hecarim", "Heimerdinger", "Illaoi",
            "Irelia", "Ivern", "Janna", "JarvanIV", "Jax", "Jayce", "Jhin", "Jinx",
            "KaiSa", "Kalista", "Karma", "Karthus", "Kassadin", "Katarina", "Kayle",
            "Kayn", "Kennen", "KhaZix", "Kindred", "Kled", "KogMaw", "KSante",
            "LeBlanc", "LeeSin", "Leona", "Lillia", "Lissandra", "Lucian", "Lulu",
            "Lux", "Malphite", "Malzahar", "Maokai", "MasterYi", "Mel", "Milio",
            "MissFortune", "Mordekaiser", "Morgana", "Naafiri", "Nami", "Nasus",
            "Nautilus", "Neeko", "Nidalee", "Nilah", "Nocturne", "Nunu", "Olaf",
            "Orianna", "Ornn", "Pantheon", "Poppy", "Pyke", "Qiyana", "Quinn",
            "Rakan", "Rammus", "RekSai", "Rell", "Renata", "Renekton", "Rengar",
            "Riven", "Rumble", "Ryze", "Samira", "Sejuani", "Senna", "Seraphine",
            "Sett", "Shaco", "Shen", "Shyvana", "Singed", "Sion", "Sivir", "Skarner",
            "Smolder", "Sona", "Soraka", "Swain", "Sylas", "Syndra", "TahmKench",
            "Taliyah", "Talon", "Taric", "Teemo", "Thresh", "Tristana", "Trundle",
            "Tryndamere", "TwistedFate", "Twitch", "Udyr", "Urgot", "Varus", "Vayne",
            "Veigar", "VelKoz", "Vex", "Vi", "Viego", "Viktor", "Vladimir", "Volibear",
            "Warwick", "Wukong", "Xayah", "Xerath", "XinZhao", "Yasuo", "Yone",
            "Yorick", "Yuumi", "Zac", "Zed", "Zeri", "Ziggs", "Zilean", "Zoe", "Zyra"
        ).sorted()
    }
}
