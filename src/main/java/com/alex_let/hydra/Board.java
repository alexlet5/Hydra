package com.alex_let.hydra;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Objects;


public class Board extends JPanel implements ActionListener
{
    //player stops and becomes invincible
    private final boolean DEBUG = false;
    //tick time
    private static final int DELAY = 400;

    private final GameFrame gameFrame;

    private static DirectionsEnum direction = DirectionsEnum.Right;
    private static Hydra[][] hydra;
    private static int SCORE;
    private final int WIDTH = Screen.getWidth();
    private final int HEIGHT = Screen.getHeight();
    private final int PIXEL_SIZE = Screen.getPixelSize();
    private final int X = WIDTH/PIXEL_SIZE;
    private final int Y = HEIGHT/PIXEL_SIZE;
    private boolean inGame = true;
    private int mouseX = 200;
    private int mouseY = 200;
    private ImageIcon body;
    private ImageIcon head;
    private ImageIcon hole;
    private ImageIcon mouse;
    private ImageIcon dead;

    public Board(GameFrame gameFrame)
    {
        this.gameFrame = gameFrame;
        addKeyListener(new KbAdapter());
        setBackground(Color.gray);
        loadImages();
        initialize();
    }

    private void initialize()
    {
        SCORE = 0;
        spawnHydra();
        Timer timer = new Timer(DELAY, this);
        timer.start();
    }

    private void spawnHydra()
    {
        hydra = new Hydra[X][Y];
        for (int i = 0; i < X; i++)
        {
            for (int j = 0; j < Y; j++)
            {
                hydra[i][j] = new Hydra();
            }
        }
        hydraRandomSpawner();
    }

    private void hydraRandomSpawner()
    {
        int d = (int) (Math.random()*4);
        switch (d)
        {
            case (0):
                hydra[0][0].spawn();
                break;
            case (1):
                hydra[X-1][0].spawn();
                break;
            case (2):
                hydra[0][Y-1].spawn();
                break;
            case (3):
                hydra[X-1][Y-1].spawn();
                break;
        }
    }

    private void checkHeads()
    {
        for (int i = 0; i < X; i++)
        {
            for (int j = 0; j < Y; j++)
            {
                if (hydra[i][j].isHead())
                {
                    return;
                }
            }
        }
        makeHead();
    }


    private void makeHead()
    {
        for (int i = 0; i < X; i++)
        {
            for (int j = 0; j < Y; j++)
            {
                if (hydra[i][j].isSomething())
                {
                    if (!Hopeless(i, j))
                    {
                        while (true)
                        {
                            int d = (int) (Math.random()*4);
                            try
                            {
                                switch (d)
                                {
                                    case 0:
                                        hydra[i + 1][j].makeHeadIfEmpty();
                                            return;
                                    case 1:
                                        hydra[i - 1][j].makeHead();
                                            return;
                                    case 2:
                                        hydra[i][j + 1].makeHead();
                                            return;
                                    case 3:
                                        hydra[i][j - 1].makeHead();
                                        return;
                                }
                            } catch (ArrayIndexOutOfBoundsException ignored)
                            {
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        doDrawing(g);
    }

    private void checkCollision()
    {
        if ((mouseX >= WIDTH - PIXEL_SIZE) || (mouseX <= 0) || (mouseY >= HEIGHT - PIXEL_SIZE) || (mouseY <= 0))
        {
            inGame = false;
        } else if (hydra[mouseX/PIXEL_SIZE][mouseY/PIXEL_SIZE].isSomething())
        {
            inGame = false;
        }
    }

    private void doDrawing(Graphics g)
    {
        if (inGame)
        {
            for (int i = 0; i <= HEIGHT; i += PIXEL_SIZE)//horizontal
            {
                g.drawLine(0, i, WIDTH, i);
            }

            for (int i = 0; i <= WIDTH; i += PIXEL_SIZE) //vertical
            {
                g.drawLine(i, 0, i, HEIGHT);
            }

            g.drawImage(mouse.getImage(), mouseX, mouseY, this);

            for (int i = 0; i < X; i++)
            {
                for (int j = 0; j < Y; j++)
                {
                    if (hydra[i][j].isHole())
                    {
                        g.drawImage(hole.getImage(), i*PIXEL_SIZE, j*PIXEL_SIZE, this);
                    }
                    if (hydra[i][j].isBody())
                    {
                        g.drawImage(body.getImage(), i*PIXEL_SIZE, j*PIXEL_SIZE, this);
                    }
                    if (hydra[i][j].isHead())
                    {
                        g.drawImage(head.getImage(), i*PIXEL_SIZE, j*PIXEL_SIZE, this);
                    }
                    if (hydra[i][j].isDead())
                    {
                        g.drawImage(dead.getImage(), i*PIXEL_SIZE, j*PIXEL_SIZE, this);
                    }
                }
            }
            Toolkit.getDefaultToolkit().sync();
        } else
        {
            gameOver(g);
        }
    }

    private void gameOver(Graphics g)
    {
        g.setColor(Color.white);
        Font small = new Font("Comic Sans", Font.BOLD, 14);
        g.setFont(small);
        FontMetrics metr = getFontMetrics(small);

        String msg = "Game Over";
        g.drawString(msg, (WIDTH - metr.stringWidth(msg))/2, HEIGHT/2);

        msg = Integer.toString(SCORE); //difficulty
        g.drawString(msg , (WIDTH - metr.stringWidth(msg))/2, (int) (HEIGHT/(1.5)));

        JButton exitButton = new JButton("Main menu");
        exitButton.setBounds((WIDTH - 120)/2, HEIGHT - 300, 120, 20);
        exitButton.setActionCommand("Main menu");

        exitButton.addActionListener(e ->
        {
            if (Objects.equals(e.getActionCommand(), "Main menu"))
            {
                gameFrame.initMenu();
            }
        });
        add(exitButton);
    }

    private void loadImages()
    {
        try
        {
            mouse = new ImageIcon("src/main/resources/mouse.png");
            hole = new ImageIcon("src/main/resources/hole.png");
            body = new ImageIcon("src/main/resources/body.png");
            head = new ImageIcon("src/main/resources/head.png");
            dead = new ImageIcon("src/main/resources/dead.png");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    @Override
    public void actionPerformed(ActionEvent e)
    {
        if (inGame)
        {
            if (!DEBUG)
                checkCollision();

            checkHeads();

            if (!DEBUG)
                move();
            hydraMove();
        }
        repaint();
    }

    private void hydraMove()
    {
        for (int i = 0; i < X; i++)
        {
            for (int j = 0; j < Y; j++)
            {
                if (hydra[i][j].isHead())
                {
                    if (Hopeless(i, j))
                    {
                        System.err.println("DEAD");
                        hydra[i][j].toDead();
                        return;
                    }
                    else
                    {
                        while (true)
                        {
                            try
                            {
                                int dir = (int) (Math.random()*1000 %4);
                                //System.out.println(dir);
                                switch (dir)
                                {
                                    default:
                                        if (!hydra[i + 1][j].isBody())
                                        {
                                            hydra[i + 1][j].makeHead();
                                            hydra[i][j].toBody();
                                            return;
                                        }
                                    case 1:
                                        if (!hydra[i - 1][j].isBody())
                                        {
                                            hydra[i - 1][j].makeHead();
                                            hydra[i][j].toBody();
                                            return;
                                        }
                                    case 2:
                                        if (!hydra[i][j + 1].isBody())
                                        {
                                            hydra[i][j + 1].makeHead();
                                            hydra[i][j].toBody();
                                            return;
                                        }
                                    case 3:
                                        if (!hydra[i][j - 1].isBody())
                                        {
                                            hydra[i][j - 1].makeHead();
                                            hydra[i][j].toBody();
                                            return;
                                        }
                                }
                            } catch (ArrayIndexOutOfBoundsException ignored)
                            {
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean Hopeless(int i, int j)
    {
        try
        {
            if (i == X - 1 || hydra[i + 1][j].isSomething())//справа
            {
                if (i == 0 || hydra[i - 1][j].isSomething())//слева
                {
                    if (j == Y - 1 || hydra[i][j + 1].isSomething())//снизу
                    {
                        if (j == 0 || hydra[i][j - 1].isSomething())//сверху
                        {
                            return true;
                        }
                    }
                }
            }
        } catch (ArrayIndexOutOfBoundsException ignored)
        {
        }
        return false;
    }

    private void move()
    {
        SCORE++;
        switch (direction)
        {
            case Right:
            {
                mouseX += PIXEL_SIZE;
                break;
            }

            case Left:
            {
                mouseX -= PIXEL_SIZE;
                break;
            }

            case Up:
            {
                mouseY -= PIXEL_SIZE;
                break;
            }

            case Down:
            {
                mouseY += PIXEL_SIZE;
                break;
            }
        }
    }


    private static class KbAdapter extends KeyAdapter
    {
        @Override
        public void keyPressed(KeyEvent e)
        {
            int key = e.getKeyCode();
            //System.out.println(key);
            switch (key)
            {
                case KeyEvent.VK_A:
                {
                    direction = DirectionsEnum.Left;
                    return;
                }

                case KeyEvent.VK_D:
                {
                    direction = DirectionsEnum.Right;
                    return;
                }

                case KeyEvent.VK_W:
                {
                    direction = DirectionsEnum.Up;
                    return;
                }

                case KeyEvent.VK_S:
                {
                    direction = DirectionsEnum.Down;
                }
            }
        }
    }
}