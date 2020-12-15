#Conceptions
###0a. Contenu
1. Modifications du code donné
2. Classes et interfaces ajoutées (extensions incluses)
3. Modification des consignes apportées par l'énoncé
4. Liste de toutes les extensions

###0b. Pour une meilleure lisibilité du document
* Les extensions seront précédées par *[extension]*
* Les classes, classes abstraites, interfaces et énumérations seront respectivement précédées 
par *(class)*, *(abstract class)*, *(interface)*, and *(enum)*.

##1. Modifications du code de départ
##### *(abstract class)* Area
*path: java/ch.epfl.cs107.play/game/areagame/**Area.java***
* Ajout de fonctionnalités de pause et de fin de jeu:  
Ajout de deux attributs *paused* et *ended*  
Ajout de deux fonctions *isPaused()* et *hasEnded()* (Getters)  
Modification de *update()*, *suspend()* et *end()* en conséquence  
***
***
##2. Classes et interfaces ajoutées (extensions incluses)
###2.1. *(package)* actor
***
#####2.1.1. *(class)* Gate
* Barrière bloquant le passage du joueur. Destinée à être ouverte avec un signal. (clé ou aire)
####
*path: java/ch.epfl.cs107.play/game/superpacman/actor/**Gate.java***  
***

#####2.1.2. *(interface)* Killable
* *Implementée par Ghost et SuperPacmanPlayer* 
* Ajout personnel afin de représenter la relation "se comporte comme une entité pouvant être tuée"
####
*path: java/ch.epfl.cs107.play/game/superpacman/actor/**Killable.java***  
***

#####2.1.3. *(class)* SuperPacmanPlayer
**Classe du joueur principal**
* *[extension]* Protection animée à sa réapparition
* *[extension]* Son à sa mort
####  
Contient une sous-classe *(class)* **SuperPacmanPlayerHandler** gérant les interactions du joueur
####
*path: java/ch.epfl.cs107.play/game/superpacman/actor/**SuperPacmanPlayer.java***  
***

#####2.1.4. *(class)* SuperPacmanPlayerStatusGUI
**Gère l'affichage de l'interface graphique liée au joueur**
* Conformément à l'énoncé, elle n'est pas définie comme une classe imbriquée du joueur
* Conformément à l'énoncé, son constructeur est défini comme *protected* afin de ne pouvoir être construite uniquement  
par des acteurs
####  
*path: java/ch.epfl.cs107.play/game/superpacman/actor/**SuperPacmanPlayerStatusGUI.java***  
***

#####2.1.5. *[extension]* *(class)* BonusPortal
**Porte permettant d'accéder au niveau bonus**
* Se comporte comme une porte possédant un sprite et ayant pour destination le niveau bonus
####  
*path: java/ch.epfl.cs107.play/game/superpacman/actor/**BonusPortal.java***  
***

#####2.1.6. *[extension]* *(class)* Boss
**Boss du niveau 3**
* Ennemi
####  
*path: java/ch.epfl.cs107.play/game/superpacman/actor/**Boss.java***  
***

#####2.1.7. *[extension]* *(abstract class)* Projectile
**Classe de base d'un projectile**
* Entité se déplaçant à vitesse constante selon une trajectoire rectiligne
####  
*path: java/ch.epfl.cs107.play/game/superpacman/actor/**Projectile.java***  
***

#####2.1.8. *[extension]* *(class)* FireBall
**Projectile lancé par le boss**
* Projectile
####  
*path: java/ch.epfl.cs107.play/game/superpacman/actor/**FireBall.java***  
***

####*(package)* collectable
Ce paquetage permet de mieux structurer le projet

#####2.2.1. *(abstract class)* CollectableReward
*path: java/ch.epfl.cs107.play/game/superpacman/collectable/**CollectableReward.java***  
**Super-classe des Collectable rapportant du score.**  
* Abstraite car elle a pour but d'être instantiée uniquement par l'intermédiaire de ses sous-classes

#####2.2.2. *(class)* Bonus
*path: java/ch.epfl.cs107.play/game/superpacman/collectable/**Bonus.java***  
* Rend le joueur invincible et capable de tuer les fantômes durant 10 secondes

#####2.2.3. *(class)* Cherry
*path: java/ch.epfl.cs107.play/game/superpacman/collectable/**Cherry.java***  
* Rapporte 200 de score

#####2.2.4. *(class)* Diamond
*path: java/ch.epfl.cs107.play/game/superpacman/collectable/**Diamond.java***
* Rapporte 10 de score

#####2.2.5. *(class)* Key
*path: java/ch.epfl.cs107.play/game/superpacman/collectable/**Key.java***  
* Se comporte comme un signal utilisé pour ouvrir des barrières

#####2.2.6. *[extension]* *(class)* Heart
*path: java/ch.epfl.cs107.play/game/superpacman/collectable/**Heart.java***    
* Rapporte un point de vie

###2.3. *(package)* ghost
Ce paquetage permet de définir des méthodes protégées entre les fantômes inaccessibles par les autres acteurs

#####2.3.1. *(abstract class)* Ghost
*path: java/ch.epfl.cs107.play/game/superpacman/ghost/**Ghost.java***  
**Super-classe de tous les fantômes.**  
* Abstraite car elle a pour but d'être instantiée uniquement par l'intermédiaire de ses sous-classes
* *[extension]* Protection animée à leur réapparition
* *[extension]* Son à leur mort

#####2.3.2. *(class)* Blinky
path: java/ch.epfl.cs107.play/game/superpacman/ghost/**Blinky.java**  

#####2.3.3. *(class)* Inky
path: java/ch.epfl.cs107.play/game/superpacman/ghost/**Inky.java**  

#####2.3.4. *(class)* Pinky
path: java/ch.epfl.cs107.play/game/superpacman/ghost/**Pinky.java**  

##3. Modification des consignes apportées par l'énoncé

##4. Liste de toutes les extensions

##### 4a. Acteurs
* *(abstract class)* Projectile
* *(class)* FireBall
* *(class)* Heart
* *(class)* BonusPortal
* *(class)* Boss

##### 4b. Aires et objets liés
* *(class)* Level3
* *(class)* BonusLevel
* *(class)* SuperPacmanMenu

##### 4c. Autres fonctionnalités
* Divers sons
    * Mort du pacman
    * Mort des fantômes
    * Collecte des *(class)* Collectable
    
* Temps d'invincibilité à la réapparition du joueur
    (accompagné d'une animation)
* Temps d'invincibilité à la réapparition des fantômes
    (accompagné d'une animation)
* Augmentation de la vitesse du joueur quand tous les diamants 
d'une aire sont ramassés

Ajout de anti-spawnkill

Ajout item pour ajouter de la vie
Ajout d'un portail avec un sprite pour une téléportation à un level bonus

Ajout d'un son quand un collectable est recup

Ajout son pour eat ghost, death d'un player

(Consigne: modifier vitesse inky quand effrayé,
 Notre projet: modifier vitesse PINKY quand effrayé car plus logique puisqu'il fuit).
 
 (Consigne: scare/unscare ghosts dans le update du jeu, 
 notre projet: scare dans "invincible" et unscare dans "refreshinvincibility" du joueur. Economise de la mémoire)

PACMAN augmente sa vitesse quand l'area est complétée

Ajout de getWindow et getFileSystem pour l'implémentation du menu pause
Ajout de méthode dans area ??

Ajout d'un boss
Ajout d'un level bonus

A faire :
    son bien lancé avec temps d'attente,...
    enlever le diamond sur la case du start du level0 + DU HEART
    ajout d'un level 3
    
    https://www.artstation.com/artwork/9eAJzy --> bow