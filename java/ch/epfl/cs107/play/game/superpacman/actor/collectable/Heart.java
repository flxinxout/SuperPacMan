package ch.epfl.cs107.play.game.superpacman.actor.collectable;

import ch.epfl.cs107.play.game.actor.SoundAcoustics;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.*;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.superpacman.handler.SuperPacmanInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Canvas;

/**
 * Heart item gives one HP when collected
 */
public class Heart extends CollectableAreaEntity {

    private final int ANIMATION_DURATION = 8; // Animation duration in frame number
    private Animation currentAnimation;

    /**
     * Default Heart constructor
     *
     * @param area        (Area): the area where is the entity. Not null
     * @param orientation (Orientation): the orientation. Not null
     * @param position    (DiscreteCoordinates): the position in the area. Not null
     */
    public Heart(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position, new SoundAcoustics("sounds/pacman/transactionOK.wav", 0.35f, false,false,false, false));

        // Extract Sprites and set animations of the heart
        Sprite[] sprites = Sprite.extractSprites("zelda/heart", 4, 1, 1, this, 16, 16);
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
    public void draw(Canvas canvas) { currentAnimation.draw(canvas); }

    @Override
    public void update(float deltaTime) {
        currentAnimation.update(deltaTime);
    }

}
