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
 * Key Item in the SuperPacman game
 * Used to unlock a gate
 */
public class Key extends CollectableAreaEntity implements Logic {

    private Sprite sprite;
    private boolean isCollected;
    private final SoundAcoustics ON_COLLECT_SOUND;


    /**
     * Default Key Constructor
     * @param area the area where is the bonus
     * @param position the position of the bonus in the specific area
     */
    public Key(Area area, DiscreteCoordinates position) {
        super(area, Orientation.DOWN, position);

        sprite = new Sprite("superpacman/key", 1, 1, this);
        isCollected = false;
        ON_COLLECT_SOUND = new SoundAcoustics("sounds/pacman/transactionOK.wav", 0.35f, false,false,false, false);

    }

    /* -------------- Implements Interactable ---------------- */

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    @Override
    public boolean isViewInteractable() {
        return false;
    }

    /* -------------- Implements Actor ---------------- */

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

    @Override
    public SoundAcoustics getSoundOnCollect() {
        return ON_COLLECT_SOUND;
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
