package com.lolinsight.data.local.knowledge

import com.lolinsight.domain.model.MatchupDifficulty
import com.lolinsight.domain.model.SynergyStrength

data class MatchupInfo(
    val opponent: String,
    val difficulty: MatchupDifficulty,
    val tips: List<String>,
    val keyItems: List<String> = emptyList()
)

data class ThreatInfo(
    val threatLevel: Int, // 1-5
    val reason: String,
    val counterTip: String,
    val dangerousFrom: String = "niveau 6"
)

data class SynergyInfo(
    val partner: String,
    val description: String,
    val combo: String,
    val strength: SynergyStrength
)

data class ChampionKnowledgeEntry(
    val name: String,
    val role: String,
    val strongAgainst: List<String>,
    val weakAgainst: List<String>,
    val matchups: List<MatchupInfo>,
    val threat: ThreatInfo,
    val synergies: List<SynergyInfo>,
    val generalTips: List<String>
)

object ChampionKnowledge {

    private fun defaultChampion(name: String): ChampionKnowledgeEntry = ChampionKnowledgeEntry(
        name = name,
        role = "UNKNOWN",
        strongAgainst = emptyList(),
        weakAgainst = emptyList(),
        matchups = emptyList(),
        threat = ThreatInfo(2, "$name est un champion de League of Legends", "Consultez op.gg pour les stratégies", "niveau 6"),
        synergies = emptyList(),
        generalTips = listOf("Farmez en sécurité", "Suivez votre équipe", "Utilisez votre ultime avec sagesse", "Achetez les objets appropriés")
    )

    val knowledgeBase: Map<String, ChampionKnowledgeEntry> = mapOf(

        "Yasuo" to ChampionKnowledgeEntry(
            name = "Yasuo",
            role = "MID/TOP",
            strongAgainst = listOf("Malzahar", "Zoe", "Tristana"),
            weakAgainst = listOf("Annie", "Lissandra", "Pantheon", "Renekton"),
            matchups = listOf(
                MatchupInfo(
                    opponent = "Annie",
                    difficulty = MatchupDifficulty.HARD,
                    tips = listOf(
                        "Annie peut stun Yasuo au travers de son Windwall avec Tibbers",
                        "Restez hors de portée pour éviter son combo instantané",
                        "Achetez Mercure/Ceinture de Mana pour réduire la durée du stun",
                        "Profitez des phases où elle n'a pas son stun chargé pour harasser"
                    ),
                    keyItems = listOf("Manteau de Mage", "Ceinture de Mana")
                ),
                MatchupInfo(
                    opponent = "Lissandra",
                    difficulty = MatchupDifficulty.HARD,
                    tips = listOf(
                        "Le CC de Lissandra traverse le Windwall de Yasuo",
                        "Sa glissade de glace permet de sortir de portée rapidement",
                        "Evitez de dash sur elle quand elle a son ultime",
                        "Achetez l'épée de Serpent pour le bouclier"
                    ),
                    keyItems = listOf("Épée de Serpent", "Manteau de Mage")
                ),
                MatchupInfo(
                    opponent = "Malzahar",
                    difficulty = MatchupDifficulty.EASY,
                    tips = listOf(
                        "Le Windwall bloque les Voidlings et les projectiles de Malzahar",
                        "Yasuo peut dash à travers ses vagues de sbires",
                        "Faites attention à son ultime de suppression - Quicksilver Sash",
                        "Poussez constamment la lane pour mettre la pression"
                    ),
                    keyItems = listOf("Brise-maléfice")
                ),
                MatchupInfo(
                    opponent = "Zed",
                    difficulty = MatchupDifficulty.MEDIUM,
                    tips = listOf(
                        "Duel équilibré - les deux champions sont des assassins AD",
                        "Windwall peut bloquer les Kunais de Zed",
                        "Évitez d'être en dessous de 50% de vie pour prévenir son ultime",
                        "Zhonya n'est pas disponible pour Yasuo - achetez un bouclier"
                    )
                ),
                MatchupInfo(
                    opponent = "Syndra",
                    difficulty = MatchupDifficulty.HARD,
                    tips = listOf(
                        "Le Windwall peut bloquer ses boules mais pas son ultime ciblé",
                        "Sa portée est supérieure à celle de Yasuo",
                        "Essayez de dash souvent pour éviter d'être ciblé",
                        "Achetez un bouclier ou vie supplémentaire"
                    )
                )
            ),
            threat = ThreatInfo(
                threatLevel = 4,
                reason = "Yasuo avec des objets est extrêmement dangereux. Son Windwall annule de nombreux sorts, et sa mobilité lui permet d'échapper aux ganks. Un Yasuo sans mort peut être inarrêtable.",
                counterTip = "Jouez des champions avec du CC qui ne soit pas des projectiles. Annie, Lissandra et Malphite sont d'excellents counters. Le Windwall de Yasuo ne bloque pas les sorts ciblés ni le CC au corps-à-corps.",
                dangerousFrom = "niveau 3 avec le passif de bouclier actif"
            ),
            synergies = listOf(
                SynergyInfo(
                    partner = "Malphite",
                    description = "Combo ultime légendaire - l'ultime de Malphite lance les ennemis en l'air, permettant à Yasuo d'activer son ultime gratuitement",
                    combo = "Malphite Unstoppable Force → Yasuo Last Breath (ultime)",
                    strength = SynergyStrength.EXCEPTIONAL
                ),
                SynergyInfo(
                    partner = "Orianna",
                    description = "Orianna place sa balle, Yasuo engage, Orianna tire la balle pour regrouper les ennemis en l'air",
                    combo = "Yasuo engage → Orianna ultime → Yasuo Last Breath",
                    strength = SynergyStrength.HIGH
                ),
                SynergyInfo(
                    partner = "Leona",
                    description = "Leona engage et stun permet à Yasuo de gagner ses stacks de tornades rapidement",
                    combo = "Leona CC → Yasuo dash et stack tornades",
                    strength = SynergyStrength.HIGH
                )
            ),
            generalTips = listOf(
                "Farmez prudemment jusqu'aux premiers objets (Sablier du Conquérant)",
                "Utilisez votre Windwall de manière proactive pour protéger vos alliés",
                "Empêchez votre Yasuo adverse de farmer sous tour avec des ganks",
                "Un Yasuo qui meurt 2 fois en early est généralement hors-jeu"
            )
        ),

        "Zed" to ChampionKnowledgeEntry(
            name = "Zed",
            role = "MID",
            strongAgainst = listOf("Veigar", "Ziggs", "Lux"),
            weakAgainst = listOf("Lissandra", "Malzahar", "Zhonya's users"),
            matchups = listOf(
                MatchupInfo(
                    opponent = "Lissandra",
                    difficulty = MatchupDifficulty.HARD,
                    tips = listOf(
                        "Lissandra peut utiliser son ultime sur elle-même pour se rendre invulnérable pendant l'ultime de Zed",
                        "Son CC arrive facilement et interrompt vos combos",
                        "Attendez qu'elle utilise ses sorts défensifs avant d'engager",
                        "Utilisez l'ombre pour approcher par les côtés"
                    ),
                    keyItems = listOf("Sablier de Zhonya")
                ),
                MatchupInfo(
                    opponent = "Veigar",
                    difficulty = MatchupDifficulty.EASY,
                    tips = listOf(
                        "Tuez Veigar avant qu'il accumule trop de PA",
                        "Évitez son Event Horizon (cage) - elle ne peut pas l'utiliser sur elle-même",
                        "Plongez tôt et souvent pour l'empêcher de farmer",
                        "Son ultime à dégâts fixes est moins efficace si vous restez en bonne santé"
                    )
                ),
                MatchupInfo(
                    opponent = "Yasuo",
                    difficulty = MatchupDifficulty.MEDIUM,
                    tips = listOf(
                        "Le Windwall de Yasuo peut bloquer certains de vos sorts",
                        "Placez vos ombres de manière à contourner le Windwall",
                        "Ce matchup dépend de qui arrive à burst l'autre en premier",
                        "Achetez un Long Épée en plus pour du burst supplémentaire"
                    )
                )
            ),
            threat = ThreatInfo(
                threatLevel = 5,
                reason = "Un Zed fed est l'un des champions les plus dangereux du jeu. Il peut one-shot les carries squishy avec son ultime sans leur laisser le temps de réagir. Sa mobilité lui permet de s'échapper après chaque kill.",
                counterTip = "Achetez Sablier de Zhonya IMMÉDIATEMENT - activer le Zhonya pendant son ultime le rend inutile. Malzahar le supprime avec son ultime. Lissandra peut se rendre invulnérable. Jouez groupé et ne vous retrouvez jamais seul.",
                dangerousFrom = "niveau 6 avec son ultime débloqué"
            ),
            synergies = listOf(
                SynergyInfo(
                    partner = "Jinx",
                    description = "Zed assassine les menaces et Jinx nettoie les combats de teamfight",
                    combo = "Zed élimine le support/ADC adverse → Jinx nettoie avec ses roquettes",
                    strength = SynergyStrength.MEDIUM
                ),
                SynergyInfo(
                    partner = "Vi",
                    description = "Vi peut cibler n'importe qui pour lancer Zed dessus avec son ultime",
                    combo = "Vi ultime sur ADC adverse → Zed saute dessus et termine",
                    strength = SynergyStrength.HIGH
                )
            ),
            generalTips = listOf(
                "Achetez Sablier de Zhonya pour contrer l'ultime de Zed",
                "Restez groupé pour ne pas être isolé et one-shotté",
                "Pingez quand Zed disparaît de la minimap - il cherche à tuer vos carries",
                "Un Zed qui est 0/3 en early est beaucoup moins dangereux"
            )
        ),

        "Thresh" to ChampionKnowledgeEntry(
            name = "Thresh",
            role = "SUPPORT",
            strongAgainst = listOf("Sona", "Soraka", "Nami"),
            weakAgainst = listOf("Blitzcrank", "Leona", "Nautilus"),
            matchups = listOf(
                MatchupInfo(
                    opponent = "Blitzcrank",
                    difficulty = MatchupDifficulty.HARD,
                    tips = listOf(
                        "Blitzcrank peut contester chaque hook de Thresh",
                        "Sa boîte aux lettres ne fonctionne pas contre Blitzcrank",
                        "Placez-vous derrière vos sbires pour éviter son crochet",
                        "Engagez quand son crochet est en cooldown"
                    )
                ),
                MatchupInfo(
                    opponent = "Soraka",
                    difficulty = MatchupDifficulty.EASY,
                    tips = listOf(
                        "Hookez Soraka pour interrompre ses soins",
                        "Sa faible mobilité la rend vulnérable à vos hooks",
                        "Achetez Accolade Exécrable pour contrer ses soins",
                        "Forcez-la à utiliser ses sorts avant de passer au combat"
                    )
                ),
                MatchupInfo(
                    opponent = "Leona",
                    difficulty = MatchupDifficulty.HARD,
                    tips = listOf(
                        "Leona gagne les échanges courts grâce à son burst de dégâts",
                        "Esquivez son Zenith Blade pour éviter son CC en chaîne",
                        "Utilisez votre Lanterne pour positionner vos alliés hors de sa portée",
                        "Jouez plus défensivement et ramassez des âmes pour vos stats"
                    )
                )
            ),
            threat = ThreatInfo(
                threatLevel = 4,
                reason = "Thresh est l'un des supports les plus polyvalents et dangereux. Un hook réussi peut envoyer votre ADC en mort instantanée. Sa Boîte aux Lettres crée une zone de contrôle massive et sa Lanterne offre une mobilité unique à son équipe.",
                counterTip = "Gardez vos sbires devant vous pour bloquer ses hooks. Restez hors de portée de son hook (570 unités). Si hooké, résistez-vous en vous éloignant de ses partenaires. Achetez la Ceinture de Mana pour la résistance aux ralentissements.",
                dangerousFrom = "niveau 1 si son hook touche"
            ),
            synergies = listOf(
                SynergyInfo(
                    partner = "Jinx",
                    description = "Thresh hook → Jinx peut placer son filet ou ses roquettes facilement. La Lanterne de Thresh permet à Jinx d'échapper aux situations dangereuses",
                    combo = "Thresh hook → Jinx Filet Flamme (E) → Jinx roquettes super",
                    strength = SynergyStrength.HIGH
                ),
                SynergyInfo(
                    partner = "Lucian",
                    description = "Thresh hook → Lucian dash vers la cible hookée pour un burst élevé en early",
                    combo = "Thresh hook → Lucian relique (W) → Lucian dash + burst",
                    strength = SynergyStrength.EXCEPTIONAL
                ),
                SynergyInfo(
                    partner = "Draven",
                    description = "Thresh hook garantit les dégâts brutaux de Draven en early pour souvent tuer en lane",
                    combo = "Thresh hook → Draven Stand Aside + AA + AA",
                    strength = SynergyStrength.HIGH
                )
            ),
            generalTips = listOf(
                "Pratiquez votre timing de hook - c'est la compétence clé de Thresh",
                "La Lanterne peut sauver des vies - anticipez les situations dangereuses",
                "Ramassez les âmes régulièrement pour vos stats passives",
                "Votre Boîte aux Lettres peut bloquer des retraites et des pursuits"
            )
        ),

        "Jinx" to ChampionKnowledgeEntry(
            name = "Jinx",
            role = "ADC",
            strongAgainst = listOf("Sivir", "Caitlyn"),
            weakAgainst = listOf("Draven", "MissFortune", "Lucian"),
            matchups = listOf(
                MatchupInfo(
                    opponent = "Draven",
                    difficulty = MatchupDifficulty.HARD,
                    tips = listOf(
                        "Draven inflige beaucoup plus de dégâts en early",
                        "Jouez très défensivement jusqu'aux premiers objets",
                        "Votre passif Jinx s'active après les kills - évitez les duels early",
                        "Demandez des ganks fréquents pour mettre Draven en difficulté"
                    ),
                    keyItems = listOf("Armure de Plaque", "Lame du Roi Déchu")
                ),
                MatchupInfo(
                    opponent = "Lucian",
                    difficulty = MatchupDifficulty.HARD,
                    tips = listOf(
                        "Lucian vous domine en early avec ses dash et son burst",
                        "Utilisez votre portée de fusée pour harasser à distance",
                        "Restez derrière vos sbires pour limiter ses all-in",
                        "Votre force est en late game - survivez jusqu'à 3 objets"
                    )
                ),
                MatchupInfo(
                    opponent = "Caitlyn",
                    difficulty = MatchupDifficulty.MEDIUM,
                    tips = listOf(
                        "Caitlyn a plus de portée en early - jouez respectueusement",
                        "Évitez ses pièges - ils réduisent votre mobilité",
                        "Votre dégâts en mid/late surpassent les siens",
                        "Utilisez votre filet pour interrompre son tir calibré"
                    )
                )
            ),
            threat = ThreatInfo(
                threatLevel = 4,
                reason = "Jinx est une des ADC avec le plus de dégâts en late game. Ses roquettes ont une portée et une aire d'effet immenses. Son ultime global peut finir des parties. Une Jinx qui reset avec des kills devient inarrêtable.",
                counterTip = "Tuez-la avant qu'elle ait ses objets. Engagez tôt avec des champions mobilestants. Ses roquettes ont une animation de chargement visible - esquivez-les. CC rapide et burst = dead Jinx. Évitez de la laisser reset avec des kills.",
                dangerousFrom = "niveau 9-11 avec 2 objets"
            ),
            synergies = listOf(
                SynergyInfo(
                    partner = "Thresh",
                    description = "Hook de Thresh → Jinx place ses pièges et ses roquettes facilement. Synergie classique et très puissante",
                    combo = "Thresh hook → Jinx pièges + rafale de roquettes",
                    strength = SynergyStrength.HIGH
                ),
                SynergyInfo(
                    partner = "Leona",
                    description = "L'engage puissant de Leona permet à Jinx de free-fire sur des cibles immobiles",
                    combo = "Leona Solar Flare → Jinx ultime + roquettes",
                    strength = SynergyStrength.EXCEPTIONAL
                ),
                SynergyInfo(
                    partner = "Vi",
                    description = "Vi ultime pin sur l'ADC adverse → Jinx l'abat facilement",
                    combo = "Vi ultime → Jinx ultime global depuis loin",
                    strength = SynergyStrength.HIGH
                ),
                SynergyInfo(
                    partner = "Blitzcrank",
                    description = "Crochet Blitzcrank → Jinx détruit la cible isolée",
                    combo = "Blitzcrank crochet → Jinx roquettes burst",
                    strength = SynergyStrength.HIGH
                )
            ),
            generalTips = listOf(
                "Farmez en toute sécurité jusqu'à vos 2 premiers objets",
                "Ne jouez pas agressivement en early - votre puissance arrive en late",
                "Utilisez vos roquettes pour CS de loin si vous êtes sous pression",
                "Gardez votre filet (E) pour les engagements ennemis inattendus"
            )
        ),

        "Jhin" to ChampionKnowledgeEntry(
            name = "Jhin",
            role = "ADC",
            strongAgainst = listOf("Ashe", "Sivir"),
            weakAgainst = listOf("Lucian", "Draven", "MissFortune"),
            matchups = listOf(
                MatchupInfo(
                    opponent = "Lucian",
                    difficulty = MatchupDifficulty.HARD,
                    tips = listOf(
                        "Lucian peut dash vers vous pendant votre rechargement de 4 balles",
                        "Placez vos pièges floraux pour limiter ses dash",
                        "Votre 4ème balle critiquée inflige des dégâts massifs",
                        "Essayez de l'engager quand il vient de dépenser ses sorts"
                    )
                ),
                MatchupInfo(
                    opponent = "Ashe",
                    difficulty = MatchupDifficulty.EASY,
                    tips = listOf(
                        "Jhin outrange Ashe avec sa 4ème balle",
                        "Vos pièges floraux neutralisent sa mobilité déjà faible",
                        "Votre ultime peut vous empêcher de recevoir son ultime",
                        "Dominez les échanges avec vos 4 balles puis reculez pour recharger"
                    )
                )
            ),
            threat = ThreatInfo(
                threatLevel = 3,
                reason = "Jhin inflige des dégâts brutaux avec sa 4ème balle critiquée. Son ultime a une portée globale et peut sniper des ennemis à travers toute la map. Ses pièges floraux contrôlent les zones et révèlent les invisibles.",
                counterTip = "Engagez sur lui pendant qu'il recharge (après sa 4ème balle). Sa mobilité est limitée hors de son ultime. Des champions à haute mobilité comme Lucian ou Draven le surpassent en early. Évitez les lignes droites quand son ultime est actif.",
                dangerousFrom = "niveau 6 avec son ultime"
            ),
            synergies = listOf(
                SynergyInfo(
                    partner = "Leona",
                    description = "Leona engage → Jhin place ses pièges et charge sa 4ème balle critiquée sur des cibles CC",
                    combo = "Leona Solar Flare → Jhin 4ème balle + ultime de finition",
                    strength = SynergyStrength.EXCEPTIONAL
                ),
                SynergyInfo(
                    partner = "Morgana",
                    description = "Morgana Black Shield protège Jhin, et sa liaison noire permet un CC long pour la 4ème balle",
                    combo = "Morgana liaison noire → Jhin 4ème balle critiquée",
                    strength = SynergyStrength.HIGH
                ),
                SynergyInfo(
                    partner = "Thresh",
                    description = "Hook Thresh rallentit la cible, Jhin peut placer ses pièges et ses shots",
                    combo = "Thresh hook → Jhin pièges + 4ème balle",
                    strength = SynergyStrength.HIGH
                )
            ),
            generalTips = listOf(
                "Mémorisez toujours combien de balles vous avez restantes",
                "Votre 4ème balle fait des dégâts bonus basés sur les PV manquants",
                "Utilisez vos pièges pour sécuriser votre zone et prévenir les flanks",
                "Votre ultime peut révéler des ennemis à travers le brouillard de guerre"
            )
        ),

        "Lux" to ChampionKnowledgeEntry(
            name = "Lux",
            role = "MID/SUPPORT",
            strongAgainst = listOf("Zed", "Talon"),
            weakAgainst = listOf("Yasuo", "Zoe", "LeBlanc"),
            matchups = listOf(
                MatchupInfo(
                    opponent = "Zed",
                    difficulty = MatchupDifficulty.HARD,
                    tips = listOf(
                        "Zed peut dash à travers votre Q pour éviter votre CC",
                        "Achetez Sablier de Zhonya pour survivre son ultime",
                        "Restez derrière vos sbires pour limiter ses angles d'approche",
                        "Utilisez votre Q à distance pour le harasser quand il farm"
                    ),
                    keyItems = listOf("Sablier de Zhonya")
                ),
                MatchupInfo(
                    opponent = "Yasuo",
                    difficulty = MatchupDifficulty.HARD,
                    tips = listOf(
                        "Windwall de Yasuo bloque votre Q et votre ultime",
                        "Essayez de le Q par le côté ou derrière lui",
                        "Votre ultime peut être bloqué si mal placé",
                        "Appelez le jungler pour gérer ce matchup"
                    )
                ),
                MatchupInfo(
                    opponent = "Talon",
                    difficulty = MatchupDifficulty.HARD,
                    tips = listOf(
                        "Talon peut vous all-in si vous êtes dans sa portée",
                        "Restez à distance maximale de votre Q",
                        "Poussez la vague et retournez à la base fréquemment",
                        "Demandez des ganks réguliers - Talon est vulnérable aux ganks"
                    ),
                    keyItems = listOf("Sablier de Zhonya")
                )
            ),
            threat = ThreatInfo(
                threatLevel = 3,
                reason = "Lux peut one-shot les carries squishy avec son combo Q → R. Son ultime de rechargement rapide lui permet de spammer les dégâts en teamfight. Son bouclier protège ses alliés et sa liaisons peut double-bind.",
                counterTip = "Esquivez son Q (liaison de lumière) - sans CC, elle ne peut pas vous tuer. Des champions mobiles comme Zed, LeBlanc ou Katarina la dominent. Tenez-vous hors de portée de son ultime longue distance.",
                dangerousFrom = "niveau 6 avec son ultime"
            ),
            synergies = listOf(
                SynergyInfo(
                    partner = "Jinx",
                    description = "Lux Q CC → Jinx peut placer ses shots et ses roquettes sans risque",
                    combo = "Lux liaison → Jinx rafale + ultime global",
                    strength = SynergyStrength.HIGH
                ),
                SynergyInfo(
                    partner = "Caitlyn",
                    description = "Lux CC → Caitlyn tir calibré gratuit sur cible immobile",
                    combo = "Lux Q → Caitlyn tir calibré + tir de sniper",
                    strength = SynergyStrength.HIGH
                )
            ),
            generalTips = listOf(
                "Visez votre Q de manière à bind deux ennemis simultanément",
                "Utilisez votre bouclier sur vos alliés proactiveement avant les échanges",
                "Votre ultime peut effacer des vagues de sbires - utilisez-le pour l'objectif",
                "Cherchez les angles où votre Q ne peut pas être esquivé"
            )
        ),

        "Ahri" to ChampionKnowledgeEntry(
            name = "Ahri",
            role = "MID",
            strongAgainst = listOf("Katarina", "Kassadin"),
            weakAgainst = listOf("Syndra", "Lissandra", "Zoe"),
            matchups = listOf(
                MatchupInfo(
                    opponent = "Syndra",
                    difficulty = MatchupDifficulty.HARD,
                    tips = listOf(
                        "Syndra vous outrange constamment avec ses boules",
                        "Ses boules peuvent vous stunner si elle en a 3 sur le terrain",
                        "Essayez d'esquiver ses sorts avec votre dash",
                        "Engagez seulement si vous avez des kills ou un avantage de niveau"
                    ),
                    keyItems = listOf("Sablier de Zhonya", "Manteau de Mage")
                ),
                MatchupInfo(
                    opponent = "Katarina",
                    difficulty = MatchupDifficulty.EASY,
                    tips = listOf(
                        "Votre charm peut interrompre son ultime (Mort de mille lames)",
                        "Vos 3 dash d'ultime vous permettent de fuir facilement",
                        "Si elle saute sur une dague, charmez-la immédiatement",
                        "Votre mobilité est supérieure à la sienne"
                    )
                )
            ),
            threat = ThreatInfo(
                threatLevel = 3,
                reason = "Ahri est une mage mobile polyvalente. Son Charm peut dicter le combat en contrôlant les cibles clés. Sa mobilité avec son ultime (3 charges) lui permet d'échapper à la plupart des situations. Excellente en teamfight et en skirmish.",
                counterTip = "Son Charm est projectile - esquivez-le! Sans son Charm, Ahri a moins de punch. Syndra et Lissandra l'outpoke et outrange. Gardez un oeil sur ses charges d'ultime pour savoir si elle peut engager ou fuir.",
                dangerousFrom = "niveau 6 avec ultime débloqué"
            ),
            synergies = listOf(
                SynergyInfo(
                    partner = "Vi",
                    description = "Ahri charm → Vi peur s'assurer le kill. Vi ultime → Ahri peut CC la cible immobilisée",
                    combo = "Ahri charm → Vi ultime + burst → Ahri ultime poursuite",
                    strength = SynergyStrength.HIGH
                ),
                SynergyInfo(
                    partner = "Jinx",
                    description = "Ahri charm isole des cibles pour Jinx",
                    combo = "Ahri charm → Jinx ultime global pour finir",
                    strength = SynergyStrength.MEDIUM
                )
            ),
            generalTips = listOf(
                "Priorisez le roaming avec votre mobilité d'ultime",
                "Votre Charm est essentiel - ne le spammez pas inutilement",
                "Utilisez votre ultime offensivement ET défensivement",
                "Ahri excelle pour diviser et isoler les cibles en teamfight"
            )
        ),

        "Blitzcrank" to ChampionKnowledgeEntry(
            name = "Blitzcrank",
            role = "SUPPORT",
            strongAgainst = listOf("Sona", "Soraka", "Yuumi"),
            weakAgainst = listOf("Braum", "Morgana", "Janna"),
            matchups = listOf(
                MatchupInfo(
                    opponent = "Morgana",
                    difficulty = MatchupDifficulty.HARD,
                    tips = listOf(
                        "Le Black Shield de Morgana rend son ADC immunisé à votre crochet",
                        "Ciblez le support adverse ou attendez que le shield expire",
                        "Evitez de gaspiller votre crochet sur la cible protégée",
                        "Votre silence passif sur AA peut interrompre ses sorts"
                    )
                ),
                MatchupInfo(
                    opponent = "Soraka",
                    difficulty = MatchupDifficulty.EASY,
                    tips = listOf(
                        "Crocheter Soraka = kill quasi assuré avec bonne coordination",
                        "Elle ne peut pas soigner si vous la CC",
                        "Achetez Accolade Exécrable pour contrer ses soins",
                        "Huntez Soraka en permanence - sa mort désactive ses soins globaux"
                    )
                ),
                MatchupInfo(
                    opponent = "Braum",
                    difficulty = MatchupDifficulty.HARD,
                    tips = listOf(
                        "Braum peut interposer son bouclier pour bloquer votre crochet",
                        "Son passif peut appliquer son stun avant votre crochet",
                        "Cibler l'ADC reste dangereux car Braum peut s'y téléporter",
                        "Jouez plus défensivement dans ce matchup"
                    )
                )
            ),
            threat = ThreatInfo(
                threatLevel = 4,
                reason = "Un crochet de Blitzcrank réussi est souvent une mort instantanée pour la cible. Sa capacité à isoler une cible et la rapprocher de son équipe est unique. Son silence passif et son knock-up peuvent CC en chaîne.",
                counterTip = "Restez toujours derrière vos sbires pour bloquer son crochet. Si vous êtes hooké, courez VERS ses alliés pour limiter les dégâts (contrer-intuitif). Achetez Mercure pour réduire le CC. Morgana's Black Shield rend son ADC immunisé.",
                dangerousFrom = "niveau 1 si le hook touche"
            ),
            synergies = listOf(
                SynergyInfo(
                    partner = "Jinx",
                    description = "Crochet Blitzcrank isole une cible → Jinx la détruit",
                    combo = "Blitzcrank crochet → Blitzcrank knock-up → Jinx rafale + pièges",
                    strength = SynergyStrength.HIGH
                ),
                SynergyInfo(
                    partner = "MissFortune",
                    description = "Hook Blitzcrank → cible immobile pendant ultime Miss Fortune",
                    combo = "Blitzcrank crochet → MissFortune ultime Ballade de Balle",
                    strength = SynergyStrength.EXCEPTIONAL
                )
            ),
            generalTips = listOf(
                "Votre crochet est votre seule vraie engage tool - ne le ratez pas",
                "Marchez dans les buissons pour créer des angles de crochet inattendus",
                "Votre passif crée un champ électrique - utilisez-le pour surprendre",
                "Achetez Rédemptioon pour des soins en teamfight"
            )
        ),

        "Malphite" to ChampionKnowledgeEntry(
            name = "Malphite",
            role = "TOP",
            strongAgainst = listOf("Yasuo", "Yone", "Vayne"),
            weakAgainst = listOf("Darius", "Fiora", "Gnar"),
            matchups = listOf(
                MatchupInfo(
                    opponent = "Yasuo",
                    difficulty = MatchupDifficulty.EASY,
                    tips = listOf(
                        "Votre ultime traverse le Windwall de Yasuo",
                        "Stackez de l'armure pour réduire ses dégâts physiques",
                        "Votre passif de bouclier absorbe son harass",
                        "Attendez niveau 6 pour l'all-in avec votre ultime"
                    ),
                    keyItems = listOf("Cuirasse du Mausolée", "Iceborn Gauntlet")
                ),
                MatchupInfo(
                    opponent = "Darius",
                    difficulty = MatchupDifficulty.HARD,
                    tips = listOf(
                        "Darius gagne tous les échanges prolongés",
                        "Jouez hyper-passif et farmez à distance",
                        "Achetez Manteau de Mage pour sa résistance magique et anti-poke",
                        "Votre ultime peut engager sur lui si votre équipe suit"
                    )
                ),
                MatchupInfo(
                    opponent = "Fiora",
                    difficulty = MatchupDifficulty.HARD,
                    tips = listOf(
                        "Fiora peut parrier votre ultime avec son W",
                        "Elle détecte et exploite vos points vitaux rapidement",
                        "Jouez à distance et farmez sans chercher les échanges",
                        "Votre valeur est d'apporter votre ultime en teamfight"
                    )
                )
            ),
            threat = ThreatInfo(
                threatLevel = 4,
                reason = "Malphite est l'un des tanks avec l'ultime d'engage le plus dangereux du jeu. Son Unstoppable Force peut commencer un teamfight instantanément depuis une longue distance. En symbiose avec des champions comme Yasuo, il crée des combos dévastateurs.",
                counterTip = "Ward les buissons pour le voir arriver. Des champions mobiles ou à CC peuvent l'interrompre avant qu'il n'atteigne sa cible. Fiora peut parrier son ultime. Kite sa lenteur après qu'il ait utilisé son ultime.",
                dangerousFrom = "niveau 6 avec son ultime"
            ),
            synergies = listOf(
                SynergyInfo(
                    partner = "Yasuo",
                    description = "Combo ultime légendaire: Malphite lance ses ennemis en l'air, Yasuo peut utiliser son ultime gratuitement sur les cibles en l'air",
                    combo = "Malphite Unstoppable Force → IMMÉDIATEMENT Yasuo Last Breath",
                    strength = SynergyStrength.EXCEPTIONAL
                ),
                SynergyInfo(
                    partner = "Orianna",
                    description = "Malphite engage et Orianna place sa balle pour maximum de cibles",
                    combo = "Malphite ultime → Orianna ultime + Yasuo ultime triple kill",
                    strength = SynergyStrength.EXCEPTIONAL
                )
            ),
            generalTips = listOf(
                "Stackez armure et résistance magique pour être un mur en teamfight",
                "Votre ultime est une porte d'entrée pour votre équipe - choisissez le bon timing",
                "Gardez votre ultime pour l'engage décisif, ne le gaspillez pas",
                "En late game, votre valeur est d'initier les teamfights"
            )
        ),

        "Leona" to ChampionKnowledgeEntry(
            name = "Leona",
            role = "SUPPORT",
            strongAgainst = listOf("Sona", "Soraka", "Nami"),
            weakAgainst = listOf("Morgana", "Janna", "Braum"),
            matchups = listOf(
                MatchupInfo(
                    opponent = "Morgana",
                    difficulty = MatchupDifficulty.HARD,
                    tips = listOf(
                        "Le Black Shield de Morgana immunise l'ADC contre tout votre CC",
                        "Ciblez Morgana elle-même si son shield expire",
                        "Votre team va perdre des échanges si vous ne pouvez pas CC",
                        "Jouez défensivement et attendez les errerus de Morgana"
                    )
                ),
                MatchupInfo(
                    opponent = "Soraka",
                    difficulty = MatchupDifficulty.EASY,
                    tips = listOf(
                        "Plongez agressivement sur Soraka pour la tuer",
                        "Son ultime de soin mondial cesse si elle est CC",
                        "Achetez Accolade Exécrable pour contrer ses soins",
                        "Donnez la priorité au kill de Soraka en teamfight"
                    )
                )
            ),
            threat = ThreatInfo(
                threatLevel = 4,
                reason = "Leona est l'un des engageurs les plus puissants du jeu. Son kit est entièrement basé sur le CC en chaîne. Elle peut stunner en chaîne avec Solar Flare + Eclipse + Zenith Blade. Impitoyable sur les cibles isolées.",
                counterTip = "Évitez d'engager Leona en terrain ouvert. Morgana et Janna la contre parfaitement. Si elle engage, courez LOIN pour minimiser les dégâts de son Eclipse. Achetez Mercure/Ceinture de Mana.",
                dangerousFrom = "niveau 2 avec E+W combo"
            ),
            synergies = listOf(
                SynergyInfo(
                    partner = "Jinx",
                    description = "Leona CC → Jinx damage suivant est gratuitement garantit. Combo lane devastating.",
                    combo = "Leona Solar Flare → Jinx roquettes et pièges sur cible CC",
                    strength = SynergyStrength.EXCEPTIONAL
                ),
                SynergyInfo(
                    partner = "Jhin",
                    description = "Leona engage → Jhin peut placer ses 4 shots sur cible immobile",
                    combo = "Leona Solar Flare → Jhin 4ème balle critiquée + ultime",
                    strength = SynergyStrength.HIGH
                ),
                SynergyInfo(
                    partner = "Yasuo",
                    description = "Leona knock-up → Yasuo Last Breath gratuit",
                    combo = "Leona Zenith Blade + Soleil → Yasuo Last Breath",
                    strength = SynergyStrength.HIGH
                )
            ),
            generalTips = listOf(
                "Engagez toujours avec E→W→Q pour maximiser votre CC",
                "Visez le carry adverse, pas le tank",
                "Flash + Solar Flare peut initier des teamfights depuis une longue portée",
                "Votre résistance vous permet de rester en fight longtemps"
            )
        ),

        "Vi" to ChampionKnowledgeEntry(
            name = "Vi",
            role = "JUNGLE",
            strongAgainst = listOf("Amumu", "Nunu", "Rammus"),
            weakAgainst = listOf("Lee Sin", "Kha'Zix", "Graves"),
            matchups = listOf(
                MatchupInfo(
                    opponent = "LeeSin",
                    difficulty = MatchupDifficulty.HARD,
                    tips = listOf(
                        "Lee Sin gagne les duels pre-6 avec sa mobilité",
                        "Attendez niveau 6 pour avoir votre ultime comme atout",
                        "Ne trailblazer pas de manière prévisible - Lee Sin peut counter-gank",
                        "Jouez autour des objectifs plutôt que des duels"
                    )
                ),
                MatchupInfo(
                    opponent = "Amumu",
                    difficulty = MatchupDifficulty.EASY,
                    tips = listOf(
                        "Vous clearez plus vite et pouvez counter-jungle Amumu",
                        "Votre ultime peut interrompre son engage ou initier avant lui",
                        "Prenez les dragon et baron avant qu'il en ait les niveaux",
                        "Invadez son jungle early pour lui voler de l'expérience"
                    )
                )
            ),
            threat = ThreatInfo(
                threatLevel = 3,
                reason = "Vi est un jungler d'engage redoutable. Son ultime Assault and Battery est non-stopable et peut viser des cibles à travers les murs. En combo avec des ADC comme Jinx ou Caitlyn, elle garantit pratiquement des kills.",
                counterTip = "Vi est vulnérable pendant l'animation de son Q charge. Quand elle Q, esquivez ou CC. Son ultime peut être interrompu par Trundle pillar, Poppy W, ou Janna tornago. Gardez un tracker pour connaître sa position.",
                dangerousFrom = "niveau 6 avec son ultime débloqué"
            ),
            synergies = listOf(
                SynergyInfo(
                    partner = "Jinx",
                    description = "Vi ultime pin l'ADC adverse → Jinx peut l'abattre en sécurité depuis l'arrière",
                    combo = "Vi ultime sur ADC adverse → Jinx rafale + roquettes = kill garanti",
                    strength = SynergyStrength.EXCEPTIONAL
                ),
                SynergyInfo(
                    partner = "Caitlyn",
                    description = "Vi ultime pin → Caitlyn tir calibré sur cible immobile",
                    combo = "Vi ultime → Caitlyn tir calibré gratuit + pièges",
                    strength = SynergyStrength.HIGH
                ),
                SynergyInfo(
                    partner = "Ahri",
                    description = "Vi ultime lance une cible → Ahri charm + burst pour finir",
                    combo = "Vi ultime → Ahri charm + combo Q/W/E",
                    strength = SynergyStrength.HIGH
                )
            ),
            generalTips = listOf(
                "Priorisez les ganks de lanes avec du CC pour maximiser votre impact",
                "Counter-jungler les champions plus faibles en early",
                "Votre ultime peut traverser les murs pour atteindre des cibles",
                "Vous êtes meilleure pour suivre l'avance de votre team que de la créer"
            )
        ),

        "LeeSin" to ChampionKnowledgeEntry(
            name = "LeeSin",
            role = "JUNGLE",
            strongAgainst = listOf("Amumu", "Warwick", "Rammus"),
            weakAgainst = listOf("Graves", "Kindred", "Karthus"),
            matchups = listOf(
                MatchupInfo(
                    opponent = "Graves",
                    difficulty = MatchupDifficulty.HARD,
                    tips = listOf(
                        "Graves inflige beaucoup plus de dégâts en duel en jungle",
                        "Évitez les duels directs - vous perdez les échanges",
                        "Jouez autour de vos ganks et de votre impact sur la carte",
                        "Votre impact early est fort mais limité contre Graves en jungle"
                    )
                ),
                MatchupInfo(
                    opponent = "Amumu",
                    difficulty = MatchupDifficulty.EASY,
                    tips = listOf(
                        "Lee Sin domine les duels early contre Amumu",
                        "Invadez son jungle dès le début pour le ralentir",
                        "Votre clear speed est supérieur - prenez tous les objectifs",
                        "Gankez avant qu'Amumu soit puissant (niv 6)"
                    )
                )
            ),
            threat = ThreatInfo(
                threatLevel = 4,
                reason = "Lee Sin est l'un des junglers les plus impactants en early game. Son Dragon's Rage (ultime) peut éjecter un ennemi dans toute son équipe. Son Insec (flash + kick) est l'une des plays les plus dévastatrices du jeu.",
                counterTip = "Lee Sin tombe en late game - survivez à son early impact. Groupez pour éviter d'être Inseced. Des champions comme Janna peuvent le blower back après son ultime. Son énergie limite combien de fois il peut utiliser ses sorts.",
                dangerousFrom = "niveau 3 avec Q+W+E"
            ),
            synergies = listOf(
                SynergyInfo(
                    partner = "Yasuo",
                    description = "Lee Sin kick Insec → Yasuo Last Breath sur la cible éjectée dans l'équipe alliée",
                    combo = "Lee Sin Dragon's Rage sur ennemi VERS équipe alliée → Yasuo Last Breath",
                    strength = SynergyStrength.EXCEPTIONAL
                ),
                SynergyInfo(
                    partner = "Orianna",
                    description = "Lee Sin kick → Orianna ultime sur le groupe d'ennemis rassemblés",
                    combo = "Lee Sin kick dans l'équipe alliée → Orianna balle + ultime",
                    strength = SynergyStrength.HIGH
                )
            ),
            generalTips = listOf(
                "Pratiquez le Ward hop pour une mobilité maximale",
                "L'Insec nécessite: Q touch → Flash → Kick → Team doit follow-up",
                "Votre impact diminue en late game - cherchez l'avantage early",
                "Ward de manière agressive pour créer des opportunités de ganks"
            )
        ),

        "Caitlyn" to ChampionKnowledgeEntry(
            name = "Caitlyn",
            role = "ADC",
            strongAgainst = listOf("Tristana", "Kog'Maw", "Vayne"),
            weakAgainst = listOf("Draven", "MissFortune", "Sivir"),
            matchups = listOf(
                MatchupInfo(
                    opponent = "Draven",
                    difficulty = MatchupDifficulty.HARD,
                    tips = listOf(
                        "Draven vous surpasse en dégâts en early",
                        "Utilisez votre portée supérieure pour harasser à distance",
                        "Placez des pièges pour ralentir ses dash",
                        "Évitez les échanges directs early - jouez sur la portée"
                    )
                ),
                MatchupInfo(
                    opponent = "Tristana",
                    difficulty = MatchupDifficulty.EASY,
                    tips = listOf(
                        "Votre portée est supérieure à celle de Tristana en early",
                        "Placez des pièges sous la tour pour la contrer si elle plonge",
                        "Harassez constamment avec vos AA critiquées",
                        "Gardez votre W net pour l'interrompre si elle saute sur vous"
                    )
                )
            ),
            threat = ThreatInfo(
                threatLevel = 3,
                reason = "Caitlyn a la plus grande portée de toutes les ADC en early/mid game. Ses pièges créent des zones de contrôle uniques. Son tir calibré peut one-shot en late game. Difficile à engage à cause de sa portée.",
                counterTip = "Engagez sur elle avec des champions mobiles. Ses pièges lui servent d'alarme - évitez-les. Son W net peut être bloqué par des minions. Des ADC à plus fort early comme Draven ou MissFortune la dominent.",
                dangerousFrom = "niveau 1 avec sa portée supérieure"
            ),
            synergies = listOf(
                SynergyInfo(
                    partner = "Vi",
                    description = "Vi ultime pin → Caitlyn tir calibré gratuit sur cible piégée",
                    combo = "Vi ultime → Caitlyn piège + tir calibré = kill garanti",
                    strength = SynergyStrength.HIGH
                ),
                SynergyInfo(
                    partner = "Lux",
                    description = "Lux Q liaisons → Caitlyn tir calibré sur cible immobile",
                    combo = "Lux Q → Caitlyn piège + tir calibré + ultime de finition Lux",
                    strength = SynergyStrength.HIGH
                )
            ),
            generalTips = listOf(
                "Placez vos pièges stratégiquement dans les zones de passage",
                "Utilisez votre portée pour dominer le trading en early",
                "Votre tir calibré combo avec le CC de votre support",
                "Pushlez votre avantage de portée before l'ennemi prend des items de range"
            )
        ),

        "Ashe" to ChampionKnowledgeEntry(
            name = "Ashe",
            role = "ADC",
            strongAgainst = listOf("Sivir", "Tristana"),
            weakAgainst = listOf("Lucian", "Draven", "Caitlyn"),
            matchups = listOf(
                MatchupInfo(
                    opponent = "Lucian",
                    difficulty = MatchupDifficulty.HARD,
                    tips = listOf(
                        "Lucian vous domine en mobilité et en burst",
                        "Vos AA ralentissantes sont inefficaces contre ses dash",
                        "Jouez défensivement et demandez des ganks",
                        "Votre force est en late game - survivez"
                    )
                ),
                MatchupInfo(
                    opponent = "Sivir",
                    difficulty = MatchupDifficulty.EASY,
                    tips = listOf(
                        "Votre ralentissement empêche Sivir de vous harasser",
                        "Votre portée est similaire mais vous avez plus de kiting",
                        "Dominez les échanges courts avec votre DPS passif",
                        "Attention à son spell shield qui peut bloquer votre ultime"
                    )
                )
            ),
            threat = ThreatInfo(
                threatLevel = 3,
                reason = "Ashe offre un ultime global de CC unique - sa flèche peut stopper un combat ou sauver un allié depuis n'importe quelle distance. Son ralentissement permanent la rend excellente pour le kiting. Hawk Shot révèle la map.",
                counterTip = "Ashe n'a pas de dash - engagez avec des champions mobiles. Évitez les lignes droites - sa flèche global voyage loin. Achetez Quicksilver Sash si son ultime vous contrarie. Ses AA ralentissent - utilisez des items de vitesse.",
                dangerousFrom = "niveau 6 avec ultime global"
            ),
            synergies = listOf(
                SynergyInfo(
                    partner = "Leona",
                    description = "Ashe ultime global → Leona peut engager immédiatement sur la cible stuned",
                    combo = "Ashe ultime → Leona Zenith Blade + CC → burst",
                    strength = SynergyStrength.HIGH
                )
            ),
            generalTips = listOf(
                "Votre ultime est projectile - visez loin pour des CC surprises",
                "Utilisez Hawk Shot pour vérifier les buissons en sécurité",
                "Kite parfaitement grâce à votre ralentissement passif",
                "En teamfight, maintenez le DPS constant plutôt que de chercher les kills"
            )
        ),

        "Morgana" to ChampionKnowledgeEntry(
            name = "Morgana",
            role = "SUPPORT/MID",
            strongAgainst = listOf("Blitzcrank", "Nautilus", "Thresh"),
            weakAgainst = listOf("Leona", "Pyke", "Vel'Koz"),
            matchups = listOf(
                MatchupInfo(
                    opponent = "Blitzcrank",
                    difficulty = MatchupDifficulty.EASY,
                    tips = listOf(
                        "Votre Black Shield immunise l'ADC contre le hook de Blitzcrank",
                        "Placez le shield proactivement avant qu'il puisse s'approcher",
                        "Votre liaison noire peut le CC pendant qu'il essaie d'approcher",
                        "Vous gagnez cette lane par défaut si vous ne ratez pas vos shields"
                    )
                ),
                MatchupInfo(
                    opponent = "Leona",
                    difficulty = MatchupDifficulty.HARD,
                    tips = listOf(
                        "Leona gagne les échanges si elle engage avant que vous puissiez shield",
                        "Placez votre Black Shield AVANT qu'elle engage, pas après",
                        "Votre liaison peut interrompre son approche",
                        "Achetez des items de résistance physique"
                    )
                )
            ),
            threat = ThreatInfo(
                threatLevel = 3,
                reason = "Morgana protège son équipe avec Black Shield, neutralisant presque toute la CC adverse. Sa liaison noire (Q) est l'un des CC les plus longs du jeu. Son ultime peut stunner toute une équipe en teamfight.",
                counterTip = "Ciblez Morgana en priorité pour l'empêcher de shielder. Ses sorts ont des cooldowns longs. Des champions à CC rapide peuvent la CC avant qu'elle ne place son shield. Sa mobilité est faible.",
                dangerousFrom = "niveau 6 avec son ultime"
            ),
            synergies = listOf(
                SynergyInfo(
                    partner = "Jinx",
                    description = "Morgana liaison → Jinx damage garanti. Black Shield protège Jinx des engage adverses",
                    combo = "Morgana liaison noire → Jinx roquettes + pièges",
                    strength = SynergyStrength.HIGH
                ),
                SynergyInfo(
                    partner = "Jhin",
                    description = "Morgana liaison noire → Jhin 4ème balle critiquée sur cible immobile",
                    combo = "Morgana liaison → Jhin 4ème balle + ultime de finition",
                    strength = SynergyStrength.HIGH
                )
            ),
            generalTips = listOf(
                "Priorisez votre Black Shield sur les carry squishy adverses à CC",
                "Votre Q (liaison) dure 3s - utilisez-la pour des kills garantis",
                "Votre ultime attache les ennemis proches - engagez de manière centrée",
                "Tormented Shadow sous la tour farm bien les waves de sbires"
            )
        ),

        "Syndra" to ChampionKnowledgeEntry(
            name = "Syndra",
            role = "MID",
            strongAgainst = listOf("Ahri", "Zoe", "LeBlanc"),
            weakAgainst = listOf("Yasuo", "Galio", "Kassadin"),
            matchups = listOf(
                MatchupInfo(
                    opponent = "Ahri",
                    difficulty = MatchupDifficulty.EASY,
                    tips = listOf(
                        "Vous outrangez Ahri avec vos boules",
                        "Votre ultime peut la burst avant qu'elle utilise le sien",
                        "Gardez 3 boules sur le terrain pour stunner si elle engage",
                        "Poussez constamment la lane pour la priver de farm"
                    )
                ),
                MatchupInfo(
                    opponent = "Yasuo",
                    difficulty = MatchupDifficulty.HARD,
                    tips = listOf(
                        "Windwall de Yasuo bloque beaucoup de vos sorts",
                        "Votre ultime peut être partiellement bloqué par le Windwall",
                        "Essayez de stunner avant qu'il place son Windwall",
                        "Demandez des ganks - Yasuo est vulnérable aux flanks"
                    )
                )
            ),
            threat = ThreatInfo(
                threatLevel = 4,
                reason = "Syndra peut one-shot la plupart des carries avec son ultime Unleashed Power si elle a suffisamment de boules. Sa portée est excellente et ses boules peuvent stunner quand elle en a 3 sur le terrain. Extrêmement dangereuse en mid/late game.",
                counterTip = "Restez en bonne santé (>70%) pour éviter son one-shot. Achetez des objets de vie. Le Windwall de Yasuo bloque ses sorts. Kassadin la counter naturellement. Engagez quand ses boules sont en cooldown.",
                dangerousFrom = "niveau 9 avec son ultime amélioré"
            ),
            synergies = listOf(
                SynergyInfo(
                    partner = "Thresh",
                    description = "Thresh CC → Syndra peut empiler ses boules sur la cible immobile",
                    combo = "Thresh hook → Syndra Force of Will + ultime",
                    strength = SynergyStrength.HIGH
                )
            ),
            generalTips = listOf(
                "Gérez toujours le placement de vos boules sur le terrain",
                "3 boules = stun avec Scatter the Weak",
                "Votre ultime inflige des dégâts par boule existante - gardez des boules",
                "Poussez agressivement la wave pour roam ou contester l'adversaire"
            )
        ),

        "Zoe" to ChampionKnowledgeEntry(
            name = "Zoe",
            role = "MID",
            strongAgainst = listOf("Katarina", "Kassadin"),
            weakAgainst = listOf("Galio", "LeBlanc", "Zed"),
            matchups = listOf(
                MatchupInfo(
                    opponent = "Zed",
                    difficulty = MatchupDifficulty.HARD,
                    tips = listOf(
                        "Zed peut dodge votre bulle de sommeil avec ses ombres",
                        "Il vous all-in facilement avec sa mobilité",
                        "Achetez Sablier de Zhonya pour survivre son ultime",
                        "Jouez très prudemment et évitez d'être isolée"
                    ),
                    keyItems = listOf("Sablier de Zhonya")
                ),
                MatchupInfo(
                    opponent = "Galio",
                    difficulty = MatchupDifficulty.HARD,
                    tips = listOf(
                        "Galio résiste à vos dégâts magiques",
                        "Son taunt peut vous bloquer pendant que vous castet",
                        "Votre bulle peut endormir des alliés de Galio pendant son ultime",
                        "Roamez souvent plutôt que de piocher contre Galio"
                    )
                )
            ),
            threat = ThreatInfo(
                threatLevel = 4,
                reason = "Zoe peut one-shot avec une bulle de sommeil bien placée suivie d'un Paddle Star. Elle peut voler des actifs d'objets et des sorts de summoner dropped par les ennemis. Sa Sleepy Trouble Bubble peut traverser toute la carte via des portails.",
                counterTip = "Esquivez la bulle en bougeant perpendiculairement. Si vous êtes endormi, vous êtes vulnérable (dégâts triplés au réveil). Des champions à dash peuvent sauter hors de son angle. Zoe est squishy - engagez rapidement sur elle.",
                dangerousFrom = "niveau 3 avec Paddle Star + bulle de sommeil"
            ),
            synergies = listOf(
                SynergyInfo(
                    partner = "Leona",
                    description = "Leona CC → Zoe peut viser sa bulle facilement sur cible immobile",
                    combo = "Leona Solar Flare → Zoe Sleepy Bubble + Paddle Star",
                    strength = SynergyStrength.HIGH
                )
            ),
            generalTips = listOf(
                "Pratiquez vos angles de bulle à travers les murs",
                "Ramassez les drops de sorts de summoner des ennemis - Flash, Ignite",
                "Votre force est de one-shot - ne durez pas en fight prolongé",
                "Portail de sommeil peut traverser certains terrains - apprenez les spots"
            )
        ),

        "Veigar" to ChampionKnowledgeEntry(
            name = "Veigar",
            role = "MID",
            strongAgainst = listOf("Annie", "Lux", "Lissandra"),
            weakAgainst = listOf("Zed", "Talon", "Katarina"),
            matchups = listOf(
                MatchupInfo(
                    opponent = "Zed",
                    difficulty = MatchupDifficulty.HARD,
                    tips = listOf(
                        "Zed peut vous one-shot avant votre combo",
                        "Placez votre cage (E) pour l'emprisonner s'il engage",
                        "Farmez constamment avec votre Q pour stacker la PA",
                        "Achetez Sablier de Zhonya - essentiel pour survivre son ultime"
                    ),
                    keyItems = listOf("Sablier de Zhonya", "Manteau de Mage")
                ),
                MatchupInfo(
                    opponent = "Annie",
                    difficulty = MatchupDifficulty.EASY,
                    tips = listOf(
                        "Votre cage peut l'emprisonner avant qu'elle stune",
                        "Plus vous stackez de PA, plus vos dégâts surpassent les siens",
                        "Évitez d'être stuned - restez hors de portée",
                        "Votre ultime inflige des dégâts proportionnels à leur PA max"
                    )
                )
            ),
            threat = ThreatInfo(
                threatLevel = 4,
                reason = "Veigar est l'un des champions avec les dégâts les plus élevés en fin de partie. Sa PA scale infiniment avec son passif. Son ultime inflige des dégâts proportionnels à la PA maximum de la cible - tuant instantanément d'autres mages. Sa cage peut changer un teamfight.",
                counterTip = "Tuez Veigar tôt et fréquemment pour l'empêcher de stacker. Des assassins AD comme Zed le countered durement. Évitez sa cage (Event Horizon) - restez sur les côtés. Un Veigar <200 PA est gérable, >400 PA est dangereux.",
                dangerousFrom = "niveau 6 avec ultime, mais plus dangereux à chaque stack de PA"
            ),
            synergies = listOf(
                SynergyInfo(
                    partner = "Leona",
                    description = "Leona engage et CC → Veigar peut placer sa cage et son combo en sécurité",
                    combo = "Leona Solar Flare → Veigar cage + Q/W/R combo",
                    strength = SynergyStrength.HIGH
                )
            ),
            generalTips = listOf(
                "Farmez TOUT avec votre Q pour stacker votre PA passive",
                "Votre cage doit piéger les ennemis DANS la zone, pas les bloquer",
                "En late game, votre ultime peut one-shot d'autres mages",
                "Placez-vous en sécurité - vous êtes squishy et sans mobilité"
            )
        ),

        "Annie" to ChampionKnowledgeEntry(
            name = "Annie",
            role = "MID/SUPPORT",
            strongAgainst = listOf("Katarina", "Yasuo", "Malzahar"),
            weakAgainst = listOf("Zed", "Talon", "LeBlanc"),
            matchups = listOf(
                MatchupInfo(
                    opponent = "Yasuo",
                    difficulty = MatchupDifficulty.EASY,
                    tips = listOf(
                        "Tibbers peut stun Yasuo même si Windwall est actif",
                        "Votre stun instantané peut interrompre ses dash",
                        "Stack votre stun avec Q et W avant d'engager",
                        "Les dégâts instantanés d'Annie laissent peu de temps à Yasuo pour réagir"
                    )
                ),
                MatchupInfo(
                    opponent = "Zed",
                    difficulty = MatchupDifficulty.HARD,
                    tips = listOf(
                        "Zed vous all-in si vous vous approchez trop",
                        "Sablier de Zhonya est OBLIGATOIRE - activez-le pendant son ultime",
                        "Restez derrière vos sbires et stakez votre stun à distance",
                        "Votre stun peut l'interrompre s'il s'approche mal"
                    ),
                    keyItems = listOf("Sablier de Zhonya")
                )
            ),
            threat = ThreatInfo(
                threatLevel = 4,
                reason = "Annie a le burst le plus instantané du jeu grâce à son stun automatique. Tibbers combo avec Flash peut one-shot n'importe quel carry. Très dangereuse en teamfight - peut stuner toute une équipe rapprochée. Excellente pour les joueurs débutants ET expérimentés.",
                counterTip = "Comptez ses sorts pour savoir si son stun est chargé (4 sorts). Restez toujours hors de portée de son Flash + stun. Gardez la distance - sa portée est limitée à 625 unités. Des assassins AD la one-shot avant qu'elle stune.",
                dangerousFrom = "niveau 1 si son stun est actif (après 4 sorts castés)"
            ),
            synergies = listOf(
                SynergyInfo(
                    partner = "Blitzcrank",
                    description = "Blitzcrank hook isole → Annie Flash + Tibbers stun + burst",
                    combo = "Blitzcrank hook → Annie Flash Tibbers → burst instantané",
                    strength = SynergyStrength.HIGH
                ),
                SynergyInfo(
                    partner = "Vi",
                    description = "Vi engage → Annie peut utiliser son stun sur la cible Vi-ulted",
                    combo = "Vi ultime → Annie stun + Tibbers = mort instantanée",
                    strength = SynergyStrength.HIGH
                )
            ),
            generalTips = listOf(
                "Farmez avec Q pour reset le cooldown et stacker votre stun",
                "Comptez mentalement vos sorts: 4 sorts = stun chargé",
                "Flash + Tibbers = engage surprise - gardez votre Flash pour cela",
                "Molten Shield (E) peut brûler les ennemis qui vous AA"
            )
        ),

        "Teemo" to ChampionKnowledgeEntry(
            name = "Teemo",
            role = "TOP",
            strongAgainst = listOf("Garen", "Malphite", "Nasus"),
            weakAgainst = listOf("Yorick", "Pantheon", "Darius"),
            matchups = listOf(
                MatchupInfo(
                    opponent = "Garen",
                    difficulty = MatchupDifficulty.EASY,
                    tips = listOf(
                        "Votre attaque empoisonnée (aveuglement) annule les dégâts de son Q",
                        "Votre mobilité et range vous permet d'harceler sans danger",
                        "Il ne peut pas engager efficacement si son Q est aveuglé",
                        "Placez des champignons dans les buissons pour préempter son passage"
                    )
                ),
                MatchupInfo(
                    opponent = "Darius",
                    difficulty = MatchupDifficulty.HARD,
                    tips = listOf(
                        "Darius vous one-shot au corps-à-corps",
                        "Restez TOUJOURS en dehors de sa portée d'accrochage",
                        "Achetez Sablier de Zhonya pour survivre si hooké",
                        "Placez des champignons pour ralentir sa poursuite"
                    )
                ),
                MatchupInfo(
                    opponent = "Malphite",
                    difficulty = MatchupDifficulty.EASY,
                    tips = listOf(
                        "Votre poison passe à travers son bouclier",
                        "Il ne peut pas engager efficacement avec un ultime seul",
                        "Harassez continuellement avec votre Q empoisonné",
                        "Vos champignons ralentissent s'il essaie d'approcher"
                    )
                )
            ),
            threat = ThreatInfo(
                threatLevel = 2,
                reason = "Teemo est un champion harceleur avec un contrôle de carte via ses champignons. Ses champignons révèlent et ralentissent les ennemis, rendant les flanks et les objetifs difficiles à prendre. Son aveuglement est unique contre les champions auto-attackers.",
                counterTip = "Achetez un Oracle Elixir pour voir ses champignons. Achetez une balayette de ward. Des champions à distance peuvent l'harasser hors de sa portée. Ses champignons peuvent être détruits - sweepez les zones d'objectifs avant de prendre Baron/Dragon.",
                dangerousFrom = "niveau 1 avec son empoisonnement permanent"
            ),
            synergies = listOf(
                SynergyInfo(
                    partner = "Singed",
                    description = "Composition de zone contrôle - Teemo champignons + Singed poison = zone intouchable",
                    combo = "Teemo champignons + Singed Fling = ralentissement + dégâts permanents",
                    strength = SynergyStrength.MEDIUM
                )
            ),
            generalTips = listOf(
                "Placez des champignons dans les buissons, les zones d'objectifs et les passages",
                "Votre invisibilité (E) permet des sorties et des attaques surprises",
                "Votre Q aveuglement est votre meilleur outil contre les AA-dealers",
                "Achetez Nashor's Tooth pour maximiser vos dégâts d'empoisonnement"
            )
        )
    )

    fun getMatchupDifficulty(champion: String, opponent: String): MatchupDifficulty {
        val entry = knowledgeBase[champion] ?: return MatchupDifficulty.MEDIUM
        val matchup = entry.matchups.find { it.opponent.equals(opponent, ignoreCase = true) }
        return matchup?.difficulty ?: when {
            entry.strongAgainst.any { it.equals(opponent, ignoreCase = true) } -> MatchupDifficulty.EASY
            entry.weakAgainst.any { it.equals(opponent, ignoreCase = true) } -> MatchupDifficulty.HARD
            else -> MatchupDifficulty.MEDIUM
        }
    }

    fun getThreatInfo(champion: String): ThreatInfo? = (knowledgeBase[champion] ?: defaultChampion(champion)).threat

    fun getSynergies(champion1: String, champion2: String): SynergyInfo? {
        val entry = knowledgeBase[champion1] ?: return null
        return entry.synergies.find { it.partner.equals(champion2, ignoreCase = true) }
    }

    fun getMatchupTips(champion: String, opponent: String): List<String> {
        val entry = knowledgeBase[champion] ?: return emptyList()
        val matchup = entry.matchups.find { it.opponent.equals(opponent, ignoreCase = true) }
        return matchup?.tips ?: entry.generalTips
    }

    fun getKeyItems(champion: String, opponent: String): List<String> {
        val entry = knowledgeBase[champion] ?: return emptyList()
        val matchup = entry.matchups.find { it.opponent.equals(opponent, ignoreCase = true) }
        return matchup?.keyItems ?: emptyList()
    }
}
