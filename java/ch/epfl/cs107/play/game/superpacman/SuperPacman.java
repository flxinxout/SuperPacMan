package ch.epfl.cs107.play.game.superpacman;

import ch.epfl.cs107.play.game.rpg.RPG;
import ch.epfl.cs107.play.game.superpacman.actor.SuperPacmanPlayer;
import ch.epfl.cs107.play.game.superpacman.area.Level0;
import ch.epfl.cs107.play.game.superpacman.area.Level1;
import ch.epfl.cs107.play.game.superpacman.area.Level2;
import ch.epfl.cs107.play.game.superpacman.area.SuperPacmanArea;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.window.Window;

/**
 * Main class of the game
 */
public class SuperPacman extends RPG  {
    private final String[] areas = {"superpacman/Level0", "superpacman/Level1", "superpacman/Level2"};

    /* ----------- External Playable ------------- */

    @Override
    public boolean begin(Window window, FileSystem fileSystem) {
        if (super.begin(window, fileSystem)) {
            startGame();
            return true;
        }
        return false;
    }

    @Override
    public void end() {
        super.end();
    }

    @Override
    public String getTitle() {
        return "Super Pac-Man";
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
    }

    /* ----------- Internal Methods ------------- */

    /**
     * Creation of the different levels in the game
     */
    private void createAreas(){
        addArea(new Level0());
        addArea(new Level1());
        addArea(new Level2());
    }

    /**
     * Initialization of the game
     */
    private void startGame() {
        createAreas();
        int areaIndex = 2;
        SuperPacmanArea area = (SuperPacmanArea) setCurrentArea(areas[areaIndex], true);

        initPlayer(new SuperPacmanPlayer(area, area.getSpawnLocation()));

        SuperPacmanPlayer pacmanPlayer = (SuperPacmanPlayer) getPlayer();
        area.setPlayer(pacmanPlayer);
    }
}
