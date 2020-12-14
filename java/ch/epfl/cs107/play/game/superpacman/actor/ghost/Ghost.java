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
public abstract class Ghost extends MovableAreaEntity implements Killable, Interactor {

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
    private final int FIELD_OF_VIEW;
    private int speed;
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
        super(area, orientation, home);

        // Creation of the handler
        handler = new GhostHandler();

        // Set the animations
        AFRAID_ANIMATION = new Animation(ANIMATION_DURATION, Sprite.extractSprites("superpacman/ghost.afraid", 2, 1, 1, this, 16, 16));
        currentAnimation = getAnimations()[orientation.ordinal()];

        // Set ghost attributes
        isAfraid = false;
        this.HOME = home;
        this.FIELD_OF_VIEW = fieldOfView > 0 ? fieldOfView : 5;
        this.speed = speed > 0 ? speed : 1;
        targetPos = getTargetPos();

        /* --------------- EXTENSIONS --------------- */

        PROTECTED_ANIMATION = new Animation(ANIMATION_DURATION, Sprite.extractSprites("superpacman/ghost.protect", 2, 1, 1, this, 16, 16));
        protect = false;
        timerProtection = PROTECTION_DURATION;

        DEATH_SOUND = new SoundAcoustics("sounds/pacman/pacman_eatghost.wav", 0.5f, false, false, false, false);

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
    public List<DiscreteCoordinates> getFieldOfViewCells() {
        List<DiscreteCoordinates> fieldOfViewList = new ArrayList<>();

        // Add the coordinates that are in the field of view of the ghost
        for (int y = -FIELD_OF_VIEW; y <= FIELD_OF_VIEW; y++) {
            for (int x = -FIELD_OF_VIEW; x <= FIELD_OF_VIEW; x++) {
                fieldOfViewList.add(new DiscreteCoordinates(getCurrentMainCellCoordinates().x + x, getCurrentMainCellCoordinates().y + y));
            }
        }

        return fieldOfViewList;
    }

    @Override
    public boolean wantsViewInteraction() { return true; }

    @Override
    public boolean wantsCellInteraction() { return false; }


    /* --------------- Implements Interactable --------------- */

    @Override
    public List<DiscreteCoordinates> getCurrentCells() { return Collections.singletonList(getCurrentMainCellCoordinates()); }

    @Override
    public boolean takeCellSpace() { return false; }

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

    /**
     * @return (Orientation): the next orientation following a specific algorithm
     */
    protected abstract Orientation getNextOrientation();

    /**
     * Choose a random cell in a specific radius around another cell
     * @param centerPos (DiscreteCoordinates): the center cell
     * @param radius    (int): the radius allowed
     * @return (DiscreteCoordinates): the cell
     */
    protected DiscreteCoordinates randomCell(DiscreteCoordinates centerPos, int radius) {
        int randomX, randomY;
        DiscreteCoordinates randomCoordinates;

        // Generate a random coordinate in the current area until this coordinate will be smaller that the allowed radius of the ghost
        do {
            randomX = RandomGenerator.getInstance().nextInt(getOwnerArea().getWidth());
            randomY = RandomGenerator.getInstance().nextInt(getOwnerArea().getHeight());
            randomCoordinates = new DiscreteCoordinates(randomX, randomY);
        } while (DiscreteCoordinates.distanceBetween(centerPos, randomCoordinates) > radius);

        return randomCoordinates;
    }

    /**
     * @return (DiscreteCoordinates): a random cell in an entire map
     */
    protected DiscreteCoordinates randomCell() {
        int width = getOwnerArea().getWidth();
        int height = getOwnerArea().getHeight();

        DiscreteCoordinates center = new DiscreteCoordinates(width/2, height/2);
        int radius = (int) (Math.sqrt(width * width + height * height)/2 + 1);

        return randomCell(center, radius);
    }

    /**
     * Called when the ghosts become scared or stop being scared
     */
    protected void onScareChange() {
        targetPos = getTargetPos();
    }

    /**
     * @return (Animation[]): the animations of the entity
     */
    protected abstract Animation[] getAnimations();

    /**
     * Sets the speed
     * @param speed (int): the new speed
     */
    protected void setSpeed(int speed) { this.speed = speed; }

    /* --------------- Public Methods --------------- */

    /**
     * Scares ghosts
     */
    public void scare() {
        isAfraid = true;
        onScareChange();
    }

    /**
     * Unscares ghosts
     */
    public void unScare() {
        isAfraid = false;
        onScareChange();
    }

    /* --------------- Getters --------------- */

    /**
     * @return (DiscreteCoordinates): the HOME
     */
    protected DiscreteCoordinates getHOME() { return HOME; }

    /** NEED TO BE OVERRIDDEN
     * @return the target accordingly to the circumstances
     */
    protected abstract DiscreteCoordinates getTargetPos();

    /**
     * @return the animation duration of ghosts
     */
    protected int getAnimationDuration() { return ANIMATION_DURATION; }

    /**
     * @return if ghosts are afraid
     */
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
