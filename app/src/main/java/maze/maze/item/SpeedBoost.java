package maze.maze.item;

import maze.maze.player.PlayerModel;

public class SpeedBoost implements Item {
  public SpeedBoost() {
  }

  @Override
  public void applyEffect(PlayerModel playerModel) {
    playerModel.boostSpeed();
  }

  @Override
  public String getName() {
    return "Speed Boost";
  }
}
