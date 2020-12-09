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
    public final static float CAMERA_SCALE_FACTOR = 20.f;

    private SuperPacmanPlayer player;

    // Behavior of the area
    private SuperPacmanBehavior behavior;
    // Attributes of the area
    private int diamondsNumber;
    // This signal is activated when every collectable in the area has been collected
    private boolean isCompleted;

    /// pause mechanics and menu to display. May be null
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
        desiredGUI = 0;
        return super.resume(window, fileSystem);
    }

    public void gameOver() {
        won = false;
        end();
    }
    protected void createArea() {
        behavior.registerActors(this);
    }

    public void addDiamond() {
        diamondsNumber++;
    }

    public void removeDiamond() {
        diamondsNumber--;
        if (diamondsNumber < 0) { diamondsNumber = 0; }
        if (diamondsNumber == 0) {
            // If there are 0 diamond on the area, the signal is set on
            isCompleted = true;

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
            createArea();

            desiredGUI = 0;

            //Initialisation GUIs
            pauseGUI = new Menu[2];
            for (int i = 0; i < pauseGUI.length; i++) {
                pauseGUI[i] = new Menu("superpacman/pause.menu" + i);
            }
            gameOverGUI = new Menu("superpacman/gameover.menu");
            winGUI = new Menu("superpacman/win.menu");
            won = false;

            return true;
        }

        return false;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        if (isPaused()) {
            //Adapt the arrow in the pause GUI
            pauseGUI[desiredGUI].draw(getWindow());
            if (getKeyboard().get(Keyboard.UP).isLastPressed()) {
                desiredGUI = 0;
            } else if (getKeyboard().get(Keyboard.DOWN).isLastPressed()) {
                desiredGUI = 1;
            }

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
    public float getCameraScaleFactor() {
        return CAMERA_SCALE_FACTOR;
    }

    public AreaGraph getGraph() { return behavior.getGraph(); }

    public SuperPacmanBehavior getBehavior() {
        return behavior;
    }

    public SuperPacmanPlayer getPlayer() { return player; }

    /* --------------- Setters --------------- */

    public void setPlayer(SuperPacmanPlayer player) { this.player = player; }
}
