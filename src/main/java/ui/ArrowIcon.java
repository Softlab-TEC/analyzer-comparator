package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Path2D;

public class ArrowIcon implements Icon {
    private final int width;
    private final int height;

    public ArrowIcon(int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        Graphics2D g2d = (Graphics2D) g.create();

        // Set color and adjust position as needed
        g2d.setColor(Color.BLACK);
        g2d.translate(x, y);

        // Draw a right arrow
        Path2D path = new Path2D.Double();
        path.moveTo(0, height / 2.0);
        path.lineTo(width, 0);
        path.lineTo(width, height);
        path.closePath();

        g2d.fill(path);
        g2d.dispose();
    }

    @Override
    public int getIconWidth() {
        return width;
    }

    @Override
    public int getIconHeight() {
        return height;
    }
}
