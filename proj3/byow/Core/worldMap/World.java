package byow.Core.worldMap;

import byow.Core.Graph.Dijkstra;
import byow.Core.Graph.UndirectedGraph;
import byow.TileEngine.TETile;
import byow.TileEngine.TERenderer;
import byow.TileEngine.Tileset;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Out;
import edu.princeton.cs.algs4.StdDraw;

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
    public static final int MAXDIVSTART = 9999999;
    public static final int MAXDIVEND = 10000009;
    public static final int MAX_LIMIT = 10; // the maximum number of grid's width and height
    public static final double RANDDOUBLE = 0.8;
    public static final int MAXROOM = 8;
    public static final int MAXINDEX = 2399;
    public static final int MAXY = 29;
    public static final int MAXX = 79;
    public static final int N = 10000;
    public static final int MAXMOVECNT = 5;
    private TERenderer ter;
    private long seed;
    private int avatarLocation;
    private boolean visualizeAll;
    private int moveCnt;
    private int skillTime;
    private int avatarTile;

    public World() {
        Out o = new Out("save.txt");
    }

    public World(int height, int width, long seed) {
        maxNumHall = 2;
        worldWidth = width;
        worldHeight = height;

        world = generateEmptyWorld(height, width);
        Random deletedRandom = new Random(N);
        seed = Math.floorMod(seed, deletedRandom.nextInt(MAXDIVSTART, MAXDIVEND));
        random = new Random(seed);

        worldGraph = generateWorldGraph();

        generateRoom();

        startIndex = setStartPoint();

        generateHallways();
        generateWalls();

        blockAt(startIndex).changeType("start");
        createAvatar();
        avatarTile = 1;
        visualizeAll = false;
        moveCnt = 0;
        skillTime = 0;
        ter = new TERenderer();
        ter.initialize(worldWidth, worldHeight);
    }

    public World(int height, int width, long seed, int start, int avatarLoc, int prevAvatarTile, Block[][] prevWorld) {
        world = prevWorld;
        worldWidth = width;
        worldHeight = height;
        this.seed = seed;
        this.random = new Random(this.seed);
        startIndex = start;
        blockAt(startIndex).changeType("start");
        this.avatarLocation = avatarLoc;
        avatarTile = prevAvatarTile;
        visualizeAll = false;
        moveCnt = 0;
        skillTime = 0;
        ter = new TERenderer();
        ter.initialize(worldWidth, worldHeight);

    }

    //--------------------------------- Tool Box -------------------------------------
    public Block blockAt(int index) {
        return world[Math.floorMod(index, worldWidth)][index / worldWidth];
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
        for (List<Integer> lst : everySP) {
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

    /**
     * The edge cannot be a door.
     */
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

    /**
     * Instantiate Block[][]
     * @param h height
     * @param w width
     * @return Block[][]
     */
    public Block[][] generateEmptyWorld(int h, int w) {
        Block[][] retWorld = new Block[w][h];
        for (int j = 0; j < h; j++) {
            for (int i = 0; i < w; i++) {
                retWorld[i][j] = new Block(j * worldWidth + i, i, j, "null"); // index check
            }
        }
        return retWorld;
    }

    // ------------------------------ Step B -----------------------------------

    /**
     * In order to use Dijkstra's algorithm, I create undirected graph.
     * e.g. a middle index of the world map is connected with four neigbors(top, bottom, left, right)
     * Just for Dijkstra's algorithm
     * @return undirected weigthed Edge graph
     */
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

    /**
     * Avoiding margin of the world map and rooms, choose a random point for a starting point
     * @return the location of starting point
     */
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

    /**
     * 1. determine random number of rooms
     * 2. determine random numbers of height and width of the room => random size
     * 3. determine random location of the room fulfills the location condition.
     * 4. Call makeNbyMRoom, then it will create a room
     *    corresponding to the above condition with random number of door.
     */
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
                if (numRoom >= 3) {
                    numRoom--;
                }
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
     * if selected door is NOT connected to the door that is already confirmed
     * AND if selected door is NOT same as the door that is already confirmed
     * add it into the confirmedDoors list
     * !worldGraph.isConnected(alreadyConfirmed, selected)
     * <p>
     * temporarily solved connected doors by comparing
     * selected door + 1 or selected door - 1 or selected door + worldWidth or selected door - worldWidth
     * is NOT equal to alreadyConfirmed door
     *
     * @param startingP
     * @param gridWidth
     * @param gridHeight
     */
    public void makeNbyMRoom(int startingP, int gridWidth, int gridHeight) {

        int numDoor = random.nextInt(1, 3);

        String[] arr = {"room", "hallway"};
        String s = "";
        if (gridHeight == 4 && maxNumHall != 0 && numDoor == 2 && gridWidth >= 7) {
            s = arr[Math.floorMod(random.nextInt(), 2)];
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

    /**
     * using Dijkstra's algorithm, connect starting point of the world and every door of rooms.
     * The shortest Path that meets two condition: (1. q != room 2. q is not at the margin of the world)
     * becomes the path of a hallway
     */
    public void generateHallways() {
        List<Integer> hallwayIndexList;
        Dijkstra dijk = new Dijkstra(worldGraph);

        for (Integer doorIndex : doorIndexLst) {

            hallwayIndexList = dijk.findPath(startIndex, doorIndex);

            for (int i : hallwayIndexList) {
                blockAt(i).changeType("hallway");
            }
        }
    }

    /**
     * for every block, it surrounds hallways with wall type block.
     */
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

    /**
     * helper func for partialVisualize
     * @return TETile[][]
     */
    public TETile[][] visualize() {

        TETile[][] visualWorld = new TETile[worldWidth][worldHeight];
        for (int i = 0; i < worldHeight; i++) {
            for (int j = 0; j < worldWidth; j++) {

                if (world[j][i].isInScope()) {
                    blockAt(i * worldWidth + j).changeScope(false);
                    if (world[j][i].isAvatar()) {
                        visualWorld[j][i] = chooseTile(avatarTile);
                    } else if (world[j][i].isRoom()) {
                        visualWorld[j][i] = Tileset.TREE;
                    } else if (world[j][i].isWall()) {
                        visualWorld[j][i] = Tileset.WALL;
                    } else if (world[j][i].isHallway()) {
                        visualWorld[j][i] = Tileset.FLOOR;
                    } else if (world[j][i].isStart()) {
                        visualWorld[j][i] = Tileset.FLOWER;
                    } else if (world[j][i].isDoor()) {
                        visualWorld[j][i] = Tileset.MOUNTAIN;
                    } else {
                        visualWorld[j][i] = Tileset.NOTHING;
                    }
                } else {
                    visualWorld[j][i] = Tileset.NOTHING;
                }
            }
        }
        return visualWorld;
    }
    /**
     * only surrounding part of avatar is visible
     * @return TETile[][]
     */
    public TETile[][] partialVisualize() {
        TETile[][] visualWorld = new TETile[worldWidth][worldHeight];
        if (visualizeAll) {
            visualWorld = allVisualize();
            return visualWorld;
        }
        int xIndex = indexToXY(avatarLocation).get(0);
        int yIndex = indexToXY(avatarLocation).get(1);
        int r = 3;
        for (int j = yIndex - r; j < yIndex + r; j++) {

            if (j > worldHeight - 1) {
                break;
            }
            if (j < 0) {
                break;
            }
            for (int i = xIndex - r; i < xIndex + r; i++) {
                int currIndex = j * worldWidth + i;
                if (i < 0) {
                    break;
                }
                if (i > worldWidth) {
                    break;
                }
                if (Math.pow(i - xIndex, 2) + Math.pow(j - yIndex, 2) <= Math.pow(r, 2)) {
                    blockAt(j * worldWidth + i).changeScope(true);
                }
            }
        }
        visualWorld = visualize();
        return visualWorld;

    }

    /**
     * every map is visible
     * @return TETile[][]
     */
    public TETile[][] allVisualize() {
        TETile[][] visualWorld = new TETile[worldWidth][worldHeight];
        for (int i = 0; i < worldHeight; i++) {
            for (int j = 0; j < worldWidth; j++) {
                blockAt(i * worldWidth + j).changeScope(false);
                if (world[j][i].isAvatar()) {
                    visualWorld[j][i] = chooseTile(avatarTile);
                } else if (world[j][i].isRoom()) {
                    visualWorld[j][i] = Tileset.TREE;
                } else if (world[j][i].isWall()) {
                    visualWorld[j][i] = Tileset.WALL;
                } else if (world[j][i].isHallway()) {
                    visualWorld[j][i] = Tileset.FLOOR;
                } else if (world[j][i].isStart()) {
                    visualWorld[j][i] = Tileset.FLOWER;
                } else if (world[j][i].isDoor()) {
                    visualWorld[j][i] = Tileset.MOUNTAIN;
                } else {
                    visualWorld[j][i] = Tileset.NOTHING;
                }
            }
        }
        return visualWorld;
    }

    // ---------------------------- Avatar -------------------------------------
    public void createAvatar() {
        avatarLocation = Block.moveAvaterTo(blockAt(startIndex), null);
    }

    public TETile chooseTile(int num) {
        if (Math.floorMod(num, 3) == 1) {
            return Tileset.AVATAR;
        } else if (Math.floorMod(num, 3) == 2) {
            return Tileset.UNLOCKED_DOOR;
        } else {
            return Tileset.LOCKED_DOOR;
        }
    }

    public void changeAvatarTile() {
        this.avatarTile += 1;
    }

    public void up() {
        if (indexToXY(avatarLocation).get(1) + 1 != MAXY && !blockAt(avatarLocation + worldWidth).isWall()) {
            avatarLocation = Block.moveAvaterTo(blockAt(avatarLocation), blockAt(avatarLocation + worldWidth));
        }
        if (visualizeAll && moveCnt < MAXMOVECNT) {
            moveCnt++;
            if (moveCnt >= MAXMOVECNT) {
                moveCnt = 0;
                visualizeAll = false;
            }
        }
        if (skillTime > 0) {
            skillTime--;
        }
    }

    public void down() {
        if (indexToXY(avatarLocation).get(1) - 1 != 0 && !blockAt(avatarLocation - worldWidth).isWall()) {
            avatarLocation = Block.moveAvaterTo(blockAt(avatarLocation), blockAt(avatarLocation - worldWidth));
        }
        if (visualizeAll && moveCnt < MAXMOVECNT) {
            moveCnt++;
            if (moveCnt >= MAXMOVECNT) {
                moveCnt = 0;
                visualizeAll = false;
            }
        }
        if (skillTime > 0) {
            skillTime--;
        }
    }

    public void left() {
        if (indexToXY(avatarLocation).get(0) - 1 != 0 && !blockAt(avatarLocation - 1).isWall()) {
            avatarLocation = Block.moveAvaterTo(blockAt(avatarLocation), blockAt(avatarLocation - 1));
        }
        if (visualizeAll && moveCnt < MAXMOVECNT) {
            moveCnt++;
            if (moveCnt >= MAXMOVECNT) {
                moveCnt = 0;
                visualizeAll = false;
            }
        }
        if (skillTime > 0) {
            skillTime--;
        }
    }

    public void right() {
        if (indexToXY(avatarLocation).get(0) + 1 != MAXX && !blockAt(avatarLocation + 1).isWall()) {
            avatarLocation = Block.moveAvaterTo(blockAt(avatarLocation), blockAt(avatarLocation + 1));
        }
        if (visualizeAll && moveCnt < MAXMOVECNT) {
            moveCnt++;
            if (moveCnt >= MAXMOVECNT) {
                moveCnt = 0;
                visualizeAll = false;
            }
        }
        if (skillTime > 0) {
            skillTime--;
        }
    }

    public void changeVisualizeMode() {
        if (skillTime == 0) {
            System.out.println(skillTime == 0);
            visualizeAll = true;
            moveCnt = 0;
            this.skillTime = 8;
        }
    }

    // ------------------------------ Save, Loading, etc -------------------------------------

    public void save() {
        Out o = new Out("save.txt");
        o.println(worldWidth);
        o.println(worldHeight);
        o.println(startIndex);
        o.println(seed);
        o.println(avatarLocation);
        o.println(avatarTile);
        for (int i = 0; i <= MAXINDEX; i++) {
            o.println(i + "," + indexToXY(i).get(0) + "," + indexToXY(i).get(1) + ","
                    + blockAt(i).blockType() + "," + blockAt(i).isAvatar() + "," + blockAt(i).isInScope());
        }
        o.close();
        System.out.println("save finished");
    }

    public static World load() {
        In in = new In("save.txt");

        //if (in.isEmpty()) {
        //    System.out.println("there's no recording");
        //    return new World(MAXY + 1, MAXX + 1, N);
        //}

        int w = Integer.parseInt(in.readLine());
        System.out.println("width: " + w);
        int h = Integer.parseInt(in.readLine());
        System.out.println("height: " + h);
        int sI = Integer.parseInt(in.readLine());
        System.out.println("startIndex: " + sI);
        Long s = Long.parseLong(in.readLine());
        System.out.println("seed: " + s);
        int aLoc = Integer.parseInt(in.readLine());
        System.out.println("avatar location: " + aLoc);
        int prevAvatarTile = Integer.parseInt(in.readLine());
        System.out.println(prevAvatarTile);

        int index = 0;
        int x = 0;
        int y = 0;
        String type = "";
        boolean isAvatar = false;
        boolean inScope = false;

        Block[][] newWorld = new Block[w][h];
        int i = 0;
        while (!in.isEmpty()) {
            String[] splitline = in.readLine().split(",");
            index = Integer.parseInt(splitline[0]);
            x = Integer.parseInt(splitline[1]);
            y = Integer.parseInt(splitline[2]);
            type = splitline[3];
            isAvatar = Boolean.parseBoolean(splitline[4]);
            inScope = Boolean.parseBoolean(splitline[5]);
            newWorld[x][y] = new Block(index, x, y, type, isAvatar, inScope);
            i++;
        }
        return new World(h, w, s, sI, aLoc, prevAvatarTile, newWorld);
    }

    public String tileAtMousePoint() {
        int xIndex = indexToXY(avatarLocation).get(0);
        int yIndex = indexToXY(avatarLocation).get(1);
        int r = 3;
        for (int j = yIndex - r; j < yIndex + r; j++) {
            for (int i = xIndex - r; i < xIndex + r; i++) {
                if (Math.pow(i - xIndex, 2) + Math.pow(j - yIndex, 2) <= Math.pow(r, 2)) {
                    blockAt(j * worldWidth + i).changeScope(true);
                }
            }
        }
        long x = Math.round(StdDraw.mouseX());
        long y = Math.round(StdDraw.mouseY());
        int indexOfMouse = (int) (x + y * worldWidth);
        if (blockAt(indexOfMouse).isAvatar()) {
            return "avatar";
        }
        if (indexOfMouse >= 0 && indexOfMouse <= MAXINDEX && blockAt(indexOfMouse).isInScope()) {
            return blockAt(indexOfMouse).blockType();
        }
        return "Not in the World Map";
    }
}


