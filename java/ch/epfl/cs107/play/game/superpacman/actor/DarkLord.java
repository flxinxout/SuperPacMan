package ch.epfl.cs107.play.game.superpacman.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.*;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.game.superpacman.handler.SuperPacmanInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RandomGenerator;
import ch.epfl.cs107.play.window.Canvas;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DarkLord extends MovableAreaEntity implements Interactor {

    // Handler of the DarkLord
    private final DarkLordHandler handler;

    // Animation of the DarkLord
    private final int ANIMATION_DURATION = 8;
    private Animation[] animations;
    private Animation currentAnimation;

    private final int DEFAULT_SPEED = 10;
    private final int FIELD_OF_VIEW = 20;

    // Orientation of the DarkLord
    private Orientation desiredOrientation;

    /**
     * Default DarkLord constructor
     *
     * @param area        (Area): Owner area. Not null
     * @param orientation (Orientation): Initial orientation of the entity in the Area. Not null
     * @param position    (DiscreteCoordinate): Initial position of the entity in the Area. Not null
     */
    public DarkLord(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);

        // Extracts the sprites of the ghost and sets the animations of the ghost
        Sprite[][] sprites = RPGSprite.extractSprites ("zelda/darkLord", 1, 1f, 1f,
                this , 25, 31, new Orientation [] { Orientation.UP ,
                        Orientation.RIGHT , Orientation.DOWN , Orientation.LEFT });
        for (int i = 0; i < sprites.length; i++) {
            for (int j = 0; j < sprites[i].length; j++) {
                sprites[i][j].setDepth(950);
            }
        }

        // Set the animations of the player
        animations = Animation.createAnimations (ANIMATION_DURATION / 2, sprites);
        currentAnimation = animations[0];

        // Initial orientation
        desiredOrientation = Orientation.RIGHT;

        handler = new DarkLordHandler();
    }

    /**
     * Sets the current animation of the Ghost
     */
    private void setAnimations() {
        if (isDisplacementOccurs()) {
            currentAnimation = animations[getOrientation().ordinal()];
        } else {
            currentAnimation.reset();
        }
    }

    @Override
    public void update(float deltaTime) {

        // Move if is possible
        if (!isDisplacementOccurs()) {

            desiredOrientation = getNextOrientation();

            if (getOwnerArea().canEnterAreaCells(this,
                    Collections.singletonList (getCurrentMainCellCoordinates().jump(desiredOrientation.toVector())))) {
                // Orientation of the player for the next move
                orientate(desiredOrientation);
            }
            // Move of the player
            move(DEFAULT_SPEED);
        }

        // Set animations
        setAnimations();
        currentAnimation.update(deltaTime);

        super.update(deltaTime);
    }

    private Orientation getNextOrientation() {
        int randomInt = RandomGenerator.getInstance().nextInt(4);
        return Orientation.fromInt(randomInt);
    }

    @Override
    public void draw(Canvas canvas) {
        currentAnimation.draw(canvas);
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() {
        List<DiscreteCoordinates> fieldOfView = new ArrayList<>();

        // Add the coordinates that are in the field of view of the ghost
        for (int y = -FIELD_OF_VIEW; y <= FIELD_OF_VIEW; y++) {
            for (int x = -FIELD_OF_VIEW; x <= FIELD_OF_VIEW; x++) {
                fieldOfView.add(new DiscreteCoordinates(getCurrentMainCellCoordinates().x + x, getCurrentMainCellCoordinates().y + y));
            }
        }

        return fieldOfView;
    }

    @Override
    public boolean wantsCellInteraction() {
        return false;
    }

    @Override
    public boolean wantsViewInteraction() {
        return true;
    }

    @Override
    public boolean takeCellSpace() {
        return true;
    }

    @Override
    public boolean isCellInteractable() {
        return true;
    }

    @Override
    public boolean isViewInteractable() {
        return false;
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v) {
        ((SuperPacmanInteractionVisitor)v).interactWith (this);
    }

    @Override
    public void interactWith(Interactable other) { other.acceptInteraction(handler); }

    /**
     * Interaction handler for a DarkLord
     */
    private class DarkLordHandler implements SuperPacmanInteractionVisitor {

        @Override
        public void interactWith(SuperPacmanPlayer pacman) {

        }
    }
}
