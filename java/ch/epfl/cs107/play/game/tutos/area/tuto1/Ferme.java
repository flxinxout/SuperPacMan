package ch.epfl.cs107.play.game.tutos.area.tuto1;

import ch.epfl.cs107.play.game.actor.Actor;
import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.game.areagame.actor.Foreground;
import ch.epfl.cs107.play.game.tutos.actor.SimpleGhost;
import ch.epfl.cs107.play.game.tutos.area.SimpleArea;
import ch.epfl.cs107.play.math.Vector;

public class Ferme extends SimpleArea {

    @Override
    protected void createArea() {
        Actor background = new Background(this);
        registerActor(background);

        Actor foreGround = new Foreground(this);
        registerActor(foreGround);

    }

    @Override
    public String getTitle() {
        return "zelda/Ferme";
    }
}
