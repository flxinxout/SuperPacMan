package ch.epfl.cs107.play.game.superpacman;

import ch.epfl.cs107.play.game.rpg.RPG;
import ch.epfl.cs107.play.game.superpacman.actor.SuperPacmanPlayer;
import ch.epfl.cs107.play.game.superpacman.area.*;
import ch.epfl.cs107.play.game.superpacman.area.level.*;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.window.Window;

/**
 * Main class of the SuperPacman game
 */
public class SuperPacman extends RPG  {

    // Array with all titles of the different areas
    private final String[] areas = {"superpacman/Level0", "superpacman/Level1", "superpacman/Level2", "superpacman/BonusLevel", "superpacman/BossLevel"};

    private final static int DEFAULT_ANIMATION_DURATION = 4;

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

    /**
     * Create the different levels of the game
     */
    private void createAreas(){
        addArea(new Level0());
        addArea(new Level1());
        addArea(new Level2());
        addArea(new BonusLevel());
        addArea(new BossLevel());
    }

    /**
     * Initialise the game 
     */
    private void startGame() {
        int areaIndex = 4;

        createAreas();
        SuperPacmanArea area = (SuperPacmanArea) setCurrentArea(areas[areaIndex], true);
        initPlayer(new SuperPacmanPlayer(area, area.getSpawnLocation()));
    }

    /**
     * Getter for the default animation duration
     * @return (int)
     */
    public static int getDefaultAnimationDuration() {
        return DEFAULT_ANIMATION_DURATION;
    }
}
