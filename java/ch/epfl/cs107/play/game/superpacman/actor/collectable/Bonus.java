package ch.epfl.cs107.play.game.superpacman.actor.collectable;

import ch.epfl.cs107.play.game.actor.SoundAcoustics;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.*;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.superpacman.handler.SuperPacmanInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Canvas;

import java.util.Collections;
import java.util.List;

/**
 * Bonus Item in the SuperPacman game
 * Gives invincibility to SuperPacmanPlayer
 */
public class Bonus extends CollectableAreaEntity {

    // Animation duration in frame number
    private final static int ANIMATION_DURATION = 8;
    private Animation currentAnimation;
    private final SoundAcoustics ON_COLLECT_SOUND;

    /**
     * Default Bonus Constructor
     * @param area the area where is the bonus
     * @param position the position of the bonus in the specific area
     */
    public Bonus(Area area, DiscreteCoordinates position) {
        super(area, Orientation.DOWN, position);

        // Extract Sprites and set animations of the bonus
        Sprite[] sprites = Sprite.extractSprites("superpacman/coin", 4, 1, 1, this, 16, 16);
        for (Sprite sprite: sprites) {
            sprite.setDepth(950);
        }
        currentAnimation = new Animation(ANIMATION_DURATION, sprites);

        ON_COLLECT_SOUND = new SoundAcoustics("sounds/pacman/transactionOK.wav", 0.35f, false,false,false, false);
    }

    /* -------------- Extends CollectableAreaEntity ---------------- */

    @Override
    public SoundAcoustics getSoundOnCollect() {
        return ON_COLLECT_SOUND;
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

    @Override
    public void acceptInteraction(AreaInteractionVisitor v) {
        ((SuperPacmanInteractionVisitor)v).interactWith(this);
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
}
