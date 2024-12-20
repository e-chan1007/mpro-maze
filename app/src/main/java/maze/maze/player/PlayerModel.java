package maze.maze.player;

import maze.maze.*;
import maze.maze.element.StartModel;
import maze.util.*;

import javax.swing.Timer;

public class PlayerModel extends Observable {
    private float playerX = 1;
    private float playerY = 1;
    MazeModel mazeModel = new MazeModel();

    public PlayerModel(MazeModel mazeModel) {
        this.mazeModel = mazeModel;

        this.mazeModel.addObserver((Observable observable, Object object) -> {
            setStartPos();
        });
        
        setStartPos();
    }

    private void setStartPos() {
        int startPos[] = mazeModel.locateElement(StartModel.class);
        if(startPos != null) {
            playerX = startPos[0];
            playerY = startPos[1];
        }
    }

    public float getPlayerX() {
        return playerX;
    }

    public float getPlayerY() {
        return playerY;
    }

    //* 連続的な動き試作 */
    public void moveLeft() {
        if(mazeModel.getElementAt(Math.round(playerX-1), Math.round(playerY)).canEnter()) {
            final int steps = 100;
            final int[] currentStep = { 0 };

            Timer timer = new Timer(10, e -> {
                if ( currentStep[0] < steps) {
                    playerX -= 0.01;
                    notifyChange();
                    currentStep[0]++;
                } else {
                    ((Timer) e.getSource()).stop();
                }
            });
            timer.start();
            // notifyChange();
            onMove();
        }
    }

    public void moveRight() {
        if(mazeModel.getElementAt(Math.round(playerX+1), Math.round(playerY)).canEnter()) {
            playerX++;
            notifyChange();
            onMove();
        }
    }

    public void moveUp() {
        if(mazeModel.getElementAt(Math.round(playerX), Math.round(playerY-1)).canEnter()) {
            playerY--;
            notifyChange();
            onMove();
        }
    }

    public void moveDown() {
        if(mazeModel.getElementAt(Math.round(playerX), Math.round(playerY+1)).canEnter()) {
            playerY++;
            notifyChange();
            onMove();
        }
    }

    private void onMove() {
        mazeModel.getElementAt(Math.round(playerX), Math.round(playerY)).onEnter(mazeModel, this);
    }

    private void notifyChange() {
        setChanged();
        notifyObservers();
    }
}
