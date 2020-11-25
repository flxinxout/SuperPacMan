package ch.epfl.cs107.play.game.tutos.actor;

import ch.epfl.cs107.play.game.actor.TextGraphics;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.MovableAreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Keyboard;
import jdk.swing.interop.SwingInterOpUtils;

import java.awt.*;
import java.util.Collections;
import java.util.List;

public class GhostPlayer extends MovableAreaEntity {

    private Sprite sprite;
    private float levelEnergie;
    private TextGraphics hpText;
    private final static int ANIMATION_DURATION = 8;

    public GhostPlayer(Area owner , Orientation orientation ,
                              DiscreteCoordinates coordinates , String sprite) {
        super(owner, orientation, coordinates);
        this.levelEnergie = 10;
        this.sprite = new Sprite(sprite, 1, 1.f, this);
        this.hpText = new TextGraphics(Integer.toString((int)levelEnergie), 0.4f, Color.BLUE);
        this.hpText.setParent(this);
        this.hpText.setAnchor(new Vector(-0.3f, 0.1f));


    }

    public boolean isWeak() {
        return levelEnergie <= 0;
    }

    public void strengthen() {
        this.levelEnergie = 10;
    }

    public void enterArea(Area area , DiscreteCoordinates position) {
        area.registerActor(this);
        setOwnerArea(area);
        setCurrentPosition(position.toVector());
        area.setViewCandidate(this);
        resetMotion();
    }

    public void leaveArea() {
        getOwnerArea().unregisterActor(this);
    }

    @Override
    public void draw(Canvas canvas) {
        hpText.draw(canvas);
        sprite.draw(canvas);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        Keyboard keyboard = getOwnerArea().getKeyboard();


        if(keyboard.get(Keyboard.LEFT).isDown()) {
            if(getOrientation().equals(Orientation.LEFT)) {
                move(ANIMATION_DURATION);
            } else {
                orientate(Orientation.LEFT);
            }
        }

        if(keyboard.get(Keyboard.RIGHT).isDown()) {
            if(getOrientation().equals(Orientation.RIGHT)) {
                move(ANIMATION_DURATION);
            } else {
                orientate(Orientation.RIGHT);
            }
        }

        if(keyboard.get(Keyboard.UP).isDown()) {
            if(getOrientation().equals(Orientation.UP)) {
                move(ANIMATION_DURATION);
            } else {
                orientate(Orientation.UP);
            }
        }

        if(keyboard.get(Keyboard.DOWN).isDown()) {
            if(getOrientation().equals(Orientation.DOWN)) {
                move(ANIMATION_DURATION);
            } else {
                orientate(Orientation.DOWN);
            }
        }

        if(levelEnergie > 0) {
            this.levelEnergie -= deltaTime;
        }

        hpText.setText(Integer.toString((int)levelEnergie));
    }

    @Override
    public List <DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }
    
    @Override
    public boolean takeCellSpace() {
        return true; // false avant
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
}
