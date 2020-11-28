package ch.epfl.cs107.play.game.superpacman.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.game.rpg.actor.Player;
import ch.epfl.cs107.play.game.rpg.actor.Sign;
import ch.epfl.cs107.play.game.superpacman.guis.SuperPacmanPlayerStatusGUI;
import ch.epfl.cs107.play.game.superpacman.handler.SuperPacmanInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Keyboard;

import java.util.Collections;
import java.util.List;

public class SuperPacmanPlayer extends Player {
    private SuperPacmanPlayerHandler handler = new SuperPacmanPlayerHandler();
    private SuperPacmanPlayerStatusGUI status = new SuperPacmanPlayerStatusGUI();
    private final static int SPEED = 6;
    private Sprite sprite;
    private final int lifeMax = 5;
    private int life;
    private int score;

    private Orientation desiredOrientation;
    /**
     * Default SuperPacmanPlayer Constructor
     * @param owner (Area): owner area of the player
     * @param coordinates(DiscreteCoordinates): coordinate of the player
     */
    public SuperPacmanPlayer(Area owner, DiscreteCoordinates coordinates) {
        super(owner, Orientation.RIGHT, coordinates);
        sprite = new Sprite("superpacman/bonus", 1.f, 1.f,this);
        desiredOrientation = Orientation.RIGHT;
        this.life = 3;
        this.score = 0;
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

        super.update(deltaTime);
    }

    @Override
    public void draw(Canvas canvas) {
        sprite.draw(canvas);
        getTransform();
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

    /**
     * Interaction handler for a SuperPacmanPlayer
     */
    private class SuperPacmanPlayerHandler implements SuperPacmanInteractionVisitor {
        @Override
        public void interactWith(Door door) {
            setIsPassingADoor(door);
        }

        @Override
        public void interactWith(Interactable other) { }

        @Override
        public void interactWith(Sign sign) { }

        @Override
        public void interactWith(SuperPacmanPlayer otherPlayer) { }
    }
}
