package ch.epfl.cs107.play.game.superpacman.actor.ennemy;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.*;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.game.superpacman.SuperPacman;
import ch.epfl.cs107.play.game.superpacman.actor.collectable.BossLife;
import ch.epfl.cs107.play.game.superpacman.actor.ghost.Ghost;
import ch.epfl.cs107.play.game.superpacman.area.SuperPacmanArea;
import ch.epfl.cs107.play.game.superpacman.handler.SuperPacmanInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Vector;

import java.util.Queue;

/**
 * [EXTENSION] End boss of the SuperPacman game
 */
public class Boss extends Ghost implements Interactor {

    // Constants
    private final int START_LIFE = 4;
    private final int FIELD_OF_VIEW = 12;
    private final int[] SPEEDS = {12, 10, 8, 6}; // Each entry of the array corresponds to a phase

    // Attributes
    private DiscreteCoordinates lastCell; // Corresponds to the last cell filled with fire
    private int hp;


    /**
     * Default Boss constructor
     *
     * @param area        (Area): owner area. Not null
     * @param orientation (Orientation): initial orientation of the entity in the Area. Not null
     * @param position    (DiscreteCoordinate): initial position of the entity in the Area. Not null
     * @param livesPosition (DiscreteCoordinate[]): positions of the boss' lives, at least of length START_LIFE. Not null
     */
    public Boss(Area area, Orientation orientation, DiscreteCoordinates position, DiscreteCoordinates[] livesPosition) {
        //TODO: PAS PROPRE PSK ON REFERENCE PAS LES CONSTANTES --> d'où getSpeed() abstract
        super(area, orientation, position, 12, 12);

        //Check if there's enough lives positions and register them.
        if(livesPosition.length >= START_LIFE) {
            for (int i = 0; i < START_LIFE; i++) {
                if (livesPosition[i] != null) {
                    BossLife life = new BossLife(getOwnerArea(), livesPosition[i], this);
                    getOwnerArea().registerActor(life);
                }
            }
        }

        lastCell = null;
        this.hp = START_LIFE;
    }

    /** Make the boss lose a hp and set its new speed */
    public void loseHP() {
        if(hp > 0) {
            hp--;

            //Set the speed of the new phase
            if (START_LIFE - hp < SPEEDS.length) {
                setSpeed(SPEEDS[START_LIFE - hp]);
            }
        }

        if (hp <= 0) {
            SuperPacmanArea owner = (SuperPacmanArea) getOwnerArea();
            owner.win();
        }
    }

    /* --------------- Extends Ghost -------------- */

    @Override
    protected Orientation getNextOrientation() {

        // Gets the area where is the ghost and the path between the ghost and the SuperPacmanPlayer
        SuperPacmanArea area = (SuperPacmanArea) getOwnerArea();
        Queue<Orientation> path = area.shortestPath(getCurrentMainCellCoordinates(), getTargetPos());

        // While the path is null or empty (for example if the ghost has not a target now), generate an other path
        while (path == null || path.isEmpty()) {
            DiscreteCoordinates cell = randomCell();
            path = area.shortestPath(getCurrentMainCellCoordinates(), cell);
        }

        return path.poll();
    }

    @Override
    protected Animation[] getAnimations() {
        // Extracts the sprites of the ghost and sets the animations
        Sprite[][] sprites = RPGSprite.extractSprites ("superpacman/flameskull", 3, 2f, 2f,
                this, 32, 32, new Vector(-0.5f, -0.5f), new Orientation [] { Orientation.UP ,
                        Orientation.LEFT , Orientation.DOWN , Orientation.RIGHT });
        for (Sprite[] sprite : sprites) {
            for (Sprite value : sprite) {
                value.setDepth(950);
            }
        }

        return Animation.createAnimations (SuperPacman.getDefaultAnimationDuration(), sprites);
    }

    @Override
    protected DiscreteCoordinates getTargetPos() {
        if(getPlayer() == null ||
                DiscreteCoordinates.distanceBetween(getPlayer().getCurrentCells().get(0), getCurrentMainCellCoordinates()) > FIELD_OF_VIEW) {
            return randomCell();
        } else {
            return getPlayer().getCurrentCells().get((0));
        }
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        if(getPlayer() != null) {

            // For each cell that the boss left, spawn fire in this cell
            for (DiscreteCoordinates cell : getLeftCells()) {
                if(cell != lastCell) {
                    lastCell = cell;
                    Fire fire = new Fire(getOwnerArea(), Orientation.UP, cell);
                    getOwnerArea().registerActor(fire);
                }
            }
        }
    }
    /* -------------- Implements Interactable ---------------- */

    @Override
    public void acceptInteraction(AreaInteractionVisitor v) {
        ((SuperPacmanInteractionVisitor)v).interactWith (this);
    }
}
