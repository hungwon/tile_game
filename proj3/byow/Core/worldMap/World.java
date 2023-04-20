package byow.Core.worldMap;

import byow.Core.Graph.Dijkstra;
import byow.Core.Graph.UndirectedGraph;
import byow.TileEngine.TETile;
import byow.TileEngine.TERenderer;
import byow.TileEngine.Tileset;
import org.apache.commons.collections.list.TreeList;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class World {

    private Block[][] world; // Block[i][j] means x_location = i, y_location = j
    private Random random;
    private int worldWidth;
    private int worldHeight;
    private int maxNumHall;

    private int MAX_LIMIT = 10; // the maximum number of grid's width and height
    private Integer startIndex;
    private UndirectedGraph worldGraph;
    private List<Integer> doorIndexLst;

    public World(int height, int width, long seed) {
        maxNumHall = 2;
        worldWidth = width;
        worldHeight = height;
        world = generateEmptyWorld(height, width);
        random = new Random(seed);
        worldGraph = generateWorldGraph();
        startIndex = setStartPoint();
        generateRoom();
        //generateHallways();
    }

    public Block blockAt(int index) {
        return world[index % worldWidth][index / worldWidth];
    }

    public List<Integer> indexToXY(int index) {

        int widthIndex = index % worldWidth;
        int heightIndex = Math.floorDiv(index, worldWidth);
        List<Integer> returnLst = new TreeList();
        returnLst.add(widthIndex);
        returnLst.add(heightIndex);

        return returnLst;
    }

    public int getWorldWidth() {
        return worldWidth;
    }

    public int getWorldHeight() {
        return worldHeight;
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
        if (index >= worldWidth * worldHeight ) {
            throw new IllegalArgumentException (index + ": index exceed 2399");
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
                if (Math.floorMod(currIndex + 1, worldWidth) != 0) {
                    retGraph.addEdge(blockAt(currIndex), blockAt(currIndex + 1), random.nextDouble(0, 1));
                }
                // rightmost 일때 해결
                retGraph.addEdge(blockAt(currIndex), blockAt(currIndex + worldWidth), random.nextDouble(0, 1));
            }
        }

        for (int j = 0; j < worldHeight - 1; j++) {
            currIndex = j* worldWidth + (worldWidth - 1);
            retGraph.addEdge(blockAt(currIndex), blockAt(currIndex + worldWidth) , random.nextDouble(0, 1));
        }

        for (int i = 0; i <worldWidth - 1; i++) {
            currIndex = (worldHeight -1) * worldWidth + i;
            retGraph.addEdge(blockAt(currIndex), blockAt(currIndex + 1), random.nextDouble());
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
    public void generateRoom() {
        doorIndexLst = new ArrayList<>();

        int numRoom = random.nextInt(5, 16); // the number of room -> [5, 15]


        // These four lists contain same size.
        List<List<Integer>> everySP = new LinkedList<>();


        for (int i = 0; i < numRoom; i++) {  // add 'numRomm' blocks into the doorIndexLst
            int gridWidth = random.nextInt(3, MAX_LIMIT + 1); // width range -> [3, 10]
            int gridHeight = random.nextInt(3, MAX_LIMIT + 1); // height range -> [3, 10]

            // maximum gridWidth is MAX_LIMIT so maximum x-coordinate will be worldWidth - MAX_LIMIT
            // maximum gridHeight is MAX_LIMIT so maximum y-coordinate will be worldHeight - MAX_LIMIT

            int maximum = (worldWidth - MAX_LIMIT) + (worldHeight - MAX_LIMIT) * worldWidth; // this is 1670 from 80 * 30 world.

            int startingP = random.nextInt(0, maximum + 1);
            int topR = startingP + worldWidth * (gridHeight - 1) + (gridWidth - 1);


            while (startingP % worldWidth > worldWidth - MAX_LIMIT || isBetween(topR, everySP)
                    || isBetween(startingP, everySP) || isBetween(startingP + gridWidth-1, everySP)
                    || isBetween(topR - gridWidth - 1, everySP)) { // we subtract 10 because our maximum length of gridWidth is 1
                startingP = random.nextInt(0, maximum + 1);
                topR = startingP + worldWidth * (gridHeight - 1) + (gridWidth - 1);
            }

            List<Integer> validSP = new LinkedList<>();
            validSP.add(startingP);
            validSP.add(topR);

            everySP.add(validSP);

            makeNbyMRoom(startingP, gridWidth, gridHeight);
        }

    }

    private boolean isBetween (int topR, List<List<Integer>> everySP) {
        // current list -> [startingP, topR, gridWidth, gridHeight]
//        System.out.println(current);
//        System.out.println();
        for (List<Integer> lst: everySP) {
            List<Integer> sp_coord = indexToXY(lst.get(0)); // starting point's coord
            List<Integer> tr_coord = indexToXY(lst.get(1)); // topright point's coord
            List<Integer> current_tr_coord = indexToXY(topR); // current's topright point's coord

            if ((current_tr_coord.get(0) >= sp_coord.get(0) - 3 && current_tr_coord.get(0) <= tr_coord.get(0) + 3)
             && (current_tr_coord.get(1) >= sp_coord.get(1) - 3 && current_tr_coord.get(1) <= tr_coord.get(1) + 3)) {
                return true;
            }
        }
        return false;
    }

    /** The edge cannot be a door. */
    private boolean invalidDoorLocation(int location) {

        return (location >= 0 && location <= (worldWidth * 2) - 1) ||
                (location >= ((worldHeight - 2) * worldWidth) && location <= (worldWidth * worldHeight) - 1) ||
                (location % worldWidth == 0) ||
                (location % worldWidth == 1) ||
                (location % worldWidth == worldWidth - 2) ||
                (location % worldWidth == worldWidth - 1);
    }

    private boolean determineDoorPotential(int i, int j, int gridWidth, int gridHeight, int current_location) {

        if (invalidDoorLocation(current_location)) {
            return false;
        } else if ((i == 0 || i == gridHeight - 1) && (j > 0 && j < gridWidth - 1)) {
            return true;
        } else if ((i > 0 && i < gridHeight - 1) && (j == 0 || j == gridWidth - 1)) {
            return true;
        } else {
            return false;
        }
    }

    private void determineDisconnect(int current) {

        if (current > 0 && current < worldWidth - 1) { // red

            worldGraph.disconnect(blockAt(current), blockAt(current - 1));
            worldGraph.disconnect(blockAt(current), blockAt(current + 1));
            worldGraph.disconnect(blockAt(current), blockAt(current + worldWidth));
        } else if (current > worldWidth * worldHeight - worldWidth && current < worldWidth * worldHeight - 1) { // blue

            worldGraph.disconnect(blockAt(current), blockAt(current - 1));
            worldGraph.disconnect(blockAt(current), blockAt(current + 1));
            worldGraph.disconnect(blockAt(current), blockAt(current - worldWidth));
        } else if (current == 0) { // green

            worldGraph.disconnect(blockAt(current), blockAt(current + 1));
            worldGraph.disconnect(blockAt(current), blockAt(current + worldWidth));
        } else if (current == worldWidth - 1) { // purple

            worldGraph.disconnect(blockAt(current), blockAt(current - 1));
            worldGraph.disconnect(blockAt(current), blockAt(current + worldWidth));
        } else if (current == worldWidth * worldHeight - worldWidth) { // pink

            worldGraph.disconnect(blockAt(current), blockAt(current + 1));
            worldGraph.disconnect(blockAt(current), blockAt(current - worldWidth));
        } else if (current == worldWidth * worldHeight - 1) { // yellow

            worldGraph.disconnect(blockAt(current), blockAt(current - 1));
            worldGraph.disconnect(blockAt(current), blockAt(current - worldWidth));
        } else if (current % worldWidth == 0) { // sky blue

            worldGraph.disconnect(blockAt(current), blockAt(current + 1));
            worldGraph.disconnect(blockAt(current), blockAt(current + worldWidth));
            worldGraph.disconnect(blockAt(current), blockAt(current - worldWidth));
        } else if (current % worldWidth == worldWidth - 1) { // sky green

            worldGraph.disconnect(blockAt(current), blockAt(current - 1));
            worldGraph.disconnect(blockAt(current), blockAt(current + worldWidth));
            worldGraph.disconnect(blockAt(current), blockAt(current - worldWidth));
        } else { // black

            worldGraph.disconnect(blockAt(current), blockAt(current + 1));
            worldGraph.disconnect(blockAt(current), blockAt(current - 1));
            worldGraph.disconnect(blockAt(current), blockAt(current + worldWidth));
            worldGraph.disconnect(blockAt(current), blockAt(current - worldWidth));
        }

    }

    public void makeNbyMRoom(int location, int gridWidth, int gridHeight) {

        /*
         Part 1. Room
         1. Change every block's type into "room".
         2. If current block's location is not on a corner of the grid, add their location index
            into the "potentialDoors" list.
         3. Every inner-loop ends, we should increment the starting location index by 80 (which is worldWidth)
            because we need to pass 80 blocks to move up.
         */

        int numDoor = random.nextInt(1, 3); // the amount of door number will be a 1 in case

        int storeLoc1 = location;

        String[] arr = {"room", "hallway"};
        String s = "";

        if (gridHeight == 4 && maxNumHall != 0 && numDoor == 2 ) {
            s = arr[ Math.floorMod(random.nextInt(), 2)];
            if (s.equals("hallway")) {
                maxNumHall--;
            }
        } else {
            s = "room";
        }



        List<Integer> potentialDoors = new LinkedList<>();

        for (int i = 0; i < gridHeight; i++) {

            for (int j = 0; j < gridWidth; j++) {

                int current_location = storeLoc1 + j;

                if (determineDoorPotential(i, j, gridWidth, gridHeight, current_location)) {
                    potentialDoors.add(current_location);
                }

                blockAt(current_location).changeType(s);
            }
            storeLoc1 += worldWidth;  // increment by the world's width length
        }


        /*
        Part 2. Doors
        1. Get a random number of a door (it will be 1 door for now ).
        2. Use a for loop to add confirmed doors' location
        3. Change the type of the block by using location indices from confirmedDoors list.
         */



        List<Integer> confirmedDoors = new LinkedList<>();

        while (confirmedDoors.size() < numDoor) {

            int selected = potentialDoors.get( random.nextInt(0, potentialDoors.size()));

            if (confirmedDoors.size() == 0) {
                confirmedDoors.add(selected);
            } else {

                int alreadyConfirmed = confirmedDoors.get(0);

                //System.out.println("CONFIRMED AND ALREADY"+ " " + selected + " " + alreadyConfirmed);


                //System.out.println(alreadyConfirmed + ", " + selected + ", " + worldGraph.isConnected(alreadyConfirmed, selected));
                //System.out.println("a: " + worldGraph.adj(alreadyConfirmed));
                //System.out.println(alreadyConfirmed + ", " + selected + ", " + worldGraph.isConnected(alreadyConfirmed, selected));

                if (!worldGraph.isConnected(alreadyConfirmed, selected)) {
                    confirmedDoors.add(selected);
                }
            }
        }

        //System.out.println("*************");

        for (int i = 0; i < confirmedDoors.size(); i++) {
            blockAt(confirmedDoors.get(i)).changeType("door");
            //System.out.println(confirmedDoors.get(i));
        }

        /*
        Part 3. Walls
        change blocks that are in potentialDoors but not in confirmedDoors
        change where (i == j) or (i == grid width - 1 and j == 0) or (i == 0 and j == grid height - 1)

         */


        int storeLoc2 = location;

        for (int i = 0; i < gridHeight; i++) {

            if (i == 0 || i == gridHeight - 1) {

                for (int j = 0; j < gridWidth; j++) {
                    int current = storeLoc2 + j;
                    if (blockAt(current).blockType().equals(s)) {
                        blockAt(current).changeType("wall");

                        System.out.println(current);

                        determineDisconnect(current);

                        System.out.println("CURRENT BLOCK DISCONNECTED?" + " " + worldGraph.isIsolated(current));

                    }
                }
            } else {

                int idx = 0;

                for (int j = 0; j < 2; j++) {

                    int current = storeLoc2 + idx;
                    if (blockAt(current).blockType().equals(s)) {
                        blockAt(current).changeType("wall");

                        System.out.println(current);

                        determineDisconnect(current);

                        System.out.println("CURRENT BLOCK DISCONNECTED?" + " " + worldGraph.isIsolated(current));
                        //
                    }

                    idx += gridWidth - 1;
                }
            }

            storeLoc2 += worldWidth;
        }

        // add into our doorIndexLst
        doorIndexLst.addAll(confirmedDoors);
    }

    // ------------------------------ Step D -----------------------------------

    /**
     * dijkstra(startIndex, doorIndex) returns list of Block
     */

    public void generateHallways() {
        List<Integer> hallwayIndexList;
        Dijkstra dijk = new Dijkstra(worldGraph);
        for (Integer doorIndex: doorIndexLst) {
            hallwayIndexList = dijk.findPath(startIndex, doorIndex);
            for (int i: hallwayIndexList) {
                if (blockAt(i).isNull()) {
                    blockAt(i).changeType("hallway");
                }
            }
        }
    }


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

                if (world[j][i].isDoor()) {

                    visualWorld[j][i] = Tileset.MOUNTAIN;
                } else if (world[j][i].isRoom()) {

                    visualWorld[j][i] = Tileset.FLOOR;
                } else if (world[j][i].isWall()) {

                    visualWorld[j][i] = Tileset.WALL;
                } else if (world[j][i].isHallway()) {

                    visualWorld[j][i] = Tileset.WATER;
                }

            }
            //System.out.println();

        }

        return visualWorld;
    }



    public void testWallIsDisconnected() {

        boolean b = true;

        for (int i = 0; i < worldWidth * worldHeight; i++) {


            if (blockAt(i).isWall() && !worldGraph.isIsolated(i)) {
                b = false;
            }
            if (!b) {
                throw new IllegalArgumentException("index: " + i + ", " + worldGraph.isIsolated(i) + ", " + blockAt(i).blockType());
            }
            b = true;
        }
    }

    public static void main(String[] args) {


        World world = new World(30, 80, 3412);

        TERenderer ter = new TERenderer();
        ter.initialize(world.worldWidth, world.worldHeight);



        TETile[][] testWorld = world.visualize();
        ter.renderFrame(testWorld);

        world.testWallIsDisconnected();
    }
}
