import javax.swing.JOptionPane;

public class PlayerModel extends Observable {
    private int playerX = 1;
    private int playerY = 1;
    MazeModel mazeModel = new MazeModel();

    public PlayerModel(MazeModel mazeModel) {
        this.mazeModel = mazeModel;
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
        }
    }

    public void moveRight() {
        if(mazeModel.getElementAt(playerX+1, playerY).canEnter()) {
            playerX++;
            notifyChange();

        }
    }

    public void moveUp() {
        if(mazeModel.getElementAt(playerX, playerY-1).canEnter()) {
            playerY--;
            notifyChange();

        }
    }

    public void moveDown() {
        if(mazeModel.getElementAt(playerX, playerY+1).canEnter()) {
            playerY++;
            notifyChange();
        }
    }

    private void notifyChange() {
        setChanged();
        notifyObservers();
    }
}
