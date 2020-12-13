package ch.epfl.cs107.play.game.superpacman.actor.ghost;

import ch.epfl.cs107.play.game.actor.SoundAcoustics;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.*;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.superpacman.actor.Killable;
import ch.epfl.cs107.play.game.superpacman.actor.SuperPacmanEnnemy;
import ch.epfl.cs107.play.game.superpacman.actor.SuperPacmanPlayer;
import ch.epfl.cs107.play.game.superpacman.handler.SuperPacmanInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Canvas;

import java.util.Collections;

/**
 * Class that represents ghosts in the SuperPacman game
 */
public abstract class Ghost extends SuperPacmanEnnemy implements Killable {

    // Constants
    private final int GHOST_SCORE = 500;

    // Target
    private SuperPacmanPlayer player;
    private DiscreteCoordinates targetPos;

    // Animations
    private final int ANIMATION_DURATION = 8; // Animation duration in frame number
    private final Animation AFRAID_ANIMATION;
    private Animation currentAnimation;

    // Attributes
    private final DiscreteCoordinates HOME;
    private boolean isAfraid;

    // Handler
    private final GhostHandler handler;

    /* --------------- EXTENSIONS --------------- */

    // Spawn protection (to avoid spawn kill)
    private boolean protect;
    private final float PROTECTION_DURATION = 2;
    private float timerProtection;
    private final Animation PROTECTED_ANIMATION;

    // Sounds
    private final SoundAcoustics DEATH_SOUND;


    /**
     * Default Ghost constructor
     * @param area        (Area): owner area. Not null
     * @param orientation (Orientation): initial orientation of the entity. Not null
     * @param home        (Coordinate): initial and HOME position of the ghost. Not null
     * @param speed       (int): initial speed of the ghost. Strictly greater than 0.
     * @param fieldOfView (int): field of view of the ghost. Strictly greater than 0.
     */
    public Ghost(Area area, Orientation orientation, DiscreteCoordinates home, int speed, int fieldOfView) {
        super(area, orientation, home, speed, fieldOfView);

        this.HOME = home;
        DEATH_SOUND = new SoundAcoustics("sounds/pacman/pacman_eatghost.wav", 0.5f, false, false, false, false);

        // Creation of the handler
        handler = new GhostHandler();

        // Sets the afraid animation which is the same for all ghosts
        AFRAID_ANIMATION = new Animation(ANIMATION_DURATION, Sprite.extractSprites("superpacman/ghost.afraid", 2, 1, 1, this, 16, 16));
        PROTECTED_ANIMATION = new Animation(ANIMATION_DURATION, Sprite.extractSprites("superpacman/ghost.protect", 2, 1, 1, this, 16, 16));
        currentAnimation = getAnimations()[orientation.ordinal()];

        // Sets some attributes of the ghost
        isAfraid = false;

        // Default target position
        targetPos = getTargetPos();

        protect = false;
        timerProtection = PROTECTION_DURATION;
    }

    /**
     * Default Ghost constructor
     * @param area        (Area): owner area. Not null
     * @param orientation (Orientation): initial orientation of the entity. Not null
     * @param home        (Coordinate): initial and HOME position of the ghost. Not null
     */
    public Ghost(Area area, Orientation orientation, DiscreteCoordinates home) {
        this(area, orientation, home, 20, 5);
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
            move(getSpeed());
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
        DEATH_SOUND.shouldBeStarted();
        DEATH_SOUND.bip(getOwnerArea().getWindow());

        // We spawn the ghost at its spawn location
        getOwnerArea().leaveAreaCells(this, getEnteredCells());
        setCurrentPosition(HOME.toVector());
        getOwnerArea().enterAreaCells(this, Collections.singletonList(HOME));
        resetMotion();

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
        if (protect) {
            currentAnimation = PROTECTED_ANIMATION;
        } else if (isAfraid) {
            currentAnimation = AFRAID_ANIMATION;
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

    /** @Note: NEED TO BE OVERRIDDEN
     * @return (Animation[]): the animations accordingly to the villain
     */
    protected abstract Animation[] getAnimations();

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

    /**@return (DiscreteCoordinates): the HOME */
    protected DiscreteCoordinates getHOME() { return HOME; }

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
