package com.lolinsight.domain.model

data class Threat(
    val champion: String,
    val threatLevel: Int, // 1-5
    val reason: String,
    val counterTip: String,
    val dangerousFrom: String = "niveau 6" // quand ce champion devient dangereux
)
