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

public class Inky extends Ghost {
    private static final int MAX_DISTANCE_WHEN_SCARED = 5;
    private static final int MAX_DISTANCE_WHEN_NOT_SCARED = 10;
    private static final int DEFAULT_SPEED = 25;
    private static final int AFRAID_SPEED = 20;
    private static int maxDistance;

    /**
     * Default Inky constructor
     * @param area        (Area): Owner area. Not null
     * @param orientation (Orientation): Initial orientation of the entity. Not null
     * @param home        (Coordinate): Initial and home position of the ghost. Not null
     */
    public Inky(Area area, Orientation orientation, DiscreteCoordinates home) {
        super(area, orientation, home);
        setSpeed(DEFAULT_SPEED);
        maxDistance = MAX_DISTANCE_WHEN_NOT_SCARED;
    }

    /* --------------- Extends Ghost --------------- */

    @Override
    protected Animation[] getAnimations() {
        Sprite[][] sprites = RPGSprite.extractSprites ("superpacman/ghost.inky", 2, 1.f, 1.f,
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
            path = area.getGraph().shortestPath(getCurrentMainCellCoordinates(), randomCell());
        }

        graphicPath = new Path(this.getPosition(), new LinkedList<>(path));
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
        setSpeed(DEFAULT_SPEED);
        updateTarget();
    }

    @Override
    protected void updateTarget() {
        if (getPlayer() == null) {
            setTargetPos(randomCell(getHome(), maxDistance));
        } else {
            setTargetPos(getPlayer().getCurrentCells().get((0)));
        }
    }
}
