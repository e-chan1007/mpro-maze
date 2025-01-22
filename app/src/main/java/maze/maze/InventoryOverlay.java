package maze.maze;

import java.awt.Graphics;
import java.util.List;
import java.awt.Image;

import javax.swing.JPanel;
import javax.swing.ImageIcon;

import maze.maze.player.PlayerModel;
import maze.util.Observable;
import maze.maze.item.ItemEffect;

public class InventoryOverlay extends JPanel implements maze.util.Observer {

  private PlayerModel playerModel;

  public InventoryOverlay(PlayerModel playerModel) {
    this.playerModel = playerModel;
    setOpaque(false);
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    
    List<ItemEffect> inventory = playerModel.getInventory();

    int iconSize = 48;
    int padding = 8;
    int startX = 8;
    int bottomY = getHeight() - 8;

    for (ItemEffect item : inventory) {
      ImageIcon icon = new ImageIcon(getClass().getResource(item.getImagePath()));
      Image img = icon.getImage();

      int x = startX;
      int y = bottomY - iconSize;

      g.drawImage(img, x, y, iconSize, iconSize, this);

      startX += iconSize + padding;
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
