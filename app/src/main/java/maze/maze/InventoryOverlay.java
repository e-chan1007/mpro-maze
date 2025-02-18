package maze.maze;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.List;

import javax.swing.JPanel;

import maze.asset.ImageManager;
import maze.maze.element.ItemModel;
import maze.maze.item.Item;
import maze.maze.player.PlayerModel;
import maze.util.Observable;

public class InventoryOverlay extends JPanel implements maze.util.Observer {
  private PlayerModel playerModel;
  // アイテムスロットの最大数
  private static final int MAX_SLOTS = 3;
  // 最大HP
  private static final int MAX_HITPOINT = 3;
  // ハートの画像
  private BufferedImage heartImage;
  private BufferedImage heartBackgroundImage;
  private BufferedImage heartBorderImage;

  // コンストラクタ
  public InventoryOverlay(PlayerModel playerModel) {
    this.playerModel = playerModel;
    setOpaque(false);
    // ハートの画像の読み込み
    heartImage = ImageManager.loadImage("/player/hp/heart.png");
    heartBackgroundImage = ImageManager.loadImage(("/player/hp/background.png"));
    heartBorderImage = ImageManager.loadImage("/player/hp/border.png");
  }

  // インベントリの描画
  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    List<Item> inventory = playerModel.getInventory();

    // アイコンのサイズとパディング
    int iconSize = 80;
    int padding = 20;
    int startHeartX = 20;
    int startItemX = 20 + MAX_HITPOINT * (iconSize + padding);
    int bottomY = getHeight() - padding;
    // インベントリの描画
    int totalWidth = (MAX_SLOTS + MAX_HITPOINT) * (iconSize + padding) + padding;
    int totalHeight = iconSize + padding * 2;
    int rectX = startHeartX - padding;
    int rectY = bottomY - padding - iconSize;
    // HPの取得
    int hitPoint = playerModel.getHitPoint();
    // インベントリの描画
    g.setColor(new Color(0, 0, 0, 60));
    g.fillRect(rectX, rectY, totalWidth, totalHeight);
    // ハートの描画
    for (int i = 0; i < hitPoint; i++) {
      g.drawImage(heartBorderImage, startHeartX + i * (iconSize + padding), rectY + padding, iconSize, iconSize, null);
      g.drawImage(heartBackgroundImage, startHeartX + i * (iconSize + padding), rectY + padding, iconSize, iconSize,
          null);
      g.drawImage(heartImage, startHeartX + i * (iconSize + padding), rectY + padding, iconSize, iconSize, null);
    }
    // アイテムの描画 
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

  // インベントリの更新
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
