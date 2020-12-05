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

public class Pinky extends Ghost {
    private final static int MIN_AFRAID_DISTANCE = 5;
    private final static int MAX_RANDOM_ATTEMPT = 200;

    /**
     * Default Pinky constructor
     *
     * @param area        (Area): Owner area. Not null
     * @param orientation (Orientation): Initial orientation of the entity. Not null
     * @param home        (Coordinate): Initial and home position of the ghost. Not null
     */
    public Pinky(Area area, Orientation orientation, DiscreteCoordinates home) {
        super(area, orientation, home);
    }

    @Override
    protected Animation[] getAnimations() {
        Sprite[][] sprites = RPGSprite.extractSprites ("superpacman/ghost.pinky", 4, 1, 1,
                this , 16, 16, new Orientation [] { Orientation.UP ,
                        Orientation.RIGHT , Orientation.DOWN , Orientation.LEFT });
        Animation[] animations = Animation.createAnimations (getAnimationDuration() /2, sprites);

        return animations;
    }

    @Override
    public Orientation getNextOrientation() {
        SuperPacmanArea area = (SuperPacmanArea) getOwnerArea();
        Queue<Orientation> path = area.getGraph().shortestPath(getCurrentMainCellCoordinates(), getTargetPos());

        while (path == null || path.isEmpty()) {
            DiscreteCoordinates cell = randomCell();
            path = area.getGraph().shortestPath(getCurrentMainCellCoordinates(), cell);
        }

        //graphicPath = new Path(this.getPosition(), new LinkedList<>(path));
        return path.poll();
    }

    @Override
    protected void onScared() {
        updateTarget();
    }

    @Override
    protected void onUnscared() {
        updateTarget();
    }

    protected DiscreteCoordinates randomCellFarFromPlayer() {
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

    @Override
    protected void updateTarget() {
        if (isAfraid()) {
            if (getPlayer() == null) {
                setTargetPos(randomCell());
            } else {
                setTargetPos(randomCellFarFromPlayer());
            }
        }
        else {
            if (getPlayer() == null) {
                setTargetPos(randomCell());
            } else {
                setTargetPos(getPlayer().getCurrentCells().get((0)));
            }
        }
    }
}
