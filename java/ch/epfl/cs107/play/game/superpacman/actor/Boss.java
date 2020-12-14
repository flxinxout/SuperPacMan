package ch.epfl.cs107.play.game.superpacman.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.*;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.game.superpacman.actor.ghost.Ghost;
import ch.epfl.cs107.play.game.superpacman.area.SuperPacmanArea;
import ch.epfl.cs107.play.game.superpacman.handler.SuperPacmanInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;

public class Boss extends Ghost implements Interactor {

    private final int FIELD_OF_VIEW = 10;

    /**
     * Default Boss constructor
     *
     * @param area        (Area): owner area. Not null
     * @param orientation (Orientation): initial orientation of the entity in the Area. Not null
     * @param position    (DiscreteCoordinate): initial position of the entity in the Area. Not null
     */
    public Boss(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position, 15, 10);
    }

    /* --------------- protected Methods -------------- */

    @Override
    protected Orientation getNextOrientation() {

        // Gets the area where is the ghost and the path between the ghost and the SuperPacmanPlayer
        SuperPacmanArea area = (SuperPacmanArea) getOwnerArea();
        Queue<Orientation> path = area.shortestPath(getCurrentMainCellCoordinates(), getTargetPos());

        // While the path is null or empty (for example if the ghost has not a target now), generate an other path
        while (path == null || path.isEmpty()) {
            DiscreteCoordinates cell = randomCell();
            path = area.shortestPath(getCurrentMainCellCoordinates(), cell);
        }

        return path.poll();
    }

    @Override
    protected Animation[] getAnimations() {
        // Extracts the sprites of the ghost and sets the animations
        Sprite[][] sprites = RPGSprite.extractSprites ("zelda/flameskull", 3, 2f, 2f,
                this, 32, 32, new Vector(-0.5f, -0.5f), new Orientation [] { Orientation.UP ,
                        Orientation.LEFT , Orientation.DOWN , Orientation.RIGHT });
        for (Sprite[] sprite : sprites) {
            for (Sprite value : sprite) {
                value.setDepth(950);
            }
        }

        return Animation.createAnimations (getAnimationDuration() /2, sprites);
    }

    /**@return (DiscreteCoordinates): the target's position */
    protected DiscreteCoordinates getTargetPos() {
        if(getPlayer() == null ||
                DiscreteCoordinates.distanceBetween(getPlayer().getCurrentCells().get(0), getCurrentMainCellCoordinates()) > FIELD_OF_VIEW) {
            return randomCell();
        } else {
            return getPlayer().getCurrentCells().get((0));
        }
    }
}
