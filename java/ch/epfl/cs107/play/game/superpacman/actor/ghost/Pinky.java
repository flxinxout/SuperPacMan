package ch.epfl.cs107.play.game.superpacman.actor.ghost;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.game.superpacman.actor.SuperPacmanPlayer;
import ch.epfl.cs107.play.game.superpacman.area.SuperPacmanArea;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RandomGenerator;

import java.util.Queue;

public class Pinky extends Ghost {

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
    protected void move(boolean isAfraid) {
        move(25);
    }

    @Override
    public Orientation getNextOrientation() {
        SuperPacmanArea area = (SuperPacmanArea) getOwnerArea();
        Queue<Orientation> path1 = area.getGraph().shortestPath(getCurrentMainCellCoordinates(), getTargetPos());

        if(path1 == null) {
            Queue<Orientation> path2 = area.getGraph().shortestPath(getCurrentMainCellCoordinates(), randomCellInARange());
            return path2.poll();
        } else {
            return path1.poll();
        }
    }

    @Override
    protected DiscreteCoordinates randomCellInARange(int range) {
        int randomX, randomY;
        DiscreteCoordinates randomCoordinates;

        do {
            randomX = RandomGenerator.getInstance().nextInt(getOwnerArea().getWidth());
            randomY = RandomGenerator.getInstance().nextInt(getOwnerArea().getHeight());
            randomCoordinates = new DiscreteCoordinates(randomX, randomY);
        }while (DiscreteCoordinates.distanceBetween(home, randomCoordinates) > MIN_AFRAID_DISTANCE);

        return randomCoordinates;
    }

    @Override
    protected DiscreteCoordinates saveTargetPos(SuperPacmanPlayer player) {
        if (player == null) {
            if (DiscreteCoordinates.distanceBetween(player.getCurrentCells().get(0), getCurrentMainCellCoordinates()) > FIELD_OF_VIEW) {
                //TODO: PAS OUF LE SETTER
                setPlayer(null);
                return randomCellInARange(getMaxDistance());
            }
        }
        return null;
    }


    protected int saveMaxDistance(boolean isAfraid) {
        //TODO: pas sur du max not scared car sinon je saurais pas quoi mettre
        return isAfraid ? MIN_AFRAID_DISTANCE : MAX_DISTANCE_WHEN_NOT_SCARED;
    }
}
