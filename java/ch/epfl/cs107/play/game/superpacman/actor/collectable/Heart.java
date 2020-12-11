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
 * Heart Item in the SuperPacman game
 * Gives one HP when collected
 */
public class Heart extends CollectableAreaEntity {

    // Animation duration in frame number
    private final static int ANIMATION_DURATION = 8;
    private Animation currentAnimation;
    private final SoundAcoustics ON_COLLECT_SOUND;

    /**
     * Default Heart constructor
     * @param area        the area where is the entity
     * @param orientation the orientation
     * @param position    the position in the area
     */
    public Heart(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);

        // Extract Sprites and set animations of the heart
        Sprite[] sprites = Sprite.extractSprites("zelda/heart", 4, 1, 1, this, 16, 16);
        for (Sprite sprite: sprites) {
            sprite.setDepth(950);
        }

        currentAnimation = new Animation(ANIMATION_DURATION, sprites);

        ON_COLLECT_SOUND = new SoundAcoustics("sounds/pacman/transactionOK.wav", 0.35f, false,false,false, false);
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

    /* -------------- Implement Actor ---------------- */

    @Override
    public void draw(Canvas canvas) { currentAnimation.draw(canvas); }

    @Override
    public void update(float deltaTime) {
        currentAnimation.update(deltaTime);
    }

    /* -------------- Extends CollectableAreaEntity ---------------- */

    @Override
    public SoundAcoustics getSoundOnCollect() {
        return ON_COLLECT_SOUND;
    }

}
