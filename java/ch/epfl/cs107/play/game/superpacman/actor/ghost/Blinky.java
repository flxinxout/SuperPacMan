package ch.epfl.cs107.play.game.superpacman.actor.ghost;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.game.superpacman.SuperPacman;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RandomGenerator;

/**
 * Blinky is a ghost which moves randomly through the map.
 * It doesn't target the player when he's close
 */
public class Blinky extends Ghost {

    /**
     * Default Blinky constructor
     *
     * @param area        (Area): owner area. Not null
     * @param orientation (Orientation): initial orientation of the entity. Not null
     * @param position    (Coordinate): initial position of the entity. Not null
     */
    public Blinky(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);
    }


    /* --------------- Extends Ghost --------------- */

    @Override
    protected Animation[] getAnimations() {

        // Extracts the sprites and set the animations
        Sprite[][] sprites = RPGSprite.extractSprites ("superpacman/ghost.blinky", 2, 1.f, 1.f,
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
        int randomInt = RandomGenerator.getInstance().nextInt(4);
        return Orientation.fromInt(randomInt);
    }

    @Override
    protected DiscreteCoordinates getTargetPos() { return null; }

    @Override
    protected void onScareChange() {
        super.onScareChange();
    }

    @Override
    protected int getSpeed() {
        return 0;
    }
}
