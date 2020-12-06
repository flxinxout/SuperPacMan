package ch.epfl.cs107.play.game.superpacman.actor.collectable;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.CollectableAreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.superpacman.SuperPacman;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

/**
 * Class that represents a collectable entity, but with a reward when it is collected
 */

public abstract class CollectableReward extends CollectableAreaEntity {

    // The reward of the collectable
    private final int REWARD;

    /**
     * Default constructor of a collectable with a reward
     * @param area the area where the collectable is
     * @param orientation the orientation
     * @param position the position in the area
     * @param reward the reward
     */
    public CollectableReward(Area area, Orientation orientation, DiscreteCoordinates position, int reward) {
        super(area, orientation, position);
        REWARD = reward;
    }


    /* ------------------- Implement Collectable ------------------ */

    @Override
    public void onCollect() {
        super.onCollect();
        SuperPacman.player.addScore(REWARD);
    }
}
