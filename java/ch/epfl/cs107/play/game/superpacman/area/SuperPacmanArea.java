package ch.epfl.cs107.play.game.superpacman.area;

import ch.epfl.cs107.play.game.actor.SoundAcoustics;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Keyboard;
import ch.epfl.cs107.play.window.Window;

import java.util.Queue;

/**
 * SuperPacmanAreas are Areas specific to the SuperPacman game
 */
public abstract class SuperPacmanArea extends Area implements Logic {

    // Constants
    // The scale factor of the window and the player in the area
    protected final static float CAMERA_SCALE_FACTOR = 20.f;

    // Attributes
    private int diamondsNumber;
    private boolean isCompleted; // Activated when every diamond in the area has been collected

    // Behavior of the area
    private SuperPacmanBehavior behavior;

    /* --------------- EXTENSIONS --------------- */

    // Pause and end mechanics and menu to display.
    private SuperPacmanMenu[] pauseGUI;
    private SuperPacmanMenu gameOverGUI;
    private SuperPacmanMenu winGUI;
    private boolean wasPaused;
    private boolean won;
    private int desiredGUI = 0;
    private final SoundAcoustics menuSound = new SoundAcoustics("sounds/pacman/dialogNext.wav");


    /* --------------- Protected Methods --------------- */

    /**
     * Register the actors of the area
     */
    protected void createArea() {
        behavior.registerActors(this);
    }

    /* --------------- Extends Area --------------- */

    @Override
    public boolean begin(Window window, FileSystem fileSystem) {
        super.begin(window, fileSystem);

        if (super.begin(window, fileSystem)) {
            behavior = new SuperPacmanBehavior(window, getTitle());
            setBehavior(behavior);
            createArea();

            /* --------------- EXTENSIONS --------------- */

            desiredGUI = 0;

            // Initialisation GUIs
            pauseGUI = new SuperPacmanMenu[2];
            for (int i = 0; i < pauseGUI.length; i++) {
                pauseGUI[i] = new SuperPacmanMenu("superpacman/pause.menu" + i);
            }

            gameOverGUI = new SuperPacmanMenu("superpacman/gameover.menu");
            winGUI = new SuperPacmanMenu("superpacman/win.menu");
            won = false;

            return true;
        }
        return false;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        /* --------------- EXTENSIONS --------------- */

        //Pause mechanics
        if (isPaused()) {
            if (!wasPaused) {
                menuSound.shouldBeStarted();
                menuSound.bip(getWindow());
            }

            // Adapt the arrow in the pause GUI
            pauseGUI[desiredGUI].draw(getWindow());

            if (getKeyboard().get(Keyboard.UP).isLastPressed()) {
                menuSound.shouldBeStarted();
                menuSound.bip(getWindow());
                desiredGUI = 0;
            } else if (getKeyboard().get(Keyboard.DOWN).isLastPressed()) {
                menuSound.shouldBeStarted();
                menuSound.bip(getWindow());
                desiredGUI = 1;
            }

            // If ENTER is pressed
            if (getKeyboard().get(Keyboard.ENTER).isPressed()) {
                menuSound.shouldBeStarted();
                menuSound.bip(getWindow());
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

        //End mechanics
        if(hasEnded()) {
            if (!won) {
                gameOverGUI.draw(getWindow());
            } else {
                winGUI.draw(getWindow());
            }

            // If ENTER is pressed, then exit the game
            if (getKeyboard().get(Keyboard.ENTER).isPressed()) {
                menuSound.shouldBeStarted();
                menuSound.bip(getWindow());

                System.exit(1);
            }
        }
        wasPaused = isPaused();
    }

    @Override
    public boolean resume(Window window, FileSystem fileSystem) {
        menuSound.shouldBeStarted();
        menuSound.bip(window);

        desiredGUI = 0;
        return super.resume(window, fileSystem);
    }

    @Override
    public float getCameraScaleFactor() { return CAMERA_SCALE_FACTOR; }

    /* --------------- Public Methods --------------- */

    /**
     * Method to cast an Area in a SuperPacmanArea
     * @param area (Area): the area to cast
     * @return (SuperPacmanArea)
     */
    public static SuperPacmanArea toSuperPacmanArea(Area area) { return (SuperPacmanArea) area; }

    /**
     * End the game by loosing
     */
    public void gameOver() {
        won = false;
        end();
    }

    /**
     * End the game by winning
     */
    public void win() {
        won = true;
        end();
    }

    /**
     * Add a diamond to keep track of the count of them in the area
     */
    public void addDiamond() {
        diamondsNumber++;
    }

    /**
     * Remove a diamond to the count and complete the area if they are all collected
     */
    public void removeDiamond() {
        diamondsNumber--;
        if (diamondsNumber == 0) {
            // If there are 0 diamond on the area, the signal is set on
            isCompleted = true;
        }
    }

    /**
     * Call shortestPath(DiscreteCoordinates from, DiscreteCoordinates to) from its behavior's graph
     */
    public Queue<Orientation> shortestPath(DiscreteCoordinates from, DiscreteCoordinates to) {
        return behavior.shortestPath(from, to);
    }

    /**
     * Call setSignal(DiscreteCoordinates coordinates, Logic signal) from its behavior's graph
     */
    public void setSignal(DiscreteCoordinates coordinates, Logic signal) {
        behavior.setSignal(coordinates, signal);
    }

    /**
     * Call scare() of its behavior
     */
    public void scareGhosts() {
        behavior.scareGhosts();
    }

    /**
     * Call unscareGhosts() of its behavior
     */
    public void unScareGhosts() {
        behavior.unScareGhosts();
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


    /* --------------- Getters --------------- */

    /**
     * Getter for the player's spawn location. NEED TO BE REDEFINED
     * @return (DiscreteCoordinates)
     */
    abstract public DiscreteCoordinates getSpawnLocation();
}
