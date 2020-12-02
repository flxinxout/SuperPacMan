package ch.epfl.cs107.play.game.superpacman.actor.ghost;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RandomGenerator;

import java.util.Collections;
import java.util.List;

public class Blinky extends Ghost {
    public Blinky(Area area, Orientation orientation, DiscreteCoordinates home) {
        super(area, orientation, home, "superpacman/ghost.blinky");
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    @Override
    public Orientation getNextOrientation() {
        int randomInt = RandomGenerator.getInstance().nextInt(4);
        return Orientation.fromInt(randomInt);
    }
}
