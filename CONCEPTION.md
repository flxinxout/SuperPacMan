Giovanni Ranieri et Dylan Vairoli
#SuperPacman conception
###0a. Contenu
1. Modifications du code de départ et remarques
2. Classes et interfaces ajoutées (extensions incluses)
3. Modification des consignes apportées par l'énoncé
4. Liste de toutes les extensions

###0b. Pour une meilleure lisibilité du document
* Les extensions seront précédées par *[extension]*
* Les classes, classes abstraites, interfaces et énumérations seront respectivement précédées 
par *(class)*, *(abstract class)*, *(interface)*, et *(enum)*.

##1.1. Modification du code de départ
##### 1.1.1. *(abstract class)* Area
* *[extension]* Ajout de fonctionnalités de pause et de fin de jeu:  
    * Ajout de deux attributs *paused* et *ended*  
    * Ajout de deux fonctions *isPaused()* et *hasEnded()* (Getters)  
    --> Modification de *update()*, *suspend()* et *end()* en conséquence  
###
*path: java/ch.epfl.cs107.play/game/areagame/**Area.java***
##

#####1.1.2. *(class)* Wall
* *[extension]* Ajout d'un sprite à son constructeur: un mur seul (sans voisinage)  
 --> Création d'un nouveau fichier PNG: *res.images.sprites.superpacman.**wall2***
###
*path: ch.epfl.cs107.play.game.superpacman.actor.setting.**Wall.java*** 
 
***

## 1.2. Remarque
Un bug bien connu par votre équipe persiste:  
Parfois, après avoir mangé un fantôme, si on retourne sur la case sur laquelle on l'a mangé, l'interaction se produit
toujours alors que le fantôme a réapparu à sa case refuge. Nous avons cherché à régler ce problème via Piazza et
Discord et on nous a informés que c'était un défaut de la maquette et nous ne devions pas nous acharner dessus.
***
***

##2. Classes, paquetages et interfaces ajoutés (explication des extensions incluses)

#### *(main class)* SuperPacman
**Classe principale du jeu SuperPacman.**
###
*path: ch.epfl.cs107.play.game.superpacman.**SuperPacman.java*** 

***

###2.0.1. *(package)* areagame.actor
##

#### *(abstract class)* CollectableAreaEntity
**Super-classe de tous les objets collectibles**  
* Abstraite car elle a pour but d'être instanciée uniquement par l'intermédiaire de ses sous-classes.
* Dans un paquetage général (non spécifique à SuperPacman) car elle est utile à tout type de jeu sur grille.
###
*path: ch.epfl.cs107.play.game.areagame.actor.**CollectableAreaEntity.java*** 

***

###2.0.2. *(package)* rpg.actor
##

#### *[extension]* *(class)* DoorGraphics
* Porte avec un sprite attaché 
* Dans un paquetage général (non spécifique à SuperPacman) car elle est utile à tout type de jeu sur grille.
###
*path: ch.epfl.cs107.play.game.rpg.actor.**DoorGraphics.java*** 

***

###2.1. *(package)* actor
Ce paquetage regroupant les acteurs de SuperPacman permet de mieux structurer le projet.
##

#### *(interface)* Killable
*Implementée par Ghost et SuperPacmanPlayer* 
* Représente la relation "se comporte comme une entité pouvant être tuée".
* Ajout personnel pour mieux organiser le projet. Dans l'idée d'ajouter d'autres entités tuables
###
*path: ch.epfl.cs107.play.game.superpacman.actor.**Killable.java***  
##

#### *(class)* SuperPacmanPlayer
**Classe du joueur principal**
* *[extension]* Protection animée à sa réapparition.
* *[extension]* Son à sa mort.
* *[extension]* Augmentation de sa vitesse ainsi qu'invincibilité (10s) lorsque tous les diamants d'une aire sont 
récoltés
* Contient une sous-classe **SuperPacmanPlayerHandler** gérant les interactions du joueur.
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
##

####2.1.1.1. *(abstract class)* CollectableReward
**Super-classe des Collectable rapportant du score**  
*Étendue par: Cherry et Diamond*
* Abstraite car elle a pour but d'être instanciée uniquement par l'intermédiaire de ses sous-classes
* Créée afin d'éviter la duplication de code. Bien que seuls 2 CollectableReward sont définis dans notre projet,
il est facilement imaginable d'en avoir une dizaine dans un jeu plus complet
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
* Représente une vie d'un boss. Quand toutes les BossLife sont récuperées, le jeu se finit
* Nous avons choisi de ne pas l'enregistrer au travers d'une image de behavior car il aurait été impossible
de correctement l'associer au boss correspondant.
###
*path: ch.epfl.cs107.play.game.superpacman.actor.collectable.**BossLife.java***    

***

###2.1.2. *[extension]* *(package)* ennemy
Ce paquetage regroupant les ennemis autres que les fantômes permet de mieux structurer le projet.
##

####2.1.2.1 *[extension]* *(abstract class)* Projectile
**Super-classe de tous les projectiles**  
*Étendue par: Arrow*
* Représente une entité se déplaçant à vitesse constante selon une trajectoire rectiligne
* Abstraite car elle a pour but d'être instanciée uniquement par l'intermédiaire de ses sous-classes.
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
* Contient une sous-classe **BowHandler** gérant les interactions des arcs.
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
* Il laisse une traînée de feu sur chaque case qu'il a quittée.
* Sa vitesse augmente à chaque vie collectée par le joueur
* Nous avons choisi de le faire étendre Ghost car il partage beaucoup de comportements avec ces-derniers, bien qu'il ne
soit pas à proprement parler un fantôme.
* Nous avons choisi de ne pas l'enregistrer au travers d'une image de behavior car il est censé y en avoir au maximum 
un par aire
###
*path: ch.epfl.cs107.play.game.superpacman.actor.ennemy.**Boss.java***  

***

###2.1.3. *(package)* ghost
Ce paquetage permet de définir des méthodes protégées entre les fantômes inaccessibles par les autres acteurs.
##

####2.1.3.1 *(abstract class)* Ghost
**Super-classe de tous les fantômes**  
*Étendue par: Blinky, Inky, Pinky, [extension] Boss*
* Abstraite car elle a pour but d'être instanciée uniquement par l'intermédiaire de ses sous-classes.
* Contient une sous-classe **GhostHandler** gérant les interactions des fantômes.
* Remarque: on a créé getSpeed() et getFieldOfView() plutôt que des attributs et des setters car on est
partis du principe que les fantômes pouvaient tous avoir des vitesses et champs de vision différents. 
(En l'occurrence non mais ils auraient pu)
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
***

###2.1.4. *(package)* setting
Ce paquetage regroupant les décors du jeu permet de mieux structurer le projet.
##

##### *(class)* Gate
* Barrière bloquant le passage du joueur. Destinée à être ouverte avec un signal. (clé ou aire)
###
*path: ch.epfl.cs107.play.game.superpacman.actor.setting.**Gate.java***  
***

###2.2. *(package)* area
Ce paquetage regroupant les classes liées aux aires du jeu SuperPacman permet de mieux structurer le projet.
##

#### *(class)* SuperPacmanArea
* Classe gérant les aires du jeu SuperPacman.
* Ajout d'une fonction statique utilitaire (toSuperPacmanArea(Area area)) afin de faciliter les nombreux casts
* *[extension]* Fonctionnalités de pause et de fin de jeu (victoire et défaite) et leurs menus et sons associés
###
*path: ch.epfl.cs107.play.game.superpacman.area.**SuperPacmanArea.java***
##

#### *(class)* SuperPacmanBehavior
* Classe gérant la grille et le comportement d'une aire du jeu SuperPacman.
* Contient une énumeration *(enum)* SuperPacmanCellType qui liste les différents types de cellules 
ainsi que la couleur leur étant associée
* *[extension]* Ajout de nouveaux types dans *(enum)* SuperPacmanCellType correspondants 
aux acteurs ajoutés par nos soins
###
*path: ch.epfl.cs107.play.game.superpacman.area.**SuperPacmanBehavior.java***
##

#### *[extension]* *(class)* SuperPacmanMenu
* Représente un menu de SuperPacman.
* Utilisé pour la pause, la victoire et la défaite du jeu.
###
*path: ch.epfl.cs107.play.game.superpacman.area.**SuperPacmanMenu.java***

***

####2.2.1. *(package)* level
Ce paquetage regroupant les différentes aires de SuperPacman permet de mieux structurer le projet.
##

#####2.2.1.1. *(class)* Level0
* Niveau 0 du jeu.
###
*path: ch.epfl.cs107.play.game.superpacman.area.level.**Level0.java*** 
##

#####2.2.1.2. *(class)* Level1
* Niveau 1 du jeu
###
*path: ch.epfl.cs107.play.game.superpacman.area.level.**Level1.java*** 
##

#####2.2.1.3. *(class)* Level2
* Niveau 2 du jeu
###
*path: ch.epfl.cs107.play.game.superpacman.area.level.**Level2.java*** 
##

#####2.2.1.4. *[extension]* *(class)* BonusLevel
* Niveau bonus du jeu
* Créé pour avoir un moyen d'obtenir beaucoup de score en prenant toutefois un risque: aucun bonus ne s'y trouve 
(invincibilité impossible)
* On y entre dans le niveau 2 et il nous ramène dans le niveau 2.
###
*path: ch.epfl.cs107.play.game.superpacman.area.level.**BonusLevel.java*** 
##

#####2.2.1.5. *[extension]* *(class)* BossLevel
* Niveau final du jeu
* Il contient un boss ainsi que de nombreux arcs. Il est conçu pour être difficile.
* Si on collecte toutes les vies du boss, le jeu se termine.
###
*path: ch.epfl.cs107.play.game.superpacman.area.level.**BossLevel.java*** 
##

#####2.2.1.6. *[extension]* *(class)* Level0Copy, Level1Copy, Level2Copy
* Copie des niveaux du jeu avec l'ajout de notre item *Heart* et l'accès aux niveaux bonus et de boss
###
*path 0: ch.epfl.cs107.play.game.superpacman.area.level.**Level0Copy.java***  
*path 1: ch.epfl.cs107.play.game.superpacman.area.level.**Level1Copy.java***   
*path 2: ch.epfl.cs107.play.game.superpacman.area.level.**Level2Copy.java***    

***
###2.3. *(package)* handler
Ce paquetage comprenant le gestionnaire d'interaction de SuperPacman permet de mieux structurer le projet. 
##

#### *(interface)* SuperPacmanInteractionVisitor
* *Implémentée par tous les handlers des Interactor du jeu*
* Interface donnant une définition (vide) par défaut à toutes les interactions possibles du jeu.
###
*path: ch.epfl.cs107.play.game.superpacman.handler.**SuperPacmanInteractionVisitor.java*** 

***
***

##3. Modification des consignes apportées par l'énoncé
 
### (Consigne: vérifier l'effraiement des fantômes dans le update de SuperPacman)
 * Notre projet: effrayer dans "invincible()" de SuperPacmanPlayer et arrêter d'effrayer dans 
 "refreshinvincibility()" de SuperPacmanPlayer. Un ajustement est nécessaire également lors de
 l'interaction avec les portes.  
 Nous avons décidé de procéder ainsi afin d'éviter de répéter la vérification à chaque frame mais seulement quand l'état
 du fantôme est susceptible de changer.  
 Le schéma pour modifier l'état des fantômes est le suivant:  
 -> **SuperPacmanPlayer** getOwnerArea().scareGhosts()/unscareGhosts()  
 -> **SuperPacmanArea** behavior.scareGhosts/unScareGhosts()  
 -> **SuperPacmanBehavior** possède une liste de tous les fantômes et donc change leur état un à un 

***
***

##4. Liste de toutes les extensions

##### 4.1. Acteurs
* *(abstract class)* Projectile
* *(class)* Fire
* *(class)* Heart
* *(class)* BonusPortal
* *(class)* Boss
* *(class)* BossLife
* *(class)* Arrow
* *(class)* Bow

##### 4.2. Aires et objets liés
* *(class)* BossLevel
* *(class)* BonusLevel
* *(class)* SuperPacmanMenu

##### 4.3. Autres fonctionnalités
* Divers sons
    * Mort du pacman
    * Mort des fantômes
    * Collecte des Collectable
    * Menus de pause et de fin
    
* Temps d'invincibilité à la réapparition du joueur (accompagné d'une animation)
* Temps d'invincibilité à la réapparition des fantômes (accompagné d'une animation)
* Temps d'invincibilité lorsque tous les diamants d'une aire sont ramassés
* Augmentation de la vitesse du joueur lorsque tous les diamants d'une aire sont ramassés
    