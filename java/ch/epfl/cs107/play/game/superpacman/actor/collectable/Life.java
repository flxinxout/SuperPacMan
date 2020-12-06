package ch.epfl.cs107.play.game.superpacman.actor.collectable;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.*;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class Life extends CollectableAreaEntity implements Sounds {

    // Default Life's Sprite
    private Sprite sprite;

    /**
     * Default constructor
     *
     * @param area        the area where is the entity
     * @param orientation the orientation
     * @param position    the position in the area
     */
    public Life(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);

        this.sprite = new Sprite("zelda/grass", 1, 1, this,
                null, Vector.ZERO, 1.0f, getSPRITE_DEPTH());

    }

    /* -------------- Implement Actor ---------------- */

    @Override
    public void draw(Canvas canvas) { sprite.draw(canvas); }

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
}
