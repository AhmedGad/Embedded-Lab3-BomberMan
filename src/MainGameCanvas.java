
import java.io.IOException;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.Ticker;
import javax.microedition.lcdui.game.GameCanvas;
import javax.microedition.lcdui.game.LayerManager;

public class MainGameCanvas extends GameCanvas implements Runnable {

    private boolean running;
    private Player player;
    private Monster[] mobs;
    public static Map backgroundLayer;
    private LayerManager layerManager;
    private Image backgroundTilesImage;
    int direction = 0;
    int spriteWidth = 25, spriteHeight = 30;
    boolean init;
    int lastx = 0, lasty = 0;
    int speed;
    int width = 14;
    int height = 14;
    int numberOfBombs;
    BombPool bp;

    public MainGameCanvas(int numberOfBombs) {
        super(true);
        this.numberOfBombs = numberOfBombs;
        init = true;
        speed = 1;

        layerManager = new LayerManager();
        player = new Player();
        layerManager.append(player.getPlayer());
        try {
            backgroundTilesImage = Image.createImage("/tiles.png");
        } catch (Exception ioe) {
            System.out.println("unable to load image");
        }
        backgroundLayer = new Map(backgroundTilesImage, width, height);
        addMonsters(5);
        bp = new BombPool(numberOfBombs);
        for (int i = 0; i < numberOfBombs; i++) {
            Bomb b = bp.GetBomb();
            for (int j = 0; j < 5; j++) {
                layerManager.append(b.getArray()[j]);
            }
            bp.release(b);
        }
        layerManager.append(backgroundLayer);

        // layerManager.setViewWindow(0, 0, getWidth(), getHeight());
    }

    public void addMonsters(int NumOfMonsters) {
        mobs = new Monster[NumOfMonsters];
        int cnt = 0;
        for (int i = 0; i < width && cnt < NumOfMonsters; i++) {
            for (int j = 0; j < height && cnt < NumOfMonsters; j++) {
                if (i < 4 && j < 4) {
                    continue;
                }
                if (backgroundLayer.isFreeCell(i, j)) {
                    mobs[cnt] = new Monster(i * 30, j * 30);
                    layerManager.append(mobs[cnt++].getMonster());
                }
            }
        }
    }

    public void start() {
        running = true;
        Thread t = new Thread(this);
        t.start();
    }
    final int RIGHT = 1;
    final int LEFT = 3;
    final int UP = 0;
    final int DOWN = 2;

    public void run() {
        Graphics g = getGraphics();
        while (running) {
            int keyStates = getKeyStates();
            int dx = 0;
            int dy = 0;
            if ((keyStates & 4) != 0) {
                dx -= speed;
                direction = LEFT;
            } else if ((keyStates & 0x20) != 0) {
                dx += speed;
                direction = RIGHT;
            } else if ((keyStates & 2) != 0) {
                dy -= speed;
                direction = DOWN;
            } else if ((keyStates & 0x40) != 0) {
                dy += speed;
                direction = UP;
            }
            if ((keyStates & GAME_A_PRESSED) != 0 && bp.canGetBomb()) {
                Bomb b = bp.GetBomb();
                b.initBomb(player.getX() + spriteWidth / 2, player.getY()
                        + spriteHeight / 2, 6);
            }
            bp.tickAllTaken();
            player.changeDirection(direction);
            g.setColor(255, 255, 255);
            g.fillRect(0, 0, getWidth(), getHeight());
            layerManager.paint(g, 0, 0);
            flushGraphics();

            boolean movePlayer = true;

            int halfW = getWidth() / 2;
            int halfH = getHeight() / 2;
            int dx1 = 0;
            int dy1 = 0;
            if ((player.getX() > halfW
                    && (direction == RIGHT && backgroundLayer.getWidth()
                    - player.getX() + backgroundLayer.getX() > halfW) || (player.getX() < halfW && direction == LEFT && (player.getX() - backgroundLayer.getX()) > halfW))) {
                moveBckGrnd(-dx, 0);
                movePlayer = false;
                dx1 = -dx;
                dy1 = 0;
            } else if ((player.getY() > halfH
                    && (direction == UP && backgroundLayer.getHeight()
                    - player.getY() + backgroundLayer.getY() > halfH) || (player.getY() < halfH && direction == DOWN && (player.getY() - backgroundLayer.getY()) > halfH))) {
                moveBckGrnd(0, -dy);
                dx1 = 0;
                dy1 = -dy;
                movePlayer = false;
            }


            for (int i = 0; i < mobs.length; i++) {
                mobs[i].move();
            }

            if (movePlayer) {

                player.moveWith(dx, dy);
            } else if (dx != 0 || dy != 0) {
                player.getPlayer().nextFrame();
            }
            if (dx == dy && dx == 0) {
                player.getPlayer().setFrame(0);
            }

            if (player.collidesWith(backgroundLayer)) {

                if (movePlayer) {
                    player.setPosition(lastx, lasty);
                } else {
                    moveBckGrnd(-dx1, -dy1);
                }

            } else {
                lastx = player.getX();
                lasty = player.getY();
            }
            try {
                Thread.sleep(5);
            } catch (InterruptedException ie) {
            }
        }
    }

    private void moveBckGrnd(int dx, int dy) {

        backgroundLayer.move(dx, dy);

        for (int i = 0; i < mobs.length; i++) {
            mobs[i].move(dx, dy);
        }

//        b.move(dx, dy);
    }
}
