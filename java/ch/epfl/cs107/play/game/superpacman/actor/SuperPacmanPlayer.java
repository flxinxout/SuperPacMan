package ch.epfl.cs107.play.game.superpacman.actor;

import ch.epfl.cs107.play.game.Updatable;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.rpg.actor.Player;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Keyboard;

import java.util.Collections;
import java.util.List;

public class SuperPacmanPlayer extends Player {
    private final int SPEED = 6;
    private Sprite sprite;

    public SuperPacmanPlayer(Area area, DiscreteCoordinates coordinates) {
        super(area, Orientation.RIGHT, coordinates);
        sprite = new Sprite("yellowDot", 1.f, 1.f,this);

        resetMotion();
    }

    @Override
    public void update(float deltaTime) {
        Orientation orientation = detectOrientation();

        if (!isDisplacementOccurs() && getOwnerArea().canEnterAreaCells(this,
                Collections.singletonList (getCurrentMainCellCoordinates().jump(orientation.toVector())))) {
            orientate(orientation);
            move(SPEED);
        }

        super.update(deltaTime);
    }

    private Orientation detectOrientation() {
        Orientation desiredOrientation = null;

        Keyboard keyboard= getOwnerArea().getKeyboard();
        if (keyboard.get(Keyboard.DOWN).isPressed()) {
            desiredOrientation = Orientation.DOWN;
        }
        else if (keyboard.get(Keyboard.UP).isPressed()) {
            desiredOrientation = Orientation.UP;
        }
        else if (keyboard.get(Keyboard.LEFT).isPressed()) {
            desiredOrientation = Orientation.LEFT;
        }
        else if (keyboard.get(Keyboard.RIGHT).isPressed()) {
            desiredOrientation = Orientation.RIGHT;
        }

        return desiredOrientation;
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() {
        return null;
    }

    @Override
    public boolean wantsCellInteraction() {
        return true;
    }

    @Override
    public boolean wantsViewInteraction() {
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
}
