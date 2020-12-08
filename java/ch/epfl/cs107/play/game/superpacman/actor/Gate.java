package ch.epfl.cs107.play.game.superpacman.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.superpacman.area.SuperPacmanArea;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Canvas;

import java.util.Collections;
import java.util.List;

/**
 * Gate in the SuperPacman game
 * Used to block the way. Can be deactivated by a signal
 */

public class Gate extends AreaEntity {

    // Signals conditioning the gate
    private Logic signal;
    private DiscreteCoordinates position;
    private Sprite sprite;

    /**
     * Default gate constructor
     * @param area        the area where is the gate
     * @param orientation the orientation of the gate
     * @param position    the position of the gate
     * @param signal      the signal of the gate
     */
    public Gate(Area area, Orientation orientation, DiscreteCoordinates position, Logic signal) {
        super(area, orientation, position);
        this.position = position;
        this.signal = signal;

        //To choose between the horizontal sprite or the vertical one
        int m = setOrientation(orientation);

        //Deactivate the nodes at the position of the gate
        SuperPacmanArea ownerArea = (SuperPacmanArea) area;
        ownerArea.getBehavior().getGraph().setSignal(position, Logic.FALSE);

        this.sprite = new Sprite("superpacman/gate", 1, 1, this, new RegionOfInterest(0, m, 64, 64));
    }

    /* --------------- Implements Interactable --------------- */

    @Override
    public boolean takeCellSpace() {
        return isOpen();
    }

    @Override
    public boolean isCellInteractable() {
        return false;
    }

    @Override
    public boolean isViewInteractable() {
        return false;
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v) {

    }

    /* -------------- Implements Graphics and Interactable -------------- */

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    @Override
    public void draw(Canvas canvas) {
        if (isOpen()) {
            sprite.draw(canvas);
        }
    }

    /* -------------- External Methods -------------- */

    private int setOrientation(Orientation orientation) {
        int m = 0;
        if (orientation == Orientation.DOWN || orientation == Orientation.UP) { m = 0; }
        else if (orientation == Orientation.RIGHT || orientation == Orientation.LEFT) { m = 64; }
        return m;
    }

    private boolean isOpen() {
        //Activate the nodes at the position of the gate
        SuperPacmanArea ownerArea = (SuperPacmanArea) getOwnerArea();
        ownerArea.getBehavior().getGraph().setSignal(position, Logic.TRUE);

        return signal.isOn();
    }
}
