package ch.epfl.cs107.play.game.superpacman.actor.collectable;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.CollectableAreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.Signal;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Canvas;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * Key Item in the SuperPacman game
 * Used to unlock the door for the next level
 */

public class Key extends CollectableAreaEntity implements Logic {

    // Default key's Sprite
    private Sprite sprite;

    private boolean isCollected;

    /**
     * Default Key Constructor
     * @param area the area where is the bonus
     * @param position the position of the bonus in the specific area
     */
    public Key(Area area, DiscreteCoordinates position) {
        super(area, Orientation.DOWN, position);
        this.sprite = new Sprite("superpacman/key", 1, 1, this);
        isCollected = false;
    }

    /* -------------- Implements Actor ---------------- */

    @Override
    public void draw(Canvas canvas) {
        sprite.draw(canvas);
    }

    @Override
    public void onSound() {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("res/sounds/pacman/transactionOk.wav").getAbsoluteFile());

            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.loop(0);

        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    /* -------------- Implements Collectable ---------------- */

    @Override
    public void onCollect() {
        super.onCollect();
        isCollected = true;
    }


    /* -------------- Implements Logic ---------------- */

    @Override
    public boolean isOn() {
        return !isCollected;
    }

    @Override
    public boolean isOff() {
        return isCollected;
    }

    @Override
    public float getIntensity() {
        return (isCollected) ? 1 : 0;
    }
}
