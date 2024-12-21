package maze.maze.element;

/**
 * 迷路のタスク要素(抽象クラス: 継承して使う)
 */
abstract public class TaskElement extends MazeElement {
  private boolean isTaskCompleted = false;

  @Override
  public boolean canEnter() {
    return true;
  }

  /**
   * タスクが完了しているかどうか
   */
  public boolean isTaskCompleted() {
    return this.isTaskCompleted;
  }

  /**
   * タスクが完了しているかどうかを設定する
   *
   * @param isTaskCompleted タスクの完了状態
   */
  public void setTaskCompleted(boolean isTaskCompleted) {
    this.isTaskCompleted = isTaskCompleted;
    this.setChanged();
    this.notifyObservers();
  }
}
