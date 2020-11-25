package ch.epfl.cs107.play.game.tutos.actor;

import ch.epfl.cs107.play.game.actor.Entity;
import ch.epfl.cs107.play.game.actor.TextGraphics;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

import java.awt.Color;

public class SimpleGhost extends Entity {
    private Sprite sprite;
    private float hp;
    private TextGraphics hpText;

    public boolean isWeak() {
        return hp <= 0;
    }

    public void strengthen() {
        this.hp = 10;
    }

    public SimpleGhost(Vector position , String spriteName) {
        super(position);

        sprite = new Sprite(spriteName,1 , 1.f, this);

        hpText = new TextGraphics(Integer.toString((int)hp), 0.4f, Color.BLUE);
        hpText.setParent(this);
        this.hpText.setAnchor(new Vector(-0.3f, 0.1f));

        this.hp = 10;
    }

    @Override
    public void draw(Canvas canvas) {
        sprite.draw(canvas);
        hpText.draw(canvas);
    }

    @Override
    public void update(float deltaTime) {
        if (hp > 0) {
            hp -= deltaTime;
        }

        hpText.setText(Integer.toString((int)hp));
    }

    public void moveUp(float delta) {
        setCurrentPosition(getPosition().add(0.f, delta));
    }

    public void moveDown(float delta) {
        setCurrentPosition(getPosition().add(0.f, -delta));
    }

    public void moveRight(float delta) {
        setCurrentPosition(getPosition().add(delta, 0.f));
    }

    public void moveLeft(float delta) {
        setCurrentPosition(getPosition().add(-delta, 0.f));
    }
}
