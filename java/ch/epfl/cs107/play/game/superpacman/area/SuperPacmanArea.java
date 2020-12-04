package ch.epfl.cs107.play.game.superpacman.area;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.superpacman.SuperPacman;
import ch.epfl.cs107.play.game.superpacman.actor.collectable.Diamond;
import ch.epfl.cs107.play.game.superpacman.actor.ghost.Ghost;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Window;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public abstract class SuperPacmanArea extends Area implements Logic {
    private List<Ghost> ghosts = new ArrayList<>();
    private SuperPacmanBehavior behavior;
    private int diamondsNumber;

    //This signal is activated when every collectable in the area has been collected
    private boolean isCompleted;

    @Override
    public boolean begin(Window window, FileSystem fileSystem) {
        super.begin(window, fileSystem);
        if (super.begin(window, fileSystem)) {
            // Set the behavior map
            behavior = new SuperPacmanBehavior(window, getTitle());
            setBehavior(behavior);
            createArea();
            return true;
        }
        return false;
    }

    //TODO: SEE IF IT STAYS NON-ABSTRACT???
    protected void createArea() {
        behavior.registerActors(this);
    }

    public Queue<Orientation> shortestPath(DiscreteCoordinates from, DiscreteCoordinates to) {
        return behavior.shortestPath(from, to);
    }

    abstract public DiscreteCoordinates getSpawnLocation();

    public void addDiamond() {
        diamondsNumber++;
    }

    public void addGhost(Ghost ghost) {
        ghosts.add(ghost);
    }

    public List<Ghost> getGhosts() {
        return ghosts;
    }

    public void removeDiamond() {
        diamondsNumber--;
        if (diamondsNumber < 0) { diamondsNumber = 0; }
        if (diamondsNumber == 0) {
            isCompleted = true;
        }
    }

    @Override
    public float getCameraScaleFactor() {
        return SuperPacman.CAMERA_SCALE_FACTOR;
    }

    /* ---------------- Implement Logic ---------------- */

    @Override
    public boolean isOn() {
        return !isCompleted;
    }

    @Override
    public boolean isOff() {
        return isCompleted;
    }

    @Override
    public float getIntensity() {
        return (isCompleted) ? 1 : 0;
    }
}
