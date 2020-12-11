package ch.epfl.cs107.play.game.superpacman.area;

import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.game.superpacman.actor.DarkLord;
import ch.epfl.cs107.play.game.superpacman.actor.Gate;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.logic.Logic;

/**
 * Level 1 of the game
 */
public class Level1 extends SuperPacmanArea{

    // The spawn position in the level
    private final DiscreteCoordinates PLAYER_SPAWN_POSITION = new DiscreteCoordinates(15, 6);


    /* --------------- Implement Playable --------------- */

    @Override
    public String getTitle() {
        return "superpacman/Level1";
    }


    /* --------------- Extends SuperPacmanArea --------------- */

    @Override
    protected void createArea() {
        super.createArea();

        // Registration of actors in the level

        DarkLord darkLord = new DarkLord(this, Orientation.DOWN, new DiscreteCoordinates(11, 3));
        registerActor(darkLord);

        Door door = new Door("superpacman/Level2", new DiscreteCoordinates(15, 29), Logic.TRUE, this,
                Orientation.DOWN, new DiscreteCoordinates(14, 0), new DiscreteCoordinates(15, 0));
        registerActor(door);

        Gate gate1 = new Gate(this, Orientation.RIGHT, new DiscreteCoordinates(14,3), this);
        registerActor(gate1);

        Gate gate2 = new Gate(this, Orientation.RIGHT, new DiscreteCoordinates(15,3), this);
        registerActor(gate2);
    }

    @Override
    public DiscreteCoordinates getSpawnLocation() {
        return PLAYER_SPAWN_POSITION;
    }
}
