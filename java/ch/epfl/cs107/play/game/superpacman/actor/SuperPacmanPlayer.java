package ch.epfl.cs107.play.game.superpacman.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.*;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.game.rpg.actor.Player;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.game.rpg.actor.Sign;
import ch.epfl.cs107.play.game.superpacman.handler.SuperPacmanInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Keyboard;

import java.util.Collections;
import java.util.List;

public class SuperPacmanPlayer extends Player {
    private SuperPacmanPlayerHandler handler = new SuperPacmanPlayerHandler();
    private SuperPacmanPlayerStatusGUI status;

    private final static int SPEED = 6;
    // Mis en public je saurais pas comment le get avec une bonne encapsulation
    public final static int MAXHP = 5;

    private int hp;
    private int score;

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
        // Create the status in turns of the current SuperPacmanPlayer
        status = new SuperPacmanPlayerStatusGUI(this);
    }



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

        super.update(deltaTime);
    }

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

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    //Player implements Interactable

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

    //Player implements Interactor

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

    // Getters

    public int getHp() {
        return hp;
    }

    public int getScore() {
        return score;
    }

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
        public void interactWith(Interactable other) { }

        @Override
        public void interactWith(Sign sign) { }

        @Override
        public void interactWith(SuperPacmanPlayer otherPlayer) { }

        public void interactWith(CollectableAreaEntity collectable) {
            //TODO: deal this interaction inside the player or like now in the collectables?
            collectable.onCollect();
        }
    }
}
