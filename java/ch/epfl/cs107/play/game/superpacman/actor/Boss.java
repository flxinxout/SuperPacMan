package ch.epfl.cs107.play.game.superpacman.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.*;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.game.superpacman.actor.ghost.Ghost;
import ch.epfl.cs107.play.game.superpacman.area.SuperPacmanArea;
import ch.epfl.cs107.play.game.superpacman.handler.SuperPacmanInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;

public class Boss extends Ghost implements Interactor {

    // Handler of the Boss
    private final BossHandler handler;

    // Animation of the Boss
    private final int ANIMATION_DURATION = 8;
    private Animation[] animations;
    private Animation currentAnimation;

    // Field of view of Boss
    private final int SPEED = 15;
    private final int FIELD_OF_VIEW = 10;

    // Orientation of the Boss
    private Orientation desiredOrientation;

    // Target's Attributes
    private SuperPacmanPlayer player;
    private DiscreteCoordinates targetPos;

    /**
     * Default Boss constructor
     *
     * @param area        (Area): owner area. Not null
     * @param orientation (Orientation): initial orientation of the entity in the Area. Not null
     * @param position    (DiscreteCoordinate): initial position of the entity in the Area. Not null
     */
    public Boss(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position, 15, 10);

        // Initial orientation
        desiredOrientation = Orientation.RIGHT;

        // Creation of the handler
        handler = new BossHandler();

        // Default target position
        targetPos = getTargetPos();

        // Extracts the sprites of the ghost and sets the animations of the ghost
        Sprite[][] sprites = RPGSprite.extractSprites ("zelda/flameskull", 3, 2f, 2f,
                this, 32, 32, new Vector(-0.5f, -0.5f), new Orientation [] { Orientation.UP ,
                        Orientation.LEFT , Orientation.DOWN , Orientation.RIGHT });
        for (Sprite[] sprite : sprites) {
            for (Sprite value : sprite) {
                value.setDepth(950);
            }
        }
        animations = Animation.createAnimations (ANIMATION_DURATION /2, sprites);

    }

    /* --------------- Implements Graphics -------------- */

    @Override
    public void update(float deltaTime) {

        // Move if is possible
        if (!isDisplacementOccurs()) {

            desiredOrientation = getNextOrientation();

            if (getOwnerArea().canEnterAreaCells(this,
                    Collections.singletonList (getCurrentMainCellCoordinates().jump(desiredOrientation.toVector())))) {
                // Orientation of the Boss for the next move
                orientate(desiredOrientation);
            }

            // Move of the Boss
            move(SPEED);
        }

        // Set animations
        setAnimations();
        currentAnimation.update(deltaTime);

        // If the ghost and his target position are very close, we update the target in turns of parameters of the game
        if (targetPos != null && DiscreteCoordinates.distanceBetween(getCurrentMainCellCoordinates(), targetPos) < 0.1) {
            targetPos = getTargetPos();
        }

        super.update(deltaTime);
    }

    @Override
    public void draw(Canvas canvas) { currentAnimation.draw(canvas); }


    /* --------------- private Methods -------------- */

    /** Sets the current animation of the Ghost */
    private void setAnimations() {
        if (isDisplacementOccurs()) {
            currentAnimation = animations[getOrientation().ordinal()];
        } else {
            currentAnimation.reset();
        }
    }

    private void shoot() {
        Arrow fireBall = new Arrow(getOwnerArea(), getOrientation(), getCurrentMainCellCoordinates().jump(getOrientation().toVector()));
        getOwnerArea().registerActor(fireBall);
    }
    /* --------------- protected Methods -------------- */

    @Override
    protected Orientation getNextOrientation() {

        // Gets the area where is the ghost and the path between the ghost and the SuperPacmanPlayer
        SuperPacmanArea area = (SuperPacmanArea) getOwnerArea();
        Queue<Orientation> path = area.shortestPath(getCurrentMainCellCoordinates(), getTargetPos());

        // While the path is null or empty (for example if the ghost has not a target now), generate an other path
        while (path == null || path.isEmpty()) {
            DiscreteCoordinates cell = randomCell();
            path = area.shortestPath(getCurrentMainCellCoordinates(), cell);
        }

        return path.poll();
    }

    @Override
    protected Animation[] getAnimations() {
        return new Animation[0];
    }

    /**@return (DiscreteCoordinates): the target's position */
    protected DiscreteCoordinates getTargetPos() {
        if(player == null) {
            return randomCell();
        } else {
            return player.getCurrentCells().get((0));
        }
    }

    /* --------------- Implements Interactable -------------- */

    @Override
    public boolean isCellInteractable() { return true; }

    @Override
    public boolean isViewInteractable() { return false; }

    @Override
    public boolean takeCellSpace() { return false; }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v) { ((SuperPacmanInteractionVisitor)v).interactWith (this); }

    /* --------------- Implements Interactor -------------- */

    @Override
    public List<DiscreteCoordinates> getCurrentCells() { return Collections.singletonList(getCurrentMainCellCoordinates()); }

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
    public boolean wantsCellInteraction() { return false; }

    @Override
    public boolean wantsViewInteraction() { return true; }

    @Override
    public void interactWith(Interactable other) { other.acceptInteraction(handler); }


    /**
     * Interaction handler for a Boss
     */
    private class BossHandler implements SuperPacmanInteractionVisitor {

        @Override
        public void interactWith(SuperPacmanPlayer pacman) {
            player = pacman;
            targetPos = getTargetPos();
        }
    }
}
