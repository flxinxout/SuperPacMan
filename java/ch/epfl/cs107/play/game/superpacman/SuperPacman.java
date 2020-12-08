package ch.epfl.cs107.play.game.superpacman;

import ch.epfl.cs107.play.game.areagame.actor.Foreground;
import ch.epfl.cs107.play.game.areagame.actor.Sound;
import ch.epfl.cs107.play.game.rpg.RPG;
import ch.epfl.cs107.play.game.superpacman.actor.SuperPacmanPlayer;
import ch.epfl.cs107.play.game.superpacman.area.Level0;
import ch.epfl.cs107.play.game.superpacman.area.Level1;
import ch.epfl.cs107.play.game.superpacman.area.Level2;
import ch.epfl.cs107.play.game.superpacman.area.SuperPacmanArea;
import ch.epfl.cs107.play.game.superpacman.area.util.State;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.window.Window;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Main class of the game
 */
public class SuperPacman extends RPG implements Sound {
    public static SuperPacmanPlayer player;
    private List<String> areas;

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

    /* ----------- External Methods ------------- */

    /**
     * Creation of the different levels in the game
     */
    private void createAreas(){
        Level0 level0 = new Level0();
        addArea(level0);
        areas.add(level0.getTitle());

        Level1 Level1 = new Level1();
        addArea(Level1);
        areas.add(Level1.getTitle());

        Level2 Level2 = new Level2();
        addArea(Level2);
        areas.add(Level2.getTitle());
    }

    /**
     * Initialization of the game
     */
    private void startGame() {
        areas = new ArrayList<>();
        onSound();

        createAreas();
        SuperPacmanArea area = (SuperPacmanArea) setCurrentArea(areas.get(2), true);

        player = new SuperPacmanPlayer(area, area.getSpawnLocation());
        initPlayer(player);
    }
}
