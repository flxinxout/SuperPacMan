package ch.epfl.cs107.play.game.superpacman.actor.collactable;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.game.superpacman.actor.Rewards;
import ch.epfl.cs107.play.game.superpacman.actor.collactable.CollectableAreaEntity;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Canvas;

import java.util.Collections;
import java.util.List;

public class Diamond extends AreaEntity implements CollectableAreaEntity {

    private final int reward = 10;
    private Sprite sprite;

    public Diamond(Area area, DiscreteCoordinates discreteCoordinates) {
        super(area, Orientation.DOWN, discreteCoordinates);
        this.sprite = new RPGSprite("superpacman/diamond", 1, 1, this);
    }

    @Override
    public void collected() {
        
    }

    @Override
    public void draw(Canvas canvas) {
        sprite.draw(canvas);
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
