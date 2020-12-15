package ch.epfl.cs107.play.game.superpacman.area.SuperPacman;

import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.superpacman.actor.ennemy.Boss;
import ch.epfl.cs107.play.game.superpacman.area.SuperPacmanArea;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

/**
 * [EXTENSION] Boss level of the game
 */
public class BossLevel extends SuperPacmanArea {

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

        DiscreteCoordinates[] bossLivesPos = new DiscreteCoordinates[] { new DiscreteCoordinates(5, 10), new DiscreteCoordinates(8, 5),
                new DiscreteCoordinates(8, 16), new DiscreteCoordinates(17, 11) };
        Boss boss = new Boss(this, Orientation.RIGHT, new DiscreteCoordinates(12,9), bossLivesPos);
        registerActor(boss);
    }

    @Override
    public DiscreteCoordinates getSpawnLocation() {
        return PLAYER_SPAWN_POSITION;
    }

}

