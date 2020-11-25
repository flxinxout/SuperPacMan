package ch.epfl.cs107.play.game.superpacman;

import ch.epfl.cs107.play.game.rpg.RPG;
import ch.epfl.cs107.play.game.rpg.actor.Player;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.window.Window;

public class SuperPacman extends RPG {

    @Override
    public boolean begin(Window window, FileSystem fileSystem) {
        return super.begin(window, fileSystem);
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
    protected void initPlayer(Player player) {
        //super.initPlayer(player);
    }

    @Override
    public String getTitle() {
        return "Super Pac-Man";
    }
}
