package ch.epfl.cs107.play.game.superpacman.actor.collectable;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.*;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Canvas;

import java.util.Collections;
import java.util.List;

public class Bonus extends CollectableAreaEntity {
    /// Animation duration in frame number
    private final static int ANIMATION_DURATION = 8;
    private Animation currentAnimation;

    public Bonus(Area area, DiscreteCoordinates position) {
        super(area, Orientation.DOWN, position);

        //Setup the animations
        Sprite[] sprites = Sprite.extractSprites("superpacman/coin", 4, 1, 1, this, 16, 16);
        this.currentAnimation = new Animation(ANIMATION_DURATION, sprites);
    }

    @Override
    public void update(float deltaTime) {
        currentAnimation.update(deltaTime);
    }

    @Override
    public void draw(Canvas canvas) {
        currentAnimation.draw(canvas);
    }
}
