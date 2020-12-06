package ch.epfl.cs107.play.game.superpacman.handler;

import ch.epfl.cs107.play.game.rpg.handler.RPGInteractionVisitor;
import ch.epfl.cs107.play.game.superpacman.actor.SuperPacmanPlayer;
import ch.epfl.cs107.play.game.superpacman.actor.collectable.Bonus;
import ch.epfl.cs107.play.game.superpacman.actor.ghost.Ghost;

/**
 * Interface that represents the different interaction of the SuperPacmamPlayer
 */
public interface SuperPacmanInteractionVisitor extends RPGInteractionVisitor {

    default void interactWith(SuperPacmanPlayer player) { }

    default void interactWith(Ghost ghost) { }

    default void interactWith(Bonus bonus) { }
}