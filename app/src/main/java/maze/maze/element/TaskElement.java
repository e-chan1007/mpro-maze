package maze.maze.element;

/**
 * 迷路のタスク要素(抽象クラス: 継承して使う)
 */
abstract public class TaskElement extends MazeElement {
  private boolean isTaskCompleted = false;

  /**
   * タスクが完了しているかどうか
   */
  public boolean isCompleted() {
    return this.isTaskCompleted;
  }

  /**
   * タスクが完了しているかどうかを設定する
   *
   * @param isCompleted タスクの完了状態
   */
  public void setCompleted(boolean isCompleted) {
    this.isTaskCompleted = isCompleted;
    this.setChanged();
    this.notifyObservers();
  }
}
