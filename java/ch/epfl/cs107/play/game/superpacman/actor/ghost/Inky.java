package ch.epfl.cs107.play.game.superpacman.actor.ghost;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Path;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.game.superpacman.area.SuperPacmanArea;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Type of ghost in the SuperPacman game
 * Ghost that follows the SuperPacmanPlayer depending on his condition
 */
public class Inky extends Ghost {

    // Attributes of Inky
    private final int MAX_DISTANCE_WHEN_SCARED = 5;
    private final int MAX_DISTANCE_WHEN_NOT_SCARED = 10;
    private final int AFRAID_SPEED = 15;

    // Represents the distance to which he obeys depending on his condition
    private int maxDistance;

    /**
     * Default Inky constructor
     * @param area        (Area): Owner area. Not null
     * @param orientation (Orientation): Initial orientation of the entity. Not null
     * @param home        (Coordinate): Initial and home position of the ghost. Not null
     */
    public Inky(Area area, Orientation orientation, DiscreteCoordinates home) {
        super(area, orientation, home);

        setSpeed(getDEFAULT_SPEED());

        maxDistance = MAX_DISTANCE_WHEN_NOT_SCARED;
    }


    /* --------------- Extends Ghost --------------- */

    @Override
    protected Animation[] getAnimations() {

        // Extracts the sprites of the ghost and sets the animations of the ghost
        Sprite[][] sprites = RPGSprite.extractSprites ("superpacman/ghost.inky", 2, 1.f, 1.f,
                this , 16, 16, new Orientation [] { Orientation.UP ,
                        Orientation.RIGHT , Orientation.DOWN , Orientation.LEFT });
        for (int i = 0; i < sprites.length; i++) {
            for (int j = 0; j < sprites[i].length; j++) {
                sprites[i][j].setDepth(950);
            }
        }

        // Sets the animations of the ghost
        Animation[] animations = Animation.createAnimations (getAnimationDuration() /2, sprites);

        return animations;
    }

    @Override
    public Orientation getNextOrientation() {

        // Gets the area where is the ghost and the path between the ghost and the SuperPacmanPlayer
        SuperPacmanArea area = (SuperPacmanArea) getOwnerArea();
        Queue<Orientation> path = area.getGraph().shortestPath(getCurrentMainCellCoordinates(), getTargetPos());

        // While the path is null or empty (for example if the ghost has not a target now), generate another path
        while (path == null || path.isEmpty()) {
            path = area.getGraph().shortestPath(getCurrentMainCellCoordinates(), randomCell());
        }

        graphicPath = new Path( this . getPosition () , new
                LinkedList< >( path));

        return path.poll();
    }

    @Override
    protected void onScared() {
        maxDistance = MAX_DISTANCE_WHEN_SCARED;
        setSpeed(AFRAID_SPEED);
        updateTarget();
    }

    @Override
    protected void onUnscared() {
        maxDistance = MAX_DISTANCE_WHEN_NOT_SCARED;
        setSpeed(getDEFAULT_SPEED());
        updateTarget();
    }

    @Override
    protected void updateTarget() {

        // If ghost is afraid
        if(isAfraid()) {

            // The target position will be a cell between his home and the maxDistance, so here the distance when he's scared
            setTargetPos(randomCell(getHome(), maxDistance));
        } else {

            // If he's not afraid and the target is null
            if (getPlayer() == null) {

                // The target position will be a random cells between his home and the maxDistance, so here the distance when he's not scared
                setTargetPos(randomCell(getHome(), maxDistance));
            } else {

                // If the target is not null, the target position will be the target's position
                setTargetPos(getPlayer().getCurrentCells().get((0)));
            }
        }
    }
}
