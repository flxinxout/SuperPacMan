package ch.epfl.cs107.play.game.superpacman.actor.collactable;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.game.superpacman.actor.collactable.CollectableAreaEntity;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Canvas;

import java.util.Collections;
import java.util.List;

public class Bonus extends AreaEntity implements CollectableAreaEntity {

    private Sprite sprite;

    /// Animation duration in frame number
    private final static int ANIMATION_DURATION = 8;
    private Animation[] animations;
    private Animation currentAnimation;

    public Bonus(Area area, DiscreteCoordinates position) {
        super(area, Orientation.DOWN, position);


        /**
         * Juste ça qu'il manque l'animation du coin marche pas mais j'ai pas réellement cherché à comprendre pourquoi
         */
        //Setup the animations for Pacman
        Sprite[][] sprites = RPGSprite.extractSprites ("superpacman/coin", 4, 1, 1,
                this , 64, 64, new Orientation [] { Orientation.UP ,
                        Orientation.RIGHT , Orientation.DOWN , Orientation.LEFT });

        // Create an array of 4 animations
        this.animations = Animation.createAnimations (ANIMATION_DURATION /2, sprites);
        this.currentAnimation = animations[2];
    }

    @Override
    public void collected() {

    }

    @Override
    public void draw(Canvas canvas) {
        currentAnimation.draw(canvas);
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    @Override
    public boolean takeCellSpace() {
        return false;
    }

    @Override
    public boolean isCellInteractable() {
        return true;
    }

    @Override
    public boolean isViewInteractable() {
        return false;
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v) {

    }
}
