package maze.maze.item;

import maze.maze.player.PlayerModel;

public class HealthPotion extends Item {
  private int healPoint;

  public HealthPotion(int x, int y, int healPoint) {
    super(x, y, "回復薬");
    this.healPoint = healPoint;
  }

  @Override
  public void applyEffect(PlayerModel playerModel) {
    playerModel.heal(this.healPoint);
    this.setCollected(true);
  }

  public int getHealPoint() {
    return healPoint;
  }
  
}
