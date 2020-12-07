package ch.epfl.cs107.play.game.superpacman.actor.ghost;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.*;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.superpacman.area.util.Eatable;
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
import java.util.List;

/**
 * Class that represents ghosts in the SuperPacman game
 */

public abstract class Ghost extends MovableAreaEntity implements Interactor, Eatable, Sound {

    // Attributes of the Ghosts
    private final int GHOST_SCORE = 500;
    private final int FIELD_OF_VIEW = 5;
    private final int DEFAULT_SPEED = 20;
    private int speed;

    // Animation duration in frame number
    private final static int ANIMATION_DURATION = 8;

    // Attributes for the behavior of the ghost when they are afraid
    private Animation afraidAnimation;
    private static boolean isAfraid;

    // Attributes of the Ghost
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

        // Sets the afraid animation which is common has all ghosts
        afraidAnimation = new Animation(ANIMATION_DURATION, Sprite.extractSprites("superpacman/ghost.afraid", 2, 1, 1, this, 16, 16));

        // Sets all animations of ghosts
        animations = getAnimations();
        currentAnimation = animations[orientation.ordinal()];

        this.home = home;
        speed = DEFAULT_SPEED;
        isAfraid = false;
        targetPos = home;
    }


    /* --------------- Implements Actor --------------- */

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        if (isDisplacementOccurs()) {
            setAnimations(deltaTime);
        } else {
            currentAnimation.reset();
            desiredOrientation = getNextOrientation();

            // If move is possible
            if (getOwnerArea().canEnterAreaCells(this,
                    Collections.singletonList(getCurrentMainCellCoordinates().jump(desiredOrientation.toVector())))) {
                orientate(desiredOrientation);
                move(speed);
            }
        }

        //TODO redefine equals?? : getCurrentMainCellCoordinates().equals(targetPos)
        if (DiscreteCoordinates.distanceBetween(getCurrentMainCellCoordinates(), targetPos) < 0.1) {
            updateTarget();
        }
    }

    @Override
    public void draw(Canvas canvas) { currentAnimation.draw(canvas); }


    /* --------------- Implements Eatable --------------- */

    @Override
    public void eaten() {
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
                //TODO: conditioning with if else
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
     * Method that set the current animation of the Ghost
     * @param deltaTime the delta time of the update
     */
    private void setAnimations(float deltaTime) {
        if (isAfraid) {
            currentAnimation = afraidAnimation;
        }

        if (!isAfraid) {
            switch (getOrientation()) {
                case UP:
                    currentAnimation = animations[0];
                    break;

                case RIGHT:
                    currentAnimation = animations[1];
                    break;

                case DOWN:
                    currentAnimation = animations[2];
                    break;

                case LEFT:
                    currentAnimation = animations[3];
                    break;
            }
            currentAnimation.update(deltaTime);
        }
    }


    /* --------------- Abstract Methods --------------- */

    protected abstract void onScared();

    protected abstract void onUnscared();

    protected abstract void updateTarget();

    protected abstract Animation[] getAnimations();

    public abstract Orientation getNextOrientation();

    /* --------------- Protected Methods --------------- */

    /**
     * Method to choose a random cell in a specific radius around another cell
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
     * Method to choose a random cell in an entire map
     */
    protected DiscreteCoordinates randomCell() {
        int width = getOwnerArea().getWidth();
        int height = getOwnerArea().getHeight();

        DiscreteCoordinates center = new DiscreteCoordinates(width/2, height/2);
        int radius = (int) (Math.sqrt(width * width + height * height)/2 + 1);

        return randomCell(center, radius);
    }

    /* --------------- Public Methods --------------- */

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

    protected void setTargetPos(DiscreteCoordinates targetPos) {
        this.targetPos = targetPos;
    }

    protected void setSpeed(int speed) { this.speed = speed; }

    /* --------------- Getters --------------- */

    protected DiscreteCoordinates getHome() {
        return home;
    }

    protected static int getAnimationDuration() {
        return ANIMATION_DURATION;
    }

    protected SuperPacmanPlayer getPlayer() {
        return player;
    }

    protected DiscreteCoordinates getTargetPos() {
        return targetPos;
    }

    protected static boolean isAfraid() {
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
