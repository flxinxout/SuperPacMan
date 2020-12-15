package ch.epfl.cs107.play.game.superpacman.actor.collectable;

import ch.epfl.cs107.play.game.actor.SoundAcoustics;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.CollectableAreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Canvas;

import java.util.Collections;
import java.util.List;

/**
 * Key item is used to unlock a gate. Behaves like a Signal
 */
public class Key extends CollectableAreaEntity implements Logic {

    // Attributes
    private Sprite sprite;
    private boolean isCollected;

    /**
     * Default Key Constructor
     *
     * @param area     (Area): the area where is the bonus. Not null
     * @param position (DiscreteCoordinates): the position of the bonus in the specific area. Not null
     */
    public Key(Area area, DiscreteCoordinates position) {
        super(area, Orientation.DOWN, position, new SoundAcoustics("sounds/pacman/transactionOK.wav", 0.35f, false,false,false, false));

        sprite = new Sprite("superpacman/key", 1, 1, this);

        // At the beginning, the key is not collected
        isCollected = false;
    }

    /* -------------- Implements Graphics ---------------- */

    @Override
    public void draw(Canvas canvas) {
        sprite.draw(canvas);
    }


    /* -------------- Extends CollectableAreaEntity ---------------- */

    @Override
    public void onCollect() {
        super.onCollect();
        isCollected = true;
    }

    /* -------------- Implements Logic ---------------- */

    @Override
    public boolean isOn() {
        return isCollected;
    }

    @Override
    public boolean isOff() {
        return !isCollected;
    }

    @Override
    public float getIntensity() {
        return (isCollected) ? 1 : 0;
    }
}
