package maze.maze.item;

import java.awt.Graphics;

import maze.asset.SoundManager;
import maze.maze.player.PlayerModel;

public class ItemModel extends ItemElement {
  private PlayerModel playerModel;

  public ItemModel(ItemType type) {
    super(type);
  }

  public void setPlayerModel(PlayerModel playerModel) {
    this.playerModel = playerModel;
  }

  @Override
  public void onEnter() {
    if (!isCollected()) {
      // アイテム取得処理
      collect(playerModel);

      // 効果音再生
      // switch (getType()) {
      // case KEY:
      // SoundManager.getInstance().play("key");
      // break;
      // case POTION:
      // SoundManager.getInstance().play("potion");
      // break;
      // case TREASURE:
      // SoundManager.getInstance().play("treasure");
      // break;
    }
  }
  
  public void draw(Graphics g, int x, int y, int size) {
    if (!this.isCollected()) {
      g.setColor(java.awt.Color.RED);
      g.fillRect(x, y, size, size);
    } else {
      g.setColor(java.awt.Color.GRAY);
      g.fillRect(x, y, size, size);
    }
  }
}
