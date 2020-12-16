package ch.epfl.cs107.play.game.superpacman.actor.ennemy;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

/**
 * [EXTENSION] Arrows are specific projectiles shot by Bows
 */
public class Arrow extends Projectile {

    // Constants
    private final int SPEED = 6;

    // Attributes
    private final Sprite sprite;

    /**
     * Default Arrow constructor
     *
     * @param area         (Area): the area where the arrow is. Not null
     * @param orientation  (Orientation): the orientation of the arrow. Not null
     * @param position     (DiscreteCoordinates): the position of the arrow. Not null
     */
    public Arrow(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);

        // Choose a sprite depending on the orientation
        Sprite[] sprites = Sprite.extractSprites("superpacman/arrow", 4, 1.25f, 1.25f, this, new Vector(-0.19f, -0.25f), 32, 32);
        sprite = sprites[orientation.ordinal()];
    }

    /* --------------- Extends Projectile --------------- */

    @Override
    public void draw(Canvas canvas) {
        sprite.draw(canvas);
    }

    @Override
    protected int getSpeed() {
        return SPEED;
    }
}
