package ch.epfl.cs107.play.game.superpacman.actor.collectable;

import ch.epfl.cs107.play.game.actor.SoundAcoustics;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.superpacman.area.SuperPacmanArea;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

/**
 * Diamond item increases the player's score of 10 when collected
 */
public class Diamond extends CollectableReward {

    // Constants
    private final int REWARD = 10;

    // Attributes
    private final Sprite sprite;

    /**
     * Default Diamond Constructor
     *
     * @param area     (Area): the area where is the diamond. Not null
     * @param position (DiscreteCoordinates): the position of the diamond in the specific area. Not null
     */
    public Diamond(Area area, DiscreteCoordinates position) {
        super(area, Orientation.DOWN, position, new SoundAcoustics("sounds/pacman/pacman_chomp.wav", 0.15f, false,false,false, false));

        this.sprite = new Sprite("superpacman/diamond", 1, 1, this,
                                null, Vector.ZERO, 1.0f, 950);
    }

    /* -------------- Implements Graphics ---------------- */

    @Override
    public void draw(Canvas canvas) {
        sprite.draw(canvas);
    }

    /* -------------- Implements Collectable ---------------- */

    @Override
    public void onCollect() {
        super.onCollect();
        SuperPacmanArea.toSuperPacmanArea(getOwnerArea()).removeDiamond();
    }

    /* -------------- Extends CollectableReward ---------------- */

    @Override
    public int getReward() {
        return REWARD;
    }
}
