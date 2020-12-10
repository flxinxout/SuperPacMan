package ch.epfl.cs107.play.game.superpacman.actor.ghost;

import ch.epfl.cs107.play.game.actor.SoundAcoustics;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.*;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.superpacman.actor.Killable;
import ch.epfl.cs107.play.game.superpacman.actor.SuperPacmanPlayer;
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
public abstract class Ghost extends MovableAreaEntity implements Interactor, Killable {

    // Constants of the Ghosts
    private final int GHOST_SCORE = 500;
    private final int FIELD_OF_VIEW = 5;
    private final int DEFAULT_SPEED = 20;
    private final float PROTECTION_DURATION = 2;
    private final SoundAcoustics DEATH_SOUND;

    // Animation duration in frame number
    private final int ANIMATION_DURATION = 8;

    // Attributes of the ghost
    private final Animation afraidAnimation;
    private boolean isAfraid;
    private int speed;

    // Animation
    private Animation[] animations;
    private Animation currentAnimation;

    // The spawn of the ghost
    private final DiscreteCoordinates home;

    // Target's Attributes
    private SuperPacmanPlayer player;
    private DiscreteCoordinates targetPos;

    // Spawn protect (to avoid spawn kill)
    private boolean protect;
    private float timerProtection;

    // Handler of the ghost
    private final GhostHandler handler;

    /**
     * Default Ghost constructor
     * @param area        (Area): Owner area. Not null
     * @param orientation (Orientation): Initial orientation of the entity. Not null
     * @param home        (Coordinate): Initial and home position of the ghost. Not null
     */
    public Ghost(Area area, Orientation orientation, DiscreteCoordinates home) {
        super(area, orientation, home);

        // Creation of the handler
        handler = new GhostHandler();

        // Sets the afraid animation which is the same for all ghosts
        afraidAnimation = new Animation(ANIMATION_DURATION, Sprite.extractSprites("superpacman/ghost.afraid", 2, 1, 1, this, 16, 16));

        // Sets all animations of ghosts
        animations = getAnimations();
        currentAnimation = animations[orientation.ordinal()];

        // Sets some attributes of the ghost
        this.home = home;
        speed = DEFAULT_SPEED;
        isAfraid = false;
        DEATH_SOUND = new SoundAcoustics("sounds/pacman/pacman_eatghost.wav", 0.50f, false,false,false, false);

        // Default target position
        targetPos = getTargetPos();

        protect = false;
        timerProtection = PROTECTION_DURATION;
    }


    /* --------------- Implements Actor --------------- */

    @Override
    public void update(float deltaTime) {
        setAnimations(deltaTime);

        if (!isDisplacementOccurs()) {
            Orientation desiredOrientation = getNextOrientation();

            if (getOwnerArea().canEnterAreaCells(this,
                    Collections.singletonList(getCurrentMainCellCoordinates().jump(desiredOrientation.toVector())))) {
                orientate(desiredOrientation);
            }
            move(speed);
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

        protect();
        getOwnerArea().leaveAreaCells(this, getEnteredCells());
        setCurrentPosition(home.toVector());
        getOwnerArea().enterAreaCells(this, Collections.singletonList(home));
        resetMotion();

        player = null;
        targetPos = getTargetPos();
    }

    /* --------------- Implements Interactor --------------- */

    @Override
    public void interactWith(Interactable other) { other.acceptInteraction(handler); }

    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() {
        List<DiscreteCoordinates> fieldOfView = new ArrayList<>();

        // Add the coordinates that are in the field of view of the ghost
        for (int y = -FIELD_OF_VIEW; y <= FIELD_OF_VIEW; y++) {
            for (int x = -FIELD_OF_VIEW; x <= FIELD_OF_VIEW; x++) {
                fieldOfView.add(new DiscreteCoordinates(getCurrentMainCellCoordinates().x + x, getCurrentMainCellCoordinates().y + y));
            }
        }

        return fieldOfView;
    }

    @Override
    public boolean wantsViewInteraction() { return true; }

    @Override
    public boolean wantsCellInteraction() { return false; }


    /* --------------- Implements Interactable --------------- */

    @Override
    public void acceptInteraction(AreaInteractionVisitor v) { ((SuperPacmanInteractionVisitor)v).interactWith (this); }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() { return Collections.singletonList(getCurrentMainCellCoordinates()); }

    @Override
    public boolean isCellInteractable() { return true; }

    @Override
    public boolean isViewInteractable() { return false; }

    @Override
    public boolean takeCellSpace() { return false; }


    /* --------------- External Methods --------------- */

    /**
     * Sets the current animation of the Ghost
     * @param deltaTime the delta time of the update
     */
    private void setAnimations(float deltaTime) {
        if (isAfraid) {
            currentAnimation = afraidAnimation;
        } else {
            if (isDisplacementOccurs()) {
                currentAnimation = animations[getOrientation().ordinal()];
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
     * @param deltaTime (float) the delta time of the update
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

    /**
     * Choose a random cell in a specific radius around another cell
     * @param centerPos (DiscreteCoordinates) the center cell
     * @param radius (int) the radius allowed
     * @return the cell
     */
    protected DiscreteCoordinates randomCell(DiscreteCoordinates centerPos, int radius) {
        int randomX, randomY;
        DiscreteCoordinates randomCoordinates;

        // Generate a random coordinate in the current area until this coordinate will be smaller that the allowed radius of the ghost
        do {
            randomX = RandomGenerator.getInstance().nextInt(getOwnerArea().getWidth());
            randomY = RandomGenerator.getInstance().nextInt(getOwnerArea().getHeight());
            randomCoordinates = new DiscreteCoordinates(randomX, randomY);
        }while (DiscreteCoordinates.distanceBetween(centerPos, randomCoordinates) > radius);

        return randomCoordinates;
    }

    //TODO: check if there's already a way to get the center of the area + the radius
    /** @return a random cell in an entire map*/
    protected DiscreteCoordinates randomCell() {
        int width = getOwnerArea().getWidth();
        int height = getOwnerArea().getHeight();

        DiscreteCoordinates center = new DiscreteCoordinates(width/2, height/2);
        int radius = (int) (Math.sqrt(width * width + height * height)/2 + 1);

        return randomCell(center, radius);
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

    /* --------------- Setters --------------- */

    /** Sets the speed */
    protected void setSpeed(int speed) { this.speed = speed; }

    /* --------------- Getters --------------- */

    /** NEED TO BE OVERRIDDEN
     *  @return the target accordingly to the circumstances
     */
    protected abstract DiscreteCoordinates getTargetPos();

    /** NEED TO BE OVERRIDDEN
     * @return the animations accordingly to the ghost
     */
    protected abstract Animation[] getAnimations();

    /** NEED TO BE OVERRIDDEN
     * @return the next orientation following a specific algorithm
     */
    protected abstract Orientation getNextOrientation();

    /**@return the home of the ghost */
    protected DiscreteCoordinates getHome() { return home; }

    /**@return the animation duration of ghosts */
    protected int getAnimationDuration() { return ANIMATION_DURATION; }

    /**@return the target of the ghost */
    protected SuperPacmanPlayer getPlayer() { return player; }

    /**@return if ghosts are afraid */
    protected boolean isAfraid() { return isAfraid; }

    /**@return if this ghost is protected */
    public boolean isProtected() { return protect; }

    /**@return the default speed of the ghost */
    protected int getDEFAULT_SPEED() { return DEFAULT_SPEED; }

    /**@return the score given on death */
    public int getScore() { return GHOST_SCORE; }


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
