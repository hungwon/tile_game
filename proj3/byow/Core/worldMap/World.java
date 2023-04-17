package byow.Core.worldMap;

import byow.Core.Graph.UndirectedGraph;
import byow.Core.worldMap.Block;
import byow.TileEngine.TETile;
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


            // maximum gridWidth is MAX_LIMIT so maximum x-coordinate will be worldWidth - MAX_LIMIT
            // maximum gridHeight is MAX_LIMIT so maximum y-coordinate will be worldHeight - MAX_LIMIT

            int maximum = (worldWidth - MAX_LIMIT) + (worldHeight - MAX_LIMIT) * worldWidth; // this is 1670

            int startingP = random.nextInt(0, maximum + 1);

            while (startingP % worldWidth > worldWidth - MAX_LIMIT) { // we subtract 10 because our maximum length of gridWidth is 10
                startingP = random.nextInt(0, maximum + 1);
            }

            doorIndexLst.add(makeNbyMRoom(startingP, gridWidth, gridHeight)); // add into our doorIndexLst
        }
    }


    

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
    public TETile[][] visualize() {
        TETile[][] visualWorld = new TETile[worldWidth][worldHeight];

        return visualWorld;
    }

    public static void main(String[] args) {


    }

}
