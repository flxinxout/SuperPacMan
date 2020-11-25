package ch.epfl.cs107.play.game.tutos.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.MovableAreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Keyboard;

import java.util.Collections;
import java.util.List;

public class GhostPlayer extends MovableAreaEntity {
    /// Animation duration in frame number
    private final static int ANIMATION_DURATION = 8;

    private float hp;
    private Sprite sprite;

    public boolean isWeak() {
        return hp <= 0;
    }

    public void strengthen() {
        this.hp = 5;
    }

    public GhostPlayer ( Area owner , Orientation orientation ,
                         DiscreteCoordinates coordinates , String sprite ) {

        super(owner, orientation, coordinates);
        this.sprite = new Sprite(sprite,1 , 1.f, this);
        this.hp = 5;
    }

    @Override
    public boolean takeCellSpace() {
        return true;
    }

    @Override
    public boolean isCellInteractable() {
        return true;
    }

    @Override
    public boolean isViewInteractable() {
        return true;
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v) {

    }

    public void enterArea(Area area , DiscreteCoordinates position) {
        setOwnerArea(area);
        area.registerActor(this);

        setCurrentPosition(position.toVector()) ;
        resetMotion();
    }

    public void exitArea() {
        getOwnerArea().unregisterActor(this);
    }

    @Override
    public List< DiscreteCoordinates > getCurrentCells () {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    @Override
    public void draw(Canvas canvas) {
        sprite.draw(canvas);
    }

    @Override
    public void update(float deltaTime) {
        Keyboard keyboard = getOwnerArea().getKeyboard();

        //Left movement
        if(keyboard.get(Keyboard.LEFT).isDown()) {
            if (getOrientation().equals(Orientation.LEFT)) {
                move(ANIMATION_DURATION);
            } else {
                orientate(Orientation.LEFT);
            }
        }

        //Right movement
        if(keyboard.get(Keyboard.RIGHT).isDown()) {
            if (getOrientation().equals(Orientation.RIGHT)) {
                move(ANIMATION_DURATION);
            } else {
                orientate(Orientation.RIGHT);
            }
        }

        //Up movement
        if(keyboard.get(Keyboard.UP).isDown()) {
            if (getOrientation().equals(Orientation.UP)) {
                move(ANIMATION_DURATION);
            } else {
                orientate(Orientation.UP);
            }
        }

        //Down movement
        if(keyboard.get(Keyboard.DOWN).isDown()) {
            if (getOrientation().equals(Orientation.DOWN)) {
                move(ANIMATION_DURATION);
            } else {
                orientate(Orientation.DOWN);
            }
        }

        super.update(deltaTime);

        if (hp > 0) {
            hp -= deltaTime;
        }
    }
}
