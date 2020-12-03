package ch.epfl.cs107.play.game.superpacman.actor.ghost;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

public class Inky extends Ghost {
    public Inky(Area area, Orientation orientation, DiscreteCoordinates home) {
        super(area, orientation, home, "superpacman/ghost.inky");
    }

    @Override
    public Orientation getNextOrientation() {
        if (isAfraid) {
            
        }
    }
}
