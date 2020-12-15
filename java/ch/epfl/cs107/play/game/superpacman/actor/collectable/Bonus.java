package ch.epfl.cs107.play.game.superpacman.actor.collectable;

import ch.epfl.cs107.play.game.actor.SoundAcoustics;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.*;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.superpacman.handler.SuperPacmanInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Canvas;

/**
 * Bonus item gives invincibility to the player when collected
 */
public class Bonus extends CollectableAreaEntity {

    private final static int ANIMATION_DURATION = 8; // Animation duration in frame number
    private Animation currentAnimation;

    /**
     * Default Bonus Constructor
     *
     * @param area     (Area): the area where is the bonus. Not null
     * @param position (DiscreteCoordinates): the position of the bonus in the specific area. Not null
     */
    public Bonus(Area area, DiscreteCoordinates position) {
        super(area, Orientation.DOWN, position, new SoundAcoustics("sounds/pacman/transactionOK.wav", 0.35f, false,false,false, false));

        // Extract Sprites and set animations of the bonus
        Sprite[] sprites = Sprite.extractSprites("superpacman/coin", 4, 1, 1, this, 16, 16);
        for (Sprite sprite: sprites) {
            sprite.setDepth(950);
        }
        currentAnimation = new Animation(ANIMATION_DURATION, sprites);
    }


    /* -------------- Implements Interactable ---------------- */

    @Override
    public void acceptInteraction(AreaInteractionVisitor v) {
        ((SuperPacmanInteractionVisitor)v).interactWith(this);
    }

    /* -------------- Implements Graphics ---------------- */

    @Override
    public void update(float deltaTime) {
        currentAnimation.update(deltaTime);
    }

    @Override
    public void draw(Canvas canvas) {
        currentAnimation.draw(canvas);
    }
}
