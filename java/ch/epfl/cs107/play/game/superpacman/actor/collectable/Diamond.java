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
import java.util.Collections;
import java.util.List;

/**
 * Diamond Item in the SuperPacman game
 * Increases the player's score of 10 when collected
 */

public class Diamond extends CollectableReward {

    // Default diamond's Sprite
    private Sprite sprite;

    /**
     * Default Diamond Constructor
     * @param area the area where is the diamond
     * @param position the position of the diamond in the specific area
     */
    public Diamond(Area area, DiscreteCoordinates position) {
        super(area, Orientation.DOWN, position, 10);
        this.sprite = new Sprite("superpacman/diamond", 1, 1, this,
                                null, Vector.ZERO, 1.0f, 950);
    }


    /* -------------- Implements Actor ---------------- */

    @Override
    public void draw(Canvas canvas) {
        sprite.draw(canvas);
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
