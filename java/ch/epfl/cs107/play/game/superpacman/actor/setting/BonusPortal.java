package ch.epfl.cs107.play.game.superpacman.actor.setting;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Canvas;

//TODO: can't we delete it?
/**
 * Class that represents a portal. Behavior like a Door
 */
public class BonusPortal extends Door {

    // Portal's Sprite
    private Sprite sprite;

    /**
     * Default BonusPortal constructor
     *
     * @param destination (String): the area's destination. Not null
     * @param signal      (Logic): the logic that work with this portal. After crossing it once, the signal becomes off. Not null
     * @param area        (Area): the area where the portal leads. Not null
     * @param orientation (Orientation): the orientation of the portal. Not null
     * @param position    (DiscreteCoordinates): the position where the portal is. Not null
     */
    public BonusPortal(String destination, Logic signal, Area area, Orientation orientation, DiscreteCoordinates position) {
        super(destination, new DiscreteCoordinates(9, 18), signal, area, orientation, position);
        this.sprite = new Sprite("superpacman/portal", 1f, 1f, this);
    }

    /* ------------ Implements Graphics ------------- */

    @Override
    public void draw(Canvas canvas) { sprite.draw(canvas); }
}
