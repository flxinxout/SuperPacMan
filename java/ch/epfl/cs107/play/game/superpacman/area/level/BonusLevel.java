package ch.epfl.cs107.play.game.superpacman.area.level;

import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.game.superpacman.area.SuperPacmanArea;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.logic.Logic;

/**
 * [EXTENSION] Bonus level of the game
 */
public class BonusLevel extends SuperPacmanArea {

    private final DiscreteCoordinates PLAYER_SPAWN_POSITION = new DiscreteCoordinates(9, 18);


    /* --------------- Implements Playable --------------- */

    @Override
    public String getTitle() {
        return "superpacman/BonusLevel";
    }

    /* --------------- Extends SuperPacmanArea --------------- */

    @Override
    protected void createArea() {
        super.createArea();

        // Registration of actors in the level

        Door door = new Door("superpacman/Level2", new DiscreteCoordinates(15, 29), Logic.TRUE, this,
                Orientation.DOWN, new DiscreteCoordinates(9, 0), new DiscreteCoordinates(10, 0));
        registerActor(door);
    }

    @Override
    public DiscreteCoordinates getSpawnLocation() {
        return PLAYER_SPAWN_POSITION;
    }
}
