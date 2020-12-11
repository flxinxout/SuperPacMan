package ch.epfl.cs107.play.game.areagame.actor;

import ch.epfl.cs107.play.game.actor.SoundAcoustics;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.handler.RPGInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

/**
 * Class that represents an entity in an area that can be collected by an actor
 */
public abstract class CollectableAreaEntity extends AreaEntity implements Collectable {

    private final SoundAcoustics soundAcoustics;

    /**
     * Default CollectableAreaEntity constructor
     *
     * @param area        (Area): the area where is the entity. Not null
     * @param orientation (Orientation): the orientation of the entity. Not null
     * @param position    (DiscreteCoordinates): the position in the area. Not null
     */
    public CollectableAreaEntity(Area area, Orientation orientation, DiscreteCoordinates position, SoundAcoustics soundAcoustics) {
        super(area, orientation, position);
        this.soundAcoustics = soundAcoustics;
    }

    /* ------------- Implement Collectable --------------- */

    @Override
    public void onCollect() {
        getOwnerArea().unregisterActor(this);
        soundAcoustics.shouldBeStarted();
        soundAcoustics.bip(getOwnerArea().getWindow());
    }

    /* ------------- Implement Interactable --------------- */

    @Override
    public boolean takeCellSpace() {
        return false;
    }

    @Override
    public boolean isCellInteractable() {
        return true;
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v) {
        ((RPGInteractionVisitor)v).interactWith(this);
    }
}
