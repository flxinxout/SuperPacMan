package ch.epfl.cs107.play.game.superpacman.actor;

/**
 * Interface that represents something killable
 */

public interface Killable {

    /**
     * Called when it dies
     */
    void onDeath();
}
