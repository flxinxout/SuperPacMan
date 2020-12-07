package ch.epfl.cs107.play.game.superpacman;

import ch.epfl.cs107.play.game.areagame.actor.Sound;
import ch.epfl.cs107.play.game.rpg.RPG;
import ch.epfl.cs107.play.game.superpacman.actor.SuperPacmanPlayer;
import ch.epfl.cs107.play.game.superpacman.area.Level0;
import ch.epfl.cs107.play.game.superpacman.area.Level1;
import ch.epfl.cs107.play.game.superpacman.area.Level2;
import ch.epfl.cs107.play.game.superpacman.area.SuperPacmanArea;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.window.Window;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

/**
 * Main class of the game
 */
public class SuperPacman extends RPG implements Sound {

    public final static float CAMERA_SCALE_FACTOR = 15.f;
    //TODO: Let it static? Make a singleton from it?
    public static SuperPacmanPlayer player;
    //TODO: maybe a map between areas and spawn coordinates
    private final String[] areas = {"superpacman/Level0", "superpacman/Level1", "superpacman/Level2"};

    //TODO: PAUSE/END/RUNNING STATES
    private State state;

    /* ----------- Implements Sounds ------------- */

    @Override
    public void onSound() {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("res/sounds/pacman/pacman_beginning.wav").getAbsoluteFile());

            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.loop(0);

        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    /* ----------- External Playable ------------- */

    @Override
    public boolean begin(Window window, FileSystem fileSystem) {
        if (super.begin(window, fileSystem)) {
            state = State.RUNNING;
            onSound();
            createAreas();
            //TODO: STRANGE WAY CAST
            SuperPacmanArea area = (SuperPacmanArea) setCurrentArea(areas[0], true);

            player = new SuperPacmanPlayer(area, area.getSpawnLocation());
            initPlayer(player);

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

    /* ----------- External Methods ------------- */

    private void createAreas(){
        addArea(new Level0());
        addArea(new Level1());
        addArea(new Level2());
    }

    public enum State {
        RUNNING,
        PAUSE
    }


}
