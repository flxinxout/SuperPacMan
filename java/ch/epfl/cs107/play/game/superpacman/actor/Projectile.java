package ch.epfl.cs107.play.game.superpacman.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.*;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.superpacman.handler.SuperPacmanInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

import java.util.Collections;
import java.util.List;

public abstract class Projectile extends MovableAreaEntity implements Interactor {
    // Handler of the projectile
    private ProjectileHandler handler;

    /**
     * Default Projectile constructor
     *
     * @param area         (Area): the area where the projectile is. Not null
     * @param orientation  (Orientation): the orientation of the projectile. Not null
     * @param position     (DiscreteCoordinates): the position of the projectile. Not null
     */
    public Projectile(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);

        handler = new ProjectileHandler();
    }

    /** Method that unregister the actor */
    private void leaveArea() {
        getOwnerArea().unregisterActor(this);
    }

    /* --------------- Extends MovableAreaEntity --------------- */

    /* --------------- Implements Graphics --------------- */

    /* --------------- Implements Interactable --------------- */

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
    public void acceptInteraction (AreaInteractionVisitor v) { ((SuperPacmanInteractionVisitor)v).interactWith (this ); }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() { return Collections.singletonList(getCurrentMainCellCoordinates()); }

    /* --------------- Implements Interactor --------------- */

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        move(getSpeed());

        if (Math.abs(getVelocity().x) < 0.1 && Math.abs(getVelocity().y) < 0.1) {
            getOwnerArea().unregisterActor(this);
        }
    }


    /* --------------- Implement Interactor --------------- */

    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() {
        return null;
    }

    @Override
    public boolean wantsCellInteraction() {
        return false;
    }

    @Override
    public boolean wantsViewInteraction() {
        return false;
    }

    @Override
    public void interactWith(Interactable other) {
        other.acceptInteraction(handler);
    }

    /* --------------- Abstract Methods --------------- */

    /**
     * @NEED TO BE OVERRIDDEN
     * @return (int): the speed of the projectile
     */
    protected abstract int getSpeed();


    /**
     * Interaction handler for a Projectile
     */
    private class ProjectileHandler implements SuperPacmanInteractionVisitor {
        @Override
        public void interactWith(Wall wall) {
            leaveArea();
        }

        @Override
        public void interactWith(SuperPacmanPlayer pacman) {
            leaveArea();
        }
    }
}
