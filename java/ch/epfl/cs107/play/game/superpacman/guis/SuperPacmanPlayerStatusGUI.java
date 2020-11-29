package ch.epfl.cs107.play.game.superpacman.guis;

import ch.epfl.cs107.play.game.actor.Graphics;
import ch.epfl.cs107.play.game.actor.ImageGraphics;
import ch.epfl.cs107.play.game.actor.TextGraphics;
import ch.epfl.cs107.play.game.areagame.io.ResourcePath;
import ch.epfl.cs107.play.game.superpacman.actor.SuperPacmanPlayer;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

import java.awt.*;

public class SuperPacmanPlayerStatusGUI implements Graphics {

    private SuperPacmanPlayer player;

    public SuperPacmanPlayerStatusGUI(SuperPacmanPlayer player) {
        this.player = player;
    }

    // Je suis sur de rien du tout mdr ils disent je pense de faire ça x)
    @Override
    public void draw(Canvas canvas) {
        //Set the referential
        float width = canvas.getScaledWidth();
        float height = canvas.getScaledHeight();
        Vector anchor = canvas.getTransform().getOrigin().sub(new Vector(width/2, height/2));

        // ---------------------
        // Draw the life
        ImageGraphics lastGraphic = drawLife(anchor, width, height, canvas);

        // ---------------------
        // Draw the score
        drawScore(canvas, anchor, height, width, lastGraphic);
    }

    private ImageGraphics drawLife(Vector anchor, float width, float height, Canvas canvas) {
        //Décalage
        float n = 1;
        // Graphic
        ImageGraphics life;

        for(int i = 1; i <= SuperPacmanPlayer.MAXHP; i++) {
            if(i <= player.getHp()) {
                life = new ImageGraphics(ResourcePath.getSprite("superpacman/lifeDisplay"),
                        1.f, 1.f, new RegionOfInterest(0, 0, 64, 64),
                        anchor.add(new Vector(n + i, height - 1.375f)), 1, width);
            } else {
                life = new ImageGraphics(ResourcePath.getSprite("superpacman/lifeDisplay"),
                        1.f, 1.f, new RegionOfInterest(64, 0, 64, 64),
                        anchor.add(new Vector(n + i, height - 1.375f)), 1, width);
            }

            life.draw(canvas);

            // A voir pour essayé d'en faire l'élément parent du score
            if(i == SuperPacmanPlayer.MAXHP) {
                return life;
            }
        }
        return null;
    }

    private void drawScore(Canvas canvas, Vector anchor, float height, float width, ImageGraphics lastGraphic) {
        int n = 1;

        // Le ImageGraphic je le passe car ça serait bien le le setParent() avec lui, comme ça
        // y'a pas besoin de faire n + 7, mais juste la position par rapport au dernier point de vie affiché en haut

        TextGraphics score = new TextGraphics("Score: " + player.getScore(), .7f, Color.YELLOW, Color.BLUE, .1f, false, false,
                anchor.add(new Vector(n + 7, height - 1.1f)));
        score.draw(canvas);
    }
}
