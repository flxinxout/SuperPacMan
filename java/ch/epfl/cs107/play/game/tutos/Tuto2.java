package ch.epfl.cs107.play.game.tutos;

import ch.epfl.cs107.play.game.areagame.AreaGame;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.tutos.actor.GhostPlayer;
import ch.epfl.cs107.play.game.tutos.actor.SimpleGhost;
import ch.epfl.cs107.play.game.tutos.area.tuto2.Ferme;
import ch.epfl.cs107.play.game.tutos.area.tuto2.Village;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Keyboard;
import ch.epfl.cs107.play.window.Window;

public class Tuto2 extends AreaGame {
    private GhostPlayer player;

    private void createArea() {
        addArea(new Ferme());
        addArea(new Village());
    }

    private void switchArea() {
        if (getCurrentArea().getTitle().equals("zelda/Ferme")) {
            setCurrentArea("zelda/Village", true);
            player.exitArea();
            player.enterArea(getCurrentArea(), new DiscreteCoordinates(5, 15));
        }
        else if (getCurrentArea().getTitle().equals("zelda/Village")) {
            setCurrentArea("zelda/Ferme", true);
            player.exitArea();
            player.enterArea(getCurrentArea(), new DiscreteCoordinates(2, 10));
        }
        getCurrentArea().setViewCandidate(player);

        player.strengthen();
    }

    @Override
    public boolean begin(Window window, FileSystem fileSystem) {
        if (super.begin(window , fileSystem)) {
            createArea();
            setCurrentArea("zelda/Ferme", true);

            DiscreteCoordinates position;
            if (getCurrentArea().getTitle().equals("zelda/Ferme")) {
                position = new DiscreteCoordinates(2,10);
            } else {
                position = new DiscreteCoordinates(5,15);
            }
            player = new GhostPlayer(getCurrentArea(), Orientation.DOWN, position, "ghost.1");

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

        if (player.isWeak()) {
            switchArea();
        }
    }

    @Override
    public String getTitle() {
        return "Tuto2";
    }
}
