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

    int FPS = 60;

    KeyHandler keyH = new KeyHandler();
    Thread gameThread;

    int playerX = 100;
    int playerY = 100;
    int playerSpeed = 5;


    enum Direction {
        UP, LEFT, DOWN, RIGHT, UP_LEFT, UP_RIGHT, DOWN_LEFT, DOWN_RIGHT, NONE
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

//    @Override
//    public void run() {
////      long currentTime = System.nanoTime();
////      long currentTime2 = System.currentTimeMillis();
//        double drawInterval = 1_000_000_000 * 1.0 / FPS;
//        double nextDrawTime = System.nanoTime() + drawInterval;
//
//        while (gameThread != null) {
//            update();
//            repaint();
//            try {
//                double remainTime = (nextDrawTime - System.nanoTime()) / 1_000_000;
//                remainTime = Math.max(remainTime, 0);
//
//                Thread.sleep((long) remainTime);
//
//                nextDrawTime += drawInterval;
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//    }

    @Override
    public void run() {
        double drawInterval = 1_000_000_000 * 1.0 / FPS;
        long lastTime = System.nanoTime();
        double delta = 0;
        long currentTime;

        while (gameThread != null) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;

            if (delta >= 1) {
                update();
                repaint();
                delta--;
            }
        }
    }
    public void update() {
        Direction direction = getPressedDirection();

        switch (direction) {

            case UP -> playerY -= playerSpeed;
            case DOWN -> playerY += playerSpeed;
            case LEFT -> playerX -= playerSpeed;
            case RIGHT -> playerX += playerSpeed;

            case UP_LEFT -> {
                playerX -= playerSpeed;
                playerY -= playerSpeed;
            }
            case UP_RIGHT -> {
                playerX += playerSpeed;
                playerY -= playerSpeed;
            }
            case DOWN_LEFT -> {
                playerX -= playerSpeed;
                playerY += playerSpeed;
            }
            case DOWN_RIGHT -> {
                playerX += playerSpeed;
                playerY += playerSpeed;
            }
        }
    }
//    public void update() {
//        if (keyH.upPressed) {
//            playerY -= playerSpeed;
//        }
//        if (keyH.leftPressed) {
//            playerX -= playerSpeed;
//        }
//        if (keyH.downPressed) {
//            playerY += playerSpeed;
//        }
//        if (keyH.rightPressed) {
//            playerX += playerSpeed;
//        }
//    }

//    public void update() {
//        Direction direction = getPressedDirection();
//        switch (direction) {
//            case UP -> playerY -= playerSpeed;
//            case LEFT -> playerX -= playerSpeed;
//            case DOWN -> playerY += playerSpeed;
//            case RIGHT -> playerX += playerSpeed;
//        }
//    }

    private Direction getPressedDirection() {
        boolean up = keyH.upPressed;
        boolean down = keyH.downPressed;
        boolean left = keyH.leftPressed;
        boolean right = keyH.rightPressed;

        if (up && left) return Direction.UP_LEFT;
        if (up && right) return Direction.UP_RIGHT;
        if (down && left) return Direction.DOWN_LEFT;
        if (down && right) return Direction.DOWN_RIGHT;

        if (up) return Direction.UP;
        if (down) return Direction.DOWN;
        if (left) return Direction.LEFT;
        if (right) return Direction.RIGHT;

        return Direction.NONE;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.WHITE);
        g2.fillRect(playerX, playerY, SCALED_TILE_SIZE, SCALED_TILE_SIZE);
        g2.dispose();
    }
}