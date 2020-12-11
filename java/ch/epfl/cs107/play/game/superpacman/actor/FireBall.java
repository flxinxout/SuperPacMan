package ch.epfl.cs107.play.game.superpacman.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Vector;

/**
 * Class that represents a FireBall in the game
 */
public class FireBall extends Projectile {

    // Attributes of the FireBall
    private final int SPEED = 10;
    private final int FRAME_DURATION = 4;

    /**
     * Default FireBall constructor
     *
     * @param area         (Area): the area where the fire ball is. Not null
     * @param orientation  (Orientation): the orientation of the fire ball. Not null
     * @param position     (DiscreteCoordinates): the position of the fire ball. Not null
     */
    public FireBall(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);
    }

    /* --------------- Extends Projectile --------------- */

    @Override
    protected Animation[] getAnimations() {
        Vector anchor = new Vector(0.155f, 0.155f);
        Sprite[] sprites = Sprite.extractSprites("zelda/orb", 6, .66f, .66f, this, anchor, 32, 32);
        Animation animation = new Animation(FRAME_DURATION, sprites);
        return new Animation[] {animation, animation, animation, animation};
    }

    @Override
    protected int getSpeed() {
        return SPEED;
    }
}
