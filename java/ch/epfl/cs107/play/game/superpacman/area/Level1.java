package ch.epfl.cs107.play.game.superpacman.area;

import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.game.superpacman.actor.Gate;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.logic.Logic;

public class Level1 extends SuperPacmanArea{
    //TODO: TRY TO PUT IT IN THE SUPER CLASS
    private final DiscreteCoordinates PLAYER_SPAWN_POSITION = new DiscreteCoordinates(15, 6);

    @Override
    public String getTitle() {
        return "superpacman/Level1";
    }

    @Override
    protected void createArea() {
        super.createArea();

        Door door = new Door("superpacman/Level2", new DiscreteCoordinates(15, 29), Logic.TRUE, this,
                Orientation.DOWN, new DiscreteCoordinates(14, 0), new DiscreteCoordinates(15, 0));
        registerActor(door);

        Gate gate1 = new Gate(this, Orientation.RIGHT, new DiscreteCoordinates(14,3), this);
        registerActor(gate1);

        Gate gate2 = new Gate(this, Orientation.RIGHT, new DiscreteCoordinates(15,3), this);
        registerActor(gate2);
    }
}
