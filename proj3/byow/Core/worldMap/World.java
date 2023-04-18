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

    public List<Integer> indexToXY(int index) {

        int widthIndex = index % worldWidth;
        int heightIndex = Math.floorDiv(index, worldWidth);
        System.out.println(index + ", " + widthIndex + ", " + heightIndex);
        List<Integer> returnLst = new TreeList();
        returnLst.add(widthIndex);
        returnLst.add(heightIndex);

        return returnLst;
    }

    /**
     * <p>
     *     true = index is not between bottomLeft and topRight
     * </p>
     *
     * <p>
     *     false = index is between bottomLeft and TopRight
     * </p>
     * @param index
     * @param bottomLeftIndex
     * @param topRightIndex
     * @return true or false
     */
    public boolean isBetween(int index, int bottomLeftIndex, int topRightIndex) {
        List<Integer> indexXY = indexToXY(index);
        List<Integer> bottomLeftXY = indexToXY(bottomLeftIndex);
        List<Integer> topRightXY = indexToXY(topRightIndex);

        if (indexXY.get(0) <= bottomLeftXY.get(0) || indexXY.get(0) >= topRightXY.get(0)) {
            return false;
        }
        if (indexXY.get(1) <= bottomLeftXY.get(1) || indexXY.get(1) >= topRightXY.get(1)) {
            return false;
        }
        return true;
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
        int maximum = (worldWidth - MAX_LIMIT) + (worldHeight - MAX_LIMIT) * worldWidth; // this is 1670
        int startingP = random.nextInt(0, maximum + 1);
        while (startingP % worldWidth > worldWidth - MAX_LIMIT) {
            // we subtract 10 because our maximum length of gridWidth is 10
            startingP = random.nextInt(0, maximum + 1);
        }
        return startingP;
    }

    // ------------------------------ Step C -----------------------------------

    /*
    public void generateRoom() {
        doorIndexLst = new ArrayList<>();

        int numRoom = random.nextInt(5, 16); // the number of room -> [5, 15]

        for (int i = 0; i < numRoom; i++) {  // add 'numRoom' blocks into the doorIndexLst

            int gridWidth = random.nextInt(3, MAX_LIMIT + 1); // width range -> [3, 10]
            int gridHeight = random.nextInt(3, MAX_LIMIT + 1); // height range -> [3, 10]



            // maximum gridWidth is MAX_LIMIT so maximum x-coordinate will be worldWidth - MAX_LIMIT
            // maximum gridHeight is MAX_LIMIT so maximum y-coordinate will be worldHeight - MAX_LIMIT

            int maximum = (worldWidth - MAX_LIMIT) + (worldHeight - MAX_LIMIT) * worldWidth; // this is 1670

            List<Integer> prevBottomLeftLst = null;
            List<Integer> prevUpperRightLst = null;

            int startingP = random.nextInt(0, maximum + 1);

            while (startingP % worldWidth > worldWidth - MAX_LIMIT) { // we subtract 10 because our maximum length of gridWidth is 10

                startingP = random.nextInt(0, maximum + 1);
            }
            for (Integer roomIndex: makeNbyMRoom(startingP, gridWidth, gridHeight)) {
                doorIndexLst.add(roomIndex);
            }
        }
    }
*/
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

    /**
     *
     * @param location Starting Point of the Room
     * @param n Width
     * @param m Height
     * @return list. list[0] = bottomLeft's index, list[1] = upperRight's index, list[2], list[3] .. = door's index
     */
    /*
    public List<Integer> makeNbyMRoom(int location, int n, int m) {

        List<Integer> doorLst = new TreeList();
        int doorNum = Math.floorMod(random.nextInt(), 2) + 1;

        int bottomLeftIndex = location;
        doorLst.add(bottomLeftIndex);

        int upperRightIndex = worldWidth * (indexToXY(location).get(1) + m - 1) + indexToXY(location).get(0) + n - 1;
        doorLst.add(upperRightIndex);

        List<Integer> possibleDoorIndex = new TreeList();

        int currIndex = location;

        for (int j = 0; j < m; j++) {
            for (int i = 0; i < n; i++) {
                currIndex = (indexToXY(location).get(1) + j )* worldWidth + indexToXY(location).get(0)+i;

                if (isEdgePoint(currIndex, bottomLeftIndex, upperRightIndex)) {
                    blockAt(currIndex).changeType("wall");
                } else if (isMarginOfRoom(currIndex, bottomLeftIndex, upperRightIndex)) {
                    blockAt(currIndex).changeType("wall");
                    if (!isMarginOfRoom(currIndex, 0, worldWidth*worldHeight - 1)
                            && !isEdgePoint(currIndex, bottomLeftIndex, upperRightIndex)) {
                        possibleDoorIndex.add(currIndex);
                    }
                } else {
                    blockAt(currIndex).changeType("room");
                }
            }
        }
        while (doorNum != 0) {
            int i = 0;
            if (doorNum == 2) {
                i = possibleDoorIndex.get(random.nextInt(0, possibleDoorIndex.size()/2));
            } else if (doorNum == 1) {
                i = possibleDoorIndex.get(random.nextInt(possibleDoorIndex.size()/2 + 1, possibleDoorIndex.size()));
            }
            blockAt(i).changeType("door");
            doorNum -= 1;
            doorLst.add(i);
        }
        return doorLst;
    }
*/

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
