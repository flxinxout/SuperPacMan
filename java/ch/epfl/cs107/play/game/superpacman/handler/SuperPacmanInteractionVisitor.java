package ch.epfl.cs107.play.game.superpacman.handler;

import ch.epfl.cs107.play.game.rpg.handler.RPGInteractionVisitor;
import ch.epfl.cs107.play.game.superpacman.actor.*;
import ch.epfl.cs107.play.game.superpacman.actor.collectable.Bonus;
import ch.epfl.cs107.play.game.superpacman.actor.collectable.CollectableReward;
import ch.epfl.cs107.play.game.superpacman.actor.collectable.Heart;
import ch.epfl.cs107.play.game.superpacman.actor.collectable.BossLife;
import ch.epfl.cs107.play.game.superpacman.actor.ennemy.Boss;
import ch.epfl.cs107.play.game.superpacman.actor.ennemy.Bow;
import ch.epfl.cs107.play.game.superpacman.actor.ennemy.Fire;
import ch.epfl.cs107.play.game.superpacman.actor.ennemy.Projectile;
import ch.epfl.cs107.play.game.superpacman.actor.ghost.Ghost;
import ch.epfl.cs107.play.game.superpacman.actor.setting.Wall;

/**
 * Interface that represents the different interaction of the SuperPacmamPlayer
 */
public interface SuperPacmanInteractionVisitor extends RPGInteractionVisitor {

    default void interactWith(SuperPacmanPlayer player) { }

    default void interactWith(Ghost ghost) { }

    default void interactWith(Bonus bonus) { }

    default void interactWith(Heart heart) { }

    default void interactWith(CollectableReward collectableReward) { }

    default void interactWith(Wall wall) { }

    default void interactWith(Projectile projectile) { }

    default void interactWith(Fire fire) { }

    default void interactWith(Boss boss) { }

    default void interactWith(Bow bow) { }

    default void interactWith(BossLife lifeBoss) { }
}