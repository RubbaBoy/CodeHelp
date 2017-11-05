package com.uddernetworks.codehelp;

import javax.swing.border.Border;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CustomBorder implements Border {

    public final static int SIDE_TOP = 0;
    public final static int SIDE_BOTTOM = 1;
    public final static int SIDE_RIGHT = 2;
    public final static int SIDE_LEFT = 3;

    private Color color;
    private int thickness;
    private int[] sides;

    public CustomBorder(Color color, int thickness, int... sides) {
        this.color = color;
        this.thickness = thickness;
        this.sides = sides;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        for (int side : sides) {
            switch (side) {
                case SIDE_TOP:

                    break;
                case SIDE_BOTTOM:

                    break;
                case SIDE_RIGHT:

                    break;
                case SIDE_LEFT:

                    break;
            }
        }
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return null;
    }

    @Override
    public boolean isBorderOpaque() {
        return false;
    }
}
