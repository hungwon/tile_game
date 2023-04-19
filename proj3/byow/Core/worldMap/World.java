package byow.Core.worldMap;

import byow.Core.Graph.UndirectedGraph;
import byow.Core.worldMap.Block;
import byow.TileEngine.TETile;
import byow.TileEngine.TERenderer;
import byow.TileEngine.Tileset;
import org.apache.commons.collections.list.TreeList;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class World {

    private Block[][] world; // Block[i][j] means x_location = i, y_location = j
    private Random random;
    private int worldWidth;
    private int worldHeight;

    private int MAX_LIMIT = 10; // the maximum number of grid's width and height
    private Integer startIndex;
    private UndirectedGraph worldGraph;
    private List<Integer> doorIndexLst;

    public World(int height, int width, int seed) {
        worldWidth = width;
        worldHeight = height;
        world = generateEmptyWorld(height, width);
        random = new Random(seed);
        worldGraph = generateWorldGraph();
        startIndex = setStartPoint();
        //generateRoom();
        generateHallways();
    }

    public int getWorldWidth() {
        return worldWidth;
    }

    public int getWorldHeight() {
        return worldHeight;
    }

    public List<Integer> indexToXY(int index) {

        int widthIndex = index % worldWidth;
        int heightIndex = Math.floorDiv(index, worldWidth);
        System.out.println(index + ", " + widthIndex + ", " + heightIndex);
        List<Integer> returnLst = new TreeList();
        returnLst.add(widthIndex);
        returnLst.add(heightIndex);

        return returnLst;
    }
    public boolean isEdgePoint(int index, int bottomLeftIndex, int upperRightIndex) {

        if (isTopLeft(index, bottomLeftIndex, upperRightIndex)) {
            return true;
        }
        if (isBottomLeft(index, bottomLeftIndex, upperRightIndex)) {
            return true;
        }
        if (isBottomRight(index, bottomLeftIndex, upperRightIndex)) {
            return true;
        }
        if (isTopRight(index, bottomLeftIndex, upperRightIndex)) {
            return true;
        }
        return false;
    }
    public boolean isBottomLeft(int index, int bottomLeftIndex, int topRightIndex) {
        List<Integer> indexXY = indexToXY(index);
        List<Integer> bottomLeftXY = indexToXY(bottomLeftIndex);

        if (indexXY.get(0) == bottomLeftXY.get(0) && indexXY.get(1) == bottomLeftXY.get(1)) {
            return true;
        }
        return false;
    }
    public boolean isBottomRight(int index, int bottomLeftIndex, int topRightIndex) {
        List<Integer> indexXY = indexToXY(index);
        List<Integer> bottomLeftXY = indexToXY(bottomLeftIndex);
        List<Integer> topRightXY = indexToXY(topRightIndex);

        if (indexXY.get(0) == topRightXY.get(0) && indexXY.get(1) == bottomLeftXY.get(1)) {
            return true;
        }
        return false;
    }
    public boolean isTopLeft(int index, int bottomLeftIndex, int topRightIndex) {
        List<Integer> indexXY = indexToXY(index);
        List<Integer> bottomLeftXY = indexToXY(bottomLeftIndex);
        List<Integer> topRightXY = indexToXY(topRightIndex);

        if (indexXY.get(0) == bottomLeftXY.get(0) && indexXY.get(1) == topRightXY.get(1)) {
            return true;
        }
        return false;
    }
    public boolean isTopRight(int index, int bottomLeftIndex, int topRightIndex) {
        List<Integer> indexXY = indexToXY(index);
        List<Integer> topRightXY = indexToXY(topRightIndex);

        if (indexXY.get(0) == topRightXY.get(0) && indexXY.get(1) == topRightXY.get(1)) {
            return true;
        }
        return false;
    }
    public boolean isMarginOfRoom(int index, int bottomLeftIndex, int upperRightIndex) {
        List<Integer> indexXY = indexToXY(index);
        List<Integer> bottomLeftXY = indexToXY(bottomLeftIndex);
        List<Integer> upperRightXY = indexToXY(upperRightIndex);

        if (indexXY.get(0) == bottomLeftXY.get(0) || indexXY.get(0) == upperRightXY.get(0)) {
            return true;
        }

        if (indexXY.get(1) == bottomLeftXY.get(1) || indexXY.get(1) == upperRightXY.get(1)) {
            return true;
        }
        return false;
    }
    public void checkIndex (int index) {
        if (index >= worldWidth*worldHeight ) {
            throw new IllegalArgumentException(index + ": index exceed 2399");
        }
    }
    public boolean isBetween(int index, List<Integer> bottomLeft, List<Integer> topRight) {
        for (int i = 0; i < bottomLeft.size(); i++) {
           if (isTopRight(index, bottomLeft.get(i), topRight.get(i))) {
               return true;
           }
        }
        return false;
    }
    public boolean isBetween(int index, int bottomLeftIndex, int topRightIndex) {
        List<Integer> indexXY = indexToXY(index);
        List<Integer> bottomLeftXY = indexToXY(bottomLeftIndex);
        List<Integer> topRightXY = indexToXY(topRightIndex);

        if (indexXY.get(0) >= bottomLeftXY.get(0) && indexXY.get(0) <= topRightXY.get(0)) {
            return true;
        }
        if (indexXY.get(1) >= bottomLeftXY.get(1) && indexXY.get(1) <= topRightXY.get(1)) {
            return true;
        }
        return false;
    }

    public Block blockAt (int index) {
        List<Integer> xyInfo = indexToXY(index);
        return world[xyInfo.get(0)][xyInfo.get(1)];
    }

    // ------------------------------ Step A -----------------------------------
    public Block[][] generateEmptyWorld(int h, int w) {
        Block[][] retWorld = new Block[w][h];
        for (int j = 0; j < h; j++) {
            for (int i = 0; i < w; i++) {
                retWorld[i][j] = new Block(j*worldWidth + i ,i, j, null); // index check
            }
        }
        return retWorld;
    }

    // ------------------------------ Step B -----------------------------------
    public UndirectedGraph generateWorldGraph() {
        UndirectedGraph retGraph = new UndirectedGraph(worldHeight*worldWidth);

        int maxIndex = worldHeight * worldWidth - 1;
        int currIndex = 0;

        for (int j = 0; j < worldHeight - 1; j++) {
            for (int i = 0; i < worldWidth - 1; i++) {
                currIndex = j*worldWidth + i;
                retGraph.addEdge(currIndex, currIndex + 1, random.nextDouble());
                retGraph.addEdge(currIndex, currIndex + worldHeight, random.nextDouble());
            }
        }
        return retGraph;
    }

    public Integer setStartPoint() {
        List<Integer> possibleStartingPoint = new ArrayList<>();
        for (int i = 0; i < worldWidth * worldHeight -1; i++) {
            if (blockAt(i).isNull()
                    && !isMarginOfRoom(i, worldWidth + 1, worldWidth*(worldHeight - 1) - 2)) {
                possibleStartingPoint.add(i);
            }
        }
        return possibleStartingPoint.get(random.nextInt(0, possibleStartingPoint.size()));
    }

    // ------------------------------ Step C -----------------------------------



    // ------------------------------ Step D -----------------------------------

    /**
     * dijkstra(startIndex, doorIndex) returns list of Block
     */
    public void generateHallways() {
        List<Integer> hallwayIndexList = new ArrayList<>();
        for (Integer doorIndex: doorIndexLst) {
            /*
            hallways = dijkstra (startIndex, doorIndex);
            for (Block b: hallways) {
                hallwayIndexList.add(b.key);
                if (b.isNull) {
                    b.type = "Hallway";
                }
            }
            makeWalls(hallwayIndexList);
            */
        }
    }


    // TO-DO
    // ------------------------------ Step E -----------------------------------
    public TETile[][] visualize() {
        TETile[][] visualWorld = new TETile[worldWidth][worldHeight];
        for (int i = 0; i < worldHeight; i++) {
            for (int j = 0; j < worldWidth; j++) {
                visualWorld[j][i] = Tileset.NOTHING;
            }
        }
        for (int i = 0; i < worldHeight; i++) {
            for (int j = 0; j < worldWidth; j++) {
                //System.out.print(world[j][i] + " ");
                if (world[j][i].blockType() == "door") {
                    visualWorld[j][i] = Tileset.MOUNTAIN;
                } else if (world[j][i].blockType() == "room") {

                    visualWorld[j][i] = Tileset.FLOOR;
                } else if (world[j][i].blockType() == "wall"){

                    visualWorld[j][i] = Tileset.WALL;
                }
            }
        }
        return visualWorld;
    }
    public static void main(String[] args) {

        World world = new World(30, 80, 1834);

        TERenderer ter = new TERenderer();
        ter.initialize(world.worldWidth, world.worldHeight);

        TETile[][] testWorld = world.visualize();
        ter.renderFrame(testWorld);
    }

}
