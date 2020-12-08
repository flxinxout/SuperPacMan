package ch.epfl.cs107.play.game.areagame.actor;

/**
 * Interface that represents something a collectable
 */
//TODO: WHY DOES IT EXTEND SOUND, IT MAKES NO SENSE LOGICALLY
public interface Collectable extends Sound {

    /**
     * Method called when it is collected
     */
    void onCollect();
}
