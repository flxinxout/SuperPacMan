package ch.epfl.cs107.play.game.superpacman.area;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Window;

public abstract class SuperPacmanArea extends Area {
    private SuperPacmanBehavior behavior;

    //TODO: SEE IF IT STAYS NON-ABSTRACT???
    protected void createArea() {
        behavior.registerActors(this);
    }

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

    @Override
    public float getCameraScaleFactor() {
        return 15.f;
    }
}
