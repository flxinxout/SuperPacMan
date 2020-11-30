package ch.epfl.cs107.play.game.superpacman.actor.collectable;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.CollectableAreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.superpacman.SuperPacman;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

//TODO: DO WE DELETE THIS CLASS?
public class CollectableReward extends CollectableAreaEntity {
    private final int REWARD;

    public CollectableReward(Area area, Orientation orientation, DiscreteCoordinates position, int reward) {
        super(area, orientation, position);
        REWARD = reward;
    }

    @Override
    public void onCollect() {
        super.onCollect();
        SuperPacman.player.addScore(REWARD);
    }
}
