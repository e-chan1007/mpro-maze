package maze.maze;

import java.awt.Graphics;
import java.util.List;
import java.awt.Color;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import maze.maze.player.PlayerModel;
import maze.util.Observable;
import maze.asset.ImageManager;
import maze.maze.item.Item;
import maze.maze.item.ItemModel;

public class InventoryOverlay extends JPanel implements maze.util.Observer {

  private PlayerModel playerModel;
  private static final int MAX_SLOTS = 3;
  private static final int MAX_HITPOINT = 3;
  private BufferedImage heartImage;
  private BufferedImage heartBackgroundImage;
  private BufferedImage heartBorderImage;

  public InventoryOverlay(PlayerModel playerModel) {
    this.playerModel = playerModel;
    setOpaque(false);
    heartImage = ImageManager.loadImage("/player/hp/heart.png");
    heartBackgroundImage = ImageManager.loadImage(("/player/hp/background.png"));
    heartBorderImage = ImageManager.loadImage("/player/hp/border.png");
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    List<Item> inventory = playerModel.getInventory();

    int iconSize = 80;
    int padding = 20;
    int startHeartX = 20;
    int startItemX = 20 + MAX_HITPOINT * (iconSize + padding);
    int bottomY = getHeight() - padding;

    int totalWidth = (MAX_SLOTS + MAX_HITPOINT) * (iconSize + padding) + padding;
    int totalHeight = iconSize + padding * 2;

    int rectX = startHeartX - padding;
    int rectY = bottomY - padding - iconSize;

    int hitPoint = playerModel.getHitPoint();

    g.setColor(new Color(0, 0, 0, 60));
    g.fillRect(rectX, rectY, totalWidth, totalHeight);

    for (int i = 0; i < hitPoint; i++) {
      g.drawImage(heartBorderImage, startHeartX + i * (iconSize + padding), rectY + padding, iconSize, iconSize, null);
      g.drawImage(heartBackgroundImage, startHeartX + i * (iconSize + padding), rectY + padding, iconSize, iconSize, null);
      g.drawImage(heartImage, startHeartX + i * (iconSize + padding), rectY + padding, iconSize, iconSize, null);
    }

    for (int i = 0; i < MAX_SLOTS; i++) {
      if (i < inventory.size()) {
        Item item = inventory.get(i);
        BufferedImage image = ItemModel.getImage(item.getName());
        g.drawImage(image, startItemX + i * (iconSize + padding), rectY + padding, iconSize, iconSize, null);
      } else {
        g.setColor(new Color(0, 0, 0, 80));
        g.fillRect(startItemX + i * (iconSize + padding), rectY + padding, iconSize, iconSize);
      }
    }
  }

  @Override
  public void update(Observable o, Object arg) {
    if (arg instanceof String) {
      String event = (String) arg;
      if (event.equals("inventoryChanged") || event.equals("hitPointChanged")) {
        repaint();
      }
    }
  }
}
