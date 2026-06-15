package com.lolinsight.presentation.analysis

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lolinsight.domain.model.GameAnalysis
import com.lolinsight.domain.repository.AnalysisRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class AnalysisUiState {
    object Loading : AnalysisUiState()
    data class Success(val analysis: GameAnalysis) : AnalysisUiState()
    data class Error(val message: String) : AnalysisUiState()
}

@HiltViewModel
class AnalysisViewModel @Inject constructor(
    private val analysisRepository: AnalysisRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<AnalysisUiState>(AnalysisUiState.Loading)
    val uiState: StateFlow<AnalysisUiState> = _uiState.asStateFlow()

    fun loadAnalysis(analysisId: Long) {
        viewModelScope.launch {
            _uiState.value = AnalysisUiState.Loading
            try {
                val analysis = analysisRepository.getAnalysisById(analysisId)
                if (analysis != null) {
                    _uiState.value = AnalysisUiState.Success(analysis)
                } else {
                    _uiState.value = AnalysisUiState.Error("Analyse introuvable")
                }
            } catch (e: Exception) {
                _uiState.value = AnalysisUiState.Error("Erreur: ${e.message}")
            }
        }
    }
}
