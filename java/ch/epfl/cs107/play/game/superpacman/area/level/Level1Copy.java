package ch.epfl.cs107.play.game.superpacman.area.level;

import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.game.superpacman.actor.setting.Gate;
import ch.epfl.cs107.play.game.superpacman.area.SuperPacmanArea;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.logic.Logic;

/**
 * [EXTENSION] Copy of the Level 1 of the game but with heart extension
 */
public class Level1Copy extends SuperPacmanArea {

    // The spawn position in the level
    private final DiscreteCoordinates PLAYER_SPAWN_POSITION = new DiscreteCoordinates(15, 6);


    /* --------------- Implement Playable --------------- */

    @Override
    public String getTitle() {
        return "superpacman/Level1Copy";
    }


    /* --------------- Extends SuperPacmanArea --------------- */

    @Override
    protected void createArea() {
        super.createArea();

        // Registration of actors in the level

        Door door = new Door("superpacman/Level2Copy", new DiscreteCoordinates(15, 29), Logic.TRUE, this,
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
