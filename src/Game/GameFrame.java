package Game;

import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame
{
    private final int WIDTH = 615;
    private final int HEIGHT = 640;
    private boolean first = true;
    private Menu menu;
    private Board board;
    //private GameOver gameOver;
    public GameFrame()
    {
        super("Hydra");
        setSize(WIDTH, HEIGHT);
        initMenu();
    }
    public void initMenu()
    {
        if(!first)
        {
            remove(board);
            board = null;
        }
        menu = new Menu(this);
        add(menu);
        setVisible(true);
        first = false;
    }

    public void initBoard(diffList difficulty)
    {
        remove(menu);
        menu = null;

        board = new Board(difficulty,this);
        add(board);
        setVisible(true);
    }
}
