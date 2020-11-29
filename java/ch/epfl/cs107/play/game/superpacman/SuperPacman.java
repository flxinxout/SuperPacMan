package ch.epfl.cs107.play.game.superpacman;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.rpg.RPG;
import ch.epfl.cs107.play.game.superpacman.actor.SuperPacmanPlayer;
import ch.epfl.cs107.play.game.superpacman.area.Level0;
import ch.epfl.cs107.play.game.superpacman.area.Level1;
import ch.epfl.cs107.play.game.superpacman.area.Level2;
import ch.epfl.cs107.play.game.superpacman.area.SuperPacmanArea;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Window;

import java.util.logging.Level;

public class SuperPacman extends RPG {
    public final static float CAMERA_SCALE_FACTOR = 15.f;

    //TODO: maybe a map between areas and spawn coordinates
    private final String[] areas = {"superpacman/Level0", "superpacman/Level1", "superpacman/Level2"};
    private final DiscreteCoordinates[] startingPositions = {new DiscreteCoordinates(10,1),
            new DiscreteCoordinates(15,6), new DiscreteCoordinates(15,29)};

    private void createAreas(){
        addArea(new Level0());
        addArea(new Level1());
        addArea(new Level2());
    }

    @Override
    public boolean begin(Window window, FileSystem fileSystem) {
        if (super.begin(window, fileSystem)) {

            createAreas();
            Area area = setCurrentArea(areas[0], true);
            SuperPacmanPlayer player = new SuperPacmanPlayer(area, startingPositions[0]);
            initPlayer(player);
            return true;
        }
        return false;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
    }

    @Override
    public void end() {
        super.end();
    }

    @Override
    public String getTitle() {
        return "Super Pac-Man";
    }
}