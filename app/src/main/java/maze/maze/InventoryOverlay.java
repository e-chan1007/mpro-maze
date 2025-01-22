package maze.maze;

import java.awt.Graphics;
import java.util.List;
import java.awt.Color;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import maze.maze.player.PlayerModel;
import maze.util.Observable;
import maze.maze.item.Item;
import maze.maze.item.ItemModel;

public class InventoryOverlay extends JPanel implements maze.util.Observer {

  private PlayerModel playerModel;

  private static final int MAX_SLOTS = 3;

  public InventoryOverlay(PlayerModel playerModel) {
    this.playerModel = playerModel;
    setOpaque(false);
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    List<Item> inventory = playerModel.getInventory();

    int iconSize = 60;
    int padding = 20;
    int startX = 100;
    int bottomY = getHeight() - 8;

    int totalWidth = MAX_SLOTS * (iconSize + padding) + padding;
    int totalHeight = iconSize + padding * 2;

    int rectX = startX - padding / 2;
    int rectY = bottomY - iconSize - padding;

    g.setColor(new Color(255, 255, 255, 150));
    g.fillRect(rectX, rectY, totalWidth, totalHeight);
    g.setColor(Color.WHITE);
    g.drawRect(rectX, rectY, totalWidth, totalHeight);

    for (int i = 0; i < MAX_SLOTS; i++) {
      int x = startX + i * (iconSize + padding);
      int y = bottomY - iconSize;

      if (i < inventory.size()) {
        Item item = inventory.get(i);
        BufferedImage image = ItemModel.getImage(item.getName());
        g.drawImage(image, x, y, iconSize, iconSize, null);
      } else {
        g.setColor(Color.GRAY);
        g.fillRect(x, y, iconSize, iconSize);
      }
    }

  }

  @Override
  public void update(Observable o, Object arg) {
    if (arg instanceof String) {
      String event = (String) arg;
      if (event.equals("inventoryChanged")) {
        repaint();
      }
    }
  }
}
