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
