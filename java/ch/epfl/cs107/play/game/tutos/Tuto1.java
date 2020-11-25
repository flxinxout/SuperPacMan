package ch.epfl.cs107.play.game.tutos;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.AreaGame;
import ch.epfl.cs107.play.game.tutos.actor.SimpleGhost;
import ch.epfl.cs107.play.game.tutos.area.tuto1.Ferme;
import ch.epfl.cs107.play.game.tutos.area.tuto1.Village;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Keyboard;
import ch.epfl.cs107.play.window.Window;

public class Tuto1 extends AreaGame {
    private SimpleGhost player;

    private void createArea() {
        addArea(new Ferme());
        addArea(new Village());
    }

    private void switchArea() {
        getCurrentArea().unregisterActor(player);

        if (getCurrentArea().getTitle().equals("zelda/Ferme")) {
            setCurrentArea("zelda/Village", true);
        }
        else if (getCurrentArea().getTitle().equals("zelda/Village")) {
            setCurrentArea("zelda/Ferme", true);
        }
        getCurrentArea().registerActor(player);
        getCurrentArea().setViewCandidate(player);

        player.strengthen();
    }

    @Override
    public boolean begin(Window window, FileSystem fileSystem) {
        if (super.begin(window , fileSystem)) {
            player = new SimpleGhost(new Vector(18, 7), "ghost.1");

            createArea();
            setCurrentArea("zelda/Ferme", true);

            getCurrentArea().registerActor(player);
            getCurrentArea().setViewCandidate(player);
            return true;
        }
        else return false;
    }

    @Override
    public void end() {
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        Keyboard keyboard = getWindow().getKeyboard() ;
        if(keyboard.get(Keyboard.UP).isDown()) {
            player.moveUp(0.05f);
        }
        else if (keyboard.get(Keyboard.DOWN).isDown()) {
            player.moveDown(0.05f);
        }
        else if (keyboard.get(Keyboard.RIGHT).isDown()) {
            player.moveRight(0.05f);
        }
        else if (keyboard.get(Keyboard.LEFT).isDown()) {
            player.moveLeft(0.05f);
        }

        if (player.isWeak()) {
            switchArea();
        }
    }

    @Override
    public String getTitle() {
        return "Tuto1";
    }
}
