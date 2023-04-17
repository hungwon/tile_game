package byow.Core;

import byow.TileEngine.TETile;
import edu.princeton.cs.algs4.Graph;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class World {

    private Block[][] world; // Block[i][j] means x_location = i, y_location = j
    private Random random;
    private int Height;
    private int Width;
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
        while (index >= Width) {
            index -= Width;
            heightIndex++;
        }
        widthIndex = index;
        return world[widthIndex][heightIndex];
    }

    public Block[][] generateEmptyWorld(int h, int w) {
        Block[][] retWorld = new Block[w][h];
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                retWorld[i][j] = new Block(i*Width + j ,i, j, null);
            }
        }
        return retWorld;
    }

    public Graph generateWorldGraph() {
        return null;
    }

    public Integer setStartPoint() {
        return null;
    }


    // TO-DO
    public int randomNumRoom() {

        

        return 0;
    }

    // TO-DO
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

    // TO-DO
    public Integer makeNbyMRoom(int location, int n, int m) {
        Block doorBlock = null;

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
    public TETile[][] visualize() {
        TETile[][] visualWorld = new TETile[Width][Height];

        return visualWorld;
    }
}
