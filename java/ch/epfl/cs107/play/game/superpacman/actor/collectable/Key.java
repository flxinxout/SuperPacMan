package ch.epfl.cs107.play.game.superpacman.actor.collectable;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.CollectableAreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.Signal;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Canvas;

import java.util.Collections;
import java.util.List;

public class Key extends CollectableAreaEntity implements Logic {
    private Sprite sprite;
    private boolean isCollected;

    public Key(Area area, DiscreteCoordinates position) {
        super(area, Orientation.DOWN, position);
        this.sprite = new Sprite("superpacman/key", 1, 1, this);
        isCollected = false;
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
    public void onCollect() {
        super.onCollect();
        isCollected = true;
    }

    @Override
    public boolean isOn() {
        return !isCollected;
    }

    @Override
    public boolean isOff() {
        return isCollected;
    }

    @Override
    public float getIntensity() {
        //TODO: TERNARY OPERATOR?
        if (isCollected) { return 1; }
        return 0;
    }
}
