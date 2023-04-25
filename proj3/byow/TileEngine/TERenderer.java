package byow.TileEngine;

import byow.Core.worldMap.World;
import edu.princeton.cs.algs4.StdDraw;

import java.awt.Color;
import java.awt.Font;

/**
 * Utility class for rendering tiles. You do not need to modify this file. You're welcome
 * to, but be careful. We strongly recommend getting everything else working before
 * messing with this renderer, unless you're trying to do something fancy like
 * allowing scrolling of the screen or tracking the avatar or something similar.
 */
public class TERenderer {
    private static final int TILE_SIZE = 16;
    private int width;
    private int height;
    private int xOffset;
    private int yOffset;

    /**
     * Same functionality as the other initialization method. The only difference is that the xOff
     * and yOff parameters will change where the renderFrame method starts drawing. For example,
     * if you select w = 60, h = 30, xOff = 3, yOff = 4 and then call renderFrame with a
     * TETile[50][25] array, the renderer will leave 3 tiles blank on the left, 7 tiles blank
     * on the right, 4 tiles blank on the bottom, and 1 tile blank on the top.
     * @param w width of the window in tiles
     * @param h height of the window in tiles.
     */
    public void initialize(int w, int h, int xOff, int yOff) {
        this.width = w;
        this.height = h;
        this.xOffset = xOff;
        this.yOffset = yOff;
        StdDraw.setCanvasSize(width * TILE_SIZE, height * TILE_SIZE);
        Font font = new Font("Monaco", Font.BOLD, TILE_SIZE - 2);
        StdDraw.setFont(font);      
        StdDraw.setXscale(0, width);
        StdDraw.setYscale(0, height);

        StdDraw.clear(new Color(0, 0, 0));

        StdDraw.enableDoubleBuffering();
        StdDraw.show();
    }

    /**
     * Initializes StdDraw parameters and launches the StdDraw window. w and h are the
     * width and height of the world in number of tiles. If the TETile[][] array that you
     * pass to renderFrame is smaller than this, then extra blank space will be left
     * on the right and top edges of the frame. For example, if you select w = 60 and
     * h = 30, this method will create a 60 tile wide by 30 tile tall window. If
     * you then subsequently call renderFrame with a TETile[50][25] array, it will
     * leave 10 tiles blank on the right side and 5 tiles blank on the top side. If
     * you want to leave extra space on the left or bottom instead, use the other
     * initializatiom method.
     * @param w width of the window in tiles
     * @param h height of the window in tiles.
     */
    public void initialize(int w, int h) {
        initialize(w, h, 0, 0);
    }

    /**
     * Takes in a 2d array of TETile objects and renders the 2d array to the screen, starting from
     * xOffset and yOffset.
     *
     * If the array is an NxM array, then the element displayed at positions would be as follows,
     * given in units of tiles.
     *
     *              positions   xOffset |xOffset+1|xOffset+2| .... |xOffset+world.length
     *                     
     * startY+world[0].length   [0][M-1] | [1][M-1] | [2][M-1] | .... | [N-1][M-1]
     *                    ...    ......  |  ......  |  ......  | .... | ......
     *               startY+2    [0][2]  |  [1][2]  |  [2][2]  | .... | [N-1][2]
     *               startY+1    [0][1]  |  [1][1]  |  [2][1]  | .... | [N-1][1]
     *                 startY    [0][0]  |  [1][0]  |  [2][0]  | .... | [N-1][0]
     *
     * By varying xOffset, yOffset, and the size of the screen when initialized, you can leave
     * empty space in different places to leave room for other information, such as a GUI.
     * This method assumes that the xScale and yScale have been set such that the max x
     * value is the width of the screen in tiles, and the max y value is the height of
     * the screen in tiles.
     * @param world the 2D TETile[][] array to render
     */
    public void renderFrame(TETile[][] world) {
        int numXTiles = world.length;
        int numYTiles = world[0].length;
        StdDraw.clear(new Color(0, 0, 0));
        for (int x = 0; x < numXTiles; x += 1) {
            for (int y = 0; y < numYTiles; y += 1) {
                if (world[x][y] == null) {
                    throw new IllegalArgumentException("Tile at position x=" + x + ", y=" + y
                            + " is null.");
                }
                world[x][y].draw(x + xOffset, y + yOffset);
            }
        }
        StdDraw.show();
        StdDraw.pause(200);
    }






    public void renderMainMenu(String s, boolean gameOver) {

        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        Font fontBig = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(fontBig);
        StdDraw.text(this.width / 2, this.height / 2, s);

        if (!gameOver) {
            StdDraw.setFont(fontBig);
            StdDraw.text(this.width - (this.width - 10), this.height - 5, "New Game (N)");
            StdDraw.text((this.width / 2) - 10, this.height - 5, "Load Game (L)");
            StdDraw.text((this.width / 2) + 10, this.height - 5, "Option (O)");
            StdDraw.text(this.width - 10, this.height - 5, "Quit (Q)");
        }

        StdDraw.show();
    }



    public void renderOption(int avatarNum, boolean goBack) {

        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        Font fontBig = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(fontBig);

        if (avatarNum == 0) {
            // avatar 0
            StdDraw.text(this.width / 2, this.height - 5, "A");
        } else if (avatarNum == 1) {
            // avatar 1
            StdDraw.text(this.width / 2, this.height - 5, "B");
        } else if (avatarNum == 2) {
            // avatar 2
            StdDraw.text(this.width / 2, this.height - 5, "C");
        } else {
            // avatar 3
            StdDraw.text(this.width / 2, this.height - 5, "D");
        }

        if (!goBack) {
            StdDraw.setFont(fontBig);
            StdDraw.text(this.width / 2, this.height / 2, "Back (B)");
        }

        StdDraw.show();
    }



    public void drawOption() {

        boolean goBack = false;

        int avatarNum = 0;

        while (!goBack) {

            if (StdDraw.hasNextKeyTyped()) {

                char c = StdDraw.nextKeyTyped();

                if (c == 'c' || c == 'C') { // keep change the avatar
                    if (avatarNum == 3) {
                        avatarNum = 0;
                    } else {
                        avatarNum++;
                    }
                    // change avatar
                } else if (c == 'b' || c == 'B') { // go back to the main menu
                    goBack = true;
                }
            }

            renderOption(avatarNum, false);
            // render the avatar
        }


    }



    public String drawWord(int n, boolean gameOver) {


        int cnt = 0;

        StringBuilder sb = new StringBuilder();

        while (cnt < n) {
            if (StdDraw.hasNextKeyTyped()) {
                char c = StdDraw.nextKeyTyped();

                if (c == 'n' || c == 'N' || c == 'l' || c == 'L' || c == 'q' || c == 'Q') {
                    sb.append(c);
                    cnt++;
                } else if (c == 'o' || c == 'O') {
                    drawOption();
                }
            }
            renderMainMenu(sb.toString(), gameOver);
            StdDraw.pause(200);
        }

        return sb.toString();
    }








    public void renderStartMenu(String s, boolean gameOver) {

        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        Font fontBig = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(fontBig);
        StdDraw.text(this.width / 2, this.height / 2, s);

        if (!gameOver) {
            StdDraw.setFont(fontBig);
            StdDraw.text(this.width / 2, this.height - 5, "Type the Seed and Press (S) to Start");
        }

        StdDraw.show();
    }




    public String drawSeed(int n, boolean gameOver) {

        int cnt = 0;
        StringBuilder sb = new StringBuilder();

        while (cnt < n) {
            if (StdDraw.hasNextKeyTyped()) {
                char c = StdDraw.nextKeyTyped();

                if (c >= '0' && c <= '9' && cnt <= 18) {
                    sb.append(c);
                    cnt++;
                } else if (cnt < 19 && (c == 's' || c == 'S')) {
                    cnt = n;
                } else if (cnt == 19 && (c == 's' || c == 'S')) {
                    cnt++;
                }
            }

            renderStartMenu(sb.toString(), gameOver);
            StdDraw.pause(200);
        }

        return sb.toString();
    }


    public void renderBlockType(String s) {

        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        Font fontBig = new Font("Monaco", Font.BOLD, 20);
        StdDraw.setFont(fontBig);
        StdDraw.text(1, this.height - 1, s);

        StdDraw.show();
    }


    public void drawBlockType(String s, boolean gameOver) {

        while (!gameOver) {

            if (!s.equals("hello")) {
                renderBlockType(s);
            } else {
                gameOver = true;
            }

        }

    }



}
