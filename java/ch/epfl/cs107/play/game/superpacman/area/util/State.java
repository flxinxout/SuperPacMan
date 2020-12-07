package ch.epfl.cs107.play.game.superpacman.area.util;

/**
 * Enum that represents the differents state of the game
 */
public enum State {

    START(false),
    RUNNING(false),
    PAUSE(false),
    END(false);

    private static State currentState;
    private boolean state;

    State(boolean state) {
        this.state = state;
    }

    public static void setState(State current) {
        currentState = current;
    }

    public static boolean isState(State current) {
        return currentState == current;
    }

    public static State getState() {
        return currentState;
    }
}
