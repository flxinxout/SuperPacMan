package ch.epfl.cs107.play.game.superpacman.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Canvas;

import java.util.Collections;
import java.util.List;

public class Gate extends AreaEntity {

    /// Signals conditioning the gate
    private Logic signal;
    private Sprite sprite;

    public Gate(Area area, Orientation orientation, DiscreteCoordinates position, Logic signal) {
        super(area, orientation, position);
        this.signal = signal;

        //To choose between the horizontal sprite or the vertical one
        int m = 0;
        if (orientation == Orientation.DOWN || orientation == Orientation.UP) { m = 0; }
        else if (orientation == Orientation.RIGHT || orientation == Orientation.LEFT) { m = 64; }

        this.sprite = new Sprite("superpacman/gate", 1, 1, this, new RegionOfInterest(0, m, 64, 64));
    }

    private boolean isOpen() {
        return signal.isOn();
    }

    private void setSignal(Logic signal) {
        this.signal = signal;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
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
}
