public class PlayerModel {
    private int playerX = 1;
    private int playerY = 1;
    public PlayerModel() {
    }

    public int getPlayerX() {
        if()
        return playerX;
    }

    public int getPlayerY() {
        return playerY;
    }

    public void moveLeft() {
        playerX--;
    }

    public void moveRight() {
        playerX++;
    }

    public void moveUp() {
        playerY--;
    }

    public void moveDown() {
        playerY++;
    }


}
