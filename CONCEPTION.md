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

##2. Classes, paquetages et interfaces ajoutés (explication des extensions incluses)
***

#### *(main class)* SuperPacman
* Classe principale du jeu SuperPacman.
###
*path: ch.epfl.cs107.play.game.superpacman.**SuperPacman.java*** 

***

###2.0. *(package)* areagame.actor
##

#### *(interface)* Collectable
* *Implementée par CollectableAreaEntity* 
* Représente la relation "se comporte comme un objet collectible"
* Dans un paquetage général (non spécifique à SuperPacman) car elle est utile à tout type de jeu sur grille.
###
*path: ch.epfl.cs107.play.game.areagame.actor.**Collectable.java*** 
##

#### *(abstract class)* CollectableAreaEntity
**Super-classe de tous les objets collectibles**  
* Abstraite car elle a pour but d'être instantiée uniquement par l'intermédiaire de ses sous-classes.
* Dans un paquetage général (non spécifique à SuperPacman) car elle est utile à tout type de jeu sur grille.
###
*path: ch.epfl.cs107.play.game.areagame.actor.**CollectableAreaEntity.java*** 

***

###2.1. *(package)* actor
Ce paquetage regroupant les acteurs de SuperPacman permet de mieux structurer le projet.
##

#### *(interface)* Killable
* *Implementée par Ghost et SuperPacmanPlayer* 
* Représente la relation "se comporte comme une entité pouvant être tuée".
* Ajout personnel pour mieux organiser le projet. Dans l'idée d'ajouter d'autres entités tuables
###
*path: ch.epfl.cs107.play.game.superpacman.actor.**Killable.java***  
##

#### *(class)* SuperPacmanPlayer
* Classe du joueur principal
* *[extension]* Protection animée à sa réapparition.
* *[extension]* Son à sa mort.
* Contient une sous-classe *(class)* **SuperPacmanPlayerHandler** gérant les interactions du joueur.
###
*path: ch.epfl.cs107.play.game.superpacman.actor.**SuperPacmanPlayer.java***  
##

#### *(class)* SuperPacmanPlayerStatusGUI
* Gère l'affichage de l'interface graphique liée au joueur
* Conformément à l'énoncé, elle n'est pas définie comme une classe imbriquée du joueur.
* Conformément à l'énoncé, son constructeur est défini comme *protected* afin de ne pouvoir être construite uniquement  
par des acteurs.
###
*path: java/ch.epfl.cs107.play/game/superpacman/actor/**SuperPacmanPlayerStatusGUI.java*** 

***

###2.1.1. *(package)* collectable
Ce paquetage permet de mieux structurer le projet. Il contient les classes considérées comme des objets collectibles
***

####2.1.1.1. *(abstract class)* CollectableReward
* Super-classe des Collectable rapportant du score  
* Abstraite car elle a pour but d'être instantiée uniquement par l'intermédiaire de ses sous-classes ayant un reward attribué.
###
*path: ch.epfl.cs107.play.game.superpacman.actor.collectable.**CollectableReward.java***  
##

####2.1.1.2. *(class)* Bonus
* Rend le joueur invincible et capable de tuer les fantômes durant 10 secondes.
###
*path: ch.epfl.cs107.play.game.superpacman.actor.collectable.**Bonus.java***  
##

####2.1.1.3. *(class)* Cherry
* Rapporte 200 de score (étend CollectableReward)
###
*path: ch.epfl.cs107.play.game.superpacman.actor.collectable.**Cherry.java***  
##

####2.1.1.4. *(class)* Diamond
* Rapporte 10 de score (étend CollectableReward)
###
*path: ch.epfl.cs107.play.game.superpacman.actor.collectable.**Diamond.java***
##

####2.1.1.5. *(class)* Key
* Se comporte comme un signal (implémente Logic) utilisé pour ouvrir des barrières.
###
*path: ch.epfl.cs107.play.game.superpacman.actor.collectable.**Key.java***  
##

####2.1.1.6. *[extension]* *(class)* Heart
* Rapporte un point de vie.
###
*path: ch.epfl.cs107.play.game.superpacman.actor.collectable.**Heart.java***    
##

####2.1.1.7. *[extension]* *(class)* BossLife
* Représente une vie d'un boss. Quand tous les BossLife sont récuperées, le jeu se finit
###
*path: ch.epfl.cs107.play.game.superpacman.actor.collectable.**BossLife.java***    

***

###2.1.2. *[extension]* *(package)* ennemy
Ce paquetage regroupant les ennemis autre que les fantômes permet de mieux structurer le projet.
***

####2.1.2.1 *[extension]* *(abstract class)* Projectile
**Super-classe de tous les projectiles**  
* Représente une entité se déplaçant à vitesse constante selon une trajectoire rectiligne
* Abstraite car elle a pour but d'être instantiée uniquement par l'intermédiaire de ses sous-classes.
###
*path: ch.epfl.cs107.play.game.superpacman.actor.ennemy.**Projectile.java***  
##

####2.1.2.2. *[extension]* *(class)* Arrow
* Représente une flèche (étend Projectile) qui tue le pacman s'il entre en contact avec elle.
###
*path: ch.epfl.cs107.play.game.superpacman.actor.ennemy.**Arrow.java***
##

####2.1.2.3 *[extension]* *(class)* Bow
* Représente un arc qui tire une flèche sur le joueur s'il est dans son champ de vision
* Son champ de vision est une croix (Sa ligne ainsi que sa colonne entières)
* La flèche est tirée à la fin de l'animation pour laisser du temps au joueur de prévoir le tir et pour plus de réalisme.
* Nous avons recréé son animation en s'inspirant grandement de l'oeuvre de Michael H.  
link: <https://www.artstation.com/artwork/9eAJzy>
###
*path: ch.epfl.cs107.play.game.superpacman.actor.ennemy.**Bow.java***  
##

####2.1.2.4 *[extension]* *(class)* Fire
* Représente une flamme occupant une cellule durant 5 secondes.
* Elle tue le joueur au contact.
* Elle est utilisée comme traînée derrière le boss.
###
*path: ch.epfl.cs107.play.game.superpacman.actor.ennemy.**Fire.java***
##

####2.1.2.5 *[extension]* *(class)* Boss
* Représente le boss final du jeu, lorsque le joueur récupère toutes ses vies, le jeu se finit.
* Nous avons choisi de le faire étendre Ghost car il partage beaucoup de comportements avec ces-derniers, bien qu'il ne  
soit pas à proprement parler un fantôme.
* Il laisse une traînée de feu sur chaque case qu'il a quittée.
* Sa vitesse augmente à chaque vie collectée par le joueur
###
*path: ch.epfl.cs107.play.game.superpacman.actor.ennemy.**Boss.java***  

***

###2.1.3. *(package)* ghost
Ce paquetage permet de définir des méthodes protégées entre les fantômes inaccessibles par les autres acteurs.
***

####2.1.3.1 *(abstract class)* Ghost
**Super-classe de tous les fantômes**  
* Abstraite car elle a pour but d'être instantiée uniquement par l'intermédiaire de ses sous-classes.
* *[extension]* Protection animée à leur réapparition.
* *[extension]* Son à leur mort.
###
*path: ch.epfl.cs107.play.game.superpacman.actor.ghost.**Ghost.java***  
##

####2.1.3.2 *(class)* Blinky
* Fantôme se déplaçant aléatoirement dans toute l'aire.
* Ne poursuit pas le joueur.
###
*path: ch.epfl.cs107.play.game.superpacman.actor.ghost.**Blinky.java***  
##

####2.1.3.3 *(class)* Inky
* Fantôme se déplaçant autour d'un certain rayon (rectangulaire) autour de sa case refuge
* Poursuit le joueur s'il rentre dans son champ de vision (rayon rectangulaire)
* S'il est effrayé, sa vitesse augmente et il se réfugie proche de sa case refuge.
###
*path: ch.epfl.cs107.play.game.superpacman.actor.ghost.**Inky.java***
##

####2.1.3.4 *(class)* Pinky
* Fantôme se déplaçant en suivant une trajectoire aléatoire dans l'aire entière
* Poursuit le joueur s'il rentre dans son champ de vision (rayon rectangulaire)
* S'il est effrayé, il fuit le joueur.
* *[extension]* S'il est effrayé, sa vitesse augmente également. (--> fuite plus efficace)
###
*path: ch.epfl.cs107.play.game.superpacman.actor.ghost.**Pinky.java***
##

###2.1.4. *(package)* setting
Ce paquetage regroupant les décors du jeu permet de mieux structurer le projet.
***

#####2.1.4.1 *(class)* Gate
* Barrière bloquant le passage du joueur. Destinée à être ouverte avec un signal. (clé ou aire)
###
*path: ch.epfl.cs107.play.game.superpacman.actor.setting.**Gate.java***  
##

#####2.1.4.2 *(class)* Wall
* Classe pré-existante dans la maquette de base
* *[extension]* Ajout d'un sprite à son constructeur: un mur seul (sans voisinage)  
 --> Création d'un nouveau fichier PNG: *res.images.sprites.superpacman.**wall2***
###
*path: ch.epfl.cs107.play.game.superpacman.actor.setting.**Wall.java*** 
 
***

###2.2. *(package)* area
Ce paquetage regroupant les classes liées aux aires du jeu SuperPacman permet de mieux structurer le projet.

#### *(class)* SuperPacmanArea
* Classe gérant les aires du jeu SuperPacman.
* *[extension]* Fonctionnalités de pause et de fin de jeu (victoire et défaite) et leurs menus associés
###
*path: ch.epfl.cs107.play.game.superpacman.area.**SuperPacmanArea.java***
##

#### *(class)* SuperPacmanBehavior
* Classe gérant la grille et le comportement d'une aire du jeu SuperPacman.
###
*path: ch.epfl.cs107.play.game.superpacman.area.**SuperPacmanBehavior.java***
##

#### *[extension]* *(class)* SuperPacmanMenu
* Classe gérant le menu du jeu.
###
*path: ch.epfl.cs107.play.game.superpacman.area.**SuperPacmanMenu.java***
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
    