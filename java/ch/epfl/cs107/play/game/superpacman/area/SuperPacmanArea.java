package ch.epfl.cs107.play.game.superpacman.area;

import ch.epfl.cs107.play.game.actor.ImageGraphics;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.AreaGraph;
import ch.epfl.cs107.play.game.areagame.io.ResourcePath;
import ch.epfl.cs107.play.game.superpacman.Menu;
import ch.epfl.cs107.play.game.superpacman.SuperPacman;
import ch.epfl.cs107.play.game.superpacman.actor.SuperPacmanPlayer;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Keyboard;
import ch.epfl.cs107.play.window.Window;

/**
 * Class that represents an Area of the game
 */
public abstract class SuperPacmanArea extends Area implements Logic {

    // The scale factor of the window and the player in the area
    public final static float CAMERA_SCALE_FACTOR = 20.f;
    private SuperPacmanPlayer player;

    // Behavior of the area
    private SuperPacmanBehavior behavior;

    // Attributes of the area
    private int diamondsNumber;

    // This signal is activated when every collectable in the area has been collected
    private boolean isCompleted;

    // Pause mechanics and menu to display. May be null
    private Menu[] pauseGUI;
    private Menu gameOverGUI;
    private Menu winGUI;
    private boolean won;
    private int desiredGUI = 0;


    /* --------------- Abstract Methods --------------- */

    abstract public DiscreteCoordinates getSpawnLocation();


    /* --------------- External Methods --------------- */

    @Override
    public boolean resume(Window window, FileSystem fileSystem) {

        // Set the parameter for the correct gui when it will be open
        desiredGUI = 0;
        return super.resume(window, fileSystem);
    }

    /** Method for the game over, when the player has 0 life */
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


    /* --------------- Implements Playable --------------- */

    @Override
    public boolean begin(Window window, FileSystem fileSystem) {
        super.begin(window, fileSystem);

        if (super.begin(window, fileSystem)) {

            // Set the behavior map
            behavior = new SuperPacmanBehavior(window, getTitle());
            setBehavior(behavior);

            // Add the area in actors list
            createArea();

            // Set the default pause gui
            desiredGUI = 0;

            // TODO PLAYER NULL A CAUSE DE QUE L'UPDATE EST BLOQUER DANS LE AREA C BIZARRE
            if(player == null) {
                System.out.println("yes");
            }

            // Initialisation GUIs
            pauseGUI = new Menu[2];
            for (int i = 0; i < pauseGUI.length; i++) {
                pauseGUI[i] = new Menu("superpacman/pause.menu" + i, 1, player);
            }

            gameOverGUI = new Menu("superpacman/gameover.menu", 0, player);
            winGUI = new Menu("superpacman/win.menu", 2, player);
            won = false;

            return true;
        }

        return false;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        // If the game is on pause
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

                        // If desiredGUI is 0, then resume the game
                        resume(getWindow(), getFileSystem());
                        break;
                    case 1:

                        // If desiredGUI is 1, then exit the game
                        System.exit(1);
                        break;

                    default:
                        break;
                }
            }
        }

        // If the game has ended
        if(hasEnded()) {

            // If the player hasn't won, then display the game over gui
            if (!won) {
                gameOverGUI.draw(getWindow());
            } else {

                // else display the win gui
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

    /* --------------- Getters --------------- */

    @Override
    public float getCameraScaleFactor() { return CAMERA_SCALE_FACTOR; }

    /** @return the graph of the behavior */
    public AreaGraph getGraph() { return behavior.getGraph(); }

    /** @return the behavior of the area */
    public SuperPacmanBehavior getBehavior() { return behavior; }

    /** @return the player of the area */
    public SuperPacmanPlayer getPlayer() { return player; }

    /* --------------- Setters --------------- */

    /** Set the player in the area */
    public void setPlayer(SuperPacmanPlayer player) { this.player = player; }
}
