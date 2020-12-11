package ch.epfl.cs107.play.game.superpacman.area;

import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.superpacman.actor.BonusPortal;
import ch.epfl.cs107.play.game.superpacman.actor.Gate;
import ch.epfl.cs107.play.game.superpacman.actor.collectable.Key;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.logic.And;
import ch.epfl.cs107.play.signal.logic.Logic;

/**
 * Bonus level of the game
 */
public class BonusLevel extends SuperPacmanArea {

    // The spawn position in the level
    private final DiscreteCoordinates PLAYER_SPAWN_POSITION = new DiscreteCoordinates(9, 19);


    /* --------------- Implement Playable --------------- */

    @Override
    public String getTitle() {
        return "superpacman/BonusLevel";
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
