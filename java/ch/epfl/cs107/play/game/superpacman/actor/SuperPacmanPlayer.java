package ch.epfl.cs107.play.game.superpacman.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.*;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.game.rpg.actor.Player;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.game.superpacman.actor.collectable.Bonus;
import ch.epfl.cs107.play.game.superpacman.actor.collectable.Heart;
import ch.epfl.cs107.play.game.superpacman.actor.ghost.Ghost;
import ch.epfl.cs107.play.game.superpacman.area.SuperPacmanArea;
import ch.epfl.cs107.play.game.superpacman.area.util.State;
import ch.epfl.cs107.play.game.superpacman.handler.SuperPacmanInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Keyboard;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * Class that represent the SuperPacmanPlayer in the game
 */

public class SuperPacmanPlayer extends Player implements Killable, Sound {

    // Handler of the SuperPacmanPlayer
    private SuperPacmanPlayerHandler handler;

    // StatusGUI of the SuperPacmanPlayer
    private SuperPacmanPlayerStatusGUI status;

    // Constants of the SuperPacmanPlayer
    private final int SPEED = 6;
    private final float INVINCIBLE_DURATION = 10;
    private final float PROTECTION_DURATION = 3;
    private final int MAXHP = 4;
    private final int START_HP = 1;

    // Attributes of the SuperPacmanPlayer
    private int hp;
    private int score;
    private boolean invincible;
    private float timerInvincible;

    // Spawn protection (to avoid spawn kill)
    private boolean protection;
    private float timerProtection;

    // Animation of the SuperPacmanPlayer
    private final int ANIMATION_DURATION = 8;
    private Animation[] animations;
    private Animation currentAnimation;

    // Orientation of the player
    private Orientation desiredOrientation;

    /**
     * Default SuperPacmanPlayer Constructor
     * @param owner (Area): owner area of the player
     * @param coordinates(DiscreteCoordinates): coordinate of the player
     */
    public SuperPacmanPlayer(Area owner, DiscreteCoordinates coordinates) {
        super(owner, Orientation.RIGHT, coordinates);

        // Creation of the handler
        handler = new SuperPacmanPlayerHandler();

        // Create the status in turns of the current SuperPacmanPlayer
        status = new SuperPacmanPlayerStatusGUI(this);

        //Setup the animations for Pacman. Default: Down
        Sprite [][] sprites = RPGSprite.extractSprites ("superpacman/pacman", 4, 1, 1,
                this , 64, 64, new Orientation [] { Orientation.DOWN ,
                        Orientation.LEFT , Orientation.UP , Orientation.RIGHT });
        for (int i = 0; i < sprites.length; i++) {
            for (int j = 0; j < sprites[i].length; j++) {
                sprites[i][j].setDepth(975);
            }
        }

        // Sets the animations of the player
        animations = Animation.createAnimations (ANIMATION_DURATION /2, sprites);
        currentAnimation = animations[0];

        // Initialization of player's attributes
        hp = START_HP;
        score = 0;
        invincible = false;
        protection = false;
        timerInvincible = INVINCIBLE_DURATION;
        timerProtection = PROTECTION_DURATION;

        // Initial orientation
        desiredOrientation = Orientation.RIGHT;
    }

    /* --------------- External Methods --------------- */

    /**
     * Method that increase the score of the player
     * @param amount the amount increased
     */
    public void addScore(int amount) {
        score += amount;
        if (score < 0) {
            score = 0;
        }
    }

    /**
     * Method that add 1 health point to the player.
     * If he already has the maximum number of HP, add 250 of score
     */
    public void addHP() {
        if(hp < MAXHP) {
            hp++;
        } else {
            score += 250;
        }
    }

    /** Method that set the invincibility state of the player */
    private void invincible() {
        invincible = true;
        SuperPacmanArea ownerArea = (SuperPacmanArea) getOwnerArea();

        // Scared all ghost in the area
        ownerArea.getBehavior().scareGhosts();
    }

    /** Method that set the protection of the player when he's killed*/
    private void protect() {
        protection = true;
    }

    /**
     * Method called in update to update the invincibility state of the player
     * @param deltaTime (float) the delta time of the update
     */
    private void refreshInvincibility(float deltaTime) {
            if (timerInvincible > 0) {
                timerInvincible -= deltaTime;
            } else {
                invincible = false;

                SuperPacmanArea ownerArea = (SuperPacmanArea) getOwnerArea();
                ownerArea.getBehavior().unScareGhosts();
                timerInvincible = INVINCIBLE_DURATION;
            }
    }

    /**
     * Method called in update to update the protection state of the player
     * @param deltaTime (float) the delta time of the update
     */
    private void refreshProtection(float deltaTime) {
        if (timerProtection > 0) {
            timerProtection -= deltaTime;
        } else {
            protection = false;
            timerProtection = PROTECTION_DURATION;
        }
    }

    /** Method that compute the desired orientation if a key is pressed */
    private void computeDesiredOrientation() {

        // Get the keyboard of the gamer
        Keyboard keyboard = getOwnerArea().getKeyboard();

        // Check if this key is pressed and set the orientation of the player
        if (keyboard.get(Keyboard.DOWN).isLastPressed()) {
            desiredOrientation = Orientation.DOWN;
        }
        else if (keyboard.get(Keyboard.UP).isLastPressed()) {
            desiredOrientation = Orientation.UP;
        }
        else if (keyboard.get(Keyboard.LEFT).isLastPressed()) {
            desiredOrientation = Orientation.LEFT;
        }
        else if (keyboard.get(Keyboard.RIGHT).isLastPressed()) {
            desiredOrientation = Orientation.RIGHT;
        }
    }

    /** Method that set the current animation of the player */
    private void setAnimations() {
        if (isDisplacementOccurs()) {
            currentAnimation = animations[getOrientation().ordinal()];
        }
        else {
            currentAnimation.reset();
        }
    }

    /**
     * Method to cast an Area in a SuperPacmanArea
     * @param area (Area) the area to cast
     * @return the SuperPacmanArea of the game
     */
    //TODO: DISGUSTING!!!!!!!!!
    private SuperPacmanArea toSuperPacmanArea(Area area) { return (SuperPacmanArea) area; }

    /* -------------- Implement Actor --------------- */

    @Override
    public void update(float deltaTime) {

        //Check the desired orientation
        computeDesiredOrientation();

        // Move if is possible
        if (!isDisplacementOccurs()) {
            if (getOwnerArea().canEnterAreaCells(this,
                    Collections.singletonList (getCurrentMainCellCoordinates().jump(desiredOrientation.toVector())))) {
                // Orientation of the player for the next move
                orientate(desiredOrientation);
            }
            // Move of the player
            move(SPEED);
        }

        super.update(deltaTime);

        // Set animations
        setAnimations();
        currentAnimation.update(deltaTime);

        // Pause the game if space is pressed
        if (getOwnerArea().getKeyboard().get(Keyboard.SPACE).isPressed()) {
            getOwnerArea().suspend();
        }

        // Check invincibility state
        if (invincible) {
            refreshInvincibility(deltaTime);
        }

        // Check the protection state
        if(protection) {
            refreshProtection(deltaTime);
        }
    }

    @Override
    public void draw(Canvas canvas) {
        currentAnimation.draw(canvas);
        status.draw(canvas);
    }

    /* --------------- Implement Interactable --------------- */

    @Override
    public List<DiscreteCoordinates> getCurrentCells() { return Collections.singletonList(getCurrentMainCellCoordinates()); }

    @Override
    public boolean isCellInteractable() { return false; }

    @Override
    public boolean isViewInteractable() { return true; }

    @Override
    public boolean takeCellSpace() { return false; }

    @Override
    public void acceptInteraction (AreaInteractionVisitor v) { ((SuperPacmanInteractionVisitor)v).interactWith (this ); }


    /* --------------- Implement Interactor --------------- */

    @Override
    public boolean wantsCellInteraction() { return true; }

    @Override
    public boolean wantsViewInteraction() { return false; }

    @Override
    public void interactWith(Interactable other) { other.acceptInteraction(handler); }

    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() { return null; }


    /* --------------- Implements Killable --------------- */

    @Override
    public void onDeath() {

        // Decrease the life
        hp--;

        // If the hp is 0 or less it's game over for the player
        if(hp <= 0) {
            SuperPacmanArea owner = (SuperPacmanArea) getOwnerArea();
            owner.gameOver();
            return;
        } else {

            // else, we set a protection to avoid spawn kill and we discharge the entity in the cells where he is and spawn the player at his home
            protect();
            getOwnerArea().leaveAreaCells(this, getEnteredCells());
            setCurrentPosition(toSuperPacmanArea(getOwnerArea()).getSpawnLocation().toVector());
            getOwnerArea().enterAreaCells(this, getCurrentCells());
            resetMotion();
        }
    }


    /* --------------- Implement Sound --------------- */

    @Override
    public void onSound() {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("res/sounds/pacman/pacman_death.wav").getAbsoluteFile());

            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.loop(0);

        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    /* --------------- Getters --------------- */

    /**@return the life of the player */
    public int getHp() { return hp; }

    /**@return the max life of the player */
    public int getMAXHP() { return MAXHP; }

    /**@return the score of the player */
    public int getScore() { return score; }


    /**
     * Interaction handler for a SuperPacmanPlayer
     */
    private class SuperPacmanPlayerHandler implements SuperPacmanInteractionVisitor {

        @Override
        public void interactWith(Door door) { setIsPassingADoor(door); }

        @Override
        public void interactWith(CollectableAreaEntity collectable) {
            collectable.onCollect();

            //TODO POLYMORPHIC VOILA POURQUOI JAVAIS ETENDU COLLECTABLE
            //collectable.onSound();

            //TODO: POLYMORPHISM!!!
            if (collectable instanceof Bonus) {
                invincible();
            }

            //TODO: POLYMORPHISM!!!
            if (collectable instanceof Heart) {
                addHP();
            }
        }

        //TODO: Polymorphism here
        @Override
        public void interactWith(Bonus bonus) {

        }

        @Override
        public void interactWith(Ghost ghost) {
            if (invincible) {
                ghost.onDeath();
                ghost.onSound();
            } else {
                if(!protection) {
                    onDeath();
                    onSound();
                }
            }
        }
    }
}
