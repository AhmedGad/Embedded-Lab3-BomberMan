/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import javax.microedition.lcdui.*;

import javax.microedition.midlet.*;
import javax.microedition.lcdui.game.GameCanvas;
import javax.microedition.lcdui.game.Layer;
import javax.microedition.lcdui.game.LayerManager;
import javax.microedition.lcdui.game.Sprite;
import javax.microedition.lcdui.game.TiledLayer;

/**
 * @author Ahmed
 */
public class test extends MIDlet {

    public void startApp() {
        AnotherGameCanvas myCanvas = new AnotherGameCanvas();
        Display.getDisplay(this).setCurrent(myCanvas);
        myCanvas.start();
    }

    public void pauseApp() {
    }

    public void destroyApp(boolean unconditional) {
    }
}

class AnotherGameCanvas extends GameCanvas implements Runnable {

    private boolean running;
    private int x, y;
    private Image images[] = new Image[4];
    private Image backgroundTilesImage = null;
    private Sprite player;
    private BackgroundLayer backgroundLayer = null;
    private LayerManager layerManager = null;

    public AnotherGameCanvas() {
        super(true);

        x = getWidth() / 2;
        y = getHeight() / 2;
        ok = true;
        try {
            images[0] = Image.createImage("/1.png");
            images[1] = Image.createImage("/2.png");
            images[2] = Image.createImage("/3.png");
            images[3] = Image.createImage("/4.png");
            backgroundTilesImage = Image.createImage("/front.png");
        } catch (Exception ioe) {
            ok = false;
            System.out.println("unable to load image");
        }
        layerManager = new LayerManager();
        if (ok) {


            // create the orb sprite and add it to the layer manager
            player = new Sprite(images[0], spriteWidth, spriteHeight);
            int seq[] = new int[50];
            for (int i = 0; i < seq.length; i++) {
                seq[i] = i / 5;
            }

            player.setFrameSequence(seq);
            layerManager.append(player);

            // add the background layer as well
            backgroundLayer = new BackgroundLayer(backgroundTilesImage, getWidth(), getHeight());
            layerManager.append(backgroundLayer);
        }
    }

    public void start() {
        running = true;
        Thread t = new Thread(this);
        t.start();
    }
    boolean ok;
    int direction = 0;
    int spriteWidth = 25,spriteHeight = 30;
            
            
    public void run() {
        Graphics g = getGraphics();

        while (running) {
            // handle the state of keys
            int keyStates = getKeyStates();
            int prevDir = direction;
            int xx = x, yy = y;

            if ((keyStates & LEFT_PRESSED) != 0) {
                x--;
                direction = 3;
            } else if ((keyStates & RIGHT_PRESSED) != 0) {
                x++;
                direction = 1;
            } else if ((keyStates & UP_PRESSED) != 0) {
                y--;
                direction = 2;
            } else if ((keyStates & DOWN_PRESSED) != 0) {
                y++;
                direction = 0;
            }

            if (x < 0) {
                x = getWidth();
            }
            if (x > getWidth()) {
                x = 0;
            }

            if (y < 0) {
                y = getHeight();
            }
            if (y > getHeight()) {
                y = 0;
            }


            if (ok) {
                if (direction != prevDir) {
                    player.setImage(images[direction], spriteWidth, spriteHeight);

                }
                // update the sprite position and animation frame
                player.setPosition(x, y);
                if (xx != x || yy != y) {
                    player.nextFrame();
                }
            }
            // clear the display
            g.setColor(255, 255, 255);
            g.fillRect(0, 0, getWidth(), getHeight());

            // draw the layers
            layerManager.paint(g, 0, 0);

            // flush the graphics buffer (GameCanvas will take care of painting)
            flushGraphics();

            // sleep a little
            try {
                Thread.sleep(5);
            } catch (InterruptedException ie) {
            }
        }

    }
}

class BackgroundLayer extends TiledLayer {

    private static final int HORIZONTAL = 1;
    private static final int VERTICAL = 2;
    private static final int CROSS = 3;

    public BackgroundLayer(Image tiles, int width, int height) {
        super(20, 20, tiles, 20, 30);
        // draw horizontal lines
        for (int y = 0; y < height / getCellHeight(); y += 3) {
            System.out.println(y);
            fillCells(0, y, width / getCellWidth() - 2, 1, HORIZONTAL);
        }

        // draw vertical lines
        for (int x = 0; x < 20; x += 5) {
            fillCells(x, 0, 1, 19, VERTICAL);
        }

        // fill in the cross pieces
        for (int y = 0; y < 20; y += 5) {
            for (int x = 0; x < 20; x += 5) {
                setCell(x, y, CROSS);
            }
        }
    }
}
