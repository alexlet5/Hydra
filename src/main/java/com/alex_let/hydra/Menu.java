package com.alex_let.hydra;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class Menu extends JPanel
{

    private final GameFrame gameFrame;
    private final int WIDTH = Screen.getWidth();
    private final int HEIGHT = Screen.getHeight();

    Menu(GameFrame gf)
    {
        gameFrame = gf;

        setBackground(Color.gray);
        ActionListener menuActionListener = new MenuActionListener();

        JButton startButton = new JButton("Start game");
        startButton.setBounds(40, 190, 120, 20);
        startButton.addActionListener(menuActionListener);
        startButton.setActionCommand("Start game");
        add(startButton);

        try
        {
            ImageIcon iim = new ImageIcon("src/main/resources/head.png");
            JLabel MenuImage = new JLabel(iim);
            add(MenuImage);
            MenuImage.setBounds((WIDTH - 120) / 2, 0, 120, 80);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        JButton exitButton = new JButton("Exit game");
        exitButton.setBounds(WIDTH - 200, 190, 120, 20);
        exitButton.addActionListener(menuActionListener);
        exitButton.setActionCommand("Exit game");
        add(exitButton);

        /*JComboBox<DifficultyEnum> difficultyBox = new JComboBox<>(DifficultyEnum.values());
        difficultyBox.setBounds((WIDTH - 120) / 2, 190, 120, 20);
        difficultyBox.addItemListener(difficultyListener);
        add(difficultyBox);*/

        /*JLabel highScoreLabel = new JLabel("High scores: ");
        highScoreLabel.setBounds((WIDTH - 100) / 2, 350, 120, 20);
        add(highScoreLabel);*/


    }

    class MenuActionListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            String command = e.getActionCommand();
            if (Objects.equals(command, "Start game"))
            {
                gameFrame.initBoard();
            }
            if (Objects.equals(command, "Exit game"))
            {
                System.exit(0);
            }
        }
    }

}