package ch.epfl.cs107.play.game.superpacman.actor.collectable;

import ch.epfl.cs107.play.game.actor.SoundAcoustics;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

/**
 *  Cherry item increases the player's score of 200 when collected
 */
public class Cherry extends CollectableReward {

    // Constants
    private final int REWARD = 200;

    // Attributes
    private final Sprite sprite;

    /**
     * Default Cherry constructor
     * @param area     (Area): the area where is the cherry. Not null
     * @param position (DiscreteCoordinates): the position of the cherry in the specific area. Not null
     */
    public Cherry(Area area, DiscreteCoordinates position) {
        //TODO: même raison que getspeed() dans Boss, pk pas un getSound() abstract dans Collectable
        super(area, Orientation.DOWN, position, new SoundAcoustics("sounds/pacman/pacman_eatfruit.wav", 0.35f, false,false,false, false));

        this.sprite = new Sprite("superpacman/cherry", 1, 1, this,
                null, Vector.ZERO, 1.0f, 950);
    }


    /* -------------- Implements Graphics ---------------- */

    @Override
    public void draw(Canvas canvas) { sprite.draw(canvas); }

    /* -------------- Extends CollectableReward---------------- */

    @Override
    public int getReward() {
        return REWARD;
    }
}
