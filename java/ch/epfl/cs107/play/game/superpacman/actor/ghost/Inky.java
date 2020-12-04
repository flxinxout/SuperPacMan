package ch.epfl.cs107.play.game.superpacman.actor.ghost;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.AreaBehavior;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.game.superpacman.SuperPacman;
import ch.epfl.cs107.play.game.superpacman.area.SuperPacmanArea;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RandomGenerator;

import java.util.Queue;

public class Inky extends Ghost {
    public Inky(Area area, Orientation orientation, DiscreteCoordinates home) {
        super(area, orientation, home);
    }

    @Override
    protected Animation[] getAnimations() {
        //Setup the animations for Pacman
        Sprite[][] sprites = RPGSprite.extractSprites ("superpacman/ghost.inky", 4, 1, 1,
                this , 16, 16, new Orientation [] { Orientation.UP ,
                        Orientation.RIGHT , Orientation.DOWN , Orientation.LEFT });
        // Create an array of 4 animations
        Animation[] animations = Animation.createAnimations (ANIMATION_DURATION /2, sprites);

        return animations;
    }

    @Override
    public Orientation getNextOrientation() {
        SuperPacmanArea area = (SuperPacmanArea) getOwnerArea();
        Queue<Orientation> path = area.shortestPath(getCurrentMainCellCoordinates() , getTargetPos());
        if (path != null) {
            return path.poll();
        } else {
            path = area.shortestPath(getCurrentMainCellCoordinates(), randomCellInARange(getMaxDistance()));
            return path.poll();
        }
    }
}
