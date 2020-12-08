package ch.epfl.cs107.play.game.areagame.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.RPG;
import ch.epfl.cs107.play.game.rpg.handler.RPGInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

import java.util.Collections;
import java.util.List;

/**
 * class that represents an entity in an area that can be collected by an actor
 */

public abstract class CollectableAreaEntity extends AreaEntity implements Collectable {

    /**
     * Default constructor
     * @param area the area where is the entity
     * @param orientation the orientation
     * @param position the position in the area
     */
    public CollectableAreaEntity(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);
    }

    /* ------------- Implement Collectable --------------- */

    @Override
    public void onCollect() {
        getOwnerArea().unregisterActor(this);
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
