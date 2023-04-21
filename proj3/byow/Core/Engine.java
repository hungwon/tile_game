package byow.Core;

import byow.Core.UI.UserInterface;
import byow.Core.worldMap.World;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import edu.princeton.cs.algs4.StdDraw;

import java.awt.*;

public class Engine {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;
    public static final int NUMBER = 10000000;

    public boolean gameOver = false;






    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */



    public void gameStart(World world) {

        boolean gameOver = false;
        int cnt = 0;

        char prev = ' ';

        while (!gameOver) {

            if (StdDraw.hasNextKeyTyped()) {
                char c = StdDraw.nextKeyTyped();


                if (prev == ':' && (c == 'q' || c == 'Q')) {
                    world.save();
                    gameOver = true;
                    System.exit(0);
                }

                if (c == 'W' || c == 'w') {
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

                }

                prev = c;
            }

            if (cnt == 100000) {
                gameOver = true;
            }
        }



    }




    public void interactWithKeyboard() {

        TERenderer ter = new TERenderer();
        ter.initialize(80, 30);

        World world; // declare the world object

        String command = ter.drawWord(1, false);

        if (command.equals("n") || command.equals("N")) {

            String inputSeed = ter.drawSeed(19, false); // greatest number of seed has 19 digits

            long longSeed = Long.parseLong(inputSeed);
            world = new World(30, 80, longSeed);
            TETile[][] testWorld = world.visualize();
            ter.renderFrame(testWorld);

            gameStart(world);




        } else if (command.equals("l") || command.equals("L")) {

            world = World.load();
            TETile[][] testWorld = world.visualize();
            ter.renderFrame(testWorld);
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
        // passed in as an argument, and return a 2D tile representation of the
        // world that would have been drawn if the same inputs had been given
        // to interactWithKeyboard().
        //
        // See proj3.byow.InputDemo for a demo of how you can make a nice clean interface
        // that works for many different input types.

        String strSeed = "";

        for (int i = 0; i < input.length(); i++) {
            if (i > 0 && i < input.length() - 1) {
                strSeed += input.charAt(i);
            }
        }

        long intSeed = Math.floorMod(Long.parseLong(strSeed), NUMBER);

        World world = new World(HEIGHT, WIDTH, intSeed);

        ter.initialize(world.getWorldWidth(), world.getWorldHeight());

        TETile[][] finalWorldFrame = world.visualize();
        return finalWorldFrame;
    }
}
