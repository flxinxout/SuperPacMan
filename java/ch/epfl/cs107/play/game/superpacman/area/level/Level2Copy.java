package ch.epfl.cs107.play.game.superpacman.area.level;

import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.game.rpg.actor.DoorGraphics;
import ch.epfl.cs107.play.game.superpacman.actor.setting.Gate;
import ch.epfl.cs107.play.game.superpacman.actor.collectable.Key;
import ch.epfl.cs107.play.game.superpacman.area.SuperPacmanArea;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.logic.And;
import ch.epfl.cs107.play.signal.logic.Logic;

/**
 * [EXTENSION] Copy of the Level 2 of the game but with heart and levels extension
 */
public class Level2Copy extends SuperPacmanArea {

    // The spawn position in the level
    private final DiscreteCoordinates PLAYER_SPAWN_POSITION = new DiscreteCoordinates(15, 29);


    /* --------------- Implements Playable --------------- */

    @Override
    public String getTitle() {
        return "superpacman/Level2Copy";
    }


    /* --------------- Extends SuperPacmanArea --------------- */

    @Override
    protected void createArea() {
        super.createArea();

        // Registration of actors in the level

        Key key1 = new Key(this, new DiscreteCoordinates(3, 16));
        registerActor(key1);

        Key key2 = new Key(this, new DiscreteCoordinates(26, 16));
        registerActor(key2);

        Key key3 = new Key(this, new DiscreteCoordinates(2, 8));
        registerActor(key3);

        Key key4 = new Key(this, new DiscreteCoordinates(27, 8));
        registerActor(key4);

        Gate gate1 = new Gate(this, Orientation.RIGHT, new DiscreteCoordinates(8,14), key1);
        registerActor(gate1);

        Gate gate2 = new Gate(this, Orientation.DOWN, new DiscreteCoordinates(5,12), key1);
        registerActor(gate2);

        Gate gate3 = new Gate(this, Orientation.RIGHT, new DiscreteCoordinates(8,10), key1);
        registerActor(gate3);

        Gate gate4 = new Gate(this, Orientation.RIGHT, new DiscreteCoordinates(8,8), key1);
        registerActor(gate4);

        Gate gate5 = new Gate(this, Orientation.RIGHT, new DiscreteCoordinates(21,14), key2);
        registerActor(gate5);

        Gate gate6 = new Gate(this, Orientation.DOWN, new DiscreteCoordinates(24,12), key2);
        registerActor(gate6);

        Gate gate7 = new Gate(this, Orientation.RIGHT, new DiscreteCoordinates(21,10), key2);
        registerActor(gate7);

        Gate gate8 = new Gate(this, Orientation.RIGHT, new DiscreteCoordinates(21,8), key2);
        registerActor(gate8);

        Gate gate9 = new Gate(this, Orientation.RIGHT, new DiscreteCoordinates(10,2), new And(key3, key4));
        registerActor(gate9);

        Gate gate10 = new Gate(this, Orientation.RIGHT, new DiscreteCoordinates(19,2), new And(key3, key4));
        registerActor(gate10);

        Gate gate11 = new Gate(this, Orientation.RIGHT, new DiscreteCoordinates(12,8), new And(key3, key4));
        registerActor(gate11);

        Gate gate12 = new Gate(this, Orientation.RIGHT, new DiscreteCoordinates(17,8), new And(key3, key4));
        registerActor(gate12);

        Gate gate13 = new Gate(this, Orientation.RIGHT, new DiscreteCoordinates(14,3), this);
        registerActor(gate13);

        Gate gate14 = new Gate(this, Orientation.RIGHT, new DiscreteCoordinates(15,3), this);
        registerActor(gate14);

        /* --------------- EXTENSIONS --------------- */

        DoorGraphics bonusDoor = new DoorGraphics("superpacman/BonusLevel", new DiscreteCoordinates(9, 18),
                Logic.TRUE, this, Orientation.DOWN, new DiscreteCoordinates(27,5));
        registerActor(bonusDoor);

        Door bossDoor = new Door("superpacman/BossLevel", new DiscreteCoordinates(1, 9), Logic.TRUE,
                this, Orientation.DOWN, new DiscreteCoordinates(14, 3), new DiscreteCoordinates(15, 3));
        registerActor(bossDoor);
    }

    @Override
    public DiscreteCoordinates getSpawnLocation() {
        return PLAYER_SPAWN_POSITION;
    }

}
