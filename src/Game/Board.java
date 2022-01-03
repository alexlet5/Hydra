package Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Objects;


public class Board extends JPanel implements ActionListener
{
    private final int WIDTH = 615;
    private final int HEIGHT = 640;
    private final boolean DEBUG = false;
    private static final int DELAY = 300;
    private final int SQUARESIZE = 20;
    private final int PIXEL_SIZE = 40;
    private static diffList difficulty;
    private boolean leftDirection = false;
    private boolean rightDirection = true;
    private boolean upDirection = false;
    private boolean downDirection = false;
    private boolean inGame = true;
    private int mouse_x = 200;
    private int mouse_y = 200;

    private ImageIcon body;
    private ImageIcon head;
    private ImageIcon hole;
    private ImageIcon mouse;
    private ImageIcon bullet;
    private ImageIcon dead;

    private static Hydra[][] h;

    static int SCORE;
    GameFrame gameFrame;
    public Board(diffList difficulty,GameFrame gf)
    {
        gameFrame = gf;
        requestFocusInWindow();
        setSize(WIDTH, HEIGHT);
        setLayout(null);
        setVisible(true);
        addKeyListener(new KbAdapter());
        setBounds(0, 0, WIDTH, HEIGHT);
        setBackground(Color.gray);
        setFocusable(true);
        loadImages();
        initialize();
    }


    public void changeDifficulty(diffList diff)
    {
        difficulty = diff;
    }

    private void initialize()
    {
        SCORE = 0;
        spawnHydra();
        Timer timer = new Timer(DELAY, this);
        timer.start();
    }

    private static void spawnHydra()
    {
        h = new Hydra[20][20];
        for (int i = 0; i < 20; i++)
        {
            for (int j = 0; j < 20; j++)
            {
                h[i][j] = new Hydra();
            }
        }
        h[0][0].spawn();
    }

    private void checkHeads()
    {
        for (int i = 0; i < 20; i++)
        {
            for (int j = 0; j < 20; j++)
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
        for (int i = 0; i < 20; i++)
        {
            for (int j = 0; j < 20; j++)
            {
                if (!Hopeless(i, j))
                {
                    while (true)
                    {
                        int d = (int) (Math.random() * 4);
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
    public void paintComponent(Graphics g) //paintcomponent ????
    {
        super.paintComponent(g);

        doDrawing(g);
    }

    private void checkCollision()
    {
        if ((mouse_x >= WIDTH - PIXEL_SIZE * 2) || (mouse_x <= 0) || (mouse_y >= HEIGHT - PIXEL_SIZE * 2) || (mouse_y <= 0))
        {
            inGame = false;
        }
        if (h[mouse_x / PIXEL_SIZE][mouse_y / PIXEL_SIZE].isSomething())
        {
            inGame = false;
        }
        if (h[mouse_x / PIXEL_SIZE][mouse_y / PIXEL_SIZE].isBullet())
        {
            inGame = false;
        }
    }


    private void doDrawing(Graphics g)
    {
        if (inGame)
        {
            for (int i = 0; i < WIDTH; i += PIXEL_SIZE)//ВНИЗ
            {
                g.drawLine(0, i, WIDTH, i);
            }
            for (int i = 0; i < HEIGHT; i += PIXEL_SIZE)
            {
                g.drawLine(i, 0, i, HEIGHT);
            }

            g.drawImage(mouse.getImage(), mouse_x, mouse_y, this);


            for (int i = 0; i < 20; i++)
            {
                for (int j = 0; j < 20; j++)
                {
                    if (h[i][j].getType() == 's')
                    {
                        g.drawImage(hole.getImage(), i * PIXEL_SIZE, j * PIXEL_SIZE, this);
                    }
                    if (h[i][j].getType() == 'b')
                    {
                        g.drawImage(body.getImage(), i * PIXEL_SIZE, j * PIXEL_SIZE, this);
                    }
                    if (h[i][j].getType() == 'h')
                    {
                        g.drawImage(head.getImage(), i * PIXEL_SIZE, j * PIXEL_SIZE, this);
                    }
                    if (h[i][j].getType() == 'd')
                    {
                        g.drawImage(dead.getImage(), i * PIXEL_SIZE, j * PIXEL_SIZE, this);
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
        g.drawString(msg, (WIDTH - metr.stringWidth(msg)) / 2, HEIGHT / 2);
        msg = Integer.toString(SCORE); //* difficulty
        g.drawString(msg, (WIDTH - metr.stringWidth(msg)) / 2, (int) (HEIGHT / (1.5)));
        JButton exitButton = new JButton("Main menu");
        exitButton.setBounds((WIDTH-120)/2, HEIGHT-300, 120, 20);
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
            if (!DEBUG) checkCollision();
            checkHeads();


            if (!DEBUG) move();
            hydraMove();
        }
        repaint();
    }

    private void hydraMove()
    {
        for (int i = 0; i < 20; i++)
        {
            for (int j = 0; j < 20; j++)
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
                                int d = (int) (Math.random() * 4);
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
            if (i == 19 || h[i + 1][j].isSomething())//справа
            {
                if (i == 0 || h[i - 1][j].isSomething())//слева
                {
                    if (j == 19 || h[i][j + 1].isSomething())//снизу
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
        if (leftDirection)
        {
            mouse_x -= PIXEL_SIZE;
        }

        if (rightDirection)
        {
            mouse_x += PIXEL_SIZE;
        }

        if (upDirection)
        {
            mouse_y -= PIXEL_SIZE;
        }

        if (downDirection)
        {
            mouse_y += PIXEL_SIZE;
        }
    }


    private class KbAdapter extends KeyAdapter
    {
        @Override
        public void keyPressed(KeyEvent e)
        {
            int key = e.getKeyCode();
            System.out.println(key);

            if ((key == KeyEvent.VK_A))
            {
                leftDirection = true;
                upDirection = false;
                downDirection = false;
                rightDirection = false;
            }

            if ((key == KeyEvent.VK_D))
            {
                rightDirection = true;
                upDirection = false;
                downDirection = false;
                leftDirection = false;
            }

            if ((key == KeyEvent.VK_W))
            {
                upDirection = true;
                rightDirection = false;
                leftDirection = false;
                downDirection = false;
            }

            if ((key == KeyEvent.VK_S))
            {
                downDirection = true;
                rightDirection = false;
                leftDirection = false;
                upDirection = false;
            }

        }
    }
}