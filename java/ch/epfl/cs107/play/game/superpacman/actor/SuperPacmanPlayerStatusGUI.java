package ch.epfl.cs107.play.game.superpacman.actor;

import ch.epfl.cs107.play.game.actor.Graphics;
import ch.epfl.cs107.play.game.actor.ImageGraphics;
import ch.epfl.cs107.play.game.actor.TextGraphics;
import ch.epfl.cs107.play.game.areagame.io.ResourcePath;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

import java.awt.Color;

/**
 * Represent the status Graphic User Interface of a SuperPacmanPlayer
 */
public class SuperPacmanPlayerStatusGUI implements Graphics {

    // The player assigned
    private SuperPacmanPlayer player;

    /**
     * Default constructor
     * @param player the SuperPacmanPlayer
     */
    protected SuperPacmanPlayerStatusGUI(SuperPacmanPlayer player) {
        this.player = player;
    }


    /* --------------- Implements Graphics --------------- */

    @Override
    public void draw(Canvas canvas) {

        // Sets the referential
        float width = canvas.getScaledWidth();
        float height = canvas.getScaledHeight();
        Vector anchor = canvas.getTransform().getOrigin().sub(new Vector(width/2, height/2));

        // Draw the life
        drawLife(anchor, height, canvas);

        // Draw the score
        drawScore(canvas, anchor, height);
    }

    /**
     * Method that create an ImageGraphics of the life of the SuperPacman
     * @param spaceBetweenImages the space between the images
     * @param anchor the vector
     * @param height the height of the window
     * @param canvas the canvas
     */
    private void drawLife(float spaceBetweenImages, Vector anchor, float height, Canvas canvas) {

        // Iterate through the life of the SuperPacman and set yellow and gray hp
        for(int i = 1; i <= player.getMAXHP(); i++) {

            // 0 if the life icon is yellow, 64 if it is gray
            int m;
            if(i <= player.getHp()) {
                m = 0;
            } else {
                m = 64;
            }

            // Creation of the imageGraphic
            ImageGraphics life = new ImageGraphics(ResourcePath.getSprite("superpacman/lifeDisplay"),
                    1.f, 1.f, new RegionOfInterest(m, 0, 64, 64),
                    anchor.add(new Vector(spaceBetweenImages * (i-1), height - 1.375f)), 1, 1);
            life.draw(canvas);
        }
    }

    /**
     * Default method that create the ImageGraphics of the life of the SuperPacman with 1 in space
     * @param anchor the vector
     * @param height the height of the window
     * @param canvas the canvas
     */
    private void drawLife(Vector anchor, float height, Canvas canvas) {
        drawLife(1, anchor, height, canvas);
    }

    /**
     * Default method that create the ImageGraphics of the score of the SuperPacman
     * @param anchor the vector
     * @param height the height of the window
     * @param canvas the canvas
     */
    private void drawScore(Canvas canvas, Vector anchor, float height) {
        Vector scoreAnchor = anchor.add(new Vector(player.getMAXHP() + 1, height - 1.2f));

        // Creation of the TextGraphics
        TextGraphics score = new TextGraphics("Score: " + player.getScore(), 1f, Color.YELLOW, Color.BLUE, .06f, true, false,
                scoreAnchor);
        score.draw(canvas);
    }
}
