# LoL Insight - Android App

## Description
Application Android permettant aux joueurs de League of Legends de photographier l'écran de champion select ou de chargement. L'app détecte les 10 champions via ML Kit OCR et affiche des analyses de matchups, menaces, synergies et conseils.

## Architecture
- **Pattern**: Clean Architecture (Domain / Data / Presentation)
- **DI**: Hilt
- **UI**: Jetpack Compose + Material3 (dark theme LoL)
- **DB**: Room
- **Network**: Retrofit2 + OkHttp3
- **OCR**: ML Kit Text Recognition
- **Camera**: CameraX

## Build
```bash
./gradlew assembleDebug
```

## Package principal
`com.lolinsight`

## Fonctionnalités
1. Prise de photo ou import depuis galerie
2. Détection OCR des noms de champions
3. Analyse de composition (matchups, menaces, synergies)
4. Historique des analyses

## Données
La base de connaissances se trouve dans `ChampionKnowledge.kt` et contient les données de matchup pour ~20 champions courants.
