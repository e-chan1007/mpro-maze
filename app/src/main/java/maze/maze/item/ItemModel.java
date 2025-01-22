package maze.maze.item;

import java.awt.Graphics;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;

import maze.maze.MazeModel;
import maze.maze.player.PlayerModel;



public class ItemModel extends ItemElement {

  private PlayerModel playerModel;
  private List<ItemEffect> effects;
  private ItemEffect selectedEffect;
  private static final Random RANDOM = new Random();

  public ItemModel(MazeModel mazeModel, PlayerModel playerModel) {
    this.playerModel = playerModel;
    this.effects = new ArrayList<>();

    effects.add(new HealEffect(1));
  }

  @Override
  public void onEnter() {
    if (!this.isCollected()) {
      selectedEffect = effects.get(RANDOM.nextInt(effects.size()));

      playerModel.addItemEffect(selectedEffect);

      this.setCollected(true);
    }
  }

  @Override
  public void draw(Graphics g, int x, int y, int size) {
    if (!this.isCollected()) {
      g.setColor(java.awt.Color.RED);
    } else {
      g.setColor(java.awt.Color.GRAY);
    }
    g.fillRect(x, y, size, size);
  }
}
