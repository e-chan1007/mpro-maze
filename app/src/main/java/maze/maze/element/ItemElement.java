package maze.maze.element;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import maze.asset.ImageManager;
import maze.asset.Sprite;
import maze.maze.MazeModel;
import maze.maze.item.HealPotion;
import maze.maze.item.Item;
import maze.maze.item.SpeedBoost;
import maze.maze.player.PlayerModel;

public class ItemElement extends MazeElement {
    private boolean isItemCollected = false;

    private PlayerModel playerModel;
    private List<Item> items;
    private Item selectedItem;
    private BufferedImage pathSprite;
    private int ovalX, ovalY;

    private static final Random RANDOM = new Random();

    private static final Sprite HEAL_POTION_SPRITE = ImageManager.loadImageAsSprite("/item/potion.png", 64, 64);
    private static final BufferedImage HEAL_POTION_IMAGE = HEAL_POTION_SPRITE.getImageAt(0, 1);
    private static final Sprite SPEED_BOOST_SPRITE = ImageManager.loadImageAsSprite("/item/potion.png", 64, 64);
    private static final BufferedImage SPEED_BOOST_IMAGE = SPEED_BOOST_SPRITE.getImageAt(0, 4);

    private static final BufferedImage INITIAL_IMAGE = ImageManager.DUNGEON_SPRITE.getImageAt(4, 8);
    private static final BufferedImage DONE_IMAGE = ImageManager.DUNGEON_SPRITE.getImageAt(5, 8);

    public ItemElement(MazeModel mazeModel, PlayerModel playerModel) {
        this.playerModel = playerModel;
        this.items = new ArrayList<>();
        items.add(new HealPotion(1));
        items.add(new SpeedBoost());
    }

    /**
     * アイテムが取得されているかどうか
     */
    public boolean isCollected() {
        return this.isItemCollected;
    }

    /**
     * アイテムが取得されているかどうかを設定
     *
     * @param isCollected アイテムの取得状態
     */
    public void setCollected(boolean isCollected) {
        this.isItemCollected = isCollected;
        this.setChanged();
        this.notifyObservers();
    }

    /*
     * アイテム名に基づいて画像を返すメソッド
     */
    public static BufferedImage getImage(String itemName) {
        return switch (itemName) {
            case "Heal Potion" -> HEAL_POTION_IMAGE;
            case "Speed Boost" -> SPEED_BOOST_IMAGE;
            default -> null;
        };
    }

    @Override
    public void onAllInitiated(MazeModel mazeModel, int x, int y) {
        this.ovalX = x;
        this.ovalY = y;

        boolean isTopWall = mazeModel.getElementAt(x, y - 1) instanceof WallModel;
        boolean isLeftWall = mazeModel.getElementAt(x - 1, y) instanceof WallModel;
        boolean isRightWall = mazeModel.getElementAt(x + 1, y) instanceof WallModel;
        boolean isBottomWall = mazeModel.getElementAt(x, y + 1) instanceof WallModel;

        if (!(isTopWall || isBottomWall || isLeftWall || isRightWall)) {
            this.pathSprite = ImageManager.DUNGEON_SPRITE.getRandomImage(7, 0, 10, 3);
        } else if (isTopWall) {
            if (isLeftWall) {
                this.pathSprite = ImageManager.DUNGEON_SPRITE.getImageAt(1, 1);
            } else if (isRightWall) {
                this.pathSprite = ImageManager.DUNGEON_SPRITE.getImageAt(4, 1);
            } else {
                this.pathSprite = ImageManager.DUNGEON_SPRITE.getRandomImage(2, 1, 3, 1);
            }
        } else if (isBottomWall) {
            this.pathSprite = ImageManager.DUNGEON_SPRITE.getRandomImage(2, 3, 3, 3);
        } else if (isLeftWall) {
            this.pathSprite = ImageManager.DUNGEON_SPRITE.getImageAt(1, 2);
        } else {
            this.pathSprite = ImageManager.DUNGEON_SPRITE.getImageAt(4, 2);
        }
    }

    @Override
    public void onEnter() {
        if (!this.isCollected()) {
            int index = RANDOM.nextInt(items.size());
            selectedItem = items.get(index);
            playerModel.addItem(selectedItem);
            this.setCollected(true);
            System.out.println("Item collected: " + selectedItem.getName());
        }
    }

    @Override
    public void draw(Graphics g, int x, int y, int size) {
        g.drawImage(pathSprite, x, y, size, size, null);

        int itemBoxSize = size / 5 * 4;
        int itemBoxX = x + size / 2 - itemBoxSize / 2;
        int itemBoxY = y + size / 2 - itemBoxSize / 2;
        if (!this.isCollected()) {
            g.drawImage(INITIAL_IMAGE, itemBoxX, itemBoxY, itemBoxSize, itemBoxSize, null);
        }
    }
}
