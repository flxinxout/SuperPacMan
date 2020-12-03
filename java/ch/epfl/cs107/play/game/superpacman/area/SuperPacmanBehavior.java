package ch.epfl.cs107.play.game.superpacman.area;

import ch.epfl.cs107.play.game.areagame.AreaBehavior;
import ch.epfl.cs107.play.game.areagame.AreaGraph;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.superpacman.actor.collectable.Bonus;
import ch.epfl.cs107.play.game.superpacman.actor.collectable.Cherry;
import ch.epfl.cs107.play.game.superpacman.actor.collectable.Diamond;
import ch.epfl.cs107.play.game.superpacman.actor.Wall;
import ch.epfl.cs107.play.game.superpacman.actor.ghost.Blinky;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Window;

public class SuperPacmanBehavior extends AreaBehavior {
    private AreaGraph graph;
    /**
     * Enum that represent all possible types of each cell in the game
     */
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

        /**
         * A method that returns the type of cell based on a number assigned to it in the enum
         * @param type number assigned on a cell in the enum
         * @return the type of the cell
         */
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

                if (color != SuperPacmanCellType.WALL) {
                    boolean[] graphEdges = computeGraphEdges(x, y);
                    graph.addNode(new DiscreteCoordinates(x, y), graphEdges[0], graphEdges[1], graphEdges[2], graphEdges[3]);
                }
            }
        }
    }

    private boolean[] computeGraphEdges(int x, int y ) {
        boolean[] graphEdges = new boolean[4];

        //TODO: multiple casts and weird method
        SuperPacmanCell leftCell = (SuperPacmanCell) getCell(x-1 , y);
        graphEdges[0] = (x > 0 && leftCell.type != SuperPacmanCellType .WALL);

        SuperPacmanCell upCell = (SuperPacmanCell) getCell(x , y+1);
        graphEdges[1] = (x < getHeight() && upCell.type != SuperPacmanCellType .WALL);

        SuperPacmanCell rightCell = (SuperPacmanCell) getCell(x+1 , y);
        graphEdges[2] = (x < getWidth() && rightCell.type != SuperPacmanCellType .WALL);

        SuperPacmanCell downCell = (SuperPacmanCell) getCell(x , y-1);
        graphEdges[3] = (x > 0 && downCell.type != SuperPacmanCellType .WALL);

        return graphEdges;
    }
    /**
     * Method that registers actors in an area
     * @param area the area where actors will be registered
     */
    //TODO: CHANGE SUPERPACMANAREA TO AREA? --> FIND A BETTER WAY FOR THE DIAMONDS COUNT
    protected void registerActors(SuperPacmanArea area) {
        for (int y = 0; y < getHeight(); y++) {
            for (int x = 0; x < getWidth(); x++) {
                SuperPacmanCell cell = (SuperPacmanCell) getCell(x,y);

                switch (cell.type) {
                    case WALL:
                        Wall wall = new Wall(area, new DiscreteCoordinates(x, y), neighborhood(cell));
                        area.registerActor(wall);
                        break;

                    case FREE_WITH_DIAMOND:
                        Diamond diamond = new Diamond(area, new DiscreteCoordinates(x, y));
                        area.registerActor(diamond);
                        area.addDiamond();
                        break;

                    case FREE_WITH_BONUS:
                        Bonus bonus = new Bonus(area, new DiscreteCoordinates(x, y));
                        area.registerActor(bonus);
                        break;
                    case FREE_WITH_CHERRY:
                        Cherry cherry = new Cherry(area, new DiscreteCoordinates(x, y));
                        area.registerActor(cherry);
                        break;

                    case FREE_WITH_BLINKY:
                        Blinky blinky = new Blinky(area, Orientation.UP, new DiscreteCoordinates(x, y));
                        area.registerActor(blinky);
                        area.addGhost(blinky);
                        break;
                }
            }
        }
    }

    private boolean[][] neighborhood(SuperPacmanCell cell) {
        //First dimension := x, Second dimension := y
        boolean[][] neighborhood = new boolean[3][3];
        DiscreteCoordinates position = cell.getCurrentCells().get(0);

        for (int x = -1; x < 2 ; x++) {
            for (int y = -1; y < 2; y++) {
                if (y != 0 || x != 0) {
                    int rx = position.x + x;
                    int ry = position.y - y;

                    if (rx >= 0 && ry >= 0 && rx < getWidth() && ry < getHeight()) {
                        // We're in the grid, check for the borders
                        SuperPacmanCell comparisonCell = (SuperPacmanCell) getCell(rx, ry);
                        if (comparisonCell.type == SuperPacmanCellType.WALL) {
                            neighborhood[x+1][y+1] = true;
                        }
                    }
                }
            }
        }
        return neighborhood;
    }

    /**
     * Cell adapted to the SuperPacman game
     */
    public class SuperPacmanCell extends AreaBehavior.Cell {
        /// Type of the cell following the enum
        private SuperPacmanCellType type;

        /**
         * Default Tuto2Cell Constructor
         * @param x (int): x coordinate of the cell
         * @param y (int): y coordinate of the cell
         * @param type (SuperPacmanCellType), not null
         */
        public SuperPacmanCell(int x, int y, SuperPacmanCellType type) {
            super(x, y);
            this.type = type;
        }

        @Override
        protected boolean canEnter(Interactable entity) {
            return !hasNonTraversableContent();
        }

        @Override
        protected boolean canLeave(Interactable entity) {
            return true;
        }

        @Override
        public boolean isCellInteractable() {
            return true;
        }

        @Override
        public boolean isViewInteractable() {
            return true;
        }

        @Override
        public void acceptInteraction(AreaInteractionVisitor v) {

        }
    }
}
