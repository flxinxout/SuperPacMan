package ch.epfl.cs107.play.game.tutos;

import ch.epfl.cs107.play.game.areagame.Cell;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;

public class Tuto2Cell extends Cell {

    private Tuto2CellType tuto2CellType;

    Tuto2Cell(int x, int y, Tuto2CellType tuto2CellType) {
        super(x, y);
        this.tuto2CellType = tuto2CellType;
    }

    @Override
    public boolean isCellInteractable() {
        return true;
    }

    @Override
    public boolean isViewInteractable() {
        return false;
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v) {

    }

    @Override
    protected boolean canLeave(Interactable entity) {
        return true; // false avant
    }

    @Override
    protected boolean canEnter(Interactable entity) {
        return tuto2CellType.isWalkable;
    }
}
