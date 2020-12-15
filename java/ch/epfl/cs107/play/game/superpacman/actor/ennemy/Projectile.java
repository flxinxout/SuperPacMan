package ch.epfl.cs107.play.game.superpacman.actor.ennemy;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.*;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.superpacman.handler.SuperPacmanInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

import java.util.Collections;
import java.util.List;

/**
 * [EXTENSION] Projectiles are movable entities that move following a straight line.
 * It leaves the area when it stops moving.
 */
public abstract class Projectile extends MovableAreaEntity {

    /**
     * Default Projectile constructor
     *
     * @param area         (Area): the area where the projectile is. Not null
     * @param orientation  (Orientation): the orientation of the projectile. Not null
     * @param position     (DiscreteCoordinates): the position of the projectile. Not null
     */
    public Projectile(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);
    }

    /* --------------- Extends AreaEntity --------------- */

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        move(getSpeed());

        if (Math.abs(getVelocity().x) < 0.1 && Math.abs(getVelocity().y) < 0.1) getOwnerArea().unregisterActor(this);
    }

    @Override
    public boolean takeCellSpace() {
        return true;
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
    public void acceptInteraction (AreaInteractionVisitor v) { ((SuperPacmanInteractionVisitor)v).interactWith (this ); }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() { return Collections.singletonList(getCurrentMainCellCoordinates()); }

    /* --------------- Protected Methods --------------- */

    /**
     * Getter for the speed
     * @return (int)
     */
    protected abstract int getSpeed();
}
