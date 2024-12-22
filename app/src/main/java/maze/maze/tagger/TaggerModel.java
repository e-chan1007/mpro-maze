package maze.maze.tagger;

import javax.swing.Timer;

import maze.maze.*;
import maze.util.*;

public class TaggerModel extends Observable {
  private float taggerX = 4;
  private float taggerY = 4;
  private final int STEPS = 30;
  private final int DELAY = 1;
  MazeModel mazeModel = new MazeModel();

  public TaggerModel(MazeModel mazeModel) {
    this.mazeModel = mazeModel;
    TaggerSearchModel(mazeModel, this);
  }

  public float getTaggerX() {
    return taggerX;
  }

  public float getTaggerY() {
    return taggerY;
  }

  public void moveLeft() {
    if (mazeModel.getElementAt(Math.round(taggerX - 1), Math.round(taggerY)).canEnter()) {
      final int[] currentStep = { 0 };
      Timer timer = new Timer(DELAY, e -> {
        if (currentStep[0] < STEPS) {
          taggerX -= 1.0 / STEPS;
          notifyChange();
          currentStep[0]++;
        } else {
          ((Timer) e.getSource()).stop();
        }
      });
      timer.start();
    }
  }

  public void moveRight() {
    if (mazeModel.getElementAt(Math.round(taggerX + 1), Math.round(taggerY)).canEnter()) {
      final int[] currentStep = { 0 };
      Timer timer = new Timer(DELAY, e -> {
        if (currentStep[0] < STEPS) {
          taggerX += 1.0 / STEPS;
          notifyChange();
          currentStep[0]++;
        } else {
          ((Timer) e.getSource()).stop();
        }
      });
      timer.start();
    }
  }

  public void moveUp() {
    if (mazeModel.getElementAt(Math.round(taggerX), Math.round(taggerY - 1)).canEnter()) {
      final int[] currentStep = { 0 };
      Timer timer = new Timer(DELAY, e -> {
        if (currentStep[0] < STEPS) {
          taggerY -= 1.0 / STEPS;
          notifyChange();
          currentStep[0]++;
        } else {
          ((Timer) e.getSource()).stop();
        }
      });
      timer.start();
    }
  }

  public void moveDown() {
    if (mazeModel.getElementAt(Math.round(taggerX), Math.round(taggerY + 1)).canEnter()) {
      final int[] currentStep = { 0 };
      Timer timer = new Timer(DELAY, e -> {
        if (currentStep[0] < STEPS) {
          taggerY += 1.0 / STEPS;
          notifyChange();
          currentStep[0]++;
        } else {
          ((Timer) e.getSource()).stop();
        }
      });
      timer.start();
    }
  }

  private void notifyChange() {
    setChanged();
    notifyObservers();
  }
}