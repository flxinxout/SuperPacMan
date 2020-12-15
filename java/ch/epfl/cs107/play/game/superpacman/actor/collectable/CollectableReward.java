package ch.epfl.cs107.play.game.superpacman.actor.collectable;

import ch.epfl.cs107.play.game.actor.SoundAcoustics;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.CollectableAreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.superpacman.handler.SuperPacmanInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

//TODO: DO WE KEEP IT?
/**
 * A CollectableRweard represents a collectable entity that increases the player's score when it is collected
 */
public abstract class CollectableReward extends CollectableAreaEntity {

    /**
     * Default constructor of a collectable reward area entity
     *
     * @param area         (Area): the area where the collectable is. Not null
     * @param orientation  (Orientation): the orientation. No null
     * @param position     (DiscreteCoordinates): the position in the area. Not null
     */
    public CollectableReward(Area area, Orientation orientation, DiscreteCoordinates position, SoundAcoustics soundAcoustics) {
        super(area, orientation, position, soundAcoustics);
    }

    /* ------------------- Implements Interactable ------------------ */

    @Override
    public void acceptInteraction(AreaInteractionVisitor v) {
        ((SuperPacmanInteractionVisitor)v).interactWith(this);
    }

    /* ------------------- Implements Collectable ------------------ */

    public abstract int getReward();
}
