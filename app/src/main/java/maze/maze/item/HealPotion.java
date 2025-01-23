package maze.maze.item;

import maze.maze.player.PlayerModel;

public class HealPotion implements Item {
  private int healAmount;

  public HealPotion(int healAmount) {
    this.healAmount = healAmount;
  }

  @Override
  public void applyEffect(PlayerModel playerModel) {
    playerModel.heal(healAmount);
  }

  @Override
  public String getName() {
    return "Heal Potion";
  }

  @Override
  public String getImagePath() {
    return "/item/heal.png";
  }
}
