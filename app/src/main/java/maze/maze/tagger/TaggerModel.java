package maze.maze.tagger;

import javax.swing.Timer;

import maze.maze.MazeModel;
import maze.util.Observable;

public class TaggerModel extends Observable {
  private float taggerX = 1;
  private float taggerY = 1;
  private final int STEPS = 30;
  private final int DELAY = 1000 / 60;
  private boolean canMove = true;
  private Direction currentDirection;
  MazeModel mazeModel = new MazeModel();
  private TaggerSearchModel searchModel;

  public TaggerModel(MazeModel mazeModel) {
    this.mazeModel = mazeModel;
    this.currentDirection = Direction.LEFT;
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
    return canMove;
  }

  public enum Direction {
    LEFT, RIGHT
  }

  public Direction getCurrentDirection() {
    return currentDirection;
  }

  public void moveLeft() {
    if (mazeModel.getElementAt(Math.round(taggerX - 1), Math.round(taggerY)).canEnter()) {
      if (canMove) {
        currentDirection = Direction.LEFT;
        final int[] currentStep = { 0 };
        canMove = false;
        Timer timer = new Timer(DELAY, e -> {
          if (mazeModel.isPaused())
            return;
          if (currentStep[0] < STEPS) {
            taggerX -= 1.0 / STEPS;
            notifyChange();
            currentStep[0]++;
          } else {
            canMove = true;
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
      if (canMove) {
        final int[] currentStep = { 0 };
        currentDirection = Direction.RIGHT;
        canMove = false;
        Timer timer = new Timer(DELAY, e -> {
          if (mazeModel.isPaused())
            return;
          if (currentStep[0] < STEPS) {
            taggerX += 1.0 / STEPS;
            notifyChange();
            currentStep[0]++;
          } else {
            canMove = true;
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
      if (canMove) {
        final int[] currentStep = { 0 };
        canMove = false;
        Timer timer = new Timer(DELAY, e -> {
          if (mazeModel.isPaused())
            return;
          if (currentStep[0] < STEPS) {
            taggerY -= 1.0 / STEPS;
            notifyChange();
            currentStep[0]++;
          } else {
            canMove = true;
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
      if (canMove) {
        final int[] currentStep = { 0 };
        canMove = false;
        Timer timer = new Timer(DELAY, e -> {
          if (mazeModel.isPaused())
            return;
          if (currentStep[0] < STEPS) {
            taggerY += 1.0 / STEPS;
            notifyChange();
            currentStep[0]++;
          } else {
            canMove = true;
            searchModel.signalConditionMet1();
            ((Timer) e.getSource()).stop();
          }
        });
        timer.start();
      }
    }
  }

  public boolean isTaggerinRange() {
    double distance = Math.pow(getTaggerX() - getPlayerX(), 2) + Math.pow(getTaggerY() - getPlayerY(), 2);
  }

  private void notifyChange() {
    setChanged();
    notifyObservers();
  }
}
