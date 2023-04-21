package byow.Core.UI;

import byow.TileEngine.TERenderer;
import edu.princeton.cs.algs4.StdDraw;

import java.awt.*;

public class UserInterface {

    private int width;
    private int height;

    private boolean gameOver = false; // if this is true, game is over
    private String gameTitle = "CS61B Farmers"; // title of the game
    private String newGame = "New Game (N)"; // new game
    private String loadGame = "Load Game (N)"; // load game
    private String quitGame = "Quit (Q)"; // quit game


    public UserInterface(int width, int height) {

        this.width = width;
        this.height = height;
    }

}
