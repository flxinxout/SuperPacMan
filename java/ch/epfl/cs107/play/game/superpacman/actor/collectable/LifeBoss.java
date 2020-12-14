package ch.epfl.cs107.play.game.superpacman.actor.collectable;

import ch.epfl.cs107.play.game.actor.SoundAcoustics;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.CollectableAreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.superpacman.handler.SuperPacmanInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Canvas;

import java.util.Collections;
import java.util.List;

/**
 * Class that represents a life of the Boss of the pacman game
 */
public class LifeBoss extends CollectableAreaEntity {

    // Animation duration in frame number
    private final static int ANIMATION_DURATION = 8;
    private Animation currentAnimation;

    /**
     * Default LifeBoss constructor
     *
     * @param area           (Area): the area where is the entity. Not null
     * @param orientation    (Orientation): the orientation of the entity. Not null
     * @param position       (DiscreteCoordinates): the position in the area. Not null
     * @param soundAcoustics (SoundAcoustic): the sound when the it's collected. Not null
     */
    public LifeBoss(Area area, Orientation orientation, DiscreteCoordinates position, SoundAcoustics soundAcoustics) {
        super(area, orientation, position, soundAcoustics);

        // Extract Sprites and set animations of the heart
        //TODO: PUT THE CORRECT SPRITE
        Sprite[] sprites = Sprite.extractSprites("zelda/heart", 4, 1, 1, this, 16, 16);
        for (Sprite sprite: sprites) {
            sprite.setDepth(950);
        }

        currentAnimation = new Animation(ANIMATION_DURATION, sprites);
    }

    /* -------------- Implements Interactable ---------------- */

    @Override
    public void acceptInteraction(AreaInteractionVisitor v) { ((SuperPacmanInteractionVisitor)v).interactWith(this); }

    /* -------------- Implements Graphics ---------------- */

    @Override
    public void draw(Canvas canvas) { currentAnimation.draw(canvas); }

    @Override
    public void update(float deltaTime) { currentAnimation.update(deltaTime); }
}
