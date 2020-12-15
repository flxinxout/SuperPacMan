package ch.epfl.cs107.play.game.superpacman.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.*;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.game.superpacman.actor.collectable.BossLife;
import ch.epfl.cs107.play.game.superpacman.actor.ghost.Ghost;
import ch.epfl.cs107.play.game.superpacman.area.SuperPacmanArea;
import ch.epfl.cs107.play.game.superpacman.handler.SuperPacmanInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;

public class Boss extends Ghost implements Interactor {

    private final int FIELD_OF_VIEW = 10;
    private DiscreteCoordinates lastCell;

    // Represents the number of life of the boss. When it's 0, the SuperPacmanPlayer win
    private final int START_LIFE = 4;
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
        super(area, orientation, position, 8, 10);

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

    public void loseHP() {
        if(hp > 0) {
            hp--;
            setSpeed(1.5 * getSpeed());
        }

        if (hp <= 0) {
            SuperPacmanArea owner = (SuperPacmanArea) getOwnerArea();
            owner.win();
        }
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        setAnimations(deltaTime);

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

    /* --------------- protected Methods -------------- */

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
        Sprite[][] sprites = RPGSprite.extractSprites ("zelda/flameskull", 3, 2f, 2f,
                this, 32, 32, new Vector(-0.5f, -0.5f), new Orientation [] { Orientation.UP ,
                        Orientation.LEFT , Orientation.DOWN , Orientation.RIGHT });
        for (Sprite[] sprite : sprites) {
            for (Sprite value : sprite) {
                value.setDepth(950);
            }
        }

        return Animation.createAnimations (getAnimationDuration() /2, sprites);
    }

    /** @return (DiscreteCoordinates): the target's position */
    protected DiscreteCoordinates getTargetPos() {
        if(getPlayer() == null ||
                DiscreteCoordinates.distanceBetween(getPlayer().getCurrentCells().get(0), getCurrentMainCellCoordinates()) > FIELD_OF_VIEW) {
            return randomCell();
        } else {
            return getPlayer().getCurrentCells().get((0));
        }
    }


    /* --------------- External Methods -------------- */

    //TODO: set differents animations
    /**
     * Set the correct animation for the boss
     * @param deltaTime the deltaTime of the update
     */
    private void setAnimations(float deltaTime) {
        switch (hp) {

            case 4:
                System.out.println("4");
                break;

            case 3:
                System.out.println("3");
                break;

            case 2:
                System.out.println("2");
                break;

            case 1:
                System.out.println("1");
                break;

            default:
                break;
        }

        currentAnimation.update(deltaTime);

    }

    /* -------------- Implements Interactable ---------------- */

    @Override
    public void acceptInteraction(AreaInteractionVisitor v) {
        ((SuperPacmanInteractionVisitor)v).interactWith (this);
    }

    /* -------------- Getters ---------------- */

    public int getHp() {
        return hp;
    }
}
