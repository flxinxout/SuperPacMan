package ch.epfl.cs107.play.game.superpacman.actor.collectable;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.superpacman.area.SuperPacmanArea;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

/**
 * Diamond Item in the SuperPacman game
 * Gives a reward when it is collected
 */

public class Diamond extends CollectableReward {

    // Default diamond's Sprite
    private Sprite sprite;

    /**
     * Default Diamond Constructor
     * @param area the area where is the bonus
     * @param position the position of the bonus in the specific area
     */
    public Diamond(Area area, DiscreteCoordinates position) {
        super(area, Orientation.DOWN, position, 10);
        this.sprite = new Sprite("superpacman/diamond", 1, 1, this,
                                null, Vector.ZERO, 1.0f, getSPRITE_DEPTH());
    }


    /* -------------- Implements Actor ---------------- */

    @Override
    public void draw(Canvas canvas) {
        sprite.draw(canvas);
    }


    /* -------------- Implements Collectable ---------------- */

    @Override
    public void onCollect() {
        super.onCollect();
        //TODO: DISGUSTING CAST, TRY TO DO BETTER WITH DIAMOND COUNT
        SuperPacmanArea area = (SuperPacmanArea) getOwnerArea();
        area.removeDiamond();
    }

    @Override
    public void onSound() {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("res/sounds/pacman/pacman_chomp.wav").getAbsoluteFile());

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
}
