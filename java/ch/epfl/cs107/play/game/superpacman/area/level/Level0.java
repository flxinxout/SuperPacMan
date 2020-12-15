package ch.epfl.cs107.play.game.superpacman.area.level;

import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.game.superpacman.actor.setting.Gate;
import ch.epfl.cs107.play.game.superpacman.actor.collectable.Key;
import ch.epfl.cs107.play.game.superpacman.area.SuperPacmanArea;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.logic.Logic;

/**
 * Level 0 of the game
 */
public class Level0 extends SuperPacmanArea {

    // The spawn position in the level
    private final DiscreteCoordinates PLAYER_SPAWN_POSITION = new DiscreteCoordinates(10, 1);


    /* --------------- Implement Playable --------------- */

    @Override
    public String getTitle() {
        return "superpacman/Level0";
    }


    /* --------------- Extends SuperPacmanArea --------------- */

    @Override
    protected void createArea() {
        super.createArea();

        // Registration of actors in the level
        Door door = new Door("superpacman/Level1", new DiscreteCoordinates(15, 6), Logic.TRUE, this,
                Orientation.UP, new DiscreteCoordinates(5, 9), new DiscreteCoordinates(6, 9));
        registerActor(door);

        Key key = new Key(this, new DiscreteCoordinates(3, 4));
        registerActor(key);

        Gate gate1 = new Gate(this, Orientation.RIGHT, new DiscreteCoordinates(5,8), key);
        registerActor(gate1);

        Gate gate2 = new Gate(this, Orientation.LEFT, new DiscreteCoordinates(6,8), key);
        registerActor(gate2);
    }

    @Override
    public DiscreteCoordinates getSpawnLocation() {
        return PLAYER_SPAWN_POSITION;
    }
}
