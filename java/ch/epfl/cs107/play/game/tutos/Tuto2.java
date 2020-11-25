package ch.epfl.cs107.play.game.tutos;

import ch.epfl.cs107.play.game.actor.Actor;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.AreaGame;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.tutos.actor.GhostPlayer;
import ch.epfl.cs107.play.game.tutos.actor.SimpleGhost;
import ch.epfl.cs107.play.game.tutos.area.tuto2.Ferme;
import ch.epfl.cs107.play.game.tutos.area.tuto2.Village;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Window;

public class Tuto2 extends AreaGame {

    private GhostPlayer player;

    private void createAreas() {
        addArea(new Ferme());
        addArea(new Village());
    }

    @Override
    public String getTitle() {
        return "Tuto2";
    }

    @Override
    public boolean begin(Window window, FileSystem fileSystem) {
        if (super.begin(window , fileSystem)) {
            // traitement spécifiques à Tuto1

            createAreas();
            Area area = setCurrentArea("zelda/Ferme", true);
            DiscreteCoordinates discreteCoordinates = new DiscreteCoordinates(2, 10);
            player = new GhostPlayer(area, Orientation.DOWN, discreteCoordinates, "ghost.1");

            player.enterArea(area, discreteCoordinates);

            return true;
        } else {
            return false;
        }
    }

    @Override
    public void update(float deltaTime) {
       super.update(deltaTime);
        if(player.isWeak()) {
            switchArea();
        }

    }

    public void switchArea() {

        player.leaveArea();

        if(getCurrentArea().getTitle().equals("zelda/Village")) {
            setCurrentArea("zelda/Ferme", true);
            player.enterArea(getCurrentArea(), new DiscreteCoordinates(2, 10));
        } else if(getCurrentArea().getTitle().equals("zelda/Ferme")) {
            setCurrentArea("zelda/Village", true);
            player.enterArea(getCurrentArea(), new DiscreteCoordinates(15, 5));
        }

        player.strengthen();
    }

    @Override
    public void end() {

    }
}
