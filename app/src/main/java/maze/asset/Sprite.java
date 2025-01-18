package maze.asset;

import java.awt.image.BufferedImage;

public class Sprite {
    private final BufferedImage image;
    private final int cellWidth;
    private final int cellHeight;

    public Sprite(BufferedImage image, int cellWidth, int cellHeight) {
        this.image = image;
        this.cellWidth = cellWidth;
        this.cellHeight = cellHeight;
    }

    public BufferedImage getImageAt(int x, int y) {
        return image.getSubimage(x * cellWidth, y * cellHeight, cellWidth, cellHeight);
    }

    public BufferedImage getRandomImage(int fromX, int fromY, int toX, int toY) {
        int x = fromX + (int) (Math.random() * (toX - fromX));
        int y = fromY + (int) (Math.random() * (toY - fromY));
        return getImageAt(x, y);
    }
}
