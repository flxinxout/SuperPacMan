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

### *(main class)* SuperPacman
*path: ch.epfl.cs107.play.game.superpacman.**SuperPacman.java*** 
* Classe principale du jeu pacman.
***

###2.0. *(package)* areagame.actor

#### *(interface)* Collectable
*path: ch.epfl.cs107.play.game.areagame.actor.**Collectable.java*** 
* Représente un objet qui peut être collecté.

#### *(abstract class)* CollectableAreaEntity
*path: ch.epfl.cs107.play.game.areagame.actor.**CollectableAreaEntity.java*** 
* Représente un objet qui peut être collecté avec divers attributs.
***

###2.1. *(package)* actor
Ce paquetage permet de mieux structurer le projet.


##### *(interface)* Killable
*path: ch.epfl.cs107.play.game.superpacman.actor.**Killable.java***  
* *Implementée par Ghost et SuperPacmanPlayer* 
* Ajout personnel afin de représenter la relation "se comporte comme une entité pouvant être tuée".
####

##### *(class)* SuperPacmanPlayer
*path: ch.epfl.cs107.play.game.superpacman.actor.**SuperPacmanPlayer.java***  
* Classe du joueur principal
* *[extension]* Protection animée à sa réapparition.
* *[extension]* Son à sa mort.
* Contient une sous-classe *(class)* **SuperPacmanPlayerHandler** gérant les interactions du joueur.
####

##### *(class)* SuperPacmanPlayerStatusGUI
*path: java/ch.epfl.cs107.play/game/superpacman/actor/**SuperPacmanPlayerStatusGUI.java*** 
* Gère l'affichage de l'interface graphique liée au joueur
* Conformément à l'énoncé, elle n'est pas définie comme une classe imbriquée du joueur.
* Conformément à l'énoncé, son constructeur est défini comme *protected* afin de ne pouvoir être construite uniquement  
par des acteurs.
####   
***

####2.1.1. *(package)* collectable
Ce paquetage permet de mieux structurer le projet. Contient les classes qui sont considérées comme des collectable
***

#####2.1.1.1. *(abstract class)* CollectableReward
*path: ch.epfl.cs107.play.game.superpacman.actor.collectable.**CollectableReward.java***  
**Super-classe des Collectable rapportant du score.**  
* Abstraite car elle a pour but d'être instantiée uniquement par l'intermédiaire de ses sous-classes ayant un reward attribué.
***

#####2.1.1.2. *(class)* Bonus
*path: ch.epfl.cs107.play.game.superpacman.actor.collectable.**Bonus.java***  
* Rend le joueur invincible et capable de tuer les fantômes durant 10 secondes.
***

#####2.1.1.3. *(class)* Cherry
*path: ch.epfl.cs107.play.game.superpacman.actor.collectable.**Cherry.java***  
* Rapporte 200 de score.
***

#####2.1.1.4. *(class)* Diamond
*path: ch.epfl.cs107.play.game.superpacman.actor.collectable.**Diamond.java***
* Rapporte 10 de score.
***

#####2.1.1.5. *(class)* Key
*path: ch.epfl.cs107.play.game.superpacman.actor.collectable.**Key.java***  
* Se comporte comme un signal utilisé pour ouvrir des barrières.
***

#####2.1.1.6. *[extension]* *(class)* Heart
*path: ch.epfl.cs107.play.game.superpacman.actor.collectable.**Heart.java***    
* Rapporte un point de vie.
***

#####2.1.1.7. *[extension]* *(class)* BossLife
*path: ch.epfl.cs107.play.game.superpacman.actor.collectable.**BossLife.java***    
* Représente une vie d'un boss. Quand tous les BossLife sont récuperés, le jeu se fini.
***

####2.1.2. *(package)* ennemy
Ce paquetage permet de mieux structurer le projet.
***

#####2.1.2.1. *[extension]* *(class)* Arrow
*path: ch.epfl.cs107.play.game.superpacman.actor.ennemy.**Arrow.java***
* Représente une flèche qui tue le pacman s'îl intéragit avec elle.
***

#####2.1.2.2 *[extension]* *(class)* Boss
*path: ch.epfl.cs107.play.game.superpacman.actor.ennemy.**Boss.java***  
**Boss du niveau 3**
* Représente le boss final du jeu, lorsque le pacman récupère toutes le BossLife, le jeu se fini.
***

#####2.1.2.3 *[extension]* *(class)* Bow
*path: ch.epfl.cs107.play.game.superpacman.actor.ennemy.**Bow.java***  
* Représente un arc qui tire sur le pacman s'il est dans sa ligne de mire.
***

#####2.1.2.4 *[extension]* *(class)* Fire
*path: ch.epfl.cs107.play.game.superpacman.actor.ennemy.**Fire.java***
* Représente du feu qui est laissé sur le passage du boss, pendant 5 secondes.
***

#####2.1.2.5 *[extension]* *(abstract class)* Projectile
*path: ch.epfl.cs107.play.game.superpacman.actor.ennemy.**Projectile.java***  
**Super-classe de tous les projectiles.** 
* Entité se déplaçant à vitesse constante selon une trajectoire rectiligne
* Abstraite car elle a pour but d'être instantiée uniquement par l'intermédiaire de ses sous-classes projectiles.
***

####2.1.3. *(package)* ghost
Ce paquetage permet de définir des méthodes protégées entre les fantômes inaccessibles par les autres acteurs.
***

#####2.1.3.1 *(abstract class)* Ghost
*path: ch.epfl.cs107.play.game.superpacman.actor.ghost.**Ghost.java***  
**Super-classe de tous les fantômes.**  
* Abstraite car elle a pour but d'être instantiée uniquement par l'intermédiaire de ses sous-classes.
* *[extension]* Protection animée à leur réapparition.
* *[extension]* Son à leur mort.
***

#####2.1.3.2 *(class)* Blinky
path: ch.epfl.cs107.play.game.superpacman.actor.ghost.**Blinky.java**
* Simple fantôme qui ne suit pas le pacman.
***

#####2.1.3.3 *(class)* Inky
path: ch.epfl.cs107.play.game.superpacman.actor.ghost.**Inky.java**
* Fantôme qui suit le pacman dans un champs de vision. S'il est effrayé, il se réfugie proche de son spawn.
***

#####2.1.3.4 *(class)* Pinky
path: ch.epfl.cs107.play.game.superpacman.actor.ghost.**Pinky.java**
* Fantôme qui suit le pacman dans un champs de vision. S'il est effrayé, il fuit le joueur.
***

####2.1.4. *(package)* setting
Ce paquetage permet de mieux structurer le projet.
***

#####2.1.4.1 *(class)* Gate
*path: ch.epfl.cs107.play.game.superpacman.actor.setting.**Gate.java***  
* Barrière bloquant le passage du joueur. Destinée à être ouverte avec un signal. (clé ou aire)
####
***

###2.2. *(package)* area
Ce paquetage permet de mieux structurer le projet.

#### *(class)* SuperPacmanArea
*path: ch.epfl.cs107.play.game.superpacman.area.**SuperPacmanArea.java***
* Classe gérant les aires de jeu.

#### *(class)* SuperPacmanBehavior
*path: ch.epfl.cs107.play.game.superpacman.area.**SuperPacmanBehavior.java***
* Classe gérant la grille du jeu et son comportement.

#### *[extension]* *(class)* SuperPacmanMenu
*path: ch.epfl.cs107.play.game.superpacman.area.**SuperPacmanMenu.java***
* Classe gérant le menu du jeu.
***

####2.2.1 *(package)* level
Ce paquetage permet de mieux structurer le projet.
***

#####2.2.1.1 *[extension]* *(class)* BonusPortal
*path: ch.epfl.cs107.play.game.superpacman.area.level.**BonusPortal.java*** 
**Porte permettant d'accéder au niveau bonus**
* Se comporte comme une porte possédant un sprite et ayant pour destination le niveau bonus
####   
***

#####2.2.1.2 *[extension]* *(class)* BossLevel
*path: ch.epfl.cs107.play.game.superpacman.area.level.**BossLevel.java*** 
**Niveau bonus**
* Possède beaucoup de cherries et fantômes. On peut revenir dans le level2 par une gate.
####   
***

#####2.2.1.3 *(class)* Level0
*path: ch.epfl.cs107.play.game.superpacman.area.level.**Level0.java*** 
* Niveau 0 du jeu.
####   
***

#####2.2.1.4 *(class)* Level1
*path: ch.epfl.cs107.play.game.superpacman.area.level.**Level1.java*** 
* Niveau 1 du jeu.
####   
***

#####2.2.1.5 *(class)* Level2
*path: ch.epfl.cs107.play.game.superpacman.area.level.**Level2.java*** 
* Niveau 2 du jeu.
####   
***

###2.3. *(package)* handler
Ce paquetage permet de mieux structurer le projet. 

#### *(interface)* SuperPacmanInteractionVisitor
*path: ch.epfl.cs107.play.game.superpacman.handler.**SuperPacmanInteractionVisitor.java*** 
* Interface qui represente les differentes interactions possible du jeu
####
***



##3. Modification des consignes apportées par l'énoncé

###3.1 (Consigne: modifier vitesse inky quand est effrayé)
 * Notre projet: modifier vitesse PINKY quand effrayé car c'est plus logique puisqu'il fuit).
 
###3.2 (Consigne: scare/unscare ghosts dans le update du jeu)
 * Notre projet: scare dans "invincible" et unscare dans "refreshinvincibility" du joueur. Economise de la mémoire)

##4. Liste de toutes les extensions

##### 4a. Acteurs
* *(abstract class)* Projectile
* *(class)* Fire
* *(class)* Heart
* *(class)* BonusPortal
* *(class)* Boss
* *(class)* BossLife
* *(class)* Arrow
* *(class)* Bow

##### 4b. Aires et objets liés
* *(class)* BossLevel
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
    
https://www.artstation.com/artwork/9eAJzy --> bow