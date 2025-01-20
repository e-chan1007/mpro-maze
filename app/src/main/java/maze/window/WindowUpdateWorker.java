package maze.window;

import javax.swing.SwingWorker;

public class WindowUpdateWorker extends SwingWorker<Void, Void> {
  private final AppWindow window;

  public WindowUpdateWorker(AppWindow window) {
    this.window = window;
  }

  @Override
  protected Void doInBackground() throws Exception {
    while (true) {
      window.revalidate();
      window.repaint();
      Thread.sleep(1000 / 30);
    }
  }
}
