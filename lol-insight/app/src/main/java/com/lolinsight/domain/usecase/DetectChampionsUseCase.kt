package com.lolinsight.domain.usecase

import android.graphics.Bitmap
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.lolinsight.domain.model.Champion
import com.lolinsight.domain.repository.ChampionRepository
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.math.min

data class DetectedTeams(
    val allyTeam: List<String>,
    val enemyTeam: List<String>,
    val rawDetectedNames: List<String>
)

class DetectChampionsUseCase @Inject constructor(
    private val championRepository: ChampionRepository
) {
    // Créé une seule fois pour éviter les fuites mémoire
    private val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
    // All champion names for matching
    private val allChampionNames = listOf(
        "Aatrox", "Ahri", "Akali", "Akshan", "Alistar", "Amumu", "Anivia", "Annie", "Aphelios",
        "Ashe", "AurelionSol", "Azir", "Bard", "BelVeth", "Blitzcrank", "Brand", "Braum",
        "Briar", "Caitlyn", "Camille", "Cassiopeia", "ChoGath", "Corki", "Darius", "Diana",
        "DrMundo", "Draven", "Ekko", "Elise", "Evelynn", "Ezreal", "Fiddlesticks", "Fiora",
        "Fizz", "Galio", "Gangplank", "Garen", "Gnar", "Gragas", "Graves", "Gwen", "Hecarim",
        "Heimerdinger", "Illaoi", "Irelia", "Ivern", "Janna", "JarvanIV", "Jax", "Jayce",
        "Jhin", "Jinx", "KSante", "Kaisa", "Kalista", "Karma", "Karthus", "Kassadin",
        "Katarina", "Kayle", "Kayn", "Kennen", "KhaZix", "Kindred", "Kled", "KogMaw",
        "LeBlanc", "LeeSin", "Leona", "Lillia", "Lissandra", "Lucian", "Lulu", "Lux",
        "Malphite", "Malzahar", "Maokai", "MasterYi", "MissFortune", "Mordekaiser", "Morgana",
        "Nami", "Nasus", "Nautilus", "Neeko", "Nidalee", "Nilah", "Nocturne", "Nunu", "Olaf",
        "Orianna", "Ornn", "Pantheon", "Poppy", "Pyke", "Qiyana", "Quinn", "Rakan", "Rammus",
        "RekSai", "Rell", "Renata", "Renekton", "Rengar", "Riven", "Rumble", "Ryze",
        "Samira", "Sejuani", "Senna", "Seraphine", "Sett", "Shaco", "Shen", "Shyvana",
        "Singed", "Sion", "Sivir", "Skarner", "Sona", "Soraka", "Swain", "Sylas", "Syndra",
        "TahmKench", "Taliyah", "Talon", "Taric", "Teemo", "Thresh", "Tristana", "Trundle",
        "Tryndamere", "TwistedFate", "Twitch", "Udyr", "Urgot", "Varus", "Vayne", "Veigar",
        "VelKoz", "Vex", "Vi", "Viego", "Viktor", "Vladimir", "Volibear", "Warwick", "Wukong",
        "Xayah", "Xerath", "XinZhao", "Yasuo", "Yone", "YorickZeri", "Zed", "Zeri", "Ziggs",
        "Zilean", "Zoe", "Zyra"
    )

    suspend operator fun invoke(bitmap: Bitmap): DetectedTeams {
        val recognizedText = recognizeText(bitmap)
        val detectedChampions = findChampionNames(recognizedText)

        return if (detectedChampions.size >= 2) {
            val midPoint = detectedChampions.size / 2
            DetectedTeams(
                allyTeam = detectedChampions.take(min(5, midPoint)),
                enemyTeam = detectedChampions.drop(midPoint).take(5),
                rawDetectedNames = detectedChampions
            )
        } else {
            DetectedTeams(
                allyTeam = detectedChampions.take(5),
                enemyTeam = emptyList(),
                rawDetectedNames = detectedChampions
            )
        }
    }

    private suspend fun recognizeText(bitmap: Bitmap): String {
        return suspendCancellableCoroutine { continuation ->
            val image = InputImage.fromBitmap(bitmap, 0)
            recognizer.process(image)
                .addOnSuccessListener { visionText ->
                    continuation.resume(visionText.text)
                }
                .addOnFailureListener { exception ->
                    continuation.resumeWithException(exception)
                }
        }
    }

    private fun findChampionNames(text: String): List<String> {
        val words = text.split("\\s+".toRegex())
            .map { it.trim() }
            .filter { it.length >= 3 }

        val detected = mutableListOf<String>()

        for (championName in allChampionNames) {
            for (word in words) {
                if (levenshteinDistance(
                        word.lowercase(),
                        championName.lowercase()
                    ) <= 2 || word.lowercase().contains(championName.lowercase())
                ) {
                    if (!detected.contains(championName)) {
                        detected.add(championName)
                    }
                    break
                }
            }
        }

        // Also try multi-word matching for champions like "Miss Fortune"
        val fullText = text.lowercase()
        val multiWordChampions = mapOf(
            "miss fortune" to "MissFortune",
            "master yi" to "MasterYi",
            "lee sin" to "LeeSin",
            "twisted fate" to "TwistedFate",
            "xin zhao" to "XinZhao",
            "jarvan iv" to "JarvanIV",
            "dr mundo" to "DrMundo",
            "kog maw" to "KogMaw",
            "vel koz" to "VelKoz",
            "cho gath" to "ChoGath",
            "bel veth" to "BelVeth",
            "kha zix" to "KhaZix",
            "rek sai" to "RekSai",
            "tahm kench" to "TahmKench",
            "k'sante" to "KSante",
            "aurelion sol" to "AurelionSol"
        )

        for ((multiWord, championName) in multiWordChampions) {
            if (fullText.contains(multiWord) && !detected.contains(championName)) {
                detected.add(championName)
            }
        }

        return detected
    }

    private fun levenshteinDistance(s1: String, s2: String): Int {
        val m = s1.length
        val n = s2.length
        val dp = Array(m + 1) { IntArray(n + 1) }

        for (i in 0..m) dp[i][0] = i
        for (j in 0..n) dp[0][j] = j

        for (i in 1..m) {
            for (j in 1..n) {
                dp[i][j] = if (s1[i - 1] == s2[j - 1]) {
                    dp[i - 1][j - 1]
                } else {
                    1 + minOf(dp[i - 1][j], dp[i][j - 1], dp[i - 1][j - 1])
                }
            }
        }

        return dp[m][n]
    }
}
