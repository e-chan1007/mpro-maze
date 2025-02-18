package maze.maze.item;

import maze.maze.player.PlayerModel;

public class HealPotion implements Item {
  private int healAmount;

  // コンストラクタ
  public HealPotion(int healAmount) {
    this.healAmount = healAmount;
  }

  // プレイヤーのHPを回復する
  @Override
  public void applyEffect(PlayerModel playerModel) {
    playerModel.heal(healAmount);
  }

  // アイテム名を返す
  @Override
  public String getName() {
    return "Heal Potion";
  }
}
