package ch.epfl.cs107.play.game.superpacman.actor.ghost;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.*;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.game.superpacman.SuperPacman;
import ch.epfl.cs107.play.game.superpacman.actor.Eatable;
import ch.epfl.cs107.play.game.superpacman.actor.SuperPacmanPlayer;
import ch.epfl.cs107.play.game.superpacman.handler.SuperPacmanInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Canvas;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Ghost extends MovableAreaEntity implements Interactor, Eatable {
    public final static int GHOST_SCORE = 500;
    protected final static int FIELD_OF_VIEW = 5;

    private final static int ANIMATION_DURATION = 8;
    private Animation[] animations;
    private Animation currentAnimation;
    protected static Animation afraidAnimation;

    protected DiscreteCoordinates home;
    public static boolean isAfraid;

    private GhostHandler handler;
    private SuperPacmanPlayer player;

    private Orientation desiredOrientation;

    /**
     * Default Ghost constructor
     *
     * @param area        (Area): Owner area. Not null
     * @param orientation (Orientation): Initial orientation of the entity. Not null
     * @param home        (Coordinate): Initial and home position of the ghost. Not null
     */
    public Ghost(Area area, Orientation orientation, DiscreteCoordinates home, String animationName) {
        super(area, orientation, home);
        this.home = home;
        afraidAnimation = new Animation(ANIMATION_DURATION, Sprite.extractSprites("superpacman/ghost.afraid", 2, 1, 1, this, 16, 16));
        isAfraid = false;

        //Setup the animations for Pacman
        Sprite[][] sprites = RPGSprite.extractSprites (animationName, 4, 1, 1,
                this , 16, 16, new Orientation [] { Orientation.UP ,
                        Orientation.RIGHT , Orientation.DOWN , Orientation.LEFT });
        // Create an array of 4 animations
        animations = Animation.createAnimations (ANIMATION_DURATION /2, sprites);
        this.currentAnimation = animations[orientation.ordinal()];

        /// Creation of the handler
        handler = new GhostHandler();
    }

    /* --------------- Implements Eatable --------------- */

    @Override
    public void eaten() {
        resetMotion();
        getOwnerArea().leaveAreaCells(this, getCurrentCells());
        getOwnerArea().enterAreaCells(this, Collections.singletonList(home));
        setCurrentPosition(home.toVector());

        player = null;
        SuperPacman.player.addScore(GHOST_SCORE);
    }

    @Override
    public void interactWith(Interactable other) {
        other.acceptInteraction(handler);
    }

    @Override
    public boolean wantsViewInteraction() {
        return true;
    }

    @Override
    public boolean wantsCellInteraction() {
        return false;
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

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
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

    public abstract Orientation getNextOrientation();

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (isDisplacementOccurs()) {
            setAnimations(deltaTime);
        } else {
            desiredOrientation = getNextOrientation();
            //Move if possible
            if (getOwnerArea().canEnterAreaCells(this,
                    Collections.singletonList (getCurrentMainCellCoordinates().jump(desiredOrientation.toVector())))) {
                orientate(desiredOrientation);
                move(18);
            }
        }
    }

    @Override
    public void draw(Canvas canvas) {
        currentAnimation.draw(canvas);
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v) {
        ((SuperPacmanInteractionVisitor)v).interactWith (this );
    }

    /**
     * Method that set the current animation of the Pacman
     * @param deltaTime the delta time of the update
     */
    public void setAnimations(float deltaTime) {
        if (isAfraid) {
            currentAnimation = afraidAnimation;
        }

        if (isDisplacementOccurs() && !isAfraid) {
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
        else {
            currentAnimation.reset();
        }
    }

    private class GhostHandler implements SuperPacmanInteractionVisitor {
        @Override
        public void interactWith(SuperPacmanPlayer pacman) {
            player = pacman;
        }
    }
}
