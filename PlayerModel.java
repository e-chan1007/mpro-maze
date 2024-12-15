public class PlayerModel {
    private int playerX = 1;
    private int playerY = 1;
    public PlayerModel() {
    }
    MazeModel mazeModel = new MazeModel();

    public int getPlayerX() {
        return playerX;
    }

    public int getPlayerY() {
        return playerY;
    }

    public void moveLeft() {
        if(mazeModel.getElementAt(playerX - 1, playerY) instanceof PathModel) {
            playerX--;
        }
    }

    public void moveRight() {
        if(mazeModel.getElementAt(playerX + 1, playerY) instanceof PathModel) {
            playerX++;
        }
    }

    public void moveUp() {
        if(mazeModel.getElementAt(playerX, playerY - 1) instanceof PathModel) {
            playerY--;
        }
    }

    public void moveDown() {
        if(mazeModel.getElementAt(playerX, playerY + 1) instanceof PathModel) {
            playerY++;
        }
    }


}
