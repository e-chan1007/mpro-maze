package maze.maze.item;

import java.awt.Graphics;
import maze.maze.player.PlayerModel;

public class ItemModel extends ItemElement {

  private PlayerModel playerModel;

  public ItemModel(ItemType type, PlayerModel playerModel) {
    super(type);
  }

  @Override
  public void onEnter() {
    // 親クラスのcollectメソッドが適切に処理を行う
    collect(playerModel);
  }

  @Override
  public void draw(Graphics g, int x, int y, int size) {
    if (!isCollected()) {
      g.setColor(java.awt.Color.RED);
      g.fillRect(x, y, size, size);
    } else {
      g.setColor(java.awt.Color.GRAY);
      g.fillRect(x, y, size, size);
    }
  }
}
