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
 * Ghosts are movable ennemies in the SuperPacman game
 */
public abstract class Ghost extends MovableAreaEntity implements Killable, Interactor {

    // Constants
    private final int GHOST_SCORE = 500;

    // Attributes
    private final DiscreteCoordinates HOME;
    private final int FIELD_OF_VIEW;
    private int speed;
    private boolean isAfraid;

    // Target
    private SuperPacmanPlayer player;
    private DiscreteCoordinates targetPos;

    // Animations
    private final int ANIMATION_DURATION = 8; // Animation duration in frame number
    private final Animation AFRAID_ANIMATION;
    private Animation currentAnimation;

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
     *
     * @param area        (Area): owner area. Not null
     * @param orientation (Orientation): initial orientation of the entity. Not null
     * @param home        (Coordinate): initial and HOME position of the ghost. Not null
     */
    public Ghost(Area area, Orientation orientation, DiscreteCoordinates home) {
        this(area, orientation, home, 15, 5);
    }

    /**
     * Complementary Ghost constructor
     *
     * @param area        (Area): owner area. Not null
     * @param orientation (Orientation): initial orientation of the entity. Not null
     * @param home        (Coordinate): initial and HOME position of the ghost. Not null
     * @param speed       (int): initial speed of the ghost. Strictly greater than 0.
     * @param fieldOfView (int): field of view of the ghost. Strictly greater than 0.
     */
    public Ghost(Area area, Orientation orientation, DiscreteCoordinates home, int speed, int fieldOfView) {
        super(area, orientation, home);

        handler = new GhostHandler();

        // Set the animations
        AFRAID_ANIMATION = new Animation(ANIMATION_DURATION, Sprite.extractSprites("superpacman/ghost.afraid", 2, 1, 1, this, 16, 16));
        currentAnimation = getAnimations()[orientation.ordinal()];

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

    /* --------------- Implements Graphics --------------- */

    @Override
    public void update(float deltaTime) {
        setAnimations(deltaTime);

        // Move and orientate if possible
        if (!isDisplacementOccurs() && !protect) {
            Orientation desiredOrientation = getNextOrientation();

            if (getOwnerArea().canEnterAreaCells(this,
                    Collections.singletonList(getCurrentMainCellCoordinates().jump(desiredOrientation.toVector())))) {
                orientate(desiredOrientation);
            }
            move(speed);
        }

        super.update(deltaTime);

        // If the ghost and his target position are very close, we update the target
        if (targetPos != null && DiscreteCoordinates.distanceBetween(getCurrentMainCellCoordinates(), targetPos) < 0.1) {
            targetPos = getTargetPos();
        }


        /* --------------- EXTENSIONS --------------- */

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

        // We spawn the ghost at its spawn location
        getOwnerArea().leaveAreaCells(this, getEnteredCells());
        setCurrentPosition(HOME.toVector());
        getOwnerArea().enterAreaCells(this, Collections.singletonList(HOME));
        resetMotion();

        /* --------------- EXTENSIONS --------------- */

        protect();
        player = null;
        targetPos = getTargetPos();

        DEATH_SOUND.shouldBeStarted();
        DEATH_SOUND.bip(getOwnerArea().getWindow());
    }

    /* --------------- Implements Interactor --------------- */

    @Override
    public void interactWith(Interactable other) { other.acceptInteraction(handler); }

    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() {
        List<DiscreteCoordinates> fieldOfViewList = new ArrayList<>();

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
     * Set the current animation of the Ghost
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

    /**
     * [extension] Set the protection of the ghost when he's killed
     */
    private void protect() {
        protect = true;
    }

    /**
     * [extension] Method called in update to refresh the protection state of the ghost
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
     * Getter for the next orientation. NEED TO BE REDEFINED
     * @return (Orientation): the orientation of the entity
     */
    protected abstract Orientation getNextOrientation();

    /**
     * Getter for the animations. NEED TO BE REDEFINED
     * @return (Animation[]): the animations of the entity
     */
    protected abstract Animation[] getAnimations();

    /**
     * Choose a random cell in a specific radius around another cell
     * @param centerPos (DiscreteCoordinates): the center cell
     * @param radius    (int): the radius allowed
     * @return (DiscreteCoordinates)
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
     * Choose a random cell in an entire map
     * @return (DiscreteCoordinates)
     */
    protected DiscreteCoordinates randomCell() {
        int width = getOwnerArea().getWidth();
        int height = getOwnerArea().getHeight();

        DiscreteCoordinates center = new DiscreteCoordinates(width/2, height/2);
        int radius = (int) (Math.sqrt(width * width + height * height)/2 + 1);

        return randomCell(center, radius);
    }

    /** Called when the ghosts become scared or stop being scared */
    protected void onScareChange() {
        targetPos = getTargetPos();
    }

    /**
     * Sets the speed with new value
     * @param speed (int): the new speed. Not null
     */
    protected void setSpeed(int speed) { this.speed = speed; }

    /* --------------- Public Methods --------------- */

    /** Scare ghosts */
    public void scare() {
        isAfraid = true;
        onScareChange();
    }

    /** Unscare ghosts */
    public void unScare() {
        isAfraid = false;
        onScareChange();
    }

    /* --------------- Getters --------------- */

    /**
     * Getter for the home
     * @return (DiscreteCoordinates)
     */
    protected DiscreteCoordinates getHOME() { return HOME; }

    /**
     * Getter for the target position. NEED TO BE OVERRIDDEN
     * @return (DiscreteCoordinates)
     */
    protected abstract DiscreteCoordinates getTargetPos();

    /**
     * Getter for the animation duration
     * @return (int)
     */
    protected int getAnimationDuration() { return ANIMATION_DURATION; }

    /**
     * Getter for the ghosts' fear
     * @return (boolean)
     */
    protected boolean isAfraid() { return isAfraid; }

    /**
     * Getter for the ghost's protection
     * @return (boolean)
     */
    public boolean isProtected() { return protect; }

    /**
     * Getter for the score given by the ghost on death
     * @return (int)
     */
    public int getScore() { return GHOST_SCORE; }

    /**
     * Getter for the player which is targeted by this ghost
     * @return (SuperPacmanPlayer)
     */
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
