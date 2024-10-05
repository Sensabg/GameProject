import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable {
    final int ORIGINAL_TILE_SIZE = 16; // 16 x 16
    final int SCALE = 3;
    final int SCALED_TILE_SIZE = ORIGINAL_TILE_SIZE * SCALE; // 48 x 48

    final int MAX_SCREEN_COL = 16;
    final int MAX_SCREEN_ROW = 12;

    final int SCREEN_WIDTH = SCALED_TILE_SIZE * MAX_SCREEN_COL; // 768 pixels
    final int SCREEN_HEIGHT = SCALED_TILE_SIZE * MAX_SCREEN_ROW; // 576 pixels

    KeyHandler keyH = new KeyHandler();
    Thread gameThread;

    int playerX = 100;
    int playerY = 100;
    int playerSpeed = 5;

    enum Direction {
        UP, LEFT, DOWN, RIGHT, NONE
    }

    public GamePanel() {
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
    }

    public void startGameThread() {

        gameThread = new Thread(this);
        gameThread.start();

    }

    @Override
    public void run() {
        while (gameThread != null) {

            long currentTime = System.nanoTime();
            long currentTime2 = System.currentTimeMillis();

            update();
            repaint();
        }
    }

    public void update() {
        Direction direction = getPressedDirection();

        switch (direction) {
            case UP -> playerY -= playerSpeed;
            case LEFT -> playerX -= playerSpeed;
            case DOWN -> playerY += playerSpeed;
            case RIGHT -> playerX += playerSpeed;
        }
    }

    private Direction getPressedDirection() {
        if (keyH.upPressed) {
            return Direction.UP;
        } else if (keyH.leftPressed) {
            return Direction.LEFT;
        } else if (keyH.downPressed) {
            return Direction.DOWN;
        } else if (keyH.rightPressed) {
            return Direction.RIGHT;
        } else {
            return Direction.NONE;
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.WHITE);
        g2.fillRect(playerX, playerY, SCALED_TILE_SIZE, SCALED_TILE_SIZE);
        g2.dispose();
    }
}
