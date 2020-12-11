package ch.epfl.cs107.play.game.superpacman.area;

import ch.epfl.cs107.play.game.actor.Graphics;
import ch.epfl.cs107.play.game.actor.ImageGraphics;
import ch.epfl.cs107.play.game.actor.TextGraphics;
import ch.epfl.cs107.play.game.areagame.io.ResourcePath;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

import java.awt.*;

/**
 * Represent the status Graphic Interface of the different GUIs of the game
 */
public class SuperPacmanMenu implements Graphics {

    // Attributes of the GUIs
    private String imageName;
    private SuperPacmanMenuType type;

    /**
     * Default constructor of the GUI
     *
     * @param imageName (String): the image name for the resource path. Not null
     * @param type      (SuperPacmanMenuType): the type of GUI. Not null
     */
    public SuperPacmanMenu(String imageName, SuperPacmanMenuType type) {
        this.imageName = imageName;
        this.type = type;
    }

    /* -------------- Implements Graphics --------------- */

    @Override
    public void draw(Canvas canvas) {
        // Sets the referential
        Vector anchor = canvas.getTransform().getOrigin();

        float imageSizeX = SuperPacmanArea.CAMERA_SCALE_FACTOR;

        // Create the imageGraphic with the proportions of the camera scale factor
        ImageGraphics image = new ImageGraphics(ResourcePath.getSprite(imageName),
                imageSizeX, imageSizeX * 0.75f, new RegionOfInterest(0, 0, 800, 600),
                anchor.add(new Vector(-imageSizeX/2, -imageSizeX * 0.75f/2)));
        image.setDepth(999);

        image.draw(canvas);
    }

    //TODO: check if it stays an inner enum or not
    /**
     * Enum that represent all possible types of menus in the game
     */
    public enum SuperPacmanMenuType {
        PAUSE,
        GAMEOVER,
        WIN;
    }
}