package ch.epfl.cs107.play.game.superpacman.actor.ghost;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RandomGenerator;

/**
 * Type of ghost in the SuperPacman game
 * Does nothing special
 */

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

        // Extracts the sprites of the ghost
        Sprite[][] sprites = RPGSprite.extractSprites ("superpacman/ghost.blinky", 2, 1.f, 1.f,
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

        // Creates a random number between 0 and 4
        int randomInt = RandomGenerator.getInstance().nextInt(4);

        // Returns an orientation from this random
        return Orientation.fromInt(randomInt);
    }

    @Override
    protected void onScared() { }

    @Override
    protected void onUnscared() { }

    @Override
    protected void updateTarget() { }
}
