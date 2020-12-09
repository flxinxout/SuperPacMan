package ch.epfl.cs107.play.game.superpacman.actor.ghost;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Path;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.game.superpacman.actor.SuperPacmanPlayer;
import ch.epfl.cs107.play.game.superpacman.area.SuperPacmanArea;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RandomGenerator;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Type of ghost Pinky in the SuperPacman game
 * Ghost that follows and escapes the SuperPacmanPlayer depending on his condition
 */
public class Pinky extends Ghost {

    // Minimum distance when he's afraid
    private final static int MIN_AFRAID_DISTANCE = 5;

    // Max attempts allowed to escape
    private final static int MAX_RANDOM_ATTEMPT = 200;
    private final int AFRAID_SPEED = 15;

    /**
     * Default Pinky constructor
     * @param area        (Area): Owner area. Not null
     * @param orientation (Orientation): Initial orientation of the entity. Not null
     * @param home        (Coordinate): Initial and home position of the ghost. Not null
     */
    public Pinky(Area area, Orientation orientation, DiscreteCoordinates home) {
        super(area, orientation, home);
    }


    /* --------------- Extends Ghost --------------- */

    @Override
    protected Animation[] getAnimations() {

        // Extracts the sprites of the ghost and sets the animations of the ghost
        Sprite[][] sprites = RPGSprite.extractSprites ("superpacman/ghost.pinky", 2, 1.f, 1.f,
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

        // While the path is null or empty (for example if the ghost has not a target now), generate an other path
        while (path == null || path.isEmpty()) {
            DiscreteCoordinates cell = randomCell();
            path = area.getGraph().shortestPath(getCurrentMainCellCoordinates(), cell);
        }

        graphicPath = new Path( this . getPosition () , new
                LinkedList< >( path));

        return path.poll();
    }

    @Override
    protected void updateTarget() {

        // If ghost is afraid
        if (isAfraid()) {

            // If he's afraid and the target is null
            if (getPlayer() == null) {

                // The target position will be a cell in the area
                setTargetPos(randomCell());
            } else {

                // The target position will be a random cell in the map in a minimal distance of the player
                setTargetPos(randomCellFarFromPlayer());
            }
        }
        else {

            // If he's not afraid and the target is null
            if (getPlayer() == null) {

                // The target position will be a cell in the area
                setTargetPos(randomCell());
            } else {

                // If the target is not null, the target position will be the target's position
                setTargetPos(getPlayer().getCurrentCells().get((0)));
            }
        }
    }

    /**
     * @Note: In the PDF it is written to increase Inky's speed when he's afraid,
     * we decided to do it with Pinky too because, as he runs away when he's scared
     * it seems more logic
     */
    @Override
    protected void onScared() {
        updateTarget();
        setSpeed(AFRAID_SPEED);
    }

    @Override
    protected void onUnscared() {
        updateTarget();
        setSpeed(getDEFAULT_SPEED());
    }


    /* --------------- External Methods --------------- */

    /**
     * Choose a random cell in the map in a minimal distance of the player
     * @return cell's coordinates far from SuperPacmanPlayer, but at "MIN_AFRAID_DISTANCE" from it
     */
    private DiscreteCoordinates randomCellFarFromPlayer() {
        DiscreteCoordinates cellAttempt;
        int attempts = 0;

        // Generate a random cell in the area until the distance between this cell is further than the MIN_AFRAID_DISTANCE and that the attempts are less that MAX_RANDOM_ATTEMPT
        do {
            cellAttempt = randomCell();
            ++attempts;
        } while(DiscreteCoordinates.distanceBetween(cellAttempt,
                getPlayer().getCurrentCells().get(0)) < MIN_AFRAID_DISTANCE
                && attempts < MAX_RANDOM_ATTEMPT);

        return cellAttempt;
    }
}
