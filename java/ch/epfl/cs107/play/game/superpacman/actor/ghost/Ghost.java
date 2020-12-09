package ch.epfl.cs107.play.game.superpacman.actor.ghost;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.*;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.superpacman.actor.Killable;
import ch.epfl.cs107.play.game.superpacman.actor.SuperPacmanPlayer;
import ch.epfl.cs107.play.game.superpacman.handler.SuperPacmanInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RandomGenerator;
import ch.epfl.cs107.play.window.Canvas;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Class that represents ghosts in the SuperPacman game
 */

public abstract class Ghost extends MovableAreaEntity implements Interactor, Killable, Sound {

    // Attributes of the Ghosts
    private final int GHOST_SCORE = 500;
    private final int FIELD_OF_VIEW = 5;
    private final int DEFAULT_SPEED = 20;
    private final float PROTECTION_DURATION = 3;
    private int speed;

    // Animation duration in frame number
    private final int ANIMATION_DURATION = 8;

    // Attributes for the behavior of the ghost when they are afraid
    private Animation afraidAnimation;
    private boolean isAfraid;

    // Animation
    private Animation[] animations;
    private Animation currentAnimation;
    private Orientation desiredOrientation;

    // The spawn of the ghost
    private DiscreteCoordinates home;

    // Target's Attributes
    private SuperPacmanPlayer player;
    private DiscreteCoordinates targetPos;

    // Spawn protection (to avoid spawn kill)
    private boolean protection;
    private float timerProtection;

    // Handler of the ghost
    private GhostHandler handler;

    protected Path graphicPath;

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

        // Default target position
        targetPos = home;

        protection = false;
        timerProtection = PROTECTION_DURATION;

        graphicPath = new Path( this . getPosition () , new LinkedList<>());
    }


    /* --------------- Implements Actor --------------- */

    @Override
    public void update(float deltaTime) {
        // Set the animations in turns of different parameters
        setAnimations(deltaTime);

        // If the ghost can not go in a direction
        if (!isDisplacementOccurs()) {

            // Set the new orientation for the ghost
            desiredOrientation = getNextOrientation();

            // If move is possible
            if (getOwnerArea().canEnterAreaCells(this,
                    Collections.singletonList(getCurrentMainCellCoordinates().jump(desiredOrientation.toVector())))) {
                // Orientation of the ghost for the next move
                orientate(desiredOrientation);
            }

            // Move of the ghost
            move(speed);
        }

        super.update(deltaTime);

        // If the ghost and his target position are very close, we update the target in turns of parameters of the game
        if (DiscreteCoordinates.distanceBetween(getCurrentMainCellCoordinates(), targetPos) < 0.1) {
            updateTarget();
        }

        // Check the protection state
        if(protection) {
            refreshProtection(deltaTime);
        }
    }

    @Override
    public void draw(Canvas canvas) {
        if (graphicPath != null) {
            graphicPath.draw(canvas);
        }
        currentAnimation.draw(canvas);
    }


    /* --------------- Implements Killable --------------- */

    @Override
    public void onDeath() {

        // If the ghost is not protected by the anti-spawnKill
        if(!protection) {
            // Discharge the entity in the cells where he is and spawn the ghost to at home
            protect();
            getOwnerArea().leaveAreaCells(this, getEnteredCells());
            setCurrentPosition(home.toVector());
            getOwnerArea().enterAreaCells(this, Collections.singletonList(home));
            resetMotion();

            // Update the score of the player who killed the ghost
            player.addScore(GHOST_SCORE);

            // Resets the target
            player = null;
            updateTarget();
        }
    }

    /* --------------- Implements Sounds --------------- */

    @Override
    public void onSound() {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("res/sounds/pacman/pacman_eatghost.wav").getAbsoluteFile());

            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.loop(0);

        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
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

            // Afraid animation
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

    /** Method that set the protection of the ghost when he's killed */
    private void protect() {
        protection = true;
    }

    /**
     * Method called in update to update the protection state of the ghost
     * @param deltaTime (float) the delta time of the update
     */
    private void refreshProtection(float deltaTime) {
        if (timerProtection > 0) {
            timerProtection -= deltaTime;
        } else {
            protection = false;
            timerProtection = PROTECTION_DURATION;
        }
    }


    /* --------------- Abstract Methods --------------- */

    /**
     * Called when the ghosts become scared
     */
    protected abstract void onScared();

    /**
     * Called when the ghosts stop being scared
     */
    protected abstract void onUnscared();

    /**
     * Update the target accordingly to the circumstances
     */
    protected abstract void updateTarget();

    /**
     * Getter for the animations
     */
    protected abstract Animation[] getAnimations();

    /**
     * Compute the next orientation following a specific algorithm
     */
    protected abstract Orientation getNextOrientation();

    /* --------------- Protected Methods --------------- */

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

    /**
     * @return a random cell in an entire map
     */
    protected DiscreteCoordinates randomCell() {
        int width = getOwnerArea().getWidth();
        int height = getOwnerArea().getHeight();

        DiscreteCoordinates center = new DiscreteCoordinates(width/2, height/2);
        int radius = (int) (Math.sqrt(width * width + height * height)/2 + 1);

        return randomCell(center, radius);
    }

    /* --------------- Public Methods --------------- */

    /**
     * Scares or stops scaring ghosts
     * @param isScared true := scare ghosts, false := stop scaring them
     */
    public void setIsScared(boolean isScared) {
        if (isScared) {
            isAfraid = true;
            onScared();
        } else {
            isAfraid = false;
            onUnscared();
        }
    }

    /* --------------- Setters --------------- */

    /** Sets the target position */
    protected void setTargetPos(DiscreteCoordinates targetPos) {
        this.targetPos = targetPos;
    }

    /** Sets the speed */
    protected void setSpeed(int speed) { this.speed = speed; }

    /* --------------- Getters --------------- */

    /**@return the home of the ghost */
    protected DiscreteCoordinates getHome() { return home; }

    /**@return the animation duration of ghosts */
    protected int getAnimationDuration() { return ANIMATION_DURATION; }

    /**@return the target of the ghost */
    protected SuperPacmanPlayer getPlayer() { return player; }

    /**@return the target's position of the ghost */
    protected DiscreteCoordinates getTargetPos() { return targetPos; }

    /**@return is ghosts are afraid */
    protected boolean isAfraid() { return isAfraid; }

    /**@return the default speed of the ghost */
    public int getDEFAULT_SPEED() { return DEFAULT_SPEED; }


    /**
     * Interaction handler for a Ghost
     */
    private class GhostHandler implements SuperPacmanInteractionVisitor {

        @Override
        public void interactWith(SuperPacmanPlayer pacman) {

            // Set the target and update it
            player = pacman;
            updateTarget();
        }
    }
}
