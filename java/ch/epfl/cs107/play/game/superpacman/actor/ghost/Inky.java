package ch.epfl.cs107.play.game.superpacman.actor.ghost;

import ch.epfl.cs107.play.game.actor.GraphicsEntity;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.AreaBehavior;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Path;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.game.superpacman.SuperPacman;
import ch.epfl.cs107.play.game.superpacman.actor.SuperPacmanPlayer;
import ch.epfl.cs107.play.game.superpacman.area.SuperPacmanArea;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RandomGenerator;

import java.util.LinkedList;
import java.util.Queue;

public class Inky extends Ghost {

    /**
     * Default Inky constructor
     * @param area        (Area): Owner area. Not null
     * @param orientation (Orientation): Initial orientation of the entity. Not null
     * @param home        (Coordinate): Initial and home position of the ghost. Not null
     */
    public Inky(Area area, Orientation orientation, DiscreteCoordinates home) {
        super(area, orientation, home);
    }

    /* --------------- Extends Ghost --------------- */

    @Override
    protected Animation[] getAnimations() {
        Sprite[][] sprites = RPGSprite.extractSprites ("superpacman/ghost.inky", 4, 1, 1,
                this , 16, 16, new Orientation [] { Orientation.UP ,
                        Orientation.RIGHT , Orientation.DOWN , Orientation.LEFT });
        Animation[] animations = Animation.createAnimations (getAnimationDuration() /2, sprites);

        return animations;
    }

    @Override
    public Orientation getNextOrientation() {
        SuperPacmanArea area = (SuperPacmanArea) getOwnerArea();
        Queue<Orientation> path = area.getGraph().shortestPath(getCurrentMainCellCoordinates(), getTargetPos());

        if (path == null) {
            path = area.getGraph().shortestPath(getCurrentMainCellCoordinates(), randomCellInARange(getMaxDistance()));
            //graphicPath = new Path(this.getPosition(), new LinkedList<Orientation>(path2));
            path.poll();
        } else {
         //   graphicPath = new Path(this.getPosition(), new LinkedList<Orientation>(path1));
            return path.poll();
        }
        return null;
    }

    protected int saveMaxDistance(boolean isAfraid) {
        return isAfraid ? MAX_DISTANCE_WHEN_SCARED : MAX_DISTANCE_WHEN_NOT_SCARED;
    }

    @Override
    protected DiscreteCoordinates randomCellInARange(int range) {
        int randomX, randomY;
        DiscreteCoordinates randomCoordinates;

        do {
            randomX = RandomGenerator.getInstance().nextInt(getOwnerArea().getWidth());
            randomY = RandomGenerator.getInstance().nextInt(getOwnerArea().getHeight());
            randomCoordinates = new DiscreteCoordinates(randomX, randomY);
        }while (DiscreteCoordinates.distanceBetween(home, randomCoordinates) > range);

        return randomCoordinates;
    }

    @Override
    protected void move(boolean isAfraid) {
        if(isAfraid) {
            move(25);
        } else {
            move(10);
        }
    }

    @Override
    protected DiscreteCoordinates saveTargetPos(SuperPacmanPlayer player) {
        if (player != null) {
            if (DiscreteCoordinates.distanceBetween(player.getCurrentCells().get(0), getCurrentMainCellCoordinates()) > FIELD_OF_VIEW) {
                //TODO: PAS OUF LE SETTER
                setPlayer(null);
                return randomCellInARange(getMaxDistance());
            }
        }
        return null;
    }
}
