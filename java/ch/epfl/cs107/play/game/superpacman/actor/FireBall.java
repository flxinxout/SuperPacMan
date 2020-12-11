package ch.epfl.cs107.play.game.superpacman.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Vector;

public class FireBall extends Projectile {
    private final int SPEED = 10;
    private final int FRAME_DURATION = 4;

    public FireBall(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);

        //TODO: IS CAST GOOD?
        //FRAME_DURATION = (int) (24 * LIFE_DURATION / FRAME_NUMBER);
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
