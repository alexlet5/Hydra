package com.alex_let.hydra;

import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame
{
    private final int WIDTH = Screen.getWidth();
    private final int HEIGHT = Screen.getHeight();
    private com.alex_let.hydra.Menu menu;
    private Board board;
    //private GameOver gameOver;
    public GameFrame()
    {
        super("Hydra");
        //setSize(WIDTH, HEIGHT);
        //setBounds(10, 10, WIDTH, HEIGHT);
        setResizable(false);
        //setMinimumSize(new Dimension(WIDTH,HEIGHT));
        //setMaximumSize(new Dimension(WIDTH,HEIGHT));
        //setPreferredSize(new Dimension(WIDTH,HEIGHT));

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initMenu();
        //initBoard(DifficultyEnum.LOW);
    }
    public void initMenu()
    {
        if(board != null)
            remove(board);

        board = null;
        menu = new Menu(this);
        add(menu);
        pack();
        setVisible(true);
    }

    public void initBoard(DifficultyEnum difficulty)
    {
        if(menu != null)
            remove(menu);

        menu = null;
        board = new Board(difficulty,this);
        add(board);
        pack();
        board.requestFocus();
        setVisible(true);
    }
    @Override
    public Dimension getPreferredSize()
    {
        return new Dimension(WIDTH+17,HEIGHT+40);
    }
}
