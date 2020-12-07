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
 * Type of ghost in the SuperPacman game
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

        // Extracts the sprites of the ghost
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

        return path.poll();
    }

    @Override
    protected void updateTarget() {
        if (isAfraid()) {
            if (getPlayer() == null) {

                // Sets a random target cell
                setTargetPos(randomCell());
            } else {

                // Sets a random target cell in the radius of the MIN_AFRAID_DISTANCE
                setTargetPos(randomCellFarFromPlayer());
            }
        }
        else {
            if (getPlayer() == null) {

                // Sets a random target cell
                setTargetPos(randomCell());
            } else {

                // Sets the target of the ghost, so the player's position
                setTargetPos(getPlayer().getCurrentCells().get((0)));
            }
        }
    }

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
     * @return cell's coordinates far from SuperPacmanPlayer, but at "MIN_AFRAID_DISTANCE" from it
     */
    private DiscreteCoordinates randomCellFarFromPlayer() {
        DiscreteCoordinates cellAttempt;
        int attempts = 0;

        do {
            cellAttempt = randomCell();
            ++attempts;
        } while(DiscreteCoordinates.distanceBetween(cellAttempt,
                getPlayer().getCurrentCells().get(0)) < MIN_AFRAID_DISTANCE
                && attempts < MAX_RANDOM_ATTEMPT);

        return cellAttempt;
    }
}
