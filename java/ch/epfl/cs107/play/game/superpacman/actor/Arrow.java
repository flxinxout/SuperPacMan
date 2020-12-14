package ch.epfl.cs107.play.game.superpacman.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

/**
 * Class that represents a Arrow in the game
 */
public class Arrow extends Projectile {

    // Constants
    private final int SPEED = 6;

    // Attributes
    private Sprite[] sprites;
    private Sprite currentSprite;

    /**
     * Default Arrow constructor
     *
     * @param area         (Area): the area where the arrow is. Not null
     * @param orientation  (Orientation): the orientation of the arrow. Not null
     * @param position     (DiscreteCoordinates): the position of the arrow. Not null
     */
    public Arrow(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);

        sprites = Sprite.extractSprites("superpacman/arrow", 4, 1.25f, 1.25f, this, new Vector(-0.19f, -0.25f), 32, 32);
        currentSprite = sprites[orientation.ordinal()];
    }

    /* --------------- Extends Projectile --------------- */

    @Override
    public void draw(Canvas canvas) {
        currentSprite.draw(canvas);
    }

    @Override
    protected int getSpeed() {
        return SPEED;
    }
}