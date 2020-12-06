package ch.epfl.cs107.play.game.superpacman.actor.collectable;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.*;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Canvas;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

/**
 * Bonus Item in the SuperPacman game
 * Gives invisibility to SuperPacmanPlayer
 */

public class Bonus extends CollectableAreaEntity {

    // Animation duration in frame number
    private final static int ANIMATION_DURATION = 8;
    private Animation currentAnimation;

    /**
     * Default Bonus Constructor
     * @param area the area where is the bonus
     * @param position the position of the bonus in the specific area
     */
    public Bonus(Area area, DiscreteCoordinates position) {
        super(area, Orientation.DOWN, position);

        // Extract Sprites of the bonus
        Sprite[] sprites = Sprite.extractSprites("superpacman/coin", 4, 1, 1, this, 16, 16);

        // Sets the depth of the bonus
        for (Sprite sprite: sprites) {
            sprite.setDepth(getSPRITE_DEPTH());
        }

        // Sets the current animation of the bonus
        this.currentAnimation = new Animation(ANIMATION_DURATION, sprites);
    }


    /* -------------- Implements Actor ---------------- */

    @Override
    public void update(float deltaTime) {
        currentAnimation.update(deltaTime);
    }

    @Override
    public void draw(Canvas canvas) {
        currentAnimation.draw(canvas);
    }

    @Override
    public void onSound() {
        //TODO: FIND A SOUND
    }
}
