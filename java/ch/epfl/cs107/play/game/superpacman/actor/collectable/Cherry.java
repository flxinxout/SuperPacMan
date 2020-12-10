package ch.epfl.cs107.play.game.superpacman.actor.collectable;

import ch.epfl.cs107.play.game.actor.SoundAcoustics;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.CollectableAreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.superpacman.SuperPacman;
import ch.epfl.cs107.play.game.superpacman.actor.SuperPacmanPlayer;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 *  Cherry item in the SuperPacman game
 *  Increases the player's score of 200 when collected
 */
public class Cherry extends CollectableReward {
    private Sprite sprite;
    private final int REWARD = 200;
    private final SoundAcoustics ON_COLLECT_SOUND;

    /**
     * Default Cherry constructor
     * @param area the area where is the cherry
     * @param position the position of the cherry in the specific area
     */
    public Cherry(Area area, DiscreteCoordinates position) {
        super(area, Orientation.DOWN, position, 200);

        this.sprite = new Sprite("superpacman/cherry", 1, 1, this,
                null, Vector.ZERO, 1.0f, 950);

        ON_COLLECT_SOUND = new SoundAcoustics("sounds/pacman/pacman_eatfruit.wav", 0.35f, false,false,false, false);
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

    /* -------------- Implement Actor ---------------- */

    @Override
    public void draw(Canvas canvas) { sprite.draw(canvas); }

    /* -------------- Extends CollectableReward---------------- */

    @Override
    public int getReward() {
        return REWARD;
    }

    @Override
    public SoundAcoustics getSoundOnCollect() {
        return ON_COLLECT_SOUND;
    }
}
