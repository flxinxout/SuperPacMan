package ch.epfl.cs107.play.game.superpacman.actor.ghost;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.game.superpacman.actor.SuperPacmanPlayer;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RandomGenerator;

public class Blinky extends Ghost {

    /**
     * Default Blinky constructor
     * @param area        (Area): Owner area. Not null
     * @param orientation (Orientation): Initial orientation of the entity. Not null
     * @param home        (Coordinate): Initial and home position of the ghost. Not null
     */
    public Blinky(Area area, Orientation orientation, DiscreteCoordinates home) {
        super(area, orientation, home);
    }

    /* --------------- Extends Ghost --------------- */

    @Override
    protected Animation[] getAnimations() {
        Sprite[][] sprites = RPGSprite.extractSprites ("superpacman/ghost.blinky", 4, 1, 1,
        this , 16, 16, new Orientation [] { Orientation.UP ,
                Orientation.RIGHT , Orientation.DOWN , Orientation.LEFT });
        Animation[] animations = Animation.createAnimations (getAnimationDuration() /2, sprites);

        return animations;
    }

    @Override
    public Orientation getNextOrientation() {
        int randomInt = RandomGenerator.getInstance().nextInt(4);
        return Orientation.fromInt(randomInt);
    }

    @Override
    protected void move(boolean isAfraid) {
        move(25);
    }

    @Override
    protected DiscreteCoordinates randomCellInARange(int range) {
        return null;
    }

    @Override
    protected DiscreteCoordinates saveTargetPos(SuperPacmanPlayer player) {
        return null;
    }
}
