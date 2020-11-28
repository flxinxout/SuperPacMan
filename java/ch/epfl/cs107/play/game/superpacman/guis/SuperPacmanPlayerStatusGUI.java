package ch.epfl.cs107.play.game.superpacman.guis;

import ch.epfl.cs107.play.game.actor.Graphics;
import ch.epfl.cs107.play.game.actor.ImageGraphics;
import ch.epfl.cs107.play.game.actor.TextGraphics;
import ch.epfl.cs107.play.game.areagame.io.ResourcePath;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

import java.awt.*;

public class SuperPacmanPlayerStatusGUI implements Graphics {


    // Je suis sur de rien du tout mdr ils disent je pense de faire ça x)
    @Override
    public void draw(Canvas canvas) {
        float width = canvas.getScaledWidth();
        float height = canvas.getScaledHeight();
        Vector anchor = canvas.getTransform().getOrigin().sub(new Vector(width/2, height/2));
        drawLife(anchor, width, height, canvas);
        //drawScore(canvas, anchor);
    }

    private void drawLife(Vector anchor, float width, float height, Canvas canvas) {

        // Le premier 0 du constructeur de RegionOfInterest était une variable 'm' au début
        // Le premier 1.4f du constructeur de Vector était une variable 'n' au début, mais ils disent pas quoi mettre...
        // Le witdh était de base un DEPTH mais jsais pas ce que c'était lol

        ImageGraphics life = new ImageGraphics(ResourcePath.getSprite("superpacman/lifeDisplay"),
                1.f, 1.f, new RegionOfInterest(0, 0, 64, 64),
                anchor.add(new Vector(1.4f, height - 1.375f)), 1, width);
        life.draw(canvas);

    }

    private void drawScore(Canvas canvas, Vector anchor) {
        TextGraphics score = new TextGraphics("Score ", .4f, Color.YELLOW, Color.BLACK, .2f, false, false, anchor);
        score.draw(canvas);
    }
}
