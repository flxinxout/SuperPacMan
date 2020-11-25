package ch.epfl.cs107.play.game.tutos.actor;

import ch.epfl.cs107.play.game.actor.Entity;
import ch.epfl.cs107.play.game.actor.TextGraphics;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Button;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Keyboard;

import java.awt.*;

public class SimpleGhost extends Entity {

    private Sprite sprite;
    private String spriteName;
    private float levelEnergie;
    private TextGraphics hpText;

    public SimpleGhost(Vector position, String spriteName) {
        super(position);
        this.spriteName = spriteName;
        this.levelEnergie = 5;
        this.sprite = new Sprite(spriteName, 1, 1.f, this);
        this.hpText = new TextGraphics(Integer.toString((int)levelEnergie), 0.4f, Color.BLUE);
        this.hpText.setParent(this);
        this.hpText.setAnchor(new Vector(-0.3f, 0.1f));

    }

    public boolean isWeak() {
        return levelEnergie <= 0;
    }

    public void strengthen() {
        this.levelEnergie = 5;
    }

    @Override
    public void draw(Canvas canvas) {
        hpText.draw(canvas);
        sprite.draw(canvas);
    }

    @Override
    public void update(float deltaTime) {
        if(levelEnergie > 0) {
            this.levelEnergie -= deltaTime;
        }
        hpText.setText(Integer.toString((int)levelEnergie));
    }

    public void moveUp(float delta) {
        setCurrentPosition(getPosition().add(0.f, delta));
    }

    public void moveDown(float delta) {
        setCurrentPosition(getPosition().add(0.f, -delta));
    }

    public void moveLeft(float delta) {
        setCurrentPosition(getPosition().add(-delta, 0.f));
    }

    public void moveRight(float delta) {
        setCurrentPosition(getPosition().add(delta, 0.f));
    }
}
