package byow.Core;

import byow.TileEngine.TETile;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class World {

    private Block[][] world; // Block[i][j] means x_location = i, y_location = j
    private Random random;
    private int Height;
    private int Width;
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
        while (index >= Width) {
            index -= Width;
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
                retWorld[i][j] = new Block(i*Width + j*Height ,i, j, null);
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

    // ------------------------------ Step C -----------------------------------
    public int randomNumRoom() {
        return 0;
    }
    public void generateRoom() {
        doorIndexLst = new ArrayList<>();
        int numRoom = randomNumRoom();

        int widthRoom = random.nextInt(); // must be changed
        int heightRoom = random.nextInt(); // must be changed
        int roomIndex = random.nextInt(); // must be changed
        for (int i = 0; i < numRoom; i++) {
            doorIndexLst.add(makeNbyMRoom(roomIndex, widthRoom, heightRoom));
        }
    }

    public Integer makeNbyMRoom(int location, int n, int m) {
        Integer doorIndex = null;

        //change type = room
        //change type = wall
        //disconnect wall with others
        //chagne type = door
        //return the Block.key whose type = door
        return  doorIndex;
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

    // ------------------------------ Step E -----------------------------------
    public TETile[][] visulize() {
        TETile[][] visualWorld = new TETile[Width][Height];
        return visualWorld;
    }
}
