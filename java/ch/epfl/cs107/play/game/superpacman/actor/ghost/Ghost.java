package ch.epfl.cs107.play.game.superpacman.actor.ghost;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.AreaBehavior;
import ch.epfl.cs107.play.game.areagame.actor.*;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.superpacman.SuperPacman;
import ch.epfl.cs107.play.game.superpacman.actor.Eatable;
import ch.epfl.cs107.play.game.superpacman.actor.SuperPacmanPlayer;
import ch.epfl.cs107.play.game.superpacman.actor.Wall;
import ch.epfl.cs107.play.game.superpacman.area.SuperPacmanArea;
import ch.epfl.cs107.play.game.superpacman.area.SuperPacmanBehavior;
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
    protected final static int FIELD_OF_VIEW = 5;
    protected static final int MAX_DISTANCE_WHEN_SCARED = 5;
    protected static final int MAX_DISTANCE_WHEN_NOT_SCARED = 10;
    protected static final int MIN_AFRAID_DISTANCE = 5;
    private final static int ANIMATION_DURATION = 8;

    private static Animation afraidAnimation;
    private static boolean isAfraid;
    private static int maxDistance;

    /// Attributes of the Ghost
    private Animation[] animations;
    private Animation currentAnimation;

    protected DiscreteCoordinates home;
    private SuperPacmanPlayer player;
    private Orientation desiredOrientation;
    private DiscreteCoordinates targetPos;

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

        isAfraid = false;
        //maxDistance = MAX_DISTANCE_WHEN_NOT_SCARED;
        maxDistance = saveMaxDistance(false);
        targetPos = randomCellInARange(maxDistance);

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

                //TODO: speed modification
                if(isAfraid) {
                    move(true);
                } else {
                    move(false);
                }
                //move(25);
            }
        }

        // SetTargetPos in turns of the type of the ghost
        targetPos = saveTargetPos(player);

        if (getPosition() == targetPos.toVector()) {
            targetPos = randomCellInARange(maxDistance);
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

    /* --------------- Public Methods --------------- */


    public static void setAfraid(boolean afraid) {
        isAfraid = afraid;
        maxDistance = afraid ? MAX_DISTANCE_WHEN_SCARED : MAX_DISTANCE_WHEN_NOT_SCARED;
        //TODO: METTRE LE SAVEMAXDISTANCE MAIS IMPOSSIBLE CAR CEST PAS STATIC MAIS JSAIS PAS COMMENT FAIRE ??
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v) {
        ((SuperPacmanInteractionVisitor)v).interactWith (this );
    }


    /* --------------- Getters --------------- */

    protected abstract Animation[] getAnimations();
    protected abstract void move(boolean isAfraid);
    protected abstract DiscreteCoordinates randomCellInARange(int range);
    protected abstract DiscreteCoordinates saveTargetPos(SuperPacmanPlayer player);
    protected abstract int saveMaxDistance(boolean isAfraid);

    public static int getAnimationDuration() {
        return ANIMATION_DURATION;
    }

    public static int getMaxDistance() {
        return maxDistance;
    }

    public SuperPacmanPlayer getPlayer() {
        return player;
    }

    protected DiscreteCoordinates getTargetPos() {
        return targetPos;
    }

    protected void setPlayer(SuperPacmanPlayer player) {
        this.player = player;
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
            targetPos = player.getCurrentCells().get(0);
        }
    }
}
