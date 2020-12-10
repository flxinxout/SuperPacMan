package ch.epfl.cs107.play.game.superpacman.actor.collectable;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.CollectableAreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.superpacman.handler.SuperPacmanInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

/**
 * Class that represents a collectable entity that increases the player's score when it is collected
 */

public abstract class CollectableReward extends CollectableAreaEntity {
    /**
     * Default constructor of a collectable reward area entity
     * @param area the area where the collectable is
     * @param orientation the orientation
     * @param position the position in the area
     * @param reward the reward
     */
    public CollectableReward(Area area, Orientation orientation, DiscreteCoordinates position, int reward) {
        super(area, orientation, position);
    }

    /* ------------------- Implements Interactable ------------------ */

    @Override
    public void acceptInteraction(AreaInteractionVisitor v) {
        ((SuperPacmanInteractionVisitor)v).interactWith(this);
    }

    /* ------------------- Implements Collectable ------------------ */

    public abstract int getReward();
}
