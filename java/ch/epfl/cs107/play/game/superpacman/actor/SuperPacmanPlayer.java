package ch.epfl.cs107.play.game.superpacman.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.*;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.game.rpg.actor.Player;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.game.superpacman.actor.ghost.Ghost;
import ch.epfl.cs107.play.game.superpacman.area.SuperPacmanArea;
import ch.epfl.cs107.play.game.superpacman.handler.SuperPacmanInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Keyboard;

import java.util.Collections;
import java.util.List;

public class SuperPacmanPlayer extends Player implements Eatable {
    /// Handler of the SuperPacman
    private SuperPacmanPlayerHandler handler;

    /// StatusGUI of the SuperPacman
    private SuperPacmanPlayerStatusGUI status;

    /// Speed and MAXHP of the SuperPacman
    private final static int SPEED = 6;
    private final static float INVINCIBLE_DURATION = 30;
    public final static int MAXHP = 5;

    /// HP AND SCORE
    private int hp;
    private int score;
    private boolean invincible;
    private float timer;
    private DiscreteCoordinates spawnLocation;
    private Orientation desiredOrientation;

    /// Animation duration in frame number
    private final static int ANIMATION_DURATION = 8;
    private Animation[] animations;
    private Animation currentAnimation;

    /**
     * Default SuperPacmanPlayer Constructor
     * @param owner (Area): owner area of the player
     * @param coordinates(DiscreteCoordinates): coordinate of the player
     */
    public SuperPacmanPlayer(Area owner, DiscreteCoordinates coordinates) {
        super(owner, Orientation.RIGHT, coordinates);

        //Setup the animations for Pacman
        Sprite [][] sprites = RPGSprite.extractSprites ("superpacman/pacman", 4, 1, 1,
                this , 64, 64, new Orientation [] { Orientation.UP ,
                        Orientation.RIGHT , Orientation.DOWN , Orientation.LEFT });
        // Create an array of 4 animations
        animations = Animation.createAnimations (ANIMATION_DURATION /2, sprites);
        currentAnimation = animations[2];

        desiredOrientation = Orientation.RIGHT;
        this.hp = 3;
        this.score = 0;

        invincible = false;
        timer = INVINCIBLE_DURATION;

        /// Creation of the handler
        handler = new SuperPacmanPlayerHandler();

        /// Create the status in turns of the current SuperPacmanPlayer
        status = new SuperPacmanPlayerStatusGUI(this);
    }

    public void invincible() {
        invincible = true;
    }

    private void refreshInvincibility(float deltaTime) {
        if (invincible) {
            if (timer > 0) {
                timer -= deltaTime;
            } else {
                invincible = false;
                timer = INVINCIBLE_DURATION;
            }
        }
    }

    //TODO: DISGUSTING!!!!!!!!!
    private SuperPacmanArea toSuperPacmanArea(Area area) {
        return (SuperPacmanArea) area;
    }

    /* -------------- Implement Actor --------------- */

    @Override
    public void update(float deltaTime) {
        //Compute the desired orientation
        Keyboard keyboard = getOwnerArea().getKeyboard();

        if (keyboard.get(Keyboard.DOWN).isLastPressed()) {
            desiredOrientation = Orientation.DOWN;
        }
        else if (keyboard.get(Keyboard.UP).isLastPressed()) {
            desiredOrientation = Orientation.UP;
        }
        else if (keyboard.get(Keyboard.LEFT).isLastPressed()) {
            desiredOrientation = Orientation.LEFT;
        }
        else if (keyboard.get(Keyboard.RIGHT).isLastPressed()) {
            desiredOrientation = Orientation.RIGHT;
        }

        //Move if possible
        if (!isDisplacementOccurs() && getOwnerArea().canEnterAreaCells(this,
                Collections.singletonList (getCurrentMainCellCoordinates().jump(desiredOrientation.toVector())))) {
            orientate(desiredOrientation);
            move(SPEED);
        }

        setAnimations(deltaTime);

        refreshInvincibility(deltaTime);

        super.update(deltaTime);
    }

    /**
     * Method that set the current animation of the Pacman
     * @param deltaTime the delta time of the update
     */
    public void setAnimations(float deltaTime) {
        if (isDisplacementOccurs()) {
            switch (getOrientation()) {
                case DOWN:
                    currentAnimation = animations[0];
                    break;

                case LEFT:
                    currentAnimation = animations[1];
                    break;

                case UP:
                    currentAnimation = animations[2];
                    break;

                case RIGHT:
                    currentAnimation = animations[3];
                    break;
            }
            currentAnimation.update(deltaTime);
        }
        else {
            currentAnimation.reset();
        }
    }

    @Override
    public void draw(Canvas canvas) {
        currentAnimation.draw(canvas);
        status.draw(canvas);
    }

    /* --------------- Implement Interactable --------------- */

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    @Override
    public boolean isCellInteractable() {
        return false;
    }

    @Override
    public boolean isViewInteractable() {
        return true;
    }

    @Override
    public boolean takeCellSpace() {
        return false;
    }

    @Override
    public void acceptInteraction (AreaInteractionVisitor v) {
        ((SuperPacmanInteractionVisitor)v).interactWith (this );
    }

    /* --------------- Implement Interactor --------------- */

    @Override
    public boolean wantsCellInteraction() {
        return true;
    }

    @Override
    public boolean wantsViewInteraction() {
        return false;
    }

    @Override
    public void interactWith(Interactable other) {
        other.acceptInteraction(handler);
    }

    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() {
        return null;
    }

    /* --------------- Implements Eatable --------------- */

    @Override
    public void eaten() {
        hp--;
        DiscreteCoordinates spawn = toSuperPacmanArea(getOwnerArea()).getSpawnLocation();

        leaveArea();
        enterArea(getOwnerArea(), spawn);
    }

    /* --------------- Getters --------------- */

    public int getHp() {
        return hp;
    }

    public int getScore() {
        return score;
    }

    public boolean isInvincible() {
        return invincible;
    }

    /* --------------- External Methods --------------- */

    public void addScore(int amount) {
        score += amount;
        if (score < 0) {
            score = 0;
        }
    }

    /**
     * Interaction handler for a SuperPacmanPlayer
     */
    private class SuperPacmanPlayerHandler implements SuperPacmanInteractionVisitor {
        @Override
        public void interactWith(Door door) { setIsPassingADoor(door); }

        @Override
        public void interactWith(CollectableAreaEntity collectable) {
            collectable.onCollect();
        }

        @Override
        public void interactWith(Ghost ghost) {
            if (invincible) {
                ghost.eaten();
            } else {
                eaten();
            }
        }
    }
}
