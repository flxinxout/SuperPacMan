package ch.epfl.cs107.play.game.superpacman.actor.ghost;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
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

        // Extracts the sprites of the ghost
        Sprite[][] sprites = RPGSprite.extractSprites ("superpacman/ghost.blinky", 2, 1.f, 1.f,
        this , 16, 16, new Orientation [] { Orientation.UP ,
                Orientation.RIGHT , Orientation.DOWN , Orientation.LEFT });

        // For each sprites, we set the depth of it
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
        int randomInt = RandomGenerator.getInstance().nextInt(4);
        return Orientation.fromInt(randomInt);
    }

    @Override
    protected DiscreteCoordinates getTargetPos() { return null; }

    @Override
    protected void onScareChange() {
        super.onScareChange();
    }
}
