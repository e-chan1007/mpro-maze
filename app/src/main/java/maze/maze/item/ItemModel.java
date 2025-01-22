package maze.maze.item;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;


import maze.asset.ImageManager;
import maze.asset.Sprite;
import maze.maze.item.Item;
import maze.maze.MazeModel;
import maze.maze.player.PlayerModel;

public class ItemModel extends ItemElement {
  private PlayerModel playerModel;
  private List<Item> items;
  private Item selectedItem;

  private static final Random RANDOM = new Random();

  private static final Sprite HEAL_POTION_SPRITE = ImageManager.loadImageAsSprite("/item/potion.png", 64, 64);
  private static final BufferedImage HEAL_POTION_IMAGE = HEAL_POTION_SPRITE.getImageAt(0, 1);
  private static final Sprite SPEED_BOOST_SPRITE = ImageManager.loadImageAsSprite("/item/potion.png", 64, 64);
  private static final BufferedImage SPEED_BOOST_IMAGE = SPEED_BOOST_SPRITE.getImageAt(0, 4);

  public ItemModel(MazeModel mazeModel, PlayerModel playerModel) {
    this.playerModel = playerModel;
    this.items = new ArrayList<>();
    items.add(new HealPotion(1));
    items.add(new SpeedBoost());
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
    if (!this.isCollected()) {
      g.setColor(java.awt.Color.RED);
    } else {
      g.setColor(java.awt.Color.GRAY);
    }
    g.fillRect(x, y, size, size);
  }
}
