package ch.epfl.cs107.play.game.areagame.actor;


import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.handler.RPGInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.swing.SoundItem;
import ch.epfl.cs107.play.window.swing.SwingSound;

import javax.sound.sampled.*;
import javax.sound.sampled.spi.AudioFileReader;
import java.io.*;
import java.util.Collections;
import java.util.List;

/**
 * class that represents an entity in an area that can be collected by an actor
 */

public abstract class CollectableAreaEntity extends AreaEntity implements Collectable {

    // Depth of collectable entities
    private final int SPRITE_DEPTH = 100;

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
    public List<DiscreteCoordinates> getCurrentCells() { return Collections.singletonList(getCurrentMainCellCoordinates()); }

    @Override
    public boolean takeCellSpace() {
        return false;
    }

    @Override
    public boolean isCellInteractable() {
        return true;
    }

    @Override
    public boolean isViewInteractable() {
        return false;
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v) {
        ((RPGInteractionVisitor)v).interactWith(this);
    }


    /* ------------- External Methods --------------- */

    protected int getSPRITE_DEPTH() {
        return SPRITE_DEPTH;
    }
}
