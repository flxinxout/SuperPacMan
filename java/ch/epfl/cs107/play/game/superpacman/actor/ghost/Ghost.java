package ch.epfl.cs107.play.game.superpacman.actor.ghost;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.*;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.superpacman.SuperPacman;
import ch.epfl.cs107.play.game.superpacman.actor.Eatable;
import ch.epfl.cs107.play.game.superpacman.actor.SuperPacmanPlayer;
import ch.epfl.cs107.play.game.superpacman.handler.SuperPacmanInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RandomGenerator;
import ch.epfl.cs107.play.window.Canvas;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public abstract class Ghost extends MovableAreaEntity implements Interactor, Eatable {
    /// Static attributes of the Ghosts
    private final static int GHOST_SCORE = 500;
    private final static int FIELD_OF_VIEW = 5;
    private static final int MIN_AFRAID_DISTANCE = 5;
    private static final int DEFAULT_SPEED = 25;

    private final static int ANIMATION_DURATION = 8;

    private static Animation afraidAnimation;
    private static boolean isAfraid;

    /// Attributes of the Ghost
    private Animation[] animations;
    private Animation currentAnimation;

    private DiscreteCoordinates home;
    private SuperPacmanPlayer player;
    private Orientation desiredOrientation;
    private DiscreteCoordinates targetPos;
    private int speed;

    private GhostHandler handler;

    //protected Path graphicPath;

    /**
     * Default Ghost constructor
     * @param area        (Area): Owner area. Not null
     * @param orientation (Orientation): Initial orientation of the entity. Not null
     * @param home        (Coordinate): Initial and home position of the ghost. Not null
     */
    public Ghost(Area area, Orientation orientation, DiscreteCoordinates home) {
        super(area, orientation, home);

        this.home = home;

        afraidAnimation = new Animation(ANIMATION_DURATION, Sprite.extractSprites("superpacman/ghost.afraid", 2, 1, 1, this, 16, 16));
        animations = getAnimations();
        this.currentAnimation = animations[orientation.ordinal()];
        speed = DEFAULT_SPEED;

        isAfraid = false;

        targetPos = home;

        /// Creation of the handler
        handler = new GhostHandler();

        //graphicPath = new Path(this.getPosition(), new LinkedList<>(null));
    }

    /* --------------- Implements Actor --------------- */

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        if (isDisplacementOccurs()) {
            setAnimations(deltaTime);
        } else {
            desiredOrientation = getNextOrientation();
            //Move if possible
            if (getOwnerArea().canEnterAreaCells(this,
                    Collections.singletonList(getCurrentMainCellCoordinates().jump(desiredOrientation.toVector())))) {
                orientate(desiredOrientation);
                move(speed);
            }
        }

        if (player != null && DiscreteCoordinates.distanceBetween(player.getCurrentCells().get(0),
                getCurrentMainCellCoordinates()) > FIELD_OF_VIEW) {
            player = null;
            updateTarget();
        }

        //TODO redefine equals?? : getCurrentMainCellCoordinates().equals(targetPos)
        if (DiscreteCoordinates.distanceBetween(getCurrentMainCellCoordinates(), targetPos) < 0.2) {
            updateTarget();
        }
    }

    @Override
    public void draw(Canvas canvas) {
        currentAnimation.draw(canvas);
        /*if(graphicPath != null) {
            graphicPath.draw(canvas);
        }*/
    }

    /* --------------- Implements Eatable --------------- */

    @Override
    public void eaten() {
        getOwnerArea().leaveAreaCells(this, getEnteredCells());
        setCurrentPosition(home.toVector());
        getOwnerArea().enterAreaCells(this, Collections.singletonList(home));
        resetMotion();

        player = null;
        updateTarget();
        SuperPacman.player.addScore(GHOST_SCORE);
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
        ((SuperPacmanInteractionVisitor)v).interactWith (this );
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

    /* --------------- Private Methods --------------- */

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

    protected abstract void onScared();

    protected abstract void onUnscared();

    protected abstract void updateTarget();

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

    protected void setSpeed(int speed) {
        this.speed = speed;
    }

    /* --------------- Getters --------------- */

    protected abstract Animation[] getAnimations();

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

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    public abstract Orientation getNextOrientation();

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
