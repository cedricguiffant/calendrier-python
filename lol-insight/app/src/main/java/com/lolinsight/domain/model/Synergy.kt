package com.lolinsight.domain.model

data class Synergy(
    val champions: List<String>,
    val description: String,
    val combo: String,
    val strength: SynergyStrength = SynergyStrength.MEDIUM
)

enum class SynergyStrength {
    LOW, MEDIUM, HIGH, EXCEPTIONAL;

    fun label(): String = when (this) {
        LOW -> "Faible"
        MEDIUM -> "Correcte"
        HIGH -> "Forte"
        EXCEPTIONAL -> "Exceptionnelle"
    }
}
