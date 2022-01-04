package Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Objects;


public class Board extends JPanel implements ActionListener
{
    private static final int DELAY = 400;
    private static DifficultyEnum difficulty;
    private static DirectionsEnum direction = DirectionsEnum.Right;
    private static Hydra[][] h;
    private static int SCORE;
    private final boolean DEBUG = false;
    private final int WIDTH = Screen.getWidth();
    private final int HEIGHT = Screen.getHeight();
    private final int PIXEL_SIZE = Screen.getPixelSize();
    private final int X = WIDTH/PIXEL_SIZE;
    private final int Y = HEIGHT/PIXEL_SIZE;
    private final GameFrame gameFrame;
    private boolean inGame = true;
    private int mouse_x = 200;
    private int mouse_y = 200;
    private ImageIcon body;
    private ImageIcon head;
    private ImageIcon hole;
    private ImageIcon mouse;
    private ImageIcon bullet;
    private ImageIcon dead;

    public Board(DifficultyEnum difficulty, GameFrame gf)
    {
        gameFrame = gf;
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
        h = new Hydra[X][Y];
        for (int i = 0; i < X; i++)
        {
            for (int j = 0; j < Y; j++)
            {
                h[i][j] = new Hydra();
            }
        }
        h[0][0].spawn();
    }

    private void checkHeads()
    {
        for (int i = 0; i < X; i++)
        {
            for (int j = 0; j < Y; j++)
            {
                if (h[i][j].isHead())
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
                                    if (!h[i + 1][j].isSomething())
                                    {
                                        h[i + 1][j].makeHead();
                                        return;
                                    }
                                case 1:
                                    if (!h[i - 1][j].isSomething())
                                    {
                                        h[i - 1][j].makeHead();
                                        return;
                                    }
                                case 2:
                                    if (!h[i][j + 1].isSomething())
                                    {
                                        h[i][j + 1].makeHead();
                                        return;
                                    }
                                case 3:
                                    if (!h[i + 1][j - 1].isSomething())
                                    {
                                        h[i][j - 1].makeHead();
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

    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        doDrawing(g);
    }

    private void checkCollision()
    {
        if ((mouse_x >= WIDTH - PIXEL_SIZE) || (mouse_x <= 0) || (mouse_y >= HEIGHT - PIXEL_SIZE) || (mouse_y <= 0))
        {
            inGame = false;
        } else if (h[mouse_x/PIXEL_SIZE][mouse_y/PIXEL_SIZE].isSomething())
        {
            inGame = false;
        } else if (h[mouse_x/PIXEL_SIZE][mouse_y/PIXEL_SIZE].isBullet())
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

            g.drawImage(mouse.getImage(), mouse_x, mouse_y, this);

            for (int i = 0; i < X; i++)
            {
                for (int j = 0; j < Y; j++)
                {
                    if (h[i][j].getType() == 's')
                    {
                        g.drawImage(hole.getImage(), i*PIXEL_SIZE, j*PIXEL_SIZE, this);
                    }
                    if (h[i][j].getType() == 'b')
                    {
                        g.drawImage(body.getImage(), i*PIXEL_SIZE, j*PIXEL_SIZE, this);
                    }
                    if (h[i][j].getType() == 'h')
                    {
                        g.drawImage(head.getImage(), i*PIXEL_SIZE, j*PIXEL_SIZE, this);
                    }
                    if (h[i][j].getType() == 'd')
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
        Font small = new Font("Comic Sans", Font.BOLD, 14);
        FontMetrics metr = getFontMetrics(small);
        g.setColor(Color.white);
        g.setFont(small);
        String msg = "Game Over";
        g.drawString(msg, (WIDTH - metr.stringWidth(msg))/2, HEIGHT/2);
        msg = Integer.toString(SCORE); //* difficulty
        g.drawString(msg, (WIDTH - metr.stringWidth(msg))/2, (int) (HEIGHT/(1.5)));
        JButton exitButton = new JButton("Main menu");
        exitButton.setBounds((WIDTH - 120)/2, HEIGHT - 300, 120, 20);
        exitButton.setActionCommand("Main menu");
        exitButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if (Objects.equals(e.getActionCommand(), "Main menu"))
                {
                    gameFrame.initMenu();
                }
            }
        });
        add(exitButton);
    }

    private void loadImages()
    {
        try
        {
            mouse = new ImageIcon("src/resources/mouse.png");
            hole = new ImageIcon("src/resources/hole.png");
            body = new ImageIcon("src/resources/body.png");
            head = new ImageIcon("src/resources/head.png");
            bullet = new ImageIcon("src/resources/bullet.png");
            dead = new ImageIcon("src/resources/dead.png");


        } catch (Exception e)
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
                if (h[i][j].isHead())
                {
                    if (Hopeless(i, j))
                    {
                        System.err.println("DEAD");
                        h[i][j].die();
                        return;
                    } else
                    {
                        while (true)
                        {
                            try
                            {
                                int d = (int) (Math.random()*4);
                                switch (d)
                                {
                                    default:
                                        if (!h[i + 1][j].isBody())
                                        {
                                            h[i + 1][j].makeHead();
                                            h[i][j].toBody();
                                            return;
                                        }
                                    case 1:
                                        if (!h[i - 1][j].isBody())
                                        {
                                            h[i - 1][j].makeHead();
                                            h[i][j].toBody();
                                            return;
                                        }
                                    case 2:
                                        if (!h[i][j + 1].isBody())
                                        {
                                            h[i][j + 1].makeHead();
                                            h[i][j].toBody();
                                            return;
                                        }
                                    case 3:
                                        if (!h[i][j - 1].isBody())
                                        {
                                            h[i][j - 1].makeHead();
                                            h[i][j].toBody();
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
            if (i == X - 1 || h[i + 1][j].isSomething())//справа
            {
                if (i == 0 || h[i - 1][j].isSomething())//слева
                {
                    if (j == Y - 1 || h[i][j + 1].isSomething())//снизу
                    {
                        if (j == 0 || h[i][j - 1].isSomething())//сверху
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
                mouse_x += PIXEL_SIZE;
                break;
            }

            case Left:
            {
                mouse_x -= PIXEL_SIZE;
                break;
            }

            case Up:
            {
                mouse_y -= PIXEL_SIZE;
                break;
            }

            case Down:
            {
                mouse_y += PIXEL_SIZE;
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