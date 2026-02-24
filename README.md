# KEYSCAPE 🗝️

KEYSCAPE est un jeu de plateforme 2D réalisé en Java avec le framework **LibGDX**.  
Le joueur peut choisir son niveau, chacun associé au monde de l’un de nous trois :

- Miami Beach
- Gotham City
- Girly

L'objectif du jeu est de délivrer notre professeur Adrien, coincé dans une tour.  
Pour le libérer, il faut terminer les 3 niveaux et récupérer une clé par niveau.

Le projet a été réalisé dans le cadre d’un projet scolaire.

---

# Fonctionnalités principales

- Moteur de jeu 2D avec LibGDX
- Gestion d'un monde de tuiles (tilemap) créé avec Tiled
- 3 niveaux, 3 maps différentes, chacune associée à un personnage
- Écran d’accueil + écran des règles du jeu
- Sélection de personnage via un écran dédié
- Menu pause du jeu : Continuer / Recommencer / Retour menu**
- Collisions complètes
- Éviter les ennemis
- Tuer les ennemis en sautant dessus, ou ramasser les pièces qu’ils laissent

---

# Technologies utilisées

- Langage :Java
- Framework : LibGDX
- Outil map 2D : Tiled
- Build : Gradle (gradlew)

---
# TRAILER 

Visionner le trailer du jeu sur youtube monté par mes soins : https://www.youtube.com/watch?v=kNmQR2jqxBY

# Installation et lancement

##Option 1 : 
#Télécharger le jeu
Rendez-vous dans l’onglet Releases du repository
Téléchargez le fichier Keyscape.jar

🔧 Prérequis
Java 17 ou supérieur
Vérifier votre version :
```
java -version
```
Si nécessaire, installer Java :
https://adoptium.net/

▶️ Lancer le jeu
Dans le dossier contenant Keyscape.jar :
```
java -jar Keyscape.jar
```
##Option 2 :
# Cloner et lancer le projet :

```bash
git clone <URL_REPO>
cd KeyScape
./gradlew lwjgl3:run
```

Commandes du jeu :
Q / D : Gauche / Droite
Espace : Saut
Double espace : Double saut
Échap : Ouvrir le menu pause
Cliquer sur l’icône 3 points en haut à gauche pour ouvrir le menu pause
Dans le menu pause :
Continuer : Reprendre la partie
Recommencer : Relancer le niveau en cours
Retour menu : Revenir à l’écran de sélection des personnages


Projet réalisé dans le cadre du module T-JAV-501 à EPITECH
