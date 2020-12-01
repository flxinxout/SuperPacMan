package ch.epfl.cs107.play.game.areagame.actor;

public interface Collectable {

    /**
     * Method called when it is collected
     */
    void onCollect();

    /**
     * Sound of a collectable TODO: A METTRE PLUS TARD STYLé JE PENSE
     */
    default void onSound() { }
}
