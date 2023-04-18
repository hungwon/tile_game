package byow.Core.worldMap;

import byow.Core.Graph.UndirectedGraph;
import byow.Core.worldMap.Block;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import byow.TileEngine.TERenderer;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class World {

    private Block[][] world; // Block[i][j] means x_location = i, y_location = j
    private Random random;
    private int worldWidth = 80; // for debugging purpose
    private int worldHeight = 30; // for debugging purpose

    private int MAX_LIMIT = 10; // the maximum number of grid's width and height
    private Integer startIndex;
    private UndirectedGraph worldGraph;

    private List<Integer> doorIndexLst;

    public World(int height, int width, int seed) {
        world = generateEmptyWorld(height, width);
        random = new Random(seed);
        worldGraph = generateWorldGraph();
        startIndex = setStartPoint();
        generateRoom();
        generateHallways();
    }

    public Block blockAt(int index) {
        int widthIndex = 0;
        int heightIndex = 0;
        while (index >= worldWidth) {
            index -= worldWidth;
            heightIndex++;
        }
        widthIndex = index;
        return world[widthIndex][heightIndex];
    }

    // ------------------------------ Step A -----------------------------------
    public Block[][] generateEmptyWorld(int h, int w) {
        Block[][] retWorld = new Block[w][h];
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                retWorld[i][j] = new Block(i * worldWidth + j, i, j, null);
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


    public void generateRoom() {
        doorIndexLst = new ArrayList<>();

        int numRoom = random.nextInt(5, 16); // the number of room -> [5, 15]

        for (int i = 0; i < numRoom; i++) {  // add 'numRomm' blocks into the doorIndexLst

            int gridWidth = random.nextInt(3, MAX_LIMIT + 1); // width range -> [3, 10]
            int gridHeight = random.nextInt(3, MAX_LIMIT + 1); // height range -> [3, 10]

            System.out.println(gridWidth + " " + gridHeight);


            // maximum gridWidth is MAX_LIMIT so maximum x-coordinate will be worldWidth - MAX_LIMIT
            // maximum gridHeight is MAX_LIMIT so maximum y-coordinate will be worldHeight - MAX_LIMIT

            int maximum = (worldWidth - MAX_LIMIT) + (worldHeight - MAX_LIMIT) * worldWidth; // this is 1670

            prevBottomLeftLst = null;
            prevUpperRightLst = null;

            int startingP = random.nextInt(0, maximum + 1);



            while (startingP % worldWidth > worldWidth - MAX_LIMIT && isInRoom(currIndex, prevBottomLeft, prevUpperRight) ) { // we subtract 10 because our maximum length of gridWidth is 10
                startingP = random.nextInt(0, maximum + 1);
            }

            doorIndexLst.add(makeNbyMRoom(startingP, gridWidth, gridHeight)); // add into our doorIndexLst


        }


    }


    private boolean determineDoorPotential(int i, int j, int gridWidth, int gridHeight) {

        // (i == 0 or i == gridHeight - 1) and (j > 0 and j < gridWidth - 1)
        // (i > 0 and i < gridHeight - 1) and (j == 0 or j == gridWidth - 1)

        if ((i == 0 || i == gridHeight - 1) && (j > 0 && j < gridWidth - 1)) {
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


                if (determineDoorPotential(i, j, gridWidth, gridHeight)) {
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


        int numDoor = random.nextInt(1, 2); // the amount of door number will be a 1 in case
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


        *
        *
        *
        *
        *
        * */

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

//        doorBlock = b;
//        b.type = "door";
//        ret b.key
//        //return the Block.key whose type = door
//        return doorIndex;

        return null;
    }

    // ------------------------------ Step D -----------------------------------

    /**
     * dijkstra(startIndex, doorIndex) returns list of Block
     */
    public void generateHallways() {
        List<Integer> hallwayIndexList = new ArrayList<>();
        for (Integer doorIndex : doorIndexLst) {
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

        World world = new World(30, 80, 1000);

        TERenderer ter = new TERenderer();
        ter.initialize(world.worldWidth, world.worldHeight);

        TETile[][] testWorld = world.visualize();
        ter.renderFrame(testWorld);
    }
}