package maze.maze.item;

import maze.maze.player.PlayerModel;

public class SpeedBoost implements Item {
  // コンストラクタ
  public SpeedBoost() {
  }

  // プレイヤーのスピードを上げる
  @Override
  public void applyEffect(PlayerModel playerModel) {
    playerModel.boostSpeed();
  }

  // アイテム名を返す
  @Override
  public String getName() {
    return "Speed Boost";
  }
}
