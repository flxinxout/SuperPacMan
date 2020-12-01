package ch.epfl.cs107.play.game.superpacman.actor.collectable;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.CollectableAreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.game.superpacman.SuperPacman;
import ch.epfl.cs107.play.game.superpacman.actor.SuperPacmanPlayer;
import ch.epfl.cs107.play.game.superpacman.area.SuperPacmanArea;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Canvas;

import java.util.Collections;
import java.util.List;

public class Diamond extends CollectableReward {

    /// Diamond's Sprite
    private Sprite sprite;

    /**
     * Default Diamond Constructor
     * @param area the area where is the bonus
     * @param position the position of the bonus in the specific area
     */
    public Diamond(Area area, DiscreteCoordinates position) {
        super(area, Orientation.DOWN, position, 10);
        this.sprite = new Sprite("superpacman/diamond", 1, 1, this);
    }

    /* -------------- Implement Actor ---------------- */

    @Override
    public void draw(Canvas canvas) {
        sprite.draw(canvas);
    }


    /* -------------- Implement Collectable ---------------- */

    @Override
    public void onCollect() {
        super.onCollect();
        //TODO: DISGUSTING CAST, TRY TO DO BETTER WITH DIAMOND COUNT
        SuperPacmanArea area = (SuperPacmanArea) getOwnerArea();
        area.removeDiamond();
    }
}
