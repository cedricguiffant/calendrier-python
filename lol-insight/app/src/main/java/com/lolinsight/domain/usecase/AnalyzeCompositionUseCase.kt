package com.lolinsight.domain.usecase

import com.lolinsight.data.local.knowledge.ChampionKnowledge
import com.lolinsight.domain.model.GameAnalysis
import com.lolinsight.domain.model.Matchup
import com.lolinsight.domain.model.MatchupDifficulty
import com.lolinsight.domain.model.Synergy
import com.lolinsight.domain.model.SynergyStrength
import com.lolinsight.domain.model.Threat
import javax.inject.Inject

class AnalyzeCompositionUseCase @Inject constructor() {

    operator fun invoke(allyTeam: List<String>, enemyTeam: List<String>): GameAnalysis {
        val matchups = buildMatchups(allyTeam, enemyTeam)
        val threats = buildThreats(enemyTeam, allyTeam)
        val allySynergies = buildSynergies(allyTeam)
        val enemySynergies = buildSynergies(enemyTeam)
        val generalAdvice = buildGeneralAdvice(allyTeam, enemyTeam, matchups, threats)
        val earlyAdvice = buildEarlyAdvice(allyTeam, enemyTeam, threats)
        val midAdvice = buildMidAdvice(allyTeam, enemyTeam)
        val teamfightAdvice = buildTeamfightAdvice(allyTeam, enemyTeam, allySynergies)
        val itemRecs = buildItemRecommendations(enemyTeam)

        return GameAnalysis(
            allyTeam = allyTeam,
            enemyTeam = enemyTeam,
            matchups = matchups,
            threats = threats,
            allySynergies = allySynergies,
            enemySynergies = enemySynergies,
            generalAdvice = generalAdvice,
            earlyGameAdvice = earlyAdvice,
            midGameAdvice = midAdvice,
            teamfightAdvice = teamfightAdvice,
            itemRecommendations = itemRecs
        )
    }

    private fun buildMatchups(allyTeam: List<String>, enemyTeam: List<String>): List<Matchup> {
        val matchups = mutableListOf<Matchup>()

        for (ally in allyTeam) {
            for (enemy in enemyTeam) {
                val difficulty = ChampionKnowledge.getMatchupDifficulty(ally, enemy)
                val tips = ChampionKnowledge.getMatchupTips(ally, enemy)
                val items = ChampionKnowledge.getKeyItems(ally, enemy)

                if (difficulty != MatchupDifficulty.MEDIUM || tips.isNotEmpty()) {
                    matchups.add(
                        Matchup(
                            champion = ally,
                            opponent = enemy,
                            difficulty = difficulty,
                            tips = tips,
                            keyItems = items
                        )
                    )
                }
            }
        }

        return matchups.sortedByDescending { it.difficulty.ordinal }
    }

    private fun buildThreats(enemyTeam: List<String>, allyTeam: List<String>): List<Threat> {
        val threats = mutableListOf<Threat>()

        for (enemy in enemyTeam) {
            val threatInfo = ChampionKnowledge.getThreatInfo(enemy)
            if (threatInfo != null) {
                threats.add(
                    Threat(
                        champion = enemy,
                        threatLevel = threatInfo.threatLevel,
                        reason = threatInfo.reason,
                        counterTip = threatInfo.counterTip,
                        dangerousFrom = threatInfo.dangerousFrom
                    )
                )
            } else {
                threats.add(
                    Threat(
                        champion = enemy,
                        threatLevel = 3,
                        reason = "$enemy est un champion qui peut s'avérer dangereux selon l'avancement de la partie.",
                        counterTip = "Restez groupé et communiquez avec votre équipe sur la position de $enemy.",
                        dangerousFrom = "niveau 6"
                    )
                )
            }
        }

        return threats.sortedByDescending { it.threatLevel }
    }

    private fun buildSynergies(team: List<String>): List<Synergy> {
        val synergies = mutableListOf<Synergy>()
        val added = mutableSetOf<String>()

        for (i in team.indices) {
            for (j in i + 1 until team.size) {
                val champ1 = team[i]
                val champ2 = team[j]
                val key = "${champ1}_${champ2}"

                if (added.contains(key)) continue

                val synergyInfo = ChampionKnowledge.getSynergies(champ1, champ2)
                    ?: ChampionKnowledge.getSynergies(champ2, champ1)

                if (synergyInfo != null) {
                    synergies.add(
                        Synergy(
                            champions = listOf(champ1, champ2),
                            description = synergyInfo.description,
                            combo = synergyInfo.combo,
                            strength = synergyInfo.strength
                        )
                    )
                    added.add(key)
                }
            }
        }

        return synergies.sortedByDescending { it.strength.ordinal }
    }

    private fun buildGeneralAdvice(
        allyTeam: List<String>,
        enemyTeam: List<String>,
        matchups: List<Matchup>,
        threats: List<Threat>
    ): List<String> {
        val advice = mutableListOf<String>()

        val hardMatchups = matchups.filter { it.difficulty == MatchupDifficulty.HARD }
        val criticalThreats = threats.filter { it.threatLevel >= 4 }

        if (criticalThreats.isNotEmpty()) {
            val threatNames = criticalThreats.joinToString(", ") { it.champion }
            advice.add("PRIORITÉ: Menaces critiques détectées - $threatNames. Concentrez-vous sur leur neutralisation.")
        }

        if (hardMatchups.isNotEmpty()) {
            advice.add("${hardMatchups.size} matchup(s) difficile(s) détecté(s). Jouez défensivement dans ces lanes et demandez des ganks.")
        }

        val enemyHasEngagge = enemyTeam.any { it in listOf("Malphite", "Leona", "Blitzcrank", "Vi", "Amumu", "Nautilus") }
        val enemyHasPoke = enemyTeam.any { it in listOf("Zoe", "Lux", "Xerath", "Jayce", "Caitlyn", "Varus") }
        val enemyHasAssassins = enemyTeam.any { it in listOf("Zed", "Talon", "Katarina", "Akali", "Kha'Zix", "Rengar") }
        val enemyHasHealers = enemyTeam.any { it in listOf("Soraka", "Yuumi", "Vladimir", "Swain", "Nami") }

        if (enemyHasEngagge) {
            advice.add("L'équipe adverse a un fort potentiel d'engage. Wardez les flancs et restez groupés pour éviter d'être séparés.")
        }
        if (enemyHasPoke) {
            advice.add("L'équipe adverse a beaucoup de poke. Achetez des potions de soin supplémentaires et jouez proche de la tour.")
        }
        if (enemyHasAssassins) {
            advice.add("Des assassins dans l'équipe adverse. Les carries doivent acheter Sablier de Zhonya ou items défensifs.")
        }
        if (enemyHasHealers) {
            advice.add("L'adversaire a des soins. Achetez Accolade Exécrable (Grievous Wounds) pour réduire leurs soins de 40%.")
        }

        if (advice.isEmpty()) {
            advice.add("Composition équilibrée. Jouez votre jeu standard et adaptez-vous à l'évolution de la partie.")
        }

        return advice
    }

    private fun buildEarlyAdvice(
        allyTeam: List<String>,
        enemyTeam: List<String>,
        threats: List<Threat>
    ): List<String> {
        val advice = mutableListOf<String>()

        advice.add("Wardez la rivière dès le départ pour prévenir les ganks early.")

        val level2Threats = listOf("Leona", "Blitzcrank", "Thresh", "Draven")
        val level6Threats = listOf("Zed", "Malphite", "Vi", "Ahri", "Annie")

        val earlyLevel = enemyTeam.filter { it in level2Threats }
        val sixLevel = enemyTeam.filter { it in level6Threats }

        if (earlyLevel.isNotEmpty()) {
            advice.add("Attention! ${earlyLevel.joinToString(", ")} est très dangereux dès le niveau 2. Jouez prudemment en début de partie.")
        }

        if (sixLevel.isNotEmpty()) {
            advice.add("${sixLevel.joinToString(", ")} devient beaucoup plus dangereux au niveau 6. Adaptez votre jeu quand ils atteignent ce niveau.")
        }

        val jungleInvade = allyTeam.any { it in listOf("Lee Sin", "Olaf", "Vi", "Warwick") }
        if (jungleInvade) {
            advice.add("Votre jungler peut invader le camp ennemi en early. Coordonnez-vous pour prendre un avantage de ressources.")
        }

        advice.add("Première vague: essayez d'obtenir un avantage de level 2 avant l'adversaire si possible.")

        return advice
    }

    private fun buildMidAdvice(allyTeam: List<String>, enemyTeam: List<String>): List<String> {
        val advice = mutableListOf<String>()

        val allyHasGoodSplit = allyTeam.any { it in listOf("Fiora", "Tryndamere", "Camille", "Jax", "Yorick") }
        val allyHasTF = allyTeam.any { it in listOf("Malphite", "Amumu", "Leona", "Vi", "Orianna") }

        if (allyHasGoodSplit) {
            advice.add("Votre composition peut split-push efficacement. Créez de la pression en side-lane pendant que le reste de l'équipe conteste les objectifs.")
        }
        if (allyHasTF) {
            advice.add("Votre composition est forte en teamfight. Groupez autour des objectifs (Dragon, Baron) pour maximiser votre impact.")
        }

        advice.add("Priorité Dragon: assurez chaque âme de dragon pour les avantages cumulés.")
        advice.add("Poussez vos avantages de lane en roamant sur les autres lanes après avoir poussé vos vagues.")
        advice.add("Achetez des Pink Wards pour contrer les embuches et les champions invisibles.")

        return advice
    }

    private fun buildTeamfightAdvice(
        allyTeam: List<String>,
        enemyTeam: List<String>,
        synergies: List<Synergy>
    ): List<String> {
        val advice = mutableListOf<String>()

        if (synergies.any { it.strength == SynergyStrength.EXCEPTIONAL }) {
            val exSynergy = synergies.first { it.strength == SynergyStrength.EXCEPTIONAL }
            advice.add("COMBO EXCEPTIONNEL disponible: ${exSynergy.combo}. Priorisez ce combo dans les teamfights.")
        }

        val enemyHasDivers = enemyTeam.any { it in listOf("Zed", "Katarina", "Akali", "Irelia", "Jarvan IV") }
        if (enemyHasDivers) {
            advice.add("L'ennemi a des divers. Vos carries doivent rester groupés et protégés par les tanks/supports.")
        }

        val allyHasPeelers = allyTeam.any { it in listOf("Janna", "Lulu", "Morgana", "Braum", "Thresh") }
        if (allyHasPeelers) {
            advice.add("Excellent peeling dans votre équipe. Protégez votre ADC avec vos sorts défensifs en teamfight.")
        }

        advice.add("Focus les cibles isolées en priorité: ADC adverse, puis support, puis mage.")
        advice.add("N'engagez jamais 5v5 si votre composition est derrière - attendez les bonne opportunités.")

        return advice
    }

    private fun buildItemRecommendations(enemyTeam: List<String>): List<String> {
        val recs = mutableListOf<String>()

        val hasAD = enemyTeam.any { it in listOf("Zed", "Talon", "Yasuo", "Yone", "Draven", "Jinx", "Caitlyn") }
        val hasAP = enemyTeam.any { it in listOf("Lux", "Syndra", "Annie", "Zoe", "Ahri", "Veigar", "Malzahar") }
        val hasHealers = enemyTeam.any { it in listOf("Soraka", "Yuumi", "Vladimir", "Swain", "Nami", "Sona") }
        val hasAssassins = enemyTeam.any { it in listOf("Zed", "Katarina", "Akali", "Kha'Zix", "Rengar", "Talon") }
        val hasEngagge = enemyTeam.any { it in listOf("Malphite", "Leona", "Nautilus", "Vi", "Blitzcrank", "Amumu") }

        if (hasAD) recs.add("Armure recommandée: Plaque Thornmail, Gilet de Gardien, Iceborn Gauntlet vs. dégâts physiques")
        if (hasAP) recs.add("Résistance Magique recommandée: Manteau de Mage, Esprit de l'Ancêtre, Sablier de Zhonya")
        if (hasHealers) recs.add("Blessures Graves OBLIGATOIRES: Accolade Exécrable, Couperet d'Exécution, Lame du Roi Déchu")
        if (hasAssassins) recs.add("Contre les assassins: Sablier de Zhonya (mages), Ga (AD carries), Mercure pour les tanks")
        if (hasEngagge) recs.add("Contre les engages: Bottes de Mercure pour réduire les stuns, Rapidité pour s'échapper")

        if (recs.isEmpty()) {
            recs.add("Construisez en fonction de la composition et de l'évolution de la partie")
        }

        return recs
    }
}
