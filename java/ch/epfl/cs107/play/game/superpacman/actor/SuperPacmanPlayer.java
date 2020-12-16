package ch.epfl.cs107.play.game.superpacman.actor;

import ch.epfl.cs107.play.game.actor.SoundAcoustics;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.*;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.game.rpg.actor.Player;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.game.superpacman.SuperPacman;
import ch.epfl.cs107.play.game.superpacman.actor.collectable.Bonus;
import ch.epfl.cs107.play.game.superpacman.actor.collectable.CollectableReward;
import ch.epfl.cs107.play.game.superpacman.actor.collectable.Heart;
import ch.epfl.cs107.play.game.superpacman.actor.collectable.BossLife;
import ch.epfl.cs107.play.game.superpacman.actor.ennemy.Boss;
import ch.epfl.cs107.play.game.superpacman.actor.ennemy.Fire;
import ch.epfl.cs107.play.game.superpacman.actor.ennemy.Projectile;
import ch.epfl.cs107.play.game.superpacman.actor.ghost.Ghost;
import ch.epfl.cs107.play.game.superpacman.area.SuperPacmanArea;
import ch.epfl.cs107.play.game.superpacman.handler.SuperPacmanInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Keyboard;

import java.util.Collections;
import java.util.List;

/**
 * SuperPacmanPlayer is the main player of the game
 */
public class SuperPacmanPlayer extends Player implements Killable {

    // Constants
    private final int DEFAULT_SPEED = 6;
    private final float INVINCIBLE_DURATION = 300;
    private final int MAXHP = 4;
    private final int START_HP = 1;

    // Attributes
    private int hp;
    private int score;
    private int speed;
    private Orientation desiredOrientation;

    // Invincibility (when a bonus is eaten)
    private boolean invincible;
    private float timerInvincible;

    // Animations
    private final Animation[] animations;
    private Animation currentAnimation;

    // Handler of the SuperPacmanPlayer
    private final SuperPacmanPlayerHandler handler;

    // StatusGUI of the SuperPacmanPlayer
    private final SuperPacmanPlayerStatusGUI status;

    /* --------------- EXTENSIONS --------------- */

    // Spawn protection (to avoid spawn kill)
    private final float PROTECTION_DURATION = 2.5f;
    private boolean protection;
    private float timerProtection;
    private final Animation[] PROTECTED_ANIMATIONS;

    // Sounds
    private final SoundAcoustics deathSound;

    /**
     * Default SuperPacmanPlayer Constructor
     *
     * @param owner       (Area): owner area of the player. Not null
     * @param coordinates (DiscreteCoordinates): coordinate of the player. Not null
     */
    public SuperPacmanPlayer(Area owner, DiscreteCoordinates coordinates) {
        super(owner, Orientation.RIGHT, coordinates);

        // Create the handler
        handler = new SuperPacmanPlayerHandler();

        // Create the status
        status = new SuperPacmanPlayerStatusGUI(this);

        //Setup the animations for Pacman
        int animationDuration = SuperPacman.getDefaultAnimationDuration();

        Sprite [][] sprites = RPGSprite.extractSprites ("superpacman/pacman", 4, 1, 1,
                this , 64, 64, new Orientation [] { Orientation.DOWN ,
                        Orientation.LEFT , Orientation.UP , Orientation.RIGHT });
        for (Sprite[] sprite : sprites) {
            for (Sprite value : sprite) {
                value.setDepth(975);
            }
        }
        animations = Animation.createAnimations (animationDuration, sprites);
        currentAnimation = animations[getOrientation().ordinal()];

        // Attributes initialization
        hp = START_HP;
        score = 0;
        speed = DEFAULT_SPEED;
        invincible = true;
        timerInvincible = INVINCIBLE_DURATION;

        // Initial orientation
        desiredOrientation = Orientation.RIGHT;

        /* --------------- EXTENSIONS --------------- */

        protection = false;
        timerProtection = PROTECTION_DURATION;

        Sprite [][] protectSprites = RPGSprite.extractSprites ("superpacman/pacman.protected", 4, 1, 1,
                this , 64, 64, new Orientation [] { Orientation.DOWN ,
                        Orientation.LEFT , Orientation.UP , Orientation.RIGHT });
        for (Sprite[] sprite : sprites) {
            for (Sprite value : sprite) {
                value.setDepth(975);
            }
        }
        PROTECTED_ANIMATIONS = Animation.createAnimations (animationDuration, protectSprites);

        deathSound = new SoundAcoustics("sounds/pacman/pacman_death.wav", 0.50f, false,false,false, true);

    }

    /* --------------- Public Methods --------------- */

    /**
     * Increase the score of a specific amount
     * @param amount (int)
     */
    public void addScore(int amount) {
        score += amount;
        if (score < 0) {
            score = 0;
        }
    }

    /**
     * Add 1 health point to the player.
     * If he already has the maximum number of hp, add 250 of score
     */
    public void addHP() {
        if(hp < MAXHP) {
            hp++;
        } else {
            score += 250;
        }
    }

    /* --------------- Private Methods --------------- */

    /**
     * Set the invincibility state of the player and scare the ghosts
     */
    private void invincible() {
        invincible = true;

        //TODO: documenter: ici plutôt que update de SuperPacman
        SuperPacmanArea ownerArea = (SuperPacmanArea) getOwnerArea();
        // Scare all ghost in the area
        ownerArea.scareGhosts();
    }

    /**
     * Method called in update to update the invincibility state of the player
     * @param deltaTime (float): the delta time of the update
     */
    private void refreshInvincibility(float deltaTime) {
            if (timerInvincible > 0) {
                timerInvincible -= deltaTime;
            } else {
                invincible = false;

                SuperPacmanArea ownerArea = (SuperPacmanArea) getOwnerArea();
                ownerArea.unScareGhosts();
                timerInvincible = INVINCIBLE_DURATION;
            }
    }

    /**
     * Compute the desired orientation if a key is pressed
     */
    private void desiredOrientation() {

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

    /**
     * Set the current animation of the player
     */
    private void setAnimations() {
        if (protection) {
            currentAnimation = PROTECTED_ANIMATIONS[getOrientation().ordinal()];
        } else {
            if (isDisplacementOccurs()) {
                currentAnimation = animations[getOrientation().ordinal()];
            } else {
                currentAnimation.reset();
            }
        }
    }

    /**
     * Method to cast an Area in a SuperPacmanArea
     * @param area (Area): the area to cast
     * @return (SuperPacmanArea)
     */
    //TODO: MOVE IT TO STATIC IN AREA
    private SuperPacmanArea toSuperPacmanArea(Area area) { return (SuperPacmanArea) area; }

    /* --------------- EXTENSIONS --------------- */

    /**
     * [extension] Start the protection of the player
     */
    private void protect() {
        protection = true;
    }

    /**
     * [extension] Method called in update to update the protection state of the player
     * @param deltaTime (float): the delta time of the update
     */
    private void refreshProtection(float deltaTime) {
        if (timerProtection > 0) {
            timerProtection -= deltaTime;
        } else {
            protection = false;
            timerProtection = PROTECTION_DURATION;
        }
    }

    /* -------------- Extends Player --------------- */

    @Override
    public void update(float deltaTime) {

        //Check the desired orientation
        desiredOrientation();

        // Move if is possible
        if (!isDisplacementOccurs()) {
            if (getOwnerArea().canEnterAreaCells(this,
                    Collections.singletonList (getCurrentMainCellCoordinates().jump(desiredOrientation.toVector())))) {
                orientate(desiredOrientation);
            }
            move(speed);
        }

        super.update(deltaTime);

        // Set animations
        setAnimations();
        currentAnimation.update(deltaTime);

        // Check invincibility state
        if (invincible) {
            refreshInvincibility(deltaTime);
        }

        /* -------------- EXTENSIONS --------------- */

        // Increase the speed and make invincible (of INVINCIBLE_DURATION seconds) if the area is completed
        SuperPacmanArea owner = toSuperPacmanArea(getOwnerArea());
        if (owner.isOn()) {
            invincible();
            speed = 9;
        } else {
            speed = DEFAULT_SPEED;
        }

        // Check the protection state
        if(protection) {
            refreshProtection(deltaTime);
        }

        // Pause the game if space is pressed
        if (getOwnerArea().getKeyboard().get(Keyboard.SPACE).isPressed()) {
            getOwnerArea().suspend();
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

        hp--;

        // If the hp are 0 it's game over
        if(hp <= 0) {
            SuperPacmanArea owner = (SuperPacmanArea) getOwnerArea();
            owner.gameOver();
            return;
        } else {

            // else, respawn and [extension] set the protection
            protect(); // [extension]
            getOwnerArea().leaveAreaCells(this, getEnteredCells());
            setCurrentPosition(toSuperPacmanArea(getOwnerArea()).getSpawnLocation().toVector());
            getOwnerArea().enterAreaCells(this, getCurrentCells());
            resetMotion();
        }

        /* --------------- EXTENSIONS --------------- */

        //Play the death sound
        deathSound.shouldBeStarted();
        deathSound.bip(getOwnerArea().getWindow());
    }

    /* --------------- Getters --------------- */

    /**
     * Getter for the hp
     * @return (int)
     */
    public int getHp() { return hp; }

    /**
     * Getter for the max hp
     * @return (int)
     */
    public int getMAXHP() { return MAXHP; }

    /**
     * Getter for the score
     * @return (int)
     */
    public int getScore() { return score; }

    /**
     * Interaction handler for a SuperPacmanPlayer
     */
    private class SuperPacmanPlayerHandler implements SuperPacmanInteractionVisitor {

        @Override
        public void interactWith(Door door) {
            setIsPassingADoor(door);
        }

        @Override
        public void interactWith(CollectableAreaEntity collectable) {
            collectable.onCollect();
        }

        @Override
        public void interactWith(CollectableReward collectable) {
            interactWith((CollectableAreaEntity) collectable);
            addScore(collectable.getReward());
        }

        @Override
        public void interactWith(Bonus bonus) {
            interactWith((CollectableAreaEntity) bonus);
            invincible();
        }

        @Override
        public void interactWith(Ghost ghost) {
            if (invincible) {
                if (!ghost.isProtected()) {
                    ghost.onDeath();
                    addScore(ghost.getScore());
                }
            } else {
                if(!protection) {
                    onDeath();
                }
            }
        }

        /* --------------- EXTENSIONS --------------- */

        @Override
        public void interactWith(Projectile projectile) {
            if (!protection && !invincible) {
                onDeath();
            }
        }

        @Override
        public void interactWith(Fire fire) {
            if (!protection && !invincible) {
                onDeath();
            }
        }

        @Override
        public void interactWith(Boss boss) {
            if (!protection && !invincible) {
                onDeath();
            }
        }

        @Override
        public void interactWith(BossLife bossLife) {
            interactWith((CollectableAreaEntity) bossLife);
            bossLife.getBoss().loseHP();
        }

        @Override
        public void interactWith(Heart heart) {
            interactWith((CollectableAreaEntity) heart);
            addHP();
        }
    }
}
