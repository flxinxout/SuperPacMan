package ch.epfl.cs107.play.game.superpacman.actor.collectable;

import ch.epfl.cs107.play.game.actor.SoundAcoustics;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.CollectableAreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.superpacman.SuperPacman;
import ch.epfl.cs107.play.game.superpacman.actor.ennemy.Boss;
import ch.epfl.cs107.play.game.superpacman.handler.SuperPacmanInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

/**
 * [EXTENSION] A BossLife represents a health point of a boss
 */
public class BossLife extends CollectableAreaEntity {

    //The boss attached to it
    private final Boss boss;

    // Animation
    private final Animation animation;

    /**
     * Default BossLife constructor
     *
     * @param area           (Area): the area where is the entity. Not null
     * @param position       (DiscreteCoordinates): the position in the area. Not null
     */
    public BossLife(Area area, DiscreteCoordinates position, Boss boss) {
        super(area, Orientation.DOWN, position, new SoundAcoustics("sounds/pacman/transactionFail.wav", 0.35f, false,false,false, false));

        this.boss = boss;

        // Extract Sprites and set animations
        Sprite[] sprites = Sprite.extractSprites("superpacman/orb", 6, .75f, .75f, this, new Vector(0.125f,0.125f), 32, 32);
        for (Sprite sprite: sprites) {
            sprite.setDepth(950);
        }

        animation = new Animation(SuperPacman.getDefaultAnimationDuration(), sprites);
    }

    /* -------------- Getters ---------------- */

    /**
     * Getter for the attached boss
     * @return (Boss)
     */
    public Boss getBoss() {
        return boss;
    }

    /* -------------- Implements Interactable ---------------- */

    @Override
    public void acceptInteraction(AreaInteractionVisitor v) { ((SuperPacmanInteractionVisitor)v).interactWith(this); }

    /* -------------- Implements Graphics ---------------- */

    @Override
    public void draw(Canvas canvas) { animation.draw(canvas); }

    @Override
    public void update(float deltaTime) { animation.update(deltaTime); }
}
