package ch.epfl.cs107.play.game.superpacman.area;

import ch.epfl.cs107.play.math.DiscreteCoordinates;

public class Level2 extends SuperPacmanArea{
    //TODO: TRY TO PUT IT IN THE SUPER CLASS
    private final DiscreteCoordinates PLAYER_SPAWN_POSITION = new DiscreteCoordinates(15, 29);

    @Override
    protected void createArea() {
        super.createArea();
    }

    @Override
    public String getTitle() {
        return "superpacman/Level2";
    }
}
