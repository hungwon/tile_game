package byow.Core;

import byow.Core.worldMap.World;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import edu.princeton.cs.algs4.StdDraw;
import java.awt.*;
import java.util.Arrays;

public class Engine {
    TERenderer ter = new TERenderer();
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;
    public static final int NUMBER = 10000000;
    public static final int GREATESTNUMBEROFSEED = 20;
    public static final int MAXIMUMKEYBOARDCOMMAND = 100000;
    public static final int FONTSIZE = 16;



    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void gameStart(World world) {
        boolean gO = false;
        int cnt = 0;
        char prev = ' ';
        while (!gO) {

            TETile[][] teTiles = world.partialVisualize();

            TETile[] navigationBar = new TETile[WIDTH];
            Arrays.fill(navigationBar, new TETile(' ', Color.white, Color.blue, "naviagtion bar"));
            String typeAtMousePt = world.tileAtMousePoint();
            for (int i = 0; i < typeAtMousePt.length(); i++) {
                navigationBar[WIDTH / 2 + i] =  new TETile(typeAtMousePt.charAt(i),
                        Color.white, Color.blue, "tile type");
            }

            TETile[][] newTeTile = new TETile[WIDTH][HEIGHT + 1];
            for (int j = 0; j < HEIGHT + 1; j++) {
                for (int i = 0; i < WIDTH; i++) {
                    if (j == HEIGHT) {
                        newTeTile[i][j] = navigationBar[i];
                    } else {
                        newTeTile[i][j] = teTiles[i][j];
                    }
                }
            }

            ter.renderFrame(newTeTile);

            if (StdDraw.hasNextKeyTyped()) {
                char c = StdDraw.nextKeyTyped();

                if (prev == ':' && (c == 'q' || c == 'Q')) {
                    world.save();
                    gO = true;
                    System.exit(0);
                } else if (c == 'W' || c == 'w') {
                    world.up();
                    cnt++;
                } else if (c == 'S' || c == 's') {
                    world.down();
                    cnt++;
                } else if (c == 'A' || c == 'a') {
                    world.left();
                    cnt++;
                } else if (c == 'D' || c == 'd') {
                    world.right();
                    cnt++;
                } else if (c == 'G' || c == 'g') {
                    world.changeVisualizeMode();
                } else if (c == 'O' || c == 'o') {
                    world.changeAvatarTile();
                }
                prev = c;
            } else {
                StdDraw.show();
            }

            if (cnt == MAXIMUMKEYBOARDCOMMAND) {
                gO = true;
            }
        }
    }

    public void interactWithKeyboard() {
        //new World();
        TERenderer terR = new TERenderer();
        terR.initialize(WIDTH, HEIGHT + 1);
        World world; // declare the world object

        World.resetAvatar(); // reset the avatar from the starting

        String command = terR.drawWord(1, false);

        if (command.equals("n") || command.equals("N")) {
            String inputSeed = terR.drawSeed(GREATESTNUMBEROFSEED, false); // greatest number of seed has 19 digits
            long longSeed = Long.parseLong(inputSeed);
            world = new World(HEIGHT, WIDTH, longSeed);
            Font fontBig = new Font("Monaco", Font.BOLD, FONTSIZE);
            StdDraw.setFont(fontBig);
            gameStart(world);
        } else if (command.equals("l") || command.equals("L")) {
            world = World.load();
            Font fontBig = new Font("Monaco", Font.BOLD, FONTSIZE);
            StdDraw.setFont(fontBig);
            gameStart(world);
        } else {
            System.exit(0);
        }
    }

    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     *
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     *
     * In other words, running both of these:
     *   - interactWithInputString("n123sss:q")
     *   - interactWithInputString("lww")
     *
     * should yield the exact same world state as:
     *   - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] interactWithInputString(String input) {
        int i = 0;
        int digitFinalIndex = 0;
        int digitStartIndex = 0;
        ter.initialize(WIDTH, HEIGHT);
        World world = null;
        String seedStr = "";
        char prev = ' ';

        while (i < input.length()) {
            char c = input.charAt(i);
            if (c == 'n' || c == 'N') {
                digitStartIndex = i;
                digitFinalIndex = digitStartIndex + 1;
            } else if (Character.isDigit(c) && i <= digitFinalIndex && i > digitStartIndex) {
                seedStr += c;
                digitFinalIndex++;
            } else if ((c == 's' || c == 'S') &&  i > digitStartIndex && i <= digitFinalIndex) {
                digitFinalIndex = i;
                long mySeed = Math.floorMod(Long.parseLong(seedStr), NUMBER);
                world = new World(HEIGHT, WIDTH, mySeed);
            } else if (c == 'l' || c == 'L') {
                world = World.load();
                digitFinalIndex = i;
            } else if (prev == ':' && (c == 'q' || c == 'Q') && world != null) {
                world.save();
                break;
            } else if (c == 'W' || c == 'w' && world != null) {
                world.up();
            } else if (c == 'S' || c == 's' && world != null) {
                world.down();
            } else if (c == 'A' || c == 'a' && world != null) {
                world.left();
            } else if (c == 'D' || c == 'd' && world != null) {
                world.right();
            }
            prev = c;
            i++;
        }

        if (world != null) {
            TETile[][] finalWorldFrame = world.allVisualize();
            return finalWorldFrame;
        } else {
            return null;
        }
    }



}
