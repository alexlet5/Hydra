package Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;




public class Board extends JFrame
{
    private final int WIDTH = 800;
    private final int HEIGHT = 600;
    private final int PIXEL_SIZE = 20;
    private final int DELAY = 100;

    private boolean leftDirection = false;
    private boolean rightDirection = true;
    private boolean upDirection = false;
    private boolean downDirection = false;
    private boolean inGame = true;

    private Image mouse;

    private Timer timer;

    int difficulty;

    public Board(int diff)
    {
        setSize(WIDTH, HEIGHT);
        setLayout(null);
        setVisible(true);
        setResizable(false);
        Panel p = new Panel(diff);
        add(p);
    }

    public class Panel extends JPanel implements ActionListener
    {
        public Panel(int d) //добавить имена
        {
            difficulty = d;
            System.out.println(d);
            initialize();
        }

        private void initialize()
        {
            KbAdapter kb = new KbAdapter();
            setBounds(0,0,WIDTH, HEIGHT);
            setBackground(Color.black);
            loadImages();
            //спавн игрока и гидры
            timer = new Timer(DELAY, this);
            timer.start();
            repaint();
        }

        private void loadImages()
        {
            try
            {
                ImageIcon iim = new ImageIcon("src/resources/apple.png");
                mouse = iim.getImage();
            } catch (Exception e)
            {
                System.err.println(e);
            }
        }

        @Override
        public void paint(Graphics g)
        {
            super.paint(g);
            g.drawLine(200, 100, 300, 400);
        }

        private class KbAdapter extends KeyAdapter
        {
            @Override
            public void keyPressed(KeyEvent e)
            {
                int key = e.getKeyCode();
                if ((key == KeyEvent.VK_LEFT))
                {
                    leftDirection = true;
                    upDirection = false;
                    downDirection = false;
                    rightDirection = false;
                }

                if ((key == KeyEvent.VK_RIGHT))
                {
                    rightDirection = true;
                    upDirection = false;
                    downDirection = false;
                    leftDirection = false;
                }

                if ((key == KeyEvent.VK_UP))
                {
                    upDirection = true;
                    rightDirection = false;
                    leftDirection = false;
                    downDirection = false;
                }

                if ((key == KeyEvent.VK_DOWN))
                {
                    downDirection = true;
                    rightDirection = false;
                    leftDirection = false;
                    upDirection = false;
                }

            }
        }

        @Override
        public void actionPerformed(ActionEvent e)
        {

        }
    }
}