package ch.epfl.cs107.play.game.superpacman.actor.setting;

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
 * Gates are used to block a way. Can be opened by a signal
 */
public class Gate extends AreaEntity {

    // Attributes
    private Logic signal; // Signal conditioning the gate
    private final Sprite sprite;

    /**
     * Default Gate constructor
     *
     * @param area        (Area): the area where is the gate
     * @param orientation (Orientation): the orientation of the gate
     * @param position    (DiscreteCoordinates): the position of the gate
     * @param signal      (Logic): the signal of the gate
     */
    public Gate(Area area, Orientation orientation, DiscreteCoordinates position, Logic signal) {
        super(area, orientation, position);
        this.signal = signal;

        //To choose between the horizontal sprite or the vertical one
        int m = setSpriteOrientation(orientation);

        //Deactivate the nodes at the position of the gate
        SuperPacmanArea.toSuperPacmanArea(area).setSignal(position, signal);

        this.sprite = new Sprite("superpacman/gate", 1, 1, this, new RegionOfInterest(0, m, 64, 64));
    }

    /* --------------- Implements Interactable --------------- */

    @Override
    public boolean takeCellSpace() {
        return !isOpen();
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
    public void acceptInteraction(AreaInteractionVisitor v) { }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() { return Collections.singletonList(getCurrentMainCellCoordinates()); }

    @Override
    public void draw(Canvas canvas) {
        if (!isOpen()) {
            sprite.draw(canvas);
        }
    }

    /* -------------- Private Methods -------------- */

    /**
     * Gives the region of interest of the sprite depending on the orientation of the gate
     * @param orientation (Orientation): orientation of the gate
     * @return (int)
     */
    private int setSpriteOrientation(Orientation orientation) {
        int m = 0;
        if (orientation == Orientation.DOWN || orientation == Orientation.UP) { m = 0; }
        else if (orientation == Orientation.RIGHT || orientation == Orientation.LEFT) { m = 64; }
        return m;
    }

    /**
     * Getter for the opening of the gate (true := signal is on, false := signal is off)
     * @return (boolean)
     */
    private boolean isOpen() {
        return signal.isOn();
    }
}
