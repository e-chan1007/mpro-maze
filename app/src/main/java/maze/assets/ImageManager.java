package maze.assets;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

public class ImageManager {
	private static final Map<String, BufferedImage> imageCache = new HashMap<>();
	private static final Map<String, Sprite> spriteCache = new HashMap<>();

	public static final Sprite DUNGEON_SPRITE = ImageManager.loadImageAsSprite("/Dungeon_Tileset.png", 16, 16);
	public static final Sprite PLAYER_IDLE_UP_SPRITE = ImageManager.loadImageAsSprite("/player/idle/idle_up.png", 48,
			64);
	public static final Sprite PLAYER_IDLE_DOWN_SPRITE = ImageManager.loadImageAsSprite("/player/idle/idle_down.png", 48,
			64);
	public static final Sprite PLAYER_IDLE_LEFT_SPRITE = ImageManager.loadImageAsSprite("/player/idle/idle_left.png", 48,
			64);
	public static final Sprite PLAYER_IDLE_RIGHT_SPRITE = ImageManager.loadImageAsSprite("/player/idle/idle_right.png",
			48, 64);
	public static final Sprite PLAYER_WALKUP_SPRITE = ImageManager.loadImageAsSprite("/player/walk/walk_up.png", 48,
			64);
	public static final Sprite PLAYER_WALKDOWN_SPRITE = ImageManager.loadImageAsSprite("/player/walk/walk_down.png", 48,
			64);
	public static final Sprite PLAYER_WALKLEFT_SPRITE = ImageManager.loadImageAsSprite("/player/walk/walk_left.png", 48,
			64);
	public static final Sprite PLAYER_WALKRIGHT_SPRITE = ImageManager.loadImageAsSprite("/player/walk/walk_right.png",
			48, 64);

	public static final Sprite TAGGER_WALKLEFT_SPRITE = ImageManager.loadImageAsSprite("/tagger/WispLeft.png", 32, 32);
	public static final Sprite TAGGER_WALKRIGHT_SPRITE = ImageManager.loadImageAsSprite("/tagger/WispRight.png", 32, 32);

	public static BufferedImage loadImage(String path) {
		if (imageCache.containsKey(path))
			return imageCache.get(path);

		try {
			return ImageIO.read(ImageManager.class.getResourceAsStream(path));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Sprite loadImageAsSprite(String path, int cellWidth, int cellHeight) {
		if (spriteCache.containsKey(path))
			return spriteCache.get(path);
		return new Sprite(loadImage(path), cellWidth, cellHeight);
	}
}
