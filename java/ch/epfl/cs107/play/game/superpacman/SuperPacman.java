package ch.epfl.cs107.play.game.superpacman;

import ch.epfl.cs107.play.game.rpg.RPG;
import ch.epfl.cs107.play.game.superpacman.actor.SuperPacmanPlayer;
import ch.epfl.cs107.play.game.superpacman.area.*;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.window.Window;

/**
 * Main class of the Pacman game
 */
public class SuperPacman extends RPG  {

    // Array with all titles of the different areas
    private final String[] areas = {"superpacman/Level0", "superpacman/Level1", "superpacman/Level2"};


    /* ----------- Implements Playable ------------- */

    @Override
    public boolean begin(Window window, FileSystem fileSystem) {
        if (super.begin(window, fileSystem)) {

            // Set up the game
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


    /* ----------- Implements Graphics ------------- */

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
    }


    /* ----------- External Methods ------------- */

    /** Creation of the different levels in the game */
    private void createAreas(){
        addArea(new Level0());
        addArea(new Level1());
        addArea(new Level2());
        addArea(new BonusLevel());
    }

    /** Initialization of the game */
    private void startGame() {
        int areaIndex = 0;

        createAreas();
        SuperPacmanArea area = (SuperPacmanArea) setCurrentArea(areas[areaIndex], true);
        initPlayer(new SuperPacmanPlayer(area, area.getSpawnLocation()));
    }
}
