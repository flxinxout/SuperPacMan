package ch.epfl.cs107.play.game.superpacman.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Canvas;

public class BonusPortal extends Door {
    private Sprite sprite;

    public BonusPortal(String destination, Logic signal, Area area, Orientation orientation, DiscreteCoordinates position) {
        super(destination, new DiscreteCoordinates(9, 18), signal, area, orientation, position);
        this.sprite = new Sprite("superpacman/portal", 1f, 1f, this);
    }

    @Override
    public void draw(Canvas canvas) {
        sprite.draw(canvas);
    }
}
