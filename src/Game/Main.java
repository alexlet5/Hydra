package Game;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Objects;

//добавить имена и рекорды
class Main
{
    public static void main(String[] args)
    {
        Menu menu = new Menu();
    }
}

class Menu extends JFrame
{
    private final int WIDTH = 815;
    private final int HEIGHT = 840;
    private final String[] diffList = {"Easy","Normal","Hard"};
    int difficulty = 1;
    Menu()
    {
        super("Hydra");
        ActionListener menuActionListener = new MenuActionListener();
        ItemListener difficultyListener = new DifficultyChangeListener();
        //Board menuBoard = new Board();

        JButton startButton = new JButton("Start game");
        startButton.setBounds(40, 190, 120, 20);
        startButton.addActionListener(menuActionListener);
        startButton.setActionCommand("Start game");
        add(startButton);

        JButton exitButton = new JButton("Exit game");
        exitButton.setBounds(WIDTH - 200, 190, 120, 20);
        exitButton.addActionListener(menuActionListener);
        exitButton.setActionCommand("Exit game");
        add(exitButton);

        JComboBox difficultyBox = new JComboBox(diffList);
        difficultyBox.setBounds((WIDTH - 120) / 2, 190, 120, 20);
        difficultyBox.addItemListener(difficultyListener);
        add(difficultyBox);

        JLabel highScoreLabel = new JLabel("High scores: ");
        highScoreLabel.setBounds((WIDTH - 100) / 2, 350, 120, 20);
        add(highScoreLabel);

        try
        {
            ImageIcon iim = new ImageIcon("src/resources/menuImage.png");
            Image menuImage = iim.getImage();
            JLabel MenuImage = new JLabel(iim);
            add(MenuImage);
            MenuImage.setBounds((WIDTH - 120) / 2, 0, 120, 80);
        } catch (Exception e)
        {
            System.err.println(e);
        }


        setSize(WIDTH, HEIGHT);
        setLayout(null);
        setVisible(true);
        setResizable(false);
        setFocusable(true);
        requestFocusInWindow();
        Container c = getContentPane();
        c.setBackground(Color.gray);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    void start()
    {
        Board board = new Board(WIDTH,HEIGHT,difficulty);
        add(board);
        board.setFocusable(true);
        board.requestFocusInWindow();
    }


    class MenuActionListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            String command = e.getActionCommand();
            if(Objects.equals(command, "Start game")){start();}
            if(Objects.equals(command, "Exit game")){System.exit(0);}
            //if(command == "Game over"){toggleVisibility();}


        }
    }

    class DifficultyChangeListener implements ItemListener
    {
        @Override
        public void itemStateChanged(ItemEvent e)
        {
            if (e.getItem()=="Easy") { difficulty = 1;}
            if (e.getItem()=="Normal") { difficulty = 2;}
            if (e.getItem()=="Hard") { difficulty = 3;}
        }

        public int getDifficulty()
        {
            return difficulty;
        }
    }

}

