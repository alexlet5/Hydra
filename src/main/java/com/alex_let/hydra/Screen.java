package com.alex_let.hydra;

import java.awt.*;

public class Screen
{
    private static final int pixelSize = 40;
    private static final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private static final int width = (int) screenSize.getWidth();
    private static final int height = (int) screenSize.getHeight();

    public static int getWidth()
    {
        return width/3 + (40 - ((width/3) % 40));
    }

    public static int getHeight()
    {
        return height/2 + (40 - ((height/2) % 40));
    }

    public static int getPixelSize()
    {
        return pixelSize;
    }
}
