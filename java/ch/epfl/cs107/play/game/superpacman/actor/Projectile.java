package ch.epfl.cs107.play.game.superpacman.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.*;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.superpacman.handler.SuperPacmanInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Canvas;

import java.util.Collections;
import java.util.List;

public abstract class Projectile extends MovableAreaEntity implements Interactor {
    private Animation[] animations;
    private Animation currentAnimation;

    private ProjectileHandler handler;

    public Projectile(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);

        animations = getAnimations();
        currentAnimation = animations[orientation.ordinal()];

        handler = new ProjectileHandler();
    }

    private void leaveArea() {
        getOwnerArea().unregisterActor(this);
    }

    /* --------------- Extends MovableAreaEntity --------------- */

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        move(getSpeed());

        currentAnimation.update(deltaTime);

        if (Math.abs(getVelocity().x) < 0.1 && Math.abs(getVelocity().y) < 0.1) {
            leaveArea();
        }
    }

    @Override
    public void draw(Canvas canvas) { currentAnimation.draw(canvas); }

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
    public void acceptInteraction (AreaInteractionVisitor v) { ((SuperPacmanInteractionVisitor)v).interactWith (this ); }

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

    /* --------------- Getters --------------- */

    protected abstract Animation[] getAnimations();

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
