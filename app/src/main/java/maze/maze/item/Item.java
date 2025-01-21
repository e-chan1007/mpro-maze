package maze.maze.item;

import maze.maze.player.PlayerModel;

public abstract class Item {
  protected int x;
  protected int y;
  protected String name;
  protected boolean isCollected;

  public Item(int x, int y, String name) {
    this.x = x;
    this.y = y;
    this.name = name;
    this.isCollected = false;
  }

  public abstract void applyEffect(PlayerModel playerModel);

  public String getName() {
    return name;
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  public boolean isCollected() {
    return isCollected;
  }

  public void setCollected(boolean isCollected) {
    this.isCollected = isCollected;
  }
}