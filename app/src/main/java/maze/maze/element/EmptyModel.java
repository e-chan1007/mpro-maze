package maze.maze.element;

import java.awt.Color;
import java.awt.Graphics;

public class EmptyModel extends MazeElement {
    @Override
    public void draw(Graphics g, int x, int y, int size) {
        g.setColor(new Color(0x25, 0x13, 0x1A));
        g.fillRect(x, y, size, size);
    }
    
}
