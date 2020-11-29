package ch.epfl.cs107.play.game.superpacman.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

public abstract class Rewards extends AreaEntity {

    /**
     * A voir, mais comme super-class ça peut etre bien mais y'a un blem avec le setParent on en discutera pour
     * le moment les 3 objet collectable extends comme Wall AreaEntity
     */

    protected Sprite sprite;
    protected int rewards;

    public Rewards(Area area, Orientation orientation, DiscreteCoordinates position, Sprite sprite, int reward) {
        super(area, orientation, position);
        this.sprite = sprite;
        this.rewards = reward;
    }
}
