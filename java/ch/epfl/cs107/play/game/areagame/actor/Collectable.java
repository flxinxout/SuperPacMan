package ch.epfl.cs107.play.game.areagame.actor;

/**
 * Interface that represents something collectable
 */

public interface Collectable extends Sound {

    /**
     * Method called when it is collected
     */
    void onCollect();
}
