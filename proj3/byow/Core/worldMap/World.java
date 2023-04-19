package byow.Core.worldMap;

import byow.Core.Graph.UndirectedGraph;
import byow.Core.worldMap.Block;
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
        generateRoom();
        generateHallways();
    }


    // ------------------------------ Step A -----------------------------------


    // ------------------------------ Step B -----------------------------------


    // ------------------------------ Step C -----------------------------------


    public Block blockAt(int index) {

        return world[index % worldWidth][index / worldWidth];
    }
    public void generateRoom() {
        doorIndexLst = new ArrayList<>();

        int numRoom = random.nextInt(5, 16); // the number of room -> [5, 15]

        System.out.println("NUMBER OF ROOMS:" + " " + numRoom);


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

            doorIndexLst.add(makeNbyMRoom(startingP, gridWidth, gridHeight)); // add into our doorIndexLst

        }

    }




    public List<Integer> indexToXY(int index) {

        int widthIndex = index % worldWidth;
        int heightIndex = Math.floorDiv(index, worldWidth);
        // System.out.println(index + ", " + widthIndex + ", " + heightIndex);
        List<Integer> returnLst = new ArrayList<>();
        returnLst.add(widthIndex);
        returnLst.add(heightIndex);

        return returnLst;
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
             && (current_tr_coord.get(1)  >= sp_coord.get(1) - 3 && current_tr_coord.get(1) <= tr_coord.get(1) + 3)) {
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



    public Integer makeNbyMRoom(int location, int gridWidth, int gridHeight) {


        /*
         Part 1. Room
         1. Change every block's type into "room".
         2. If current block's location is not on a corner of the grid, add their location index
            into the "potentialDoors" list.
         3. Every inner-loop ends, we should increment the starting location index by 80 (which is worldWidth)
            because we need to pass 80 blocks to move up.
         */

        int storeLoc1 = location;

        List<Integer> potentialDoors = new LinkedList<>();

        for (int i = 0; i < gridHeight; i++) {

            for (int j = 0; j < gridWidth; j++) {

                int current_location = storeLoc1 + j;

                if (determineDoorPotential(i, j, gridWidth, gridHeight, current_location)) {
                    potentialDoors.add(current_location);
                }

                blockAt(current_location).changeType("room");
            }
            storeLoc1 += worldWidth;  // increment by the world's width length
        }


        /*
        Part 2. Doors
        1. Get a random number of a door (it will be 1 door for now ).
        2. Use a for loop to add confirmed doors' location
        3. Change the type of the block by using location indices from confirmedDoors list.
         */


        int numDoor = random.nextInt(1, 3); // the amount of door number will be a 1 in case





        List<Integer> confirmedDoors = new LinkedList<>();
        for (int i = 0; i < numDoor; i++) {

            int confirmed = random.nextInt(0, potentialDoors.size()); // will give a random index



            confirmedDoors.add(potentialDoors.remove(confirmed));
        }

        for (int i = 0; i < confirmedDoors.size(); i++) {
            blockAt(confirmedDoors.get(i)).changeType("door");
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
                    if (blockAt(current).blockType().equals("room")) {
                        blockAt(current).changeType("wall");
                    }
                }
            } else {

                int idx = 0;

                for (int j = 0; j < 2; j++) {

                    int current = storeLoc2 + idx;
                    if (blockAt(current).blockType().equals("room")) {
                        blockAt(current).changeType("wall");
                    }

                    idx += gridWidth - 1;
                }
            }

            storeLoc2 += worldWidth;
        }

        return null;
    }



    // ------------------------------ Step D -----------------------------------

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

                System.out.print(world[j][i] + " ");

                if (world[j][i].blockType() == "door") {

                    visualWorld[j][i] = Tileset.MOUNTAIN;
                } else if (world[j][i].blockType() == "room") {

                    visualWorld[j][i] = Tileset.FLOOR;
                } else if (world[j][i].blockType() == "wall"){

                    visualWorld[j][i] = Tileset.WALL;
                }

            }
            System.out.println();

        }


        return visualWorld;
    }
    public static void main(String[] args) {

        World world = new World(30, 80, 1234);

        TERenderer ter = new TERenderer();
        ter.initialize(world.worldWidth, world.worldHeight);

        TETile[][] testWorld = world.visualize();
        ter.renderFrame(testWorld);
    }

}
