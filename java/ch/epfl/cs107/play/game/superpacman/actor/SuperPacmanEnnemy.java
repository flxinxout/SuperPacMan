package ch.epfl.cs107.play.game.superpacman.actor;

import ch.epfl.cs107.play.game.actor.SoundAcoustics;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.Interactor;
import ch.epfl.cs107.play.game.areagame.actor.MovableAreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.superpacman.actor.Killable;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RandomGenerator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class SuperPacmanEnnemy extends MovableAreaEntity implements Interactor {

    // Attributes of an ennemy
    private final int fieldOfView;
    private int speed;

    /**
     * Default SuperPacmanEnnemy constructor
     *
     * @param area              (Area): owner area. Not null
     * @param orientation       (Orientation): initial orientation of the entity. Not null
     * @param position          (Coordinate): initial position of the entity. Not null
     * @param speed             (int): the speed of the entity. Strictly greater than 0
     * @param fieldOfView       (int): the field of view of the entity. Strictly greater than 0
     */
    public SuperPacmanEnnemy(Area area, Orientation orientation, DiscreteCoordinates position, int speed, int fieldOfView) {
        super(area, orientation, position);

        this.speed = speed > 0 ? speed : 1;
        this.fieldOfView = fieldOfView > 0 ? fieldOfView : 1;
    }

    /* ------------- Protected Methods --------------- */

    /**
     * Choose a random cell in a specific radius around another cell
     * @param centerPos (DiscreteCoordinates): the center cell
     * @param radius    (int): the radius allowed
     * @return (DiscreteCoordinates): the cell
     */
    protected DiscreteCoordinates randomCell(DiscreteCoordinates centerPos, int radius) {
        int randomX, randomY;
        DiscreteCoordinates randomCoordinates;

        // Generate a random coordinate in the current area until this coordinate will be smaller that the allowed radius of the ghost
        do {
            randomX = RandomGenerator.getInstance().nextInt(getOwnerArea().getWidth());
            randomY = RandomGenerator.getInstance().nextInt(getOwnerArea().getHeight());
            randomCoordinates = new DiscreteCoordinates(randomX, randomY);
        } while (DiscreteCoordinates.distanceBetween(centerPos, randomCoordinates) > radius);

        return randomCoordinates;
    }

    /** @return (DiscreteCoordinates): a random cell in an entire map */
    protected DiscreteCoordinates randomCell() {
        int width = getOwnerArea().getWidth();
        int height = getOwnerArea().getHeight();

        DiscreteCoordinates center = new DiscreteCoordinates(width/2, height/2);
        int radius = (int) (Math.sqrt(width * width + height * height)/2 + 1);

        return randomCell(center, radius);
    }

    /** @NEED TO BE OVERRIDDEN
     * @return (Orientation): the next orientation following a specific algorithm
     */
    protected abstract Orientation getNextOrientation();

    /* ------------- Implements Interactor --------------- */

    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() {
        List<DiscreteCoordinates> fieldOfViewList = new ArrayList<>();

        // Add the coordinates that are in the field of view of the ghost
        for (int y = -fieldOfView; y <= fieldOfView; y++) {
            for (int x = -fieldOfView; x <= fieldOfView; x++) {
                fieldOfViewList.add(new DiscreteCoordinates(getCurrentMainCellCoordinates().x + x, getCurrentMainCellCoordinates().y + y));
            }
        }

        return fieldOfViewList;
    }

    public List<DiscreteCoordinates> getCurrentCells() { return Collections.singletonList(getCurrentMainCellCoordinates()); }

    /* --------------- Implements Interactable --------------- */

    @Override
    public boolean takeCellSpace() { return false; }

    /* --------------- Setters --------------- */

    /** Sets the speed */
    protected void setSpeed(int speed) { this.speed = speed; }

    /* --------------- Getters --------------- */

    /** @return ennemy's speed */
    protected int getSpeed() { return speed; }

}
