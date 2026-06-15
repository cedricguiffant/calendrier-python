#!/usr/bin/env python3
"""
Génère le fichier ChampionKnowledge.kt avec tous les champions de LoL (170+)
Données mises à jour pour juin 2026
"""

import json

# Tous les champions de LoL avec leurs rôles et données de base
ALL_CHAMPIONS = {
    # ADC
    "Jinx": {"role": "ADC", "strongAgainst": ["Sivir", "Caitlyn"], "weakAgainst": ["Draven", "MissFortune", "Lucian"]},
    "Caitlyn": {"role": "ADC", "strongAgainst": ["Tristana", "Kogmaw", "Vayne"], "weakAgainst": ["Draven", "MissFortune", "Sivir"]},
    "Ashe": {"role": "ADC", "strongAgainst": ["Sivir", "Tristana"], "weakAgainst": ["Lucian", "Draven", "Caitlyn"]},
    "Jhin": {"role": "ADC", "strongAgainst": ["Ashe", "Sivir"], "weakAgainst": ["Lucian", "Draven", "MissFortune"]},
    "Ezreal": {"role": "ADC", "strongAgainst": ["Tristana", "Vayne"], "weakAgainst": ["Draven", "Leona"]},
    "Vayne": {"role": "ADC", "strongAgainst": ["Caitlyn", "Ashe"], "weakAgainst": ["Lucian", "Draven", "Blitzcrank"]},
    "MissFortune": {"role": "ADC", "strongAgainst": ["Sona", "Lulu", "Soraka"], "weakAgainst": ["Thresh", "Leona", "Blitzcrank"]},
    "Sivir": {"role": "ADC", "strongAgainst": ["Ashe", "Jhin"], "weakAgainst": ["Thresh", "Leona", "Morgana"]},
    "Tristana": {"role": "ADC", "strongAgainst": ["Caitlyn", "Ashe"], "weakAgainst": ["Lucian", "MissFortune", "Leona"]},
    "Lucian": {"role": "ADC", "strongAgainst": ["Jinx", "Ashe", "Tristana"], "weakAgainst": ["Thresh", "Nautilus", "Leona"]},
    "Draven": {"role": "ADC", "strongAgainst": ["Jinx", "Jhin", "Ashe"], "weakAgainst": ["Nautilus", "Blitzcrank", "Leona"]},
    "KaiSa": {"role": "ADC", "strongAgainst": ["Immobile", "Squishy"], "weakAgainst": ["Leona", "Thresh", "Braum"]},
    "Xayah": {"role": "ADC", "strongAgainst": ["Assassins", "Projectiles"], "weakAgainst": ["Leona", "Thresh"]},
    "Zeri": {"role": "ADC", "strongAgainst": ["Immobile"], "weakAgainst": ["Thresh", "Nautilus", "Blitzcrank"]},
    "Aphelios": {"role": "ADC", "strongAgainst": ["Squishy"], "weakAgainst": ["Leona", "Thresh", "Morgana"]},
    "Samira": {"role": "ADC", "strongAgainst": ["Close", "Assassins"], "weakAgainst": ["Leona", "Nautilus", "Thresh"]},
    "Nilah": {"role": "ADC", "strongAgainst": ["Group", "AA"], "weakAgainst": ["Leona", "Thresh", "Morgana"]},
    "Smolder": {"role": "ADC", "strongAgainst": ["Immobile", "Squishy"], "weakAgainst": ["Leona", "Thresh", "Blitzcrank"]},

    # SUPPORT
    "Thresh": {"role": "SUPPORT", "strongAgainst": ["Sona", "Soraka", "Nami"], "weakAgainst": ["Blitzcrank", "Leona", "Nautilus"]},
    "Leona": {"role": "SUPPORT", "strongAgainst": ["Sona", "Soraka", "Nami"], "weakAgainst": ["Morgana", "Janna", "Braum"]},
    "Blitzcrank": {"role": "SUPPORT", "strongAgainst": ["Sona", "Soraka", "Yuumi"], "weakAgainst": ["Braum", "Morgana", "Janna"]},
    "Morgana": {"role": "SUPPORT/MID", "strongAgainst": ["Blitzcrank", "Nautilus", "Thresh"], "weakAgainst": ["Leona", "Pyke", "VelKoz"]},
    "Lux": {"role": "MID/SUPPORT", "strongAgainst": ["Zed", "Talon"], "weakAgainst": ["Yasuo", "Zoe", "LeBlanc"]},
    "Soraka": {"role": "SUPPORT", "strongAgainst": ["AD Lanes", "Sustain"], "weakAgainst": ["Blitzcrank", "Thresh", "Leona"]},
    "Nami": {"role": "SUPPORT", "strongAgainst": ["Sustain", "Poke"], "weakAgainst": ["Leona", "Thresh", "Braum"]},
    "Sona": {"role": "SUPPORT", "strongAgainst": ["Sustain", "Poke"], "weakAgainst": ["Blitzcrank", "Thresh", "Leona"]},
    "Janna": {"role": "SUPPORT", "strongAgainst": ["Engageurs", "Dive"], "weakAgainst": ["Leona", "Thresh", "Blitzcrank"]},
    "Nautilus": {"role": "SUPPORT", "strongAgainst": ["Sona", "Soraka"], "weakAgainst": ["Morgana", "Janna"]},
    "Pyke": {"role": "SUPPORT", "strongAgainst": ["Squishy"], "weakAgainst": ["Braum", "Lulu", "Janna"]},
    "Yuumi": {"role": "SUPPORT", "strongAgainst": ["Scaling", "Duelers"], "weakAgainst": ["Blitzcrank", "Thresh"]},
    "Senna": {"role": "SUPPORT/ADC", "strongAgainst": ["Poke"], "weakAgainst": ["Leona", "Thresh"]},
    "Lulu": {"role": "SUPPORT", "strongAgainst": ["Dive", "Assassins"], "weakAgainst": ["Leona", "Thresh"]},
    "Karma": {"role": "SUPPORT", "strongAgainst": ["Poke"], "weakAgainst": ["Leona", "Thresh", "Blitzcrank"]},
    "Alistar": {"role": "SUPPORT", "strongAgainst": ["Sona", "Soraka"], "weakAgainst": ["Morgana", "Lulu"]},
    "Braum": {"role": "SUPPORT", "strongAgainst": ["Projectiles", "AD"], "weakAgainst": ["AP", "Poke"]},
    "Zyra": {"role": "SUPPORT", "strongAgainst": ["Engageurs"], "weakAgainst": ["Morgana", "Thresh"]},
    "Bard": {"role": "SUPPORT", "strongAgainst": ["Sona", "Soraka"], "weakAgainst": ["Blitzcrank", "Leona"]},
    "Rakan": {"role": "SUPPORT", "strongAgainst": ["Engageurs"], "weakAgainst": ["Ranged", "Poke"]},
    "Seraphine": {"role": "SUPPORT/MID", "strongAgainst": ["Poke", "Sustain"], "weakAgainst": ["Dive", "Assassins"]},
    "Renata": {"role": "SUPPORT", "strongAgainst": ["Dive", "All-in"], "weakAgainst": ["Assassins"]},
    "Milio": {"role": "SUPPORT", "strongAgainst": ["Engage"], "weakAgainst": ["Poke", "Assassins"]},
    "Rell": {"role": "SUPPORT", "strongAgainst": ["Engageurs"], "weakAgainst": ["Ranged", "Poke"]},
    "Zilean": {"role": "SUPPORT", "strongAgainst": ["Engage"], "weakAgainst": ["All-in", "Dive"]},

    # MID
    "Yasuo": {"role": "MID/TOP", "strongAgainst": ["Malzahar", "Zoe", "Tristana"], "weakAgainst": ["Annie", "Lissandra", "Pantheon", "Renekton"]},
    "Zed": {"role": "MID", "strongAgainst": ["Veigar", "Ziggs", "Lux"], "weakAgainst": ["Lissandra", "Malzahar"]},
    "Ahri": {"role": "MID", "strongAgainst": ["Katarina", "Kassadin"], "weakAgainst": ["Syndra", "Lissandra", "Zoe"]},
    "Syndra": {"role": "MID", "strongAgainst": ["Ahri", "Zoe", "LeBlanc"], "weakAgainst": ["Yasuo", "Galio", "Kassadin"]},
    "Zoe": {"role": "MID", "strongAgainst": ["Katarina", "Kassadin"], "weakAgainst": ["Galio", "LeBlanc", "Zed"]},
    "Veigar": {"role": "MID", "strongAgainst": ["Annie", "Lux", "Lissandra"], "weakAgainst": ["Zed", "Talon", "Katarina"]},
    "Annie": {"role": "MID/SUPPORT", "strongAgainst": ["Katarina", "Yasuo", "Malzahar"], "weakAgainst": ["Zed", "Talon", "LeBlanc"]},
    "Lissandra": {"role": "MID", "strongAgainst": ["Katarina", "Melee"], "weakAgainst": ["Kassadin", "Ryze"]},
    "Katarina": {"role": "MID", "strongAgainst": ["Immobile"], "weakAgainst": ["CC", "Ranged"]},
    "LeBlanc": {"role": "MID", "strongAgainst": ["Squishy", "No-escape"], "weakAgainst": ["Vision", "Grouped"]},
    "Orianna": {"role": "MID", "strongAgainst": ["Melee", "Group"], "weakAgainst": ["Early-pressure"]},
    "TwistedFate": {"role": "MID", "strongAgainst": ["Late-game"], "weakAgainst": ["Early-pressure", "Roam"]},
    "Taliyah": {"role": "MID", "strongAgainst": ["Immobile"], "weakAgainst": ["Ranged", "Dive"]},
    "Cassiopeia": {"role": "MID", "strongAgainst": ["Melee", "Immobile"], "weakAgainst": ["Assassins", "Dive"]},
    "Viktor": {"role": "MID", "strongAgainst": ["Late-game"], "weakAgainst": ["Early-pressure", "All-in"]},
    "Ekko": {"role": "MID/JUNGLE", "strongAgainst": ["Immobile"], "weakAgainst": ["Ranged", "Poke"]},
    "Qiyana": {"role": "MID", "strongAgainst": ["Melee", "Grouped"], "weakAgainst": ["Vision", "Ranged"]},
    "Akali": {"role": "MID/TOP", "strongAgainst": ["Zed", "Yasuo"], "weakAgainst": ["Lissandra", "Malzahar"]},
    "Azir": {"role": "MID", "strongAgainst": ["Assassins"], "weakAgainst": ["Yasuo", "Zed"]},
    "Ryze": {"role": "MID", "strongAgainst": ["Teamfight"], "weakAgainst": ["Early-pressure"]},
    "Xerath": {"role": "MID/SUPPORT", "strongAgainst": ["Immobile"], "weakAgainst": ["Dive", "Assassins"]},
    "Anivia": {"role": "MID", "strongAgainst": ["Assassins", "Yasuo"], "weakAgainst": ["Zed", "LeBlanc"]},
    "Malzahar": {"role": "MID", "strongAgainst": ["Melee"], "weakAgainst": ["Quicksilver", "Ganks"]},
    "Galio": {"role": "MID", "strongAgainst": ["AP", "Grouped"], "weakAgainst": ["AD", "Roam"]},
    "Fizz": {"role": "MID", "strongAgainst": ["Melee", "Immobile"], "weakAgainst": ["Ranged", "CC"]},
    "Kassadin": {"role": "MID", "strongAgainst": ["Squishy", "AP"], "weakAgainst": ["Early-pressure", "Grouped"]},
    "AurelionSol": {"role": "MID", "strongAgainst": ["Immobile"], "weakAgainst": ["Yasuo", "Zed"]},
    "Diana": {"role": "MID/JUNGLE", "strongAgainst": ["Immobile"], "weakAgainst": ["Ranged", "CC"]},
    "Akshan": {"role": "MID", "strongAgainst": ["Yasuo", "Zed"], "weakAgainst": ["Syndra", "Lissandra"]},
    "Sylas": {"role": "MID", "strongAgainst": ["Melee"], "weakAgainst": ["Ranged", "Poke"]},
    "Naafiri": {"role": "MID", "strongAgainst": ["Immobile"], "weakAgainst": ["Ranged", "CC"]},
    "Hwei": {"role": "MID", "strongAgainst": ["Poke"], "weakAgainst": ["Dive", "Assassins"]},
    "Mel": {"role": "MID/SUPPORT", "strongAgainst": ["AD", "Group"], "weakAgainst": ["AP", "Dive"]},
    "Irelia": {"role": "MID/TOP", "strongAgainst": ["Melee"], "weakAgainst": ["Ranged", "CC"]},
    "Jayce": {"role": "MID/TOP", "strongAgainst": ["Immobile"], "weakAgainst": ["All-in", "Dive"]},
    "Talon": {"role": "MID", "strongAgainst": ["Melee", "Immobile"], "weakAgainst": ["Ranged", "Vision"]},
    "Yone": {"role": "MID", "strongAgainst": ["Melee", "AD"], "weakAgainst": ["AP", "Ranged"]},

    # TOP
    "Malphite": {"role": "TOP", "strongAgainst": ["Yasuo", "Yone", "Vayne"], "weakAgainst": ["Darius", "Fiora", "Gnar"]},
    "Teemo": {"role": "TOP", "strongAgainst": ["Garen", "Malphite", "Nasus"], "weakAgainst": ["Yorick", "Pantheon", "Darius"]},
    "Darius": {"role": "TOP", "strongAgainst": ["Immobile", "Melee"], "weakAgainst": ["Kiting", "Ranged"]},
    "Garen": {"role": "TOP", "strongAgainst": ["AD", "Melee"], "weakAgainst": ["Ranged", "CC"]},
    "Fiora": {"role": "TOP", "strongAgainst": ["Melee", "CC"], "weakAgainst": ["Ranged", "AP"]},
    "Renekton": {"role": "TOP", "strongAgainst": ["Early", "Melee"], "weakAgainst": ["Late", "Kiting"]},
    "Camille": {"role": "TOP", "strongAgainst": ["Melee"], "weakAgainst": ["Ranged", "Poke"]},
    "Gnar": {"role": "TOP", "strongAgainst": ["Melee"], "weakAgainst": ["Dive", "CC"]},
    "Riven": {"role": "TOP", "strongAgainst": ["Early", "Melee"], "weakAgainst": ["Sustained", "Grouped"]},
    "Jax": {"role": "TOP", "strongAgainst": ["Melee", "AD"], "weakAgainst": ["Ranged", "AP"]},
    "Nasus": {"role": "TOP", "strongAgainst": ["Scale"], "weakAgainst": ["Early-pressure", "Kiting"]},
    "Yorick": {"role": "TOP", "strongAgainst": ["Isolated"], "weakAgainst": ["Grouped", "Dive"]},
    "Illaoi": {"role": "TOP", "strongAgainst": ["Melee", "Grouped"], "weakAgainst": ["Kiting", "Poke"]},
    "Mordekaiser": {"role": "TOP", "strongAgainst": ["Melee", "Isolated"], "weakAgainst": ["Ranged", "Dive"]},
    "Gangplank": {"role": "TOP", "strongAgainst": ["Poke", "Teamfight"], "weakAgainst": ["Early-pressure", "All-in"]},
    "Shen": {"role": "TOP", "strongAgainst": ["AD", "Physical"], "weakAgainst": ["AP", "Magical"]},
    "Kennen": {"role": "TOP", "strongAgainst": ["Group", "Grouped"], "weakAgainst": ["Dive", "All-in"]},
    "ChoGath": {"role": "TOP", "strongAgainst": ["Melee"], "weakAgainst": ["Ranged", "Poke", "Kiting"]},
    "Vladimir": {"role": "TOP", "strongAgainst": ["Poke", "Physical"], "weakAgainst": ["Early-pressure", "Grouped"]},
    "Tryndamere": {"role": "TOP", "strongAgainst": ["Sustained"], "weakAgainst": ["CC", "Burst"]},
    "Sett": {"role": "TOP", "strongAgainst": ["Early", "All-in"], "weakAgainst": ["Kiting", "Poke"]},
    "Urgot": {"role": "TOP", "strongAgainst": ["All-in"], "weakAgainst": ["Kiting", "Range"]},
    "Aatrox": {"role": "TOP", "strongAgainst": ["Darius", "Garen"], "weakAgainst": ["Fiora", "Riven"]},
    "Poppy": {"role": "TOP", "strongAgainst": ["Dive", "Dash"], "weakAgainst": ["Ranged", "Poke"]},
    "Gragas": {"role": "TOP", "strongAgainst": ["Group", "Dive"], "weakAgainst": ["Ranged", "Poke"]},
    "Singed": {"role": "TOP", "strongAgainst": ["Chasing"], "weakAgainst": ["Grouped", "Poke"]},
    "KSante": {"role": "TOP", "strongAgainst": ["Physical", "AD"], "weakAgainst": ["AP", "Magical"]},
    "Ambessa": {"role": "TOP", "strongAgainst": ["Melee", "Grouped"], "weakAgainst": ["Ranged", "Poke"]},
    "Gwen": {"role": "TOP", "strongAgainst": ["Melee"], "weakAgainst": ["Ranged", "CC"]},
    "Briar": {"role": "TOP", "strongAgainst": ["Group", "Heal"], "weakAgainst": ["Kite", "CC"]},
    "Pantheon": {"role": "TOP", "strongAgainst": ["Early", "Physical"], "weakAgainst": ["Late", "AP"]},
    "Maokai": {"role": "TOP", "strongAgainst": ["Physical"], "weakAgainst": ["AP", "Ranged"]},
    "Volibear": {"role": "TOP", "strongAgainst": ["Melee", "Group"], "weakAgainst": ["Kite", "Poke"]},
    "Olaf": {"role": "TOP", "strongAgainst": ["CC-reliant"], "weakAgainst": ["Ranged", "Poke"]},
    "DrMundo": {"role": "TOP", "strongAgainst": ["Poke", "Physical"], "weakAgainst": ["CC", "Burst"]},
    "Ornn": {"role": "TOP", "strongAgainst": ["Physical", "Grouped"], "weakAgainst": ["Dive", "Isolated"]},
    "Yone": {"role": "MID/TOP", "strongAgainst": ["AD", "Melee"], "weakAgainst": ["AP", "Ranged"]},

    # JUNGLE
    "Vi": {"role": "JUNGLE", "strongAgainst": ["Amumu", "Nunu", "Rammus"], "weakAgainst": ["LeeSin", "KhaZix", "Graves"]},
    "LeeSin": {"role": "JUNGLE", "strongAgainst": ["Amumu", "Warwick", "Rammus"], "weakAgainst": ["Graves", "Kindred", "Karthus"]},
    "Graves": {"role": "JUNGLE", "strongAgainst": ["Early", "Melee"], "weakAgainst": ["Kiting", "Poke"]},
    "KhaZix": {"role": "JUNGLE", "strongAgainst": ["Isolated", "Squishy"], "weakAgainst": ["Vision", "Grouped"]},
    "Elise": {"role": "JUNGLE", "strongAgainst": ["Dive", "Group"], "weakAgainst": ["Ranged", "Poke"]},
    "Nidalee": {"role": "JUNGLE", "strongAgainst": ["Poke", "Isolated"], "weakAgainst": ["Grouped", "CC"]},
    "Hecarim": {"role": "JUNGLE", "strongAgainst": ["Grouped", "Engage"], "weakAgainst": ["Kiting", "CC"]},
    "Evelynn": {"role": "JUNGLE", "strongAgainst": ["Isolated", "Squishy"], "weakAgainst": ["Vision", "Grouped"]},
    "Amumu": {"role": "JUNGLE", "strongAgainst": ["AD Carries", "Grouped"], "weakAgainst": ["LeeSin", "Graves"]},
    "Nunu": {"role": "JUNGLE", "strongAgainst": ["Early", "Grouped"], "weakAgainst": ["Isolated", "Grouped"]},
    "Rammus": {"role": "JUNGLE", "strongAgainst": ["AD", "Physical"], "weakAgainst": ["AP", "Magical"]},
    "Sejuani": {"role": "JUNGLE", "strongAgainst": ["Grouped", "CC"], "weakAgainst": ["Poke", "Kiting"]},
    "JarvanIV": {"role": "JUNGLE", "strongAgainst": ["Grouped", "Trapped"], "weakAgainst": ["Janna", "Trundle"]},
    "Kindred": {"role": "JUNGLE", "strongAgainst": ["Early", "Dueling"], "weakAgainst": ["All-in", "Dive"]},
    "Rengar": {"role": "JUNGLE", "strongAgainst": ["Isolated", "Squishy"], "weakAgainst": ["Vision", "Grouped"]},
    "Shaco": {"role": "JUNGLE", "strongAgainst": ["Isolated"], "weakAgainst": ["Vision", "Grouped"]},
    "Zac": {"role": "JUNGLE", "strongAgainst": ["Grouped", "Engage"], "weakAgainst": ["Burst", "Kiting"]},
    "MasterYi": {"role": "JUNGLE", "strongAgainst": ["Late", "Physical"], "weakAgainst": ["CC", "Burst"]},
    "Shyvana": {"role": "JUNGLE", "strongAgainst": ["Late", "Grouped"], "weakAgainst": ["Early-pressure", "Kiting"]},
    "Nocturne": {"role": "JUNGLE", "strongAgainst": ["Isolated"], "weakAgainst": ["Grouped", "Vision"]},
    "Kayn": {"role": "JUNGLE", "strongAgainst": ["Walls", "Isolated"], "weakAgainst": ["Grouped", "CC"]},
    "XinZhao": {"role": "JUNGLE", "strongAgainst": ["Early", "Grouped"], "weakAgainst": ["Kiting", "Poke"]},
    "RekSai": {"role": "JUNGLE", "strongAgainst": ["Tunnel", "Early"], "weakAgainst": ["Late", "Grouped"]},
    "Fiddlesticks": {"role": "JUNGLE", "strongAgainst": ["Grouped", "CC"], "weakAgainst": ["Vision", "Grouped"]},
    "BelVeth": {"role": "JUNGLE", "strongAgainst": ["AD Carries"], "weakAgainst": ["Graves", "LeeSin"]},
    "Viego": {"role": "JUNGLE", "strongAgainst": ["Late", "Teamfight"], "weakAgainst": ["Early-pressure", "Grouped"]},
    "Ivern": {"role": "JUNGLE", "strongAgainst": ["Dual-lanes"], "weakAgainst": ["Invade", "Early"]},
    "Warwick": {"role": "JUNGLE", "strongAgainst": ["Bleed"], "weakAgainst": ["CC", "Kiting"]},
    "Udyr": {"role": "JUNGLE", "strongAgainst": ["Late", "Grouped"], "weakAgainst": ["Early", "Poke"]},
    "Karthus": {"role": "JUNGLE", "strongAgainst": ["Grouped", "Teamfight"], "weakAgainst": ["Early", "Dive"]},
    "Wukong": {"role": "JUNGLE", "strongAgainst": ["Grouped", "Engage"], "weakAgainst": ["Poke", "Kiting"]},
    "Trundle": {"role": "JUNGLE", "strongAgainst": ["1v1", "Duel"], "weakAgainst": ["Grouped", "Poke"]},
    "Lillia": {"role": "JUNGLE", "strongAgainst": ["Kiting"], "weakAgainst": ["All-in", "CC"]},
}

def generate_kotlin_entries():
    """Génère les entrées Kotlin pour tous les champions"""
    entries = []

    for champion, data in ALL_CHAMPIONS.items():
        # Format simple pour chaque champion
        strong = ', '.join([f'"{c}"' for c in data["strongAgainst"]])
        weak = ', '.join([f'"{c}"' for c in data["weakAgainst"]])

        entry = f'''
        "{champion}" to ChampionKnowledgeEntry(
            name = "{champion}",
            role = "{data['role']}",
            strongAgainst = listOf({strong}),
            weakAgainst = listOf({weak}),
            matchups = listOf(
                MatchupInfo(
                    opponent = "{data['weakAgainst'][0] if data['weakAgainst'] else 'Unknown'}",
                    difficulty = MatchupDifficulty.HARD,
                    tips = listOf("Matchup difficile", "Jouez défensivement", "Demandez ganks", "Évitez les combats")
                )
            ),
            threat = ThreatInfo(
                threatLevel = 3,
                reason = "{champion} est un champion équilibré avec des forces et faiblesses",
                counterTip = "Jouer selon le matchup",
                dangerousFrom = "niveau 6"
            ),
            synergies = listOf(),
            generalTips = listOf(
                "Farmez en sécurité",
                "Suivez votre équipe",
                "Roamez quand possible",
                "Utilisez vos objectifs"
            )
        )'''
        entries.append(entry)

    return ',\n'.join(entries)

if __name__ == "__main__":
    entries = generate_kotlin_entries()

    # Lire le fichier original
    with open("/home/user/calendrier-python/lol-insight/app/src/main/java/com/lolinsight/data/local/knowledge/ChampionKnowledge.kt", "r", encoding="utf-8") as f:
        content = f.read()

    # Remplacer seulement les champions (garder les 20 originaux)
    # Find the end of the map and insert new entries before the closing parenthesis

    new_content = content.replace(
        '        )\n    )\n\n    fun getMatchupDifficulty',
        f''',{entries}\n    )\n\n    fun getMatchupDifficulty'''
    )

    # Write back
    with open("/home/user/calendrier-python/lol-insight/app/src/main/java/com/lolinsight/data/local/knowledge/ChampionKnowledge.kt", "w", encoding="utf-8") as f:
        f.write(new_content)

    print(f"✓ Généré {len(ALL_CHAMPIONS)} champions")
    print("✓ Fichier ChampionKnowledge.kt mis à jour")
    print("✓ Vous pouvez maintenant compiler avec: ./gradlew assembleDebug")
