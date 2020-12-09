package ch.epfl.cs107.play.game.superpacman;

import ch.epfl.cs107.play.game.actor.Graphics;
import ch.epfl.cs107.play.game.actor.ImageGraphics;
import ch.epfl.cs107.play.game.areagame.io.ResourcePath;
import ch.epfl.cs107.play.game.superpacman.area.SuperPacmanArea;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

public class Menu implements Graphics {
    private String imageName;

    public Menu(String imageName) {
        this.imageName = imageName;
    }

    @Override
    public void draw(Canvas canvas) {
        // Sets the referential
        Vector anchor = canvas.getTransform().getOrigin();

        float imageSizeX = SuperPacmanArea.CAMERA_SCALE_FACTOR;

        ImageGraphics image = new ImageGraphics(ResourcePath.getSprite(imageName),
                imageSizeX, imageSizeX * 0.75f, new RegionOfInterest(0, 0, 800, 600),
                anchor.add(new Vector(-imageSizeX/2, -imageSizeX * 0.75f/2)));
        image.setDepth(999);

        image.draw(canvas);
    }
}
