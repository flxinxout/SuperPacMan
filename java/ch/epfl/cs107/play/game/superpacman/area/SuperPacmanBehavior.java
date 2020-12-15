package ch.epfl.cs107.play.game.superpacman.area;

import ch.epfl.cs107.play.game.areagame.AreaBehavior;
import ch.epfl.cs107.play.game.areagame.AreaGraph;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.superpacman.actor.ennemy.Bow;
import ch.epfl.cs107.play.game.superpacman.actor.collectable.*;
import ch.epfl.cs107.play.game.superpacman.actor.setting.Wall;
import ch.epfl.cs107.play.game.superpacman.actor.ghost.Blinky;
import ch.epfl.cs107.play.game.superpacman.actor.ghost.Ghost;
import ch.epfl.cs107.play.game.superpacman.actor.ghost.Inky;
import ch.epfl.cs107.play.game.superpacman.actor.ghost.Pinky;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Window;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 * Class that represents the behavior of the SuperPacman game
 */
public class SuperPacmanBehavior extends AreaBehavior {

    // Graph associated to the area
    private AreaGraph graph = new AreaGraph();

    // List of ghosts in the game
    private List<Ghost> ghosts = new ArrayList<>();

    /**
     * Default SuperPacmanBehavior Constructor
     *
     * @param window (Window): the window. Not null
     * @param name   (String): the name of the Behavior. Not null
     */
    public SuperPacmanBehavior(Window window, String name){
        super(window, name);

        // Set the height and the width
        int height = getHeight();
        int width = getWidth();

        // Create all cells of the grid
        SuperPacmanCellType color;
        for(int y = 0; y < height; y++) {
            for (int x = 0; x < width ; x++) {
                color = SuperPacmanCellType.toType(getRGB(height - 1 - y, x));
                setCell(x, y, new SuperPacmanCell(x,y,color));
            }
        }

        // Create the nodes of the graph
        for(int y = 0; y < height; y++) {
            for (int x = 0; x < width ; x++) {
                SuperPacmanCell cell = (SuperPacmanCell) getCell(x, y);
                if (cell.type != SuperPacmanCellType.WALL) {
                    boolean[] graphEdges = computeGraphEdges(x, y);
                    graph.addNode(new DiscreteCoordinates(x, y), graphEdges[0], graphEdges[1], graphEdges[2], graphEdges[3]);
                }
            }
        }
    }

    /* --------------- Protected Methods --------------- */

    /**
     * Registers all actors in an area
     * @param area (Area): the area where actors will be registered
     */
    protected void registerActors(SuperPacmanArea area) {

        // For each cell in the area, we create an actor at this position if the cell type correspond to a cell type of the area
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
                        ghosts.add(blinky);
                        break;

                    case FREE_WITH_INKY:
                        Inky inky = new Inky(area, Orientation.UP, new DiscreteCoordinates(x, y));
                        area.registerActor(inky);
                        ghosts.add(inky);
                        break;

                    case FREE_WITH_PINKY:
                        Pinky pinky = new Pinky(area, Orientation.UP, new DiscreteCoordinates(x, y));
                        area.registerActor(pinky);
                        ghosts.add(pinky);
                        break;

                    case FREE_WITH_BOW:
                        Bow bow = new Bow(area, Orientation.UP, new DiscreteCoordinates(x, y));
                        area.registerActor(bow);
                        break;

                    case FREE_WITH_LIFE:
                        Heart heart = new Heart(area, Orientation.UP, new DiscreteCoordinates(x, y));
                        area.registerActor(heart);
                        break;

                    default:
                        break;
                }
            }
        }
    }

    /* --------------- External Methods --------------- */

    /**
     * Computes the graph edges of a cell
     * @param x (int): the x coordinate of the cell
     * @param y (int): the y coordinate of the cell
     * @return (boolean[]): boolean array of the edges (true := exists): 0:= left, 1:= up, 2:= right, 3:= down
     */
    private boolean[] computeGraphEdges(int x, int y) {
        boolean[] graphEdges = new boolean[4];

        //TODO: multiple casts and weird method
        if (x > 0) {
            SuperPacmanCell leftCell = (SuperPacmanCell) getCell(x - 1, y);
            graphEdges[0] = (leftCell.type != SuperPacmanCellType.WALL);
        }

        if (y < getHeight() - 1) {
            SuperPacmanCell upCell = (SuperPacmanCell) getCell(x, y+1);
            graphEdges[1] = (upCell.type != SuperPacmanCellType.WALL);
        }

        if (x < getWidth() - 1) {
            SuperPacmanCell rightCell = (SuperPacmanCell) getCell(x + 1, y);
            graphEdges[2] = (rightCell.type != SuperPacmanCellType.WALL);
        }

        if (y > 0) {
            SuperPacmanCell downCell = (SuperPacmanCell) getCell(x, y-1);
            graphEdges[3] = (downCell.type != SuperPacmanCellType.WALL);
        }

        return graphEdges;
    }

    /**
     * Computes the wall neighborhood of a cell
     * @param cell (SuperPacmanCell): the cell
     * @return (boolean[][]): 2D boolean array of the neighborhood of the cell (true = there's a wall)
     */
    private boolean[][] neighborhood(SuperPacmanCell cell) {

        //First dimension := x, Second dimension := y of the array
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

    /** Scares the ghosts */
    public void scareGhosts() {
        for (Ghost ghost: ghosts) {
            ghost.scare();
        }
    }

    /** Stops scaring the ghosts */
    public void unScareGhosts() {
        for (Ghost ghost: ghosts) {
            ghost.unScare();
        }
    }

    /* --------------- Getters --------------- */

    /** Calls shortestPath(DiscreteCoordinates from, DiscreteCoordinates to) from its graph */
    public Queue<Orientation> shortestPath(DiscreteCoordinates from, DiscreteCoordinates to) {
        return graph.shortestPath(from, to);
    }

    /** Calls setSignal(DiscreteCoordinates coordinates, Logic signal) from its graph */
    public void setSignal(DiscreteCoordinates coordinates, Logic signal) {
        graph.setSignal(coordinates, signal);
    }


    /**
    * Cell adapted to the SuperPacman game
    */
    public class SuperPacmanCell extends AreaBehavior.Cell {

        private SuperPacmanCellType type;

        /**
         * Default SuperPacmanCell Constructor
         *
         * @param x     (int): x coordinate of the cell. Not null
         * @param y     (int): y coordinate of the cell. Not null
         * @param type  (SuperPacmanCellType). Not null
         */
        public SuperPacmanCell(int x, int y, SuperPacmanCellType type) {
            super(x, y);
            this.type = type;
        }

        /* --------------- Extends Cell --------------- */

        @Override
        protected boolean canEnter(Interactable entity) {
            return !hasNonTraversableContent();
        }

        @Override
        protected boolean canLeave(Interactable entity) {
            return true;
        }

        /* --------------- Implements Interactable --------------- */

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

    /**
     * Enum that represent all possible types of cells in the game
     */
    public enum SuperPacmanCellType {
        NONE (0) , // never used as real content
        WALL ( -16777216) , // black
        FREE_WITH_DIAMOND ( -1) , // white
        FREE_WITH_BLINKY ( -65536) , // red
        FREE_WITH_PINKY ( -157237) , // pink
        FREE_WITH_INKY ( -16724737) , // cyan
        FREE_WITH_BOW ( -14046643) , // green
        FREE_WITH_CHERRY ( -36752) , // light red
        FREE_WITH_BONUS ( -16478723) , // light blue
        FREE_EMPTY ( -6118750) , // sort of gray
        FREE_WITH_LIFE (-256) ; // sort of yellow

        final int type;

        SuperPacmanCellType(int type){
            this.type = type;
        }

        /**
         * A method that returns the type of cell based on a number assigned to it in the enum
         * @param type (int): number assigned on a cell in the enum
         * @return (SuperPacmanCellType): the type of the cell
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
}
