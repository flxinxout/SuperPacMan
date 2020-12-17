package ch.epfl.cs107.play.game.superpacman.actor.ghost;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.superpacman.actor.SuperPacmanPlayer;
import ch.epfl.cs107.play.game.superpacman.area.SuperPacmanArea;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

import java.util.Queue;

/**
 * SmartGhosts are ghosts which move following a shortest path to their target position
 */
public abstract class SmartGhost extends Ghost {

    // Attributes
    private DiscreteCoordinates targetPos;
    private SmartGhostHandler handler;

    /**
     * Default SmartGhost constructor
     *
     * @param area        (Area): owner area. Not null
     * @param orientation (Orientation): initial orientation of the entity. Not null
     * @param home        (Coordinate): initial and home position of the ghost. Not null
     */
    public SmartGhost(Area area, Orientation orientation, DiscreteCoordinates home) {
        super(area, orientation, home);

        targetPos = getTargetPos();
        handler = new SmartGhostHandler();
    }

    /**
     * Getter for the target position. NEED TO BE OVERRIDDEN
     * @return (DiscreteCoordinates)
     */
    protected abstract DiscreteCoordinates getTargetPos();

    /* --------------- Extends Ghost --------------- */

    @Override
    public Orientation getNextOrientation() {

        // Gets the area where is the ghost and the path between the ghost and the target position
        SuperPacmanArea area = SuperPacmanArea.toSuperPacmanArea(getOwnerArea());
        Queue<Orientation> path = area.shortestPath(getCurrentMainCellCoordinates(), getTargetPos());

        // While the path is null or empty, generate another path
        while (path == null || path.isEmpty()) {
            path = area.shortestPath(getCurrentMainCellCoordinates(), randomCell());
        }

        return path.poll();
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        // If the ghost and his target position are very close, we update the target
        if (targetPos != null && DiscreteCoordinates.distanceBetween(getCurrentMainCellCoordinates(), targetPos) < 0.1) {
            targetPos = getTargetPos();
        }
    }

    @Override
    public void onDeath() {
        super.onDeath();
        targetPos = getTargetPos();
    }

    @Override
    protected void onScareChange() {
        targetPos = getTargetPos();
    }

    @Override
    public void interactWith(Interactable other) {
        other.acceptInteraction(handler);
    }

    /**
     * Interaction handler for a SmartGhost
     */
    private class SmartGhostHandler extends GhostHandler {
        @Override
        public void interactWith(SuperPacmanPlayer pacman) {
            super.interactWith(pacman);
            targetPos = getTargetPos();
        }
    }
}
