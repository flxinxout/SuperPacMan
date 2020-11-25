package ch.epfl.cs107.play.game.tutos;

import ch.epfl.cs107.play.game.areagame.AreaBehavior;
import ch.epfl.cs107.play.game.areagame.Cell;
import ch.epfl.cs107.play.game.areagame.io.ResourcePath;
import ch.epfl.cs107.play.window.Image;
import ch.epfl.cs107.play.window.Window;

public class Tuto2Behavior extends AreaBehavior {

    public Tuto2Behavior(Window window, String name) {
        super(window, name);

        for (int y = 0; y < getHeight(); y++) {
            for (int x = 0; x < getWidth(); x++) {
                Tuto2CellType tuto2CellType = Tuto2CellType.toType(getRGB(getHeight() - 1 - y, x));
                Tuto2Cell tuto2Cell = new Tuto2Cell(x, y, tuto2CellType);
                setCell(x, y, tuto2Cell);
            }
        }
    }
}

enum Tuto2CellType {
    NULL(0, false),
    WALL(-16777216, false), // #000000 RGB code of black
    IMPASSABLE (-8750470, false), // #7A7A7A , RGB color of gray
    INTERACT(-256, true), // #FFFF00 , RGB color of yellow
    DOOR(-195580, true), // #FD0404 , RGB color of red
    WALKABLE(-1, true),; // #FFFFFF , RGB color of white
    final int type;
    final boolean isWalkable;

    Tuto2CellType(int type , boolean isWalkable){
        this.type = type;
        this.isWalkable = isWalkable;
    }

    static Tuto2CellType toType(int type) {
        for(Tuto2CellType tuto2CellType : values()) {
            if(tuto2CellType.type == type) {
                return tuto2CellType;
            }
        }
        return NULL;
    }

}

