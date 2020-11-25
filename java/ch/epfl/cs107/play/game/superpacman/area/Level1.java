package ch.epfl.cs107.play.game.superpacman.area;

import ch.epfl.cs107.play.math.DiscreteCoordinates;

public class Level1 extends SuperPacmanArea{
    //TODO: TRY TO PUT IT IN THE SUPER CLASS
    private final DiscreteCoordinates PLAYER_SPAWN_POSITION = new DiscreteCoordinates(15, 6);

    @Override
    protected void createArea() {
        super.createArea();
    }

    @Override
    public String getTitle() {
        return "superpacman/Level0";
    }
}
