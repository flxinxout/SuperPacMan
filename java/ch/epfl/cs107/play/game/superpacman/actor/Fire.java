package ch.epfl.cs107.play.game.superpacman.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.superpacman.handler.SuperPacmanInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Canvas;

import java.util.Collections;
import java.util.List;

public class Fire extends AreaEntity {
    // Animations
    private final int ANIMATION_DURATION = 8; // Animation duration in frame number
    private Animation animation;

    private final float LIFE_DURATION = 5f;
    private float lifeTimer;

    /**
     * Default Fire constructor
     *
     * @param area        (Area): Owner area. Not null
     * @param orientation (Orientation): Initial orientation of the entity in the Area. Not null
     * @param position    (DiscreteCoordinate): Initial position of the entity in the Area. Not null
     */
    public Fire(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);

        this.lifeTimer = LIFE_DURATION;

        Sprite[] sprites = Sprite.extractSprites("superpacman/fire", 7, 1, 1, this, 16, 16);
        animation = new Animation(ANIMATION_DURATION/2, sprites);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        lifeTimer -= deltaTime;
        if(lifeTimer <= 0) {
            getOwnerArea().unregisterActor(this);
        }

        animation.update(deltaTime);
    }

    @Override
    public void draw(Canvas canvas) {
        animation.draw(canvas);
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
    public void acceptInteraction (AreaInteractionVisitor v) { ((SuperPacmanInteractionVisitor)v).interactWith (this ); }
}
