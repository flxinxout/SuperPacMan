package ch.epfl.cs107.play.game.rpg.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Canvas;

/**
 * DoorGraphics are basically doors with a sprite attached to them
 */
public class DoorGraphics extends Door {

    private Sprite sprite;

    /**
     * Default DoorGraphics constructor
     *
     * @param destination (String): the area's destination. Not null
     * @param otherSideCoordinates (DiscreteCoordinate):Coordinates of the other side, not null
     * @param signal      (Logic): the logic that work with this door. After crossing it once, the signal becomes off. Not null
     * @param area        (Area): the area where the door leads. Not null
     * @param orientation (Orientation): the orientation of the door. Not null
     * @param position    (DiscreteCoordinates): the position where the door is. Not null
     */
    public DoorGraphics(String destination, DiscreteCoordinates otherSideCoordinates, Logic signal, Area area, Orientation orientation, DiscreteCoordinates position) {
        super(destination, otherSideCoordinates, signal, area, orientation, position);
        this.sprite = new Sprite("superpacman/portal", 1f, 1f, this);
    }

    /* ------------ Implements Graphics ------------- */

    @Override
    public void draw(Canvas canvas) { sprite.draw(canvas); }
}
