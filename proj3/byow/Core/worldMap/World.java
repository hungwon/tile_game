package byow.Core.worldMap;

import byow.Core.Graph.UndirectedGraph;
import byow.Core.worldMap.Block;
import byow.TileEngine.TETile;
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
        worldHeight = height
        world = generateEmptyWorld(height, width);
        random = new Random(seed);
        worldGraph = generateWorldGraph();
        startIndex = setStartPoint();
        generateRoom();
        generateHallways();
    }

    public List<Integer> indexToXY(int index) {

        int widthIndex = index % worldHeight;
        int heightIndex = Math.floorDiv(index, worldHeight);

        List<Integer> returnLst = new TreeList();
        returnLst.add(widthIndex);
        returnLst.add(heightIndex);

        return returnLst;
    }

    public Block blockAt (int index) {
        List<Integer> xyInfo = indexToXY(index);
        return world[xyInfo.get(0)][xyInfo.get(1)];
    }

    // ------------------------------ Step A -----------------------------------
    public Block[][] generateEmptyWorld(int h, int w) {
        Block[][] retWorld = new Block[w][h];
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                retWorld[i][j] = new Block(i*worldWidth + j ,i, j, null);
            }
        }
        return retWorld;
    }

    // ------------------------------ Step B -----------------------------------
    public UndirectedGraph generateWorldGraph() {
        return null;
    }

    public Integer setStartPoint() {
        return null;
    }


    // TO-DO
    // ------------------------------ Step C -----------------------------------


    public void generateRoomVer2() {
        doorIndexLst = new ArrayList<>();

        int numRoom = random.nextInt(5, 16); // the number of room -> [5, 15]

        for (int i = 0; i < numRoom; i++) {  // add 'numRomm' blocks into the doorIndexLst

            int gridWidth = random.nextInt(3, MAX_LIMIT + 1); // width range -> [3, 10]
            int gridHeight = random.nextInt(3, MAX_LIMIT + 1); // height range -> [3, 10]


            // maximum gridWidth is MAX_LIMIT so maximum x-coordinate will be worldWidth - MAX_LIMIT
            // maximum gridHeight is MAX_LIMIT so maximum y-coordinate will be worldHeight - MAX_LIMIT

            int maximum = (worldWidth - MAX_LIMIT) + (worldHeight - MAX_LIMIT) * worldWidth; // this is 1670

            int startingP = random.nextInt(0, maximum + 1);

            while (startingP % worldWidth > worldWidth - MAX_LIMIT) { // we subtract 10 because our maximum length of gridWidth is 10
                startingP = random.nextInt(0, maximum + 1);
            }
            for (Integer roomIndex: makeNbyMRoomVer2(startingP, gridWidth, gridHeight)) {
                doorIndexLst.add(roomIndex);
            }
        }
    }

    public boolean isEdgePoint(int index, int bottomLeftIndex, int upperRightIndex, int n, int m) {
        List<Integer> indexXY = indexToXY(index);
        List<Integer> bottomLeftXY = indexToXY(bottomLeftIndex);
        List<Integer> upperRightXY = indexToXY(upperRightIndex);

        if (indexXY.get(0) == bottomLeftXY.get(0) && indexXY.get(1) == bottomLeftXY.get(1)) {
            return true;
        } else if (indexXY.get(0) == (bottomLeftXY.get(0) + n) && indexXY.get(1) == bottomLeftXY.get(1)) {
            return true;
        } else if (indexXY.get(0) == upperRightXY.get(0) && indexXY.get(1) + m == upperRightXY.get(1)) {
            return true;
        } else if (indexXY.get(0) + n == upperRightXY.get(0) && indexXY.get(1) + m == upperRightXY.get(1)) {
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



    /**
     *
     * @param location Starting Point of the Room
     * @param n Width
     * @param m Height
     * @return Index of the door Location
     */
    public List<Integer> makeNbyMRoomVer2(int location, int n, int m) {

        List<Integer> doorLst = new TreeList();
        int doorNum = random.nextInt(1, 3);

        int bottomLeftIndex = location;
        int upperRightIndex = (indexToXY(location).get(1) + m) * worldWidth + n;
        int currIndex = location;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                currIndex = ((indexToXY(currIndex)).get(1) + j)* worldWidth + i;
                if (isEdgePoint(currIndex, bottomLeftIndex, upperRightIndex, n, m)) {
                    blockAt(currIndex).changeType("wall");
                } else if ( isMarginOfRoom(currIndex, bottomLeftIndex, upperRightIndex)) {
                    if (random.nextBoolean() == true && doorNum != 0) {
                        blockAt(currIndex).changeType("door");
                        doorNum -= 1;
                        doorLst.add(currIndex);
                    } else {
                        blockAt(currIndex).changeType("wall");
                    }
                    } else {
                    blockAt(currIndex).changeType("room");
                }
            }
        }

        return doorLst;




        // Block doorBlock = null;
        //change type = room
        //change type = wall
        //disconnect wall with others
        //
        /*
        doorBlock = b;
        b.type = "door";
        ret b.key
        //return the Block.key whose type = door
        return doorIndex;
         */
    }

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

        return visualWorld;
    }

    public static void main(String[] args) {


    }

}
