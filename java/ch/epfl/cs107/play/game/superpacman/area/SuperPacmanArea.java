package ch.epfl.cs107.play.game.superpacman.area;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.superpacman.actor.SuperPacmanPlayer;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Keyboard;
import ch.epfl.cs107.play.window.Window;

import java.util.Queue;

/**
 * Class that represents an Area of the game
 */
public abstract class SuperPacmanArea extends Area implements Logic {
    // The scale factor of the window and the player in the area
    protected final static float CAMERA_SCALE_FACTOR = 20.f;

    // Behavior of the area
    private SuperPacmanBehavior behavior;

    // Attributes of the area
    private int diamondsNumber;

    // This signal is activated when every collectable in the area has been collected
    private boolean isCompleted;

    // Pause mechanics and menu to display.
    private SuperPacmanMenu[] pauseGUI;
    private SuperPacmanMenu gameOverGUI;
    private SuperPacmanMenu winGUI;
    private int playerScore;
    private boolean won;
    private int desiredGUI = 0;


    /* --------------- External Methods --------------- */

    @Override
    public boolean resume(Window window, FileSystem fileSystem) {
        desiredGUI = 0;
        return super.resume(window, fileSystem);
    }

    /** Method for the game over, when the player has 0 hp */
    public void gameOver() {
        won = false;
        end();
    }

    /** Add the area in actors list */
    protected void createArea() {
        behavior.registerActors(this);
    }

    /** Increase diamond that has been collected */
    public void addDiamond() {
        diamondsNumber++;
    }

    /** Decrease diamond number and check if all diamonds in the area were collected for set the correct signal */
    public void removeDiamond() {
        diamondsNumber--;
        if (diamondsNumber < 0) { diamondsNumber = 0; }
        if (diamondsNumber == 0) {
            // If there are 0 diamond on the area, the signal is set on
            isCompleted = true;

            // If there are 0 diamond in the last level, then the player wins
            if (this instanceof Level2) {
                won = true;
                end();
            }
        }
    }

    /**
     * Calls shortestPath(DiscreteCoordinates from, DiscreteCoordinates to) from its behavior's graph
     */
    public Queue<Orientation> shortestPath(DiscreteCoordinates from, DiscreteCoordinates to) {
        return behavior.shortestPath(from, to);
    }

    /**
     * Calls setSignal(DiscreteCoordinates coordinates, Logic signal) from its behavior's graph
     */
    public void setSignal(DiscreteCoordinates coordinates, Logic signal) {
        behavior.setSignal(coordinates, signal);
    }

    /**
     * Calls scare() of its behavior
     */
    public void scareGhosts() {
        behavior.scareGhosts();
    }

    /**
     * Calls unscareGhosts() of its behavior
     */
    public void unScareGhosts() {
        behavior.unScareGhosts();
    }

    /* --------------- Implements Playable --------------- */

    @Override
    public boolean begin(Window window, FileSystem fileSystem) {
        super.begin(window, fileSystem);

        if (super.begin(window, fileSystem)) {
            behavior = new SuperPacmanBehavior(window, getTitle());
            setBehavior(behavior);

            createArea();

            desiredGUI = 0;
            // Initialisation GUIs
            pauseGUI = new SuperPacmanMenu[2];
            for (int i = 0; i < pauseGUI.length; i++) {
                pauseGUI[i] = new SuperPacmanMenu("superpacman/pause.menu" + i, SuperPacmanMenu.SuperPacmanMenuType.PAUSE, playerScore);
            }

            gameOverGUI = new SuperPacmanMenu("superpacman/gameover.menu", SuperPacmanMenu.SuperPacmanMenuType.GAMEOVER, playerScore);
            winGUI = new SuperPacmanMenu("superpacman/win.menu", SuperPacmanMenu.SuperPacmanMenuType.WIN, playerScore);
            won = false;

            return true;
        }
        return false;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        if (isPaused()) {
            // Adapt the arrow in the pause GUI
            pauseGUI[desiredGUI].draw(getWindow());

            if (getKeyboard().get(Keyboard.UP).isLastPressed()) {
                desiredGUI = 0;
            } else if (getKeyboard().get(Keyboard.DOWN).isLastPressed()) {
                desiredGUI = 1;
            }

            // If ENTER is pressed
            if (getKeyboard().get(Keyboard.ENTER).isPressed()) {
                switch(desiredGUI) {
                    case 0:
                        resume(getWindow(), getFileSystem());
                        break;

                    case 1:
                        System.exit(1);
                        break;

                    default:
                        break;
                }
            }
        }

        if(hasEnded()) {
            if (!won) {
                gameOverGUI.draw(getWindow());
            } else {
                winGUI.draw(getWindow());
            }

            // If ENTER is pressed, then exit the game
            if (getKeyboard().get(Keyboard.ENTER).isPressed()) {
                System.exit(1);
            }
        }
    }

    /* ---------------- Implements Logic ---------------- */

    @Override
    public boolean isOn() {
        return isCompleted;
    }

    @Override
    public boolean isOff() {
        return !isCompleted;
    }

    @Override
    public float getIntensity() {
        return (isCompleted) ? 1 : 0;
    }

    /* --------------- Setters --------------- */

    /** sets the player's score
     * used in the end menus
     * @param playerScore player's score
     */
    public void setPlayerScore(int playerScore) {
        this.playerScore = playerScore;
    }

    /* --------------- Getters --------------- */

    /** @Note: Need to be redefine
     * @return the player's spawn location of this area
     */
    abstract public DiscreteCoordinates getSpawnLocation();

    @Override
    public float getCameraScaleFactor() { return CAMERA_SCALE_FACTOR; }
}
