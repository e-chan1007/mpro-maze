package maze.maze.player;

import maze.maze.*;
import maze.maze.element.StartModel;
import maze.util.*;

public class PlayerModel extends Observable {
    private int playerX = 1;
    private int playerY = 1;
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

    public int getPlayerX() {
        return playerX;
    }

    public int getPlayerY() {
        return playerY;
    }

    public void moveLeft() {
        if(mazeModel.getElementAt(playerX-1, playerY).canEnter()) {
            playerX--;
            notifyChange();
            onMove();
        }
    }

    public void moveRight() {
        if(mazeModel.getElementAt(playerX+1, playerY).canEnter()) {
            playerX++;
            notifyChange();
            onMove();
        }
    }

    public void moveUp() {
        if(mazeModel.getElementAt(playerX, playerY-1).canEnter()) {
            playerY--;
            notifyChange();
            onMove();
        }
    }

    public void moveDown() {
        if(mazeModel.getElementAt(playerX, playerY+1).canEnter()) {
            playerY++;
            notifyChange();
            onMove();
        }
    }

    private void onMove() {
        mazeModel.getElementAt(playerX, playerY).onEnter(mazeModel, this);
    }

    private void notifyChange() {
        setChanged();
        notifyObservers();
    }
}
