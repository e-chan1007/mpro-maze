package maze.maze.item;

import maze.maze.player.PlayerModel;

public class HealEffect implements ItemEffect {
    private int healAmount;

    public HealEffect(int healAmount) {
      this.healAmount = healAmount;
    }

    @Override
    public void applyEffect(PlayerModel playerModel) {
        playerModel.heal(healAmount);
    }

    @Override
    public String getName() {
      return "Heal +" + healAmount;
    }
}
