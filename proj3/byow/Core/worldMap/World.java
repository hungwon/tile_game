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
    private Integer startIndex;
    private UndirectedGraph worldGraph;
    private List<Integer> doorIndexLst;
    public static final int MAXDIVSTART = 10000000;
    public static final int MAXDIVEND = 10000009;
    public static final int MAX_LIMIT = 10; // the maximum number of grid's width and height
    public static final double RANDDOUBLE = 0.8;
    public static final int MAXROOM = 8;
    public static final int MAXINDEX = 2399;
    public World(int height, int width, long seed) {
        maxNumHall = 2;
        worldWidth = width;
        worldHeight = height;
        world = generateEmptyWorld(height, width);
        Random deletedRandom = new Random(10000);
        random = new Random(Math.floorMod(seed, deletedRandom.nextInt(MAXDIVSTART, MAXDIVEND)));
        worldGraph = generateWorldGraph();
        generateRoom();
        startIndex = setStartPoint();
        generateHallways();
        generateWalls();
        blockAt(startIndex).changeType("start");
    }

    public Block blockAt(int index) {
        return world[index % worldWidth][index / worldWidth];
    }

    public List<Integer> indexToXY(int index) {

        int widthIndex = index % worldWidth;
        int heightIndex = Math.floorDiv(index, worldWidth);
        List<Integer> returnLst = new LinkedList<>();
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
    //--------------------------------- Tool Box -------------------------------------
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

    public void checkIndex(int index) {
        if (index >= worldWidth * worldHeight) {
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

    // --------------------------------- Tool Box for Creating Room --------------------------------
    private boolean isBetween(int topR, List<List<Integer>> everySP) {
        for (List<Integer> lst: everySP) {
            List<Integer> spCoord = indexToXY(lst.get(0)); // starting point's coord
            List<Integer> trCoord = indexToXY(lst.get(1)); // topright point's coord
            List<Integer> newTopRightCoord = indexToXY(topR); // current's topright point's coord

            if ((newTopRightCoord.get(0) >= spCoord.get(0) - 3 && newTopRightCoord.get(0) <= trCoord.get(0) + 3)) {
                if ((newTopRightCoord.get(1) >= spCoord.get(1) - 3 && newTopRightCoord.get(1) <= trCoord.get(1) + 3)) {
                    return true;
                }
            }
        }
        return false;
    }

    /** The edge cannot be a door. */
    private boolean invalidDoorLocation(int location) {

        return (location >= 0 && location <= (worldWidth * 2) - 1)
                || (location >= ((worldHeight - 2) * worldWidth) && location <= (worldWidth * worldHeight) - 1)
                || (location % worldWidth == 0)
                || (location % worldWidth == 1)
                || (location % worldWidth == worldWidth - 2)
                || (location % worldWidth == worldWidth - 1);
    }

    private boolean determineDoorPotential(int y, int x, int gridWidth, int gridHeight, int currentLocation) {
        // If current location is part of the margin (or margin - 1 square), immediately return false
        if (invalidDoorLocation(currentLocation)) {
            return false;
        }
        if ((y == 0 || y == gridHeight - 1) && (x > 0 && x < gridWidth - 1)) {
            return true;
        }
        if ((y > 0 && y < gridHeight - 1) && (x == 0 || x == gridWidth - 1)) {
            return true;
        }
        return false;
    }

    private void determineDisconnect(int current) {

        // see the drawing that I drew on the notion

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

    // ------------------------------ Step A -----------------------------------
    public Block[][] generateEmptyWorld(int h, int w) {
        Block[][] retWorld = new Block[w][h];
        for (int j = 0; j < h; j++) {
            for (int i = 0; i < w; i++) {
                retWorld[i][j] = new Block(j * worldWidth + i, i, j, null); // index check
            }
        }
        return retWorld;
    }

    // ------------------------------ Step B -----------------------------------
    public UndirectedGraph generateWorldGraph() {
        UndirectedGraph retGraph = new UndirectedGraph(worldHeight * worldWidth);

        int maxIndex = worldHeight * worldWidth - 1;
        int currIndex = 0;

        for (int j = 0; j < worldHeight - 1; j++) {
            for (int i = 0; i < worldWidth - 1; i++) {
                double d;
                if (i == 0 || j == 0) {
                    d = random.nextDouble(RANDDOUBLE, 1);
                } else {
                    d = random.nextDouble(0, 1);
                }
                currIndex = j * worldWidth + i;
                if (Math.floorMod(currIndex + 1, worldWidth) != 0) {
                    retGraph.addEdge(blockAt(currIndex), blockAt(currIndex + 1), d);
                }
                retGraph.addEdge(blockAt(currIndex), blockAt(currIndex + worldWidth), d);
            }
        }

        for (int j = 0; j < worldHeight - 1; j++) { // right margin
            currIndex = j * worldWidth + (worldWidth - 1);
            retGraph.addEdge(blockAt(currIndex), blockAt(currIndex + worldWidth), random.nextDouble(RANDDOUBLE, 1));
        }

        for (int i = 0; i < worldWidth - 1; i++) { //top margin
            currIndex = (worldHeight - 1) * worldWidth + i;
            retGraph.addEdge(blockAt(currIndex), blockAt(currIndex + 1), random.nextDouble(RANDDOUBLE, 1));
        }
        return retGraph;
    }

    public Integer setStartPoint() {
        List<Integer> possibleStartingPoint = new ArrayList<>();
        for (int i = 0; i < worldWidth * worldHeight - 1; i++) {
            if (blockAt(i).isNull()
                    && !isMarginOfRoom(i, worldWidth + 1, worldWidth * (worldHeight - 1) - 2)
                    && !isMarginOfRoom(i, 0, MAXINDEX)) {
                possibleStartingPoint.add(i);
            }
        }
        return possibleStartingPoint.get(random.nextInt(0, possibleStartingPoint.size()));
    }

    // ------------------------------ Step C -----------------------------------
    public void generateRoom() {

        // doorIndexLst will contain every door's index (should be used to make hallways)
        doorIndexLst = new ArrayList<>();


        // numRoom is the number of room; we will have 5 to 15 rooms per world.
        int numRoom = random.nextInt(5, MAXROOM);


        // everySP is a list of list. It will contain every valid starting point's information
        // Each of inner list have two specific index: [0] = starting point index , [1] = top right point index
        List<List<Integer>> everySP = new LinkedList<>();


        for (int i = 0; i < numRoom; i++) {

            // Create random gridWidth and gridHeight -> [4, MAX_LIMIT]
            int gridWidth = random.nextInt(4, MAX_LIMIT + 1);
            int gridHeight = random.nextInt(4, MAX_LIMIT + 1);


            // Instantiate a maximum starting point by using worldWidth, worldHeight, and MAX_LIMIT
            int maximum = (worldWidth - MAX_LIMIT) + (worldHeight - MAX_LIMIT) * worldWidth;

            // Create a random starting point which is between [0, MAXIMUM]
            // This will prevent choosing an invalid starting point that are over worldHeight - MAX_LIMIT
            // Instantiate the top right point of current starting point
            // These will be changed if the 'random' gives an invalid number
            int startingP = random.nextInt(0, maximum + 1);

            int topR = startingP + worldWidth * (gridHeight - 1) + (gridWidth - 1); // top right
            int bottomL = startingP; // bottom left
            int bottomR = startingP + gridWidth - 1; // bottom right
            int topL = topR - gridWidth + 1; // top left



            while (startingP % worldWidth > worldWidth - MAX_LIMIT || isBetween(topR, everySP)
                    || isBetween(bottomL, everySP) || isBetween(bottomR, everySP)
                    || isBetween(topL, everySP)) {

                startingP = random.nextInt(0, maximum + 1);

                topR = startingP + worldWidth * (gridHeight - 1) + (gridWidth - 1);
                bottomL = startingP; // to test our starting point is valid or not
                bottomR = startingP + gridWidth - 1;
                topL = topR - gridWidth + 1;
            }


            // create a list that will store starting point and top right point
            List<Integer> validSP = new LinkedList<>();
            validSP.add(startingP);
            validSP.add(topR);

            // add every valid starting point's bottom left (itself) and the top right point
            everySP.add(validSP);


            // draw a room
            makeNbyMRoom(startingP, gridWidth, gridHeight);
        }
    }

    /**
     * for step 2:
     *  if selected door is NOT connected to the door that is already confirmed
     *      AND if selected door is NOT same as the door that is already confirmed
     *      add it into the confirmedDoors list
     *      !worldGraph.isConnected(alreadyConfirmed, selected)
     *
     *      temporarily solved connected doors by comparing
     *      selected door + 1 or selected door - 1 or selected door + worldWidth or selected door - worldWidth
     *      is NOT equal to alreadyConfirmed door
     * @param startingP
     * @param gridWidth
     * @param gridHeight
     */
    public void makeNbyMRoom(int startingP, int gridWidth, int gridHeight) {

        int numDoor = random.nextInt(1, 3);
        String[] arr = {"room", "hallway"};
        String s = "";
        if (gridHeight == 4 && maxNumHall != 0 && numDoor == 2 && gridWidth >= 7) {
            s = arr[ Math.floorMod(random.nextInt(), 2)];
            if (s.equals("hallway")) {
                maxNumHall--;
                //System.out.println("MAKE H = 2 HALLWAY");
            }
        } else {
            s = "room";
        }

        // step 1. change every grid to room
        int store1 = startingP;
        List<Integer> potentialDoors = new LinkedList<>();
        for (int y = 0; y < gridHeight; y++) {
            for (int x = 0; x < gridWidth; x++) {
                int currentLocation = store1 + x;
                if (determineDoorPotential(y, x, gridWidth, gridHeight, currentLocation)) {
                    potentialDoors.add(currentLocation);
                }
                blockAt(currentLocation).changeType(s);
            }
            store1 += worldWidth;
        }

        // step 2. change some grid to door
        List<Integer> confirmedDoors = new LinkedList<>();
        while (confirmedDoors.size() < numDoor) {
            int selected = potentialDoors.get(random.nextInt(0, potentialDoors.size()));
            if (confirmedDoors.size() == 0) {
                confirmedDoors.add(selected);
            } else {
                int alreadyConfirmed = confirmedDoors.get(0);
                if (selected + 1 != alreadyConfirmed
                        && selected - 1 != alreadyConfirmed
                        && selected + worldWidth != alreadyConfirmed
                        && selected - worldWidth != alreadyConfirmed
                        && alreadyConfirmed != selected) {
                    confirmedDoors.add(selected);
                }
            }
        }
        for (int i = 0; i < confirmedDoors.size(); i++) {
            blockAt(confirmedDoors.get(i)).changeType("door");
        }

        // step 3. Change the edge rooms that are NOT door into wall
        int storeLoc2 = startingP;
        for (int i = 0; i < gridHeight; i++) {
            if (i == 0 || i == gridHeight - 1) {
                for (int j = 0; j < gridWidth; j++) {
                    int current = storeLoc2 + j;
                    if (blockAt(current).blockType().equals(s)) {
                        blockAt(current).changeType("wall");
                        determineDisconnect(current);
                    }
                }
            } else {
                int idx = 0;
                for (int j = 0; j < 2; j++) {
                    int current = storeLoc2 + idx;
                    if (blockAt(current).blockType().equals(s)) {
                        blockAt(current).changeType("wall");
                        determineDisconnect(current);
                    }
                    idx += gridWidth - 1;
                }
            }
            storeLoc2 += worldWidth;
        }
        // add every door index into the doorIndexLst list
        doorIndexLst.addAll(confirmedDoors);
    }

    // ------------------------------ Step D -----------------------------------
    public void generateHallways() {
        List<Integer> hallwayIndexList;
        Dijkstra dijk = new Dijkstra(worldGraph);

        for (Integer doorIndex: doorIndexLst) {

            hallwayIndexList = dijk.findPath(startIndex, doorIndex);

            for (int i: hallwayIndexList) {
                blockAt(i).changeType("hallway");
                //if (blockAt(i).isNull()) {
                //    blockAt(i).changeType("hallway");
                //}
            }
        }
    }

    public void generateWalls() {
        for (int i = worldWidth + 1; i < (worldWidth * worldHeight) - worldWidth - 1; i++) {
            if (blockAt(i).isHallway()) {
                if (blockAt(i + 1).isNull()) {
                    blockAt(i + 1).changeType("wall");
                }
                if (blockAt(i - 1).isNull()) {
                    blockAt(i - 1).changeType("wall");
                }
                if (blockAt(i + worldWidth).isNull()) {
                    blockAt(i + worldWidth).changeType("wall");
                }
                if (blockAt(i - worldWidth).isNull()) {
                    blockAt(i - worldWidth).changeType("wall");
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
                if (world[j][i].isDoor()) {
                    visualWorld[j][i] = Tileset.MOUNTAIN;
                } else if (world[j][i].isRoom()) {
                    visualWorld[j][i] = Tileset.TREE;
                } else if (world[j][i].isWall()) {
                    visualWorld[j][i] = Tileset.WALL;
                } else if (world[j][i].isHallway()) {
                    visualWorld[j][i] = Tileset.FLOOR;
                } else if (world[j][i].isStart()) {
                    visualWorld[j][i] = Tileset.FLOWER;

                }
            }
        }
        return visualWorld;
    }

    // ------------------------------ Main --------------------------------------

    public static void main(String[] args) {

        int num2 = 123498;
        World world = new World(30, 80, num2);
        TERenderer ter = new TERenderer();
        ter.initialize(world.worldWidth, world.worldHeight);
        TETile[][] testWorld = world.visualize();
        ter.renderFrame(testWorld);
    }
}