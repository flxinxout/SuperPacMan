package ch.epfl.cs107.play.game.superpacman;

import ch.epfl.cs107.play.game.actor.Graphics;
import ch.epfl.cs107.play.game.actor.ImageGraphics;
import ch.epfl.cs107.play.game.actor.TextGraphics;
import ch.epfl.cs107.play.game.areagame.io.ResourcePath;
import ch.epfl.cs107.play.game.superpacman.actor.SuperPacmanPlayer;
import ch.epfl.cs107.play.game.superpacman.area.SuperPacmanArea;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

import java.awt.*;

/**
 * Represent the status Graphic Interface of the different GUIs of the game
 */
public class Menu implements Graphics {

    // Attributes of the GUIs
    private SuperPacmanPlayer player;
    private String imageName;
    private int type;

    /**
     * Default constructor of the GUI
     * @param imageName the image name for the resource path
     * @param type the type of GUI (represented by an int)
     * @param player the player associated
     */
    public Menu(String imageName, int type, SuperPacmanPlayer player) {
        this.imageName = imageName;
        this.type = type;
        this.player = player;
    }

    @Override
    public void draw(Canvas canvas) {

        // Sets the referential
        Vector anchor = canvas.getTransform().getOrigin();

        // Get the camera scale factor of the game
        float imageSizeX = SuperPacmanArea.CAMERA_SCALE_FACTOR;

        // Create the imageGraphic with the proportions of the camera scale factor
        ImageGraphics image = new ImageGraphics(ResourcePath.getSprite(imageName),
                imageSizeX, imageSizeX * 0.75f, new RegionOfInterest(0, 0, 800, 600),
                anchor.add(new Vector(-imageSizeX/2, -imageSizeX * 0.75f/2)));
        image.setDepth(999);

        image.draw(canvas);

        TextGraphics score;

        switch (type) {
            case 0:
                Vector scoreAnchor = anchor.add(new Vector(0, 50f));
                /*if(player.getScore() == null) {
                    System.out.println("yes");
                }*/
               // System.out.println("Score " + player.getScore());
                //score = new TextGraphics("Score " + player.getScore(), 2f, Color.YELLOW, Color.BLACK, .6f, false, false, scoreAnchor);
                //score.draw(canvas);
                break;

            default:
                break;
        }
    }
}
