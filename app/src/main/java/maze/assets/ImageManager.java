package maze.assets;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

public class ImageManager {  
    private static final Map<String, BufferedImage> imageCache = new HashMap<>();
    private static final Map<String, Sprite> spriteCache = new HashMap<>();

    public static final Sprite DUNGEON_SPRITE = ImageManager.loadImageAsSprite("/Dungeon_Tileset.png", 16, 16);
    public static final Sprite PLAYER_IDLE_SPRITE = ImageManager.loadImageAsSprite("/idle_down.png", 48, 64);

    public static BufferedImage loadImage(String path) {
        if(imageCache.containsKey(path)) return imageCache.get(path);
        
        try {
            return ImageIO.read(ImageManager.class.getResourceAsStream(path));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Sprite loadImageAsSprite(String path, int cellWidth, int cellHeight) {
        if(spriteCache.containsKey(path)) return spriteCache.get(path);
        return new Sprite(loadImage(path), cellWidth, cellHeight);
    }
}
