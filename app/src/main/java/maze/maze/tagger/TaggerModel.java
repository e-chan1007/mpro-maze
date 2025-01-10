package maze.maze.tagger;

import javax.swing.Timer;

import maze.maze.MazeModel;
import maze.util.Observable;

public class TaggerModel extends Observable {
  private float taggerX = 1;
  private float taggerY = 1;
  private final int STEPS = 30;
  private final int DELAY = 1;
  private boolean flag = true;
  MazeModel mazeModel = new MazeModel();
  private TaggerSearchModel searchModel;

  public TaggerModel(MazeModel mazeModel) {
    this.mazeModel = mazeModel;
  }

  public MazeModel getMazeModel() {
    return mazeModel;
  }

  public void setSearchModel(TaggerSearchModel searchModel) {
    this.searchModel = searchModel;
  }

  public float getTaggerX() {
    return taggerX;
  }

  public float getTaggerY() {
    return taggerY;
  }

  public boolean getFlag() {
    return flag;
  }

  public void moveLeft() {
    if (mazeModel.getElementAt(Math.round(taggerX - 1), Math.round(taggerY)).canEnter()) {
      if (flag) {
        final int[] currentStep = { 0 };
        flag = false;
        Timer timer = new Timer(DELAY, e -> {
          if (mazeModel.isPaused())
            return;
          if (currentStep[0] < STEPS) {
            taggerX -= 1.0 / STEPS;
            notifyChange();
            currentStep[0]++;
          } else {
            flag = true;
            searchModel.signalConditionMet1();
            ((Timer) e.getSource()).stop();
          }
        });
        timer.start();
      }
    }
  }

  public void moveRight() {
    if (mazeModel.getElementAt(Math.round(taggerX + 1), Math.round(taggerY)).canEnter()) {
      if (flag) {
        final int[] currentStep = { 0 };
        flag = false;
        Timer timer = new Timer(DELAY, e -> {
          if (mazeModel.isPaused())
            return;
          if (currentStep[0] < STEPS) {
            taggerX += 1.0 / STEPS;
            notifyChange();
            currentStep[0]++;
          } else {
            flag = true;
            searchModel.signalConditionMet1();
            ((Timer) e.getSource()).stop();
          }
        });
        timer.start();
      }
    }
  }

  public void moveUp() {
    if (mazeModel.getElementAt(Math.round(taggerX), Math.round(taggerY - 1)).canEnter()) {
      if (flag) {
        final int[] currentStep = { 0 };
        flag = false;
        Timer timer = new Timer(DELAY, e -> {
          if (mazeModel.isPaused())
            return;
          if (currentStep[0] < STEPS) {
            taggerY -= 1.0 / STEPS;
            notifyChange();
            currentStep[0]++;
          } else {
            flag = true;
            searchModel.signalConditionMet1();
            ((Timer) e.getSource()).stop();
          }
        });
        timer.start();
      }
    }
  }

  public void moveDown() {
    if (mazeModel.getElementAt(Math.round(taggerX), Math.round(taggerY + 1)).canEnter()) {
      if (flag) {
        final int[] currentStep = { 0 };
        flag = false;
        Timer timer = new Timer(DELAY, e -> {
          if (mazeModel.isPaused())
            return;
          if (currentStep[0] < STEPS) {
            taggerY += 1.0 / STEPS;
            notifyChange();
            currentStep[0]++;
          } else {
            flag = true;
            searchModel.signalConditionMet1();
            ((Timer) e.getSource()).stop();
          }
        });
        timer.start();
      }
    }
  }

  private void notifyChange() {
    setChanged();
    notifyObservers();
  }
}
