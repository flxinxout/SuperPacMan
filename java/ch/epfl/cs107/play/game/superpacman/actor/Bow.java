package ch.epfl.cs107.play.game.superpacman.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.*;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.superpacman.handler.SuperPacmanInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Canvas;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Bow extends AreaEntity implements Interactor {

    // Attributes
    private boolean isShooting;
    private SuperPacmanPlayer player;
    private float shootTimer;

    // Constants
    private final float SHOOT_RATE = 1.5f;

    // Animations
    private final int ANIMATION_DURATION = 2; // Animation duration in frame number
    private Animation[] animations;
    private Animation currentAnimation;

    //Handler
    private BowHandler handler;

    /**
     * Default Bow constructor
     * @param area        (Area): Owner area. Not null
     * @param orientation (Orientation): Initial orientation of the entity in the Area. Not null
     * @param position    (DiscreteCoordinate): Initial position of the entity in the Area. Not null
     */
    public Bow(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);

        isShooting = false;
        shootTimer = 0f;

        Sprite[][] sprites = Sprite.extractSprites("superpacman/bow", 4, 1.f, 1.f, this, 27, 27,
                new Orientation [] { Orientation.DOWN , Orientation.LEFT , Orientation.UP , Orientation.RIGHT });
        animations = Animation.createAnimations(ANIMATION_DURATION, sprites, false);
        currentAnimation = animations[orientation.ordinal()];

        handler = new BowHandler();
    }

    /* --------------- Private Methods --------------- */

    private void refreshPlayerTargetting() {
        if (player != null && player.getPosition().x != getCurrentMainCellCoordinates().x
                && player.getPosition().y != getCurrentMainCellCoordinates().y) {
            player = null;
        }
    }

    private void refreshShootTimer(float deltaTime) {
        if (shootTimer > 0) {
            shootTimer -= deltaTime;
        } else if (shootTimer < 0){
            shootTimer = 0;
        }
    }

    private void shoot() {
        shootTimer = SHOOT_RATE;

        Arrow arrow = new Arrow(getOwnerArea(), getOrientation(), getCurrentMainCellCoordinates().jump(getOrientation().toVector()));
        if (getOwnerArea().canEnterAreaCells(arrow, Collections.singletonList(getCurrentMainCellCoordinates().jump(getOrientation().toVector())))) {
            getOwnerArea().registerActor(arrow);
        }
    }

    /**
     * @return (Orientation): the next orientation following a specific algorithm
     */
    private Orientation getNextOrientation() {
        if (player != null) {
            if (player.getPosition().x < getCurrentMainCellCoordinates().x) {
                return Orientation.LEFT;
            }
            else if ( player.getPosition().x > getCurrentMainCellCoordinates().x) {
                return Orientation.RIGHT;
            }
            else if (player.getPosition().y < getCurrentMainCellCoordinates().y) {
                return Orientation.DOWN;
            }
            else if ( player.getPosition().y > getCurrentMainCellCoordinates().y) {
                return Orientation.UP;
            }
            else {
                return getOrientation();
            }
        }
        else {
            return getOrientation();
        }
    }

    /* --------------- Extends AreaEntity --------------- */

    @Override
    public void draw(Canvas canvas) {
        currentAnimation.draw(canvas);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        // Refresh player targeting
        if (player != null && player.getPosition().x != getCurrentMainCellCoordinates().x
                && player.getPosition().y != getCurrentMainCellCoordinates().y) {
            player = null;
        }

        // Orientate
        if (getNextOrientation() != getOrientation()) {
            orientate(getNextOrientation());
            currentAnimation = animations[getOrientation().ordinal()];
        }

        // Shoot mechanism
        refreshShootTimer(deltaTime);
        if (player != null && shootTimer == 0) {
            isShooting = true;
        }

        if (isShooting) {
            currentAnimation.update(deltaTime);
        }

        if (currentAnimation.isCompleted()) {
            shoot();
            isShooting = false;
            currentAnimation.reset();
        }
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

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
        ((SuperPacmanInteractionVisitor)v).interactWith (this );
    }

    /* --------------- Implements Interactor --------------- */


    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() {
        //return a + (cross) field of view
        List<DiscreteCoordinates> fieldOfViewList = new ArrayList<>();

        for (int x = 0; x < getOwnerArea().getWidth(); x++) {
            fieldOfViewList.add(new DiscreteCoordinates(x, getCurrentMainCellCoordinates().y));
        }

        for (int y = 0; y < getOwnerArea().getWidth(); y++) {
            fieldOfViewList.add(new DiscreteCoordinates(getCurrentMainCellCoordinates().x, y));
        }

        return  fieldOfViewList;
    }

    @Override
    public boolean wantsCellInteraction() {
        return false;
    }

    @Override
    public boolean wantsViewInteraction() {
        return true;
    }

    @Override
    public void interactWith(Interactable other) { other.acceptInteraction(handler); }

    /**
     * Interaction handler for a Bow
     */
    private class BowHandler implements SuperPacmanInteractionVisitor {
        @Override
        public void interactWith(SuperPacmanPlayer pacman) {
            player = pacman;
        }
    }
}
