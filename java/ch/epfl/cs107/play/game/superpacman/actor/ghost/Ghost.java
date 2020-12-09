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

    // The spawn
    private DiscreteCoordinates home;

    // Target's Attributes
    private SuperPacmanPlayer player;
    private DiscreteCoordinates targetPos;

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

        this.home = home;
        speed = DEFAULT_SPEED;
        isAfraid = false;
        targetPos = home;

        graphicPath = new Path( this . getPosition () , new LinkedList<>());
    }


    /* --------------- Implements Actor --------------- */

    @Override
    public void update(float deltaTime) {
            setAnimations(deltaTime);
            if (!isDisplacementOccurs()) {
                desiredOrientation = getNextOrientation();
                // move if possible
                if (getOwnerArea().canEnterAreaCells(this,
                        Collections.singletonList(getCurrentMainCellCoordinates().jump(desiredOrientation.toVector())))) {
                    orientate(desiredOrientation);
                }
                move(speed);
            }

        super.update(deltaTime);

        if (DiscreteCoordinates.distanceBetween(getCurrentMainCellCoordinates(), targetPos) < 0.1) {
            updateTarget();
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
        getOwnerArea().leaveAreaCells(this, getEnteredCells());
        setCurrentPosition(home.toVector());
        getOwnerArea().enterAreaCells(this, Collections.singletonList(home));
        resetMotion();

        player.addScore(GHOST_SCORE);

        // Resets the target
        player = null;
        updateTarget();
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
    public void interactWith(Interactable other) {
        other.acceptInteraction(handler);
    }

    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() {
        List<DiscreteCoordinates> fieldOfView = new ArrayList<>();

        for (int y = -FIELD_OF_VIEW; y <= FIELD_OF_VIEW; y++) {
            for (int x = -FIELD_OF_VIEW; x <= FIELD_OF_VIEW; x++) {
                fieldOfView.add(new DiscreteCoordinates(getCurrentMainCellCoordinates().x + x, getCurrentMainCellCoordinates().y + y));
            }
        }
        return fieldOfView;
    }

    @Override
    public boolean wantsViewInteraction() {
        return true;
    }

    @Override
    public boolean wantsCellInteraction() {
        return false;
    }


    /* --------------- Implements Interactable --------------- */

    @Override
    public void acceptInteraction(AreaInteractionVisitor v) {
        ((SuperPacmanInteractionVisitor)v).interactWith (this);
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
    public boolean takeCellSpace() {
        return false;
    }


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
     */
    protected DiscreteCoordinates randomCell(DiscreteCoordinates centerPos, int radius) {
        int randomX, randomY;
        DiscreteCoordinates randomCoordinates;

        do {
            randomX = RandomGenerator.getInstance().nextInt(getOwnerArea().getWidth());
            randomY = RandomGenerator.getInstance().nextInt(getOwnerArea().getHeight());
            randomCoordinates = new DiscreteCoordinates(randomX, randomY);
        }while (DiscreteCoordinates.distanceBetween(centerPos, randomCoordinates) > radius);

        return randomCoordinates;
    }

    /**
     * Choose a random cell in an entire map
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

    /**
     * Sets the target position
     */
    protected void setTargetPos(DiscreteCoordinates targetPos) {
        this.targetPos = targetPos;
    }

    /**
     * Sets the speed
     */
    protected void setSpeed(int speed) { this.speed = speed; }

    /* --------------- Getters --------------- */

    protected DiscreteCoordinates getHome() {
        return home;
    }

    protected int getAnimationDuration() {
        return ANIMATION_DURATION;
    }

    protected SuperPacmanPlayer getPlayer() {
        return player;
    }

    protected DiscreteCoordinates getTargetPos() {
        return targetPos;
    }

    protected boolean isAfraid() {
        return isAfraid;
    }

    public int getDEFAULT_SPEED() { return DEFAULT_SPEED; }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() { return Collections.singletonList(getCurrentMainCellCoordinates()); }

    /**
     * Interaction handler for a Ghost
     */
    private class GhostHandler implements SuperPacmanInteractionVisitor {

        @Override
        public void interactWith(SuperPacmanPlayer pacman) {
            player = pacman;
            updateTarget();
        }
    }
}
