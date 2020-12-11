package ch.epfl.cs107.play.game.superpacman.actor.ghost;

import ch.epfl.cs107.play.game.actor.SoundAcoustics;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.*;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.superpacman.actor.Killable;
import ch.epfl.cs107.play.game.superpacman.actor.SuperPacmanPlayer;
import ch.epfl.cs107.play.game.superpacman.actor.killer.Villain;
import ch.epfl.cs107.play.game.superpacman.handler.SuperPacmanInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RandomGenerator;
import ch.epfl.cs107.play.window.Canvas;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class that represents ghosts in the SuperPacman game
 */
public abstract class Ghost extends Villain implements Killable {

    // Constants of the Ghosts
    private final int GHOST_SCORE = 500;
    private final float PROTECTION_DURATION = 2;

    // Target's Attributes
    private SuperPacmanPlayer player;
    private DiscreteCoordinates targetPos;

    // Animation duration in frame number
    private final int ANIMATION_DURATION = 8;

    // Attributes of the ghost
    private final Animation afraidAnimation;
    private boolean isAfraid;
    private Animation currentAnimation;

    // Spawn protect (to avoid spawn kill)
    private boolean protect;
    private float timerProtection;

    // Handler of the ghost
    private final GhostHandler handler;

    /**
     * Default Ghost constructor
     * @param area        (Area): owner area. Not null
     * @param orientation (Orientation): initial orientation of the entity. Not null
     * @param home        (Coordinate): initial and home position of the ghost. Not null
     */
    public Ghost(Area area, Orientation orientation, DiscreteCoordinates home) {
        super(area, orientation, home,
                new SoundAcoustics("sounds/pacman/pacman_eatghost.wav", 0.50f, false,false,false, false),
                20, 5);

        // Creation of the handler
        handler = new GhostHandler();

        // Sets the afraid animation which is the same for all ghosts
        afraidAnimation = new Animation(ANIMATION_DURATION, Sprite.extractSprites("superpacman/ghost.afraid", 2, 1, 1, this, 16, 16));
        currentAnimation = getAnimations()[orientation.ordinal()];

        // Sets some attributes of the ghost
        isAfraid = false;

        // Default target position
        targetPos = getTargetPos();

        protect = false;
        timerProtection = PROTECTION_DURATION;
    }


    /* --------------- Implements Graphics --------------- */

    @Override
    public void update(float deltaTime) {
        setAnimations(deltaTime);

        if (!isDisplacementOccurs() && !protect) {
            Orientation desiredOrientation = getNextOrientation();

            if (getOwnerArea().canEnterAreaCells(this,
                    Collections.singletonList(getCurrentMainCellCoordinates().jump(desiredOrientation.toVector())))) {
                orientate(desiredOrientation);
            }
            move(getDEFAULT_SPEED());
        }

        super.update(deltaTime);

        // If the ghost and his target position are very close, we update the target in turns of parameters of the game
        if (targetPos != null && DiscreteCoordinates.distanceBetween(getCurrentMainCellCoordinates(), targetPos) < 0.1) {
            targetPos = getTargetPos();
        }

        // Check the protect state
        if (protect) {
            refreshProtection(deltaTime);
        }
    }

    @Override
    public void draw(Canvas canvas) {
        currentAnimation.draw(canvas);
    }


    /* --------------- Implements Killable --------------- */

    @Override
    public void onDeath() {
        super.onDeath();

        protect();
        player = null;
        targetPos = getTargetPos();
    }

    /* --------------- Implements Interactor --------------- */

    @Override
    public void interactWith(Interactable other) { other.acceptInteraction(handler); }

    @Override
    public boolean wantsViewInteraction() { return true; }

    @Override
    public boolean wantsCellInteraction() { return false; }


    /* --------------- Implements Interactable --------------- */

    @Override
    public void acceptInteraction(AreaInteractionVisitor v) { ((SuperPacmanInteractionVisitor)v).interactWith (this); }

    @Override
    public boolean isCellInteractable() { return true; }

    @Override
    public boolean isViewInteractable() { return false; }

    /* --------------- External Methods --------------- */

    /**
     * Sets the current animation of the Ghost
     * @param deltaTime (float): the delta time of the update. Not null
     */
    private void setAnimations(float deltaTime) {
        if (isAfraid) {
            currentAnimation = afraidAnimation;
        } else {
            if (isDisplacementOccurs()) {
                currentAnimation = getAnimations()[getOrientation().ordinal()];
            } else {
                currentAnimation.reset();
            }
        }

        currentAnimation.update(deltaTime);
    }

    /** Method that set the protect of the ghost when he's killed */
    private void protect() {
        protect = true;
    }

    /**
     * Method called in update to refresh the protect state of the ghost
     * @param deltaTime (float): the delta time of the update. Not null
     */
    private void refreshProtection(float deltaTime) {
        if (timerProtection > 0) {
            timerProtection -= deltaTime;
        } else {
            protect = false;
            timerProtection = PROTECTION_DURATION;
        }
    }

    /* --------------- Protected Methods --------------- */

    /** Called when the ghosts become scared or stop being scared */
    protected void onScareChange() {
        targetPos = getTargetPos();
    }

    /* --------------- Public Methods --------------- */

    /** Scares ghosts */
    public void scare() {
        isAfraid = true;
        onScareChange();
    }

    /** unScares ghosts */
    public void unScare() {
        isAfraid = false;
        onScareChange();
    }

    /* --------------- Getters --------------- */

    /** NEED TO BE OVERRIDDEN
     * @return the target accordingly to the circumstances
     */
    protected abstract DiscreteCoordinates getTargetPos();

    /** @return the animation duration of ghosts */
    protected int getAnimationDuration() { return ANIMATION_DURATION; }

    /** @return if ghosts are afraid */
    protected boolean isAfraid() { return isAfraid; }

    /** @return if this ghost is protected */
    public boolean isProtected() { return protect; }

    /** @return the score given on death */
    public int getScore() { return GHOST_SCORE; }

    /** @return the target of the ghost */
    protected SuperPacmanPlayer getPlayer() { return player; }


    /**
     * Interaction handler for a Ghost
     */
    private class GhostHandler implements SuperPacmanInteractionVisitor {
        @Override
        public void interactWith(SuperPacmanPlayer pacman) {
            player = pacman;
            targetPos = getTargetPos();
        }
    }
}
