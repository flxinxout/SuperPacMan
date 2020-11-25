package ch.epfl.cs107.play.game.superpacman.area;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.AreaBehavior;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.superpacman.actor.Wall;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Window;

public class SuperPacmanBehavior extends AreaBehavior {
    /**
     * Default SuperPacmanBehavior Constructor
     * @param window (Window), not null
     * @param name (String): Name of the Behavior, not null
     */
    public SuperPacmanBehavior(Window window, String name){
        super(window, name);
        int height = getHeight();
        int width = getWidth();
        for(int y = 0; y < height; y++) {
            for (int x = 0; x < width ; x++) {
                SuperPacmanCellType color = SuperPacmanCellType.toType(getRGB(height - 1 - y, x));
                setCell(x, y, new SuperPacmanCell(x,y,color));
            }
        }
    }

    protected void registerActors(Area area) {
        for (int y = 0; y < getHeight(); y++) {
            for (int x = 0; x < getWidth(); x++) {
                SuperPacmanCell cell = (SuperPacmanCell) getCell(x,y);
                if (cell.type == SuperPacmanCellType.WALL) {
                    Wall wall = new Wall(area, new DiscreteCoordinates(x, y), checkNeighborhood(cell));
                    area.registerActor(wall);
                }
            }
        }
    }

    private boolean[][] checkNeighborhood(SuperPacmanCell cell) {
        boolean[][] neighborhood = new boolean[3][3];
        DiscreteCoordinates position = cell.getCurrentCells().get(0);

        for (int y = -1; y <= 1; y++) {
            for (int x = -1; x <= 1; x++) {
                //Check for the borders
                if ( !((position.x == 0 && x == -1)
                        || (position.y == 0 && y == -1)
                        || (position.x == getWidth() - 1 && x == 1)
                        || (position.y == getHeight() - 1 && y == 1))) {
                    SuperPacmanCell comparisonCell = (SuperPacmanCell) getCell(position.x + x, position.y + y);
                    if (comparisonCell.type == SuperPacmanCellType.WALL) {
                        neighborhood[x+1][y+1] = true;
                    }
                }
            }
        }
        return neighborhood;
    }

    public class SuperPacmanCell extends AreaBehavior.Cell {
        private SuperPacmanCellType type;

        public SuperPacmanCell(int x, int y, SuperPacmanCellType type) {
            super(x, y);
            this.type = type;
        }

        @Override
        protected boolean canEnter(Interactable entity) {
            return takeCellSpace();
        }
    }

    public enum SuperPacmanCellType {
        NONE (0) , // never used as real content
        WALL ( -16777216) , // black
        FREE_WITH_DIAMOND ( -1) , // white
        FREE_WITH_BLINKY ( -65536) , // red
        FREE_WITH_PINKY ( -157237) , // pink
        FREE_WITH_INKY ( -16724737) , // cyan
        FREE_WITH_CHERRY ( -36752) , // light red
        FREE_WITH_BONUS ( -16478723) , // light blue
        FREE_EMPTY ( -6118750) ; // sort of gray

        final int type;

        SuperPacmanCellType(int type){
            this.type = type;
        }

        public static SuperPacmanCellType toType(int type){
            for(SuperPacmanCellType ict : SuperPacmanCellType.values()){
                if(ict.type == type)
                    return ict;
            }
            // When you add a new color, you can print the int value here before assign it to a type
            System.out.println(type);
            return NONE;
        }
    }
}
