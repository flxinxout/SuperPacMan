package ch.epfl.cs107.play.game.tutos.area.tuto1;

import ch.epfl.cs107.play.game.actor.Actor;
import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.game.areagame.actor.Foreground;
import ch.epfl.cs107.play.game.tutos.actor.SimpleGhost;
import ch.epfl.cs107.play.game.tutos.area.SimpleArea;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Window;

public class Village extends SimpleArea {

    @Override
    protected void createArea() {
        Actor simpleGhost = new SimpleGhost(new Vector(18, 7), "ghost.2");
        registerActor(simpleGhost);

        Actor background = new Background(this);
        registerActor(background);

        Actor foreGround = new Foreground(this);
        registerActor(foreGround);

    }

    @Override
    public String getTitle() {
        return "zelda/Village";
    }
}
