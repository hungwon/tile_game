package byow.Core;

import byow.TileEngine.TETile;
import edu.princeton.cs.algs4.Graph;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class World {

    private Block[][] world; // Block[i][j] means x_location = i, y_location = j
    private Random random;
    private int worldWidth;
    private int worldHeight;

    private int MAX_LIMIT = 10;
    private Integer startIndex;
    private Graph worldGraph;

    private List<Integer> doorIndexLst;
    public World(int height, int width, int seed) {
        world = generateEmptyWorld(height, width);
        random = new Random(seed);
        worldGraph = generateWorldGraph();
        startIndex = setStartPoint();
        generateRoom();
        generateHallways();
    }

    public Block blockAt (int index) {
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
                retWorld[i][j] = new Block(i*worldWidth + j ,i, j, null);
            }
        }
        return retWorld;
    }

    // ------------------------------ Step B -----------------------------------
    public Graph generateWorldGraph() {
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


            // maximum gridWidth is MAX_LIMIT so maximum x-coordinate will be worldWidth - MAX_LIMIT
            // maximum gridHeight is MAX_LIMIT so maximum y-coordinate will be worldHeight - MAX_LIMIT

            int startingPoint = random.nextInt(0, (worldWidth - MAX_LIMIT) * (worldHeight - MAX_LIMIT)); // room Index (location) [0, 1670] because the greatest grid is 10 * 10
            // this is [0, 1670]

            while(startingPoint % worldWidth > worldWidth - 10) { // we subtract 10 because our maximum length of gridWidth is 10
                startingPoint = random.nextInt(0, 1671);
            }

            doorIndexLst.add(makeNbyMRoom(roomIndex, widthRoom, heightRoom)); // add into our doorIndexLst
        }
    }

    /** make weight by height room will be created by following steps
     * 1.
     *
     *
     *
     *
     * */

    public Integer makeNbyMRoom(int location, int n, int m) {





       // Block doorBlock = null;
        //change type = room
        //change type = wall
        //disconnect wall with others
        //
        doorBlock = b;
        b.type = "door";
        ret b.key
        //return the Block.key whose type = door
        return doorIndex;
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
    public TETile[][] visulize() {
        TETile[][] visualWorld = new TETile[Width][Height];

        return visualWorld;
    }

    public static void main(String[] args) {


    }

}
