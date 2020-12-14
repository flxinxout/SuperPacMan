package ch.epfl.cs107.play.game.superpacman.area;

import ch.epfl.cs107.play.math.DiscreteCoordinates;

/**
 * Boss level of the game
 */
public class BossLevel extends SuperPacmanArea{

    // The spawn position in the level
    private final DiscreteCoordinates PLAYER_SPAWN_POSITION = new DiscreteCoordinates(1, 9);


    /* --------------- Implements Playable --------------- */

    @Override
    public String getTitle() {
        return "superpacman/BossLevel";
    }


    /* --------------- Extends SuperPacmanArea --------------- */

    @Override
    protected void createArea() {
        super.createArea();
    }

    @Override
    public DiscreteCoordinates getSpawnLocation() {
        return PLAYER_SPAWN_POSITION;
    }

}

