package maze.maze.tagger;

import javax.swing.Timer;

import maze.maze.*;
import maze.util.*;

public class TaggerModel extends Observable {
  private float taggerX = 18;
  private float taggerY = 17;
  private final int STEPS = 30;
  private final int DELAY = 1;
  private boolean flag = true;
  MazeModel mazeModel = new MazeModel();
  private TaggerSearchModel searchModel;

  public TaggerModel(MazeModel mazeModel) {
    this.mazeModel = mazeModel;
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
          if (currentStep[0] < STEPS) {
            taggerX -= 1.0 / STEPS;
            System.out.println("Moving Left: taggerX = " + taggerX); // デバッグログ
            notifyChange();
            currentStep[0]++;
          } else {
            flag = true;
            searchModel.signalConditionMet();
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
          if (currentStep[0] < STEPS) {
            taggerX += 1.0 / STEPS;
            System.out.println("Moving Right: taggerX = " + taggerX); // デバッグログ
            notifyChange();
            currentStep[0]++;
          } else {
            flag = true;
            searchModel.signalConditionMet();
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
          if (currentStep[0] < STEPS) {
            taggerY -= 1.0 / STEPS;
            System.out.println("Moving Up: taggerY = " + taggerY); // デバッグログ
            notifyChange();
            currentStep[0]++;
          } else {
            flag = true;
            searchModel.signalConditionMet();
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
          if (currentStep[0] < STEPS) {
            taggerY += 1.0 / STEPS;
            System.out.println("Moving Down: taggerY = " + taggerY); // デバッグログ
            notifyChange();
            currentStep[0]++;
          } else {
            flag = true;
            searchModel.signalConditionMet();
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