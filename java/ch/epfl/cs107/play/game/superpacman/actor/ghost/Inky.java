package ch.epfl.cs107.play.game.superpacman.actor.ghost;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.game.superpacman.SuperPacman;
import ch.epfl.cs107.play.game.superpacman.area.SuperPacmanArea;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

import java.util.Queue;

/**
 * Inky is a ghost which moves in a specific radius around its home.
 * It starts targeting the player when he's close
 */
public class Inky extends Ghost {

    // Constants
    private final int FIELD_OF_VIEW = 5;
    private final int MAX_DISTANCE_WHEN_SCARED = 5;
    private final int MAX_DISTANCE_WHEN_NOT_SCARED = 10;
    private final int DEFAULT_SPEED = 20;
    private final int AFRAID_SPEED = 15;

    // Represents the distance to which it obeys depending on his condition
    private int maxDistance;

    /**
     * Default Inky constructor
     *
     * @param area        (Area): owner area. Not null
     * @param orientation (Orientation): initial orientation of the entity. Not null
     * @param home        (Coordinate): initial and home position of the ghost. Not null
     */
    public Inky(Area area, Orientation orientation, DiscreteCoordinates home) {
        super(area, orientation, home);

        maxDistance = MAX_DISTANCE_WHEN_NOT_SCARED;
    }


    /* --------------- Extends Ghost --------------- */

    @Override
    protected Animation[] getAnimations() {

        // Extracts the sprites of the ghost and sets the animations
        Sprite[][] sprites = RPGSprite.extractSprites ("superpacman/ghost.inky", 2, 1.f, 1.f,
                this , 16, 16, new Orientation [] { Orientation.UP ,
                        Orientation.RIGHT , Orientation.DOWN , Orientation.LEFT });
        for (Sprite[] sprite : sprites) {
            for (Sprite value : sprite) {
                value.setDepth(950);
            }
        }

        return Animation.createAnimations (SuperPacman.getDefaultAnimationDuration(), sprites);
    }

    @Override
    public Orientation getNextOrientation() {

        // Gets the area where is the ghost and the path between the ghost and the target positiom
        SuperPacmanArea area = SuperPacmanArea.toSuperPacmanArea(getOwnerArea());
        Queue<Orientation> path = area.shortestPath(getCurrentMainCellCoordinates(), getTargetPos());

        // While the path is null or empty, generate another path
        while (path == null || path.isEmpty()) {
            path = area.shortestPath(getCurrentMainCellCoordinates(), randomCell());
        }

        return path.poll();
    }

    @Override
    protected void onScareChange() {
        //Set the max distance
        if (!isAfraid()) {
            maxDistance = MAX_DISTANCE_WHEN_SCARED;
        } else {
            maxDistance = MAX_DISTANCE_WHEN_NOT_SCARED;
        }

        super.onScareChange();
    }

    @Override
    protected DiscreteCoordinates getTargetPos() {
        if(isAfraid()) {
            return randomCell(getHOME(), maxDistance);
        } else {
            if (getPlayer() == null) {
                return randomCell(getHOME(), maxDistance);
            } else {
                return getPlayer().getCurrentCells().get((0));
            }
        }
    }

    @Override
    protected int getSpeed() {
        if (!isAfraid()) {
            return AFRAID_SPEED;
        } else {
            return DEFAULT_SPEED;
        }
    }
}
