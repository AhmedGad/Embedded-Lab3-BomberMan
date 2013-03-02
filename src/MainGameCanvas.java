
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.GameCanvas;
import javax.microedition.lcdui.game.LayerManager;

public class MainGameCanvas extends GameCanvas
        implements Runnable {

    private boolean running;
    private Player player;
    private Map backgroundLayer;
    private LayerManager layerManager;
    private Image backgroundTilesImage;
    int direction = 0;
    int spriteWidth = 25, spriteHeight = 30;
    boolean init;
    int lastx = 0, lasty = 0;
    int speed;

    public MainGameCanvas() {
        super(true);
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
        backgroundLayer = new Map(backgroundTilesImage, 100, 100, 1);
        layerManager.append(backgroundLayer);
    }

    public void start() {
        running = true;
        Thread t = new Thread(this);
        t.start();
    }

    public void run() {
        Graphics g = getGraphics();
        while (running) {
            int keyStates = getKeyStates();
            int dx = 0;
            int dy = 0;
            if ((keyStates & 4) != 0) {
                dx -= speed;
                direction = 3;
            } else if ((keyStates & 0x20) != 0) {
                dx += speed;
                direction = 1;
            } else if ((keyStates & 2) != 0) {
                dy -= speed;
                direction = 2;
            } else if ((keyStates & 0x40) != 0) {
                dy += speed;
                direction = 0;
            }

            player.changeDirection(direction);
            player.moveWith(dx, dy);
            g.setColor(255, 255, 255);
            g.fillRect(0, 0, getWidth(), getHeight());
            layerManager.paint(g, 0, 0);
            flushGraphics();

            if (player.collidesWith(backgroundLayer)) {
                player.setPosition(lastx, lasty);
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
}
