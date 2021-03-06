
import java.io.IOException;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Layer;

public class Player {

    private Character player;

    public Player(int lev) {
        Image images[] = new Image[4];
        try {
            images[0] = Image.createImage("/" + lev + "1.png");
            images[1] = Image.createImage("/" + lev + "2.png");
            images[2] = Image.createImage("/" + lev + "3.png");
            images[3] = Image.createImage("/" + lev + "4.png");
        } catch (Exception ioe) {
            System.out.println("unable to load image");
        }
        int framesPerImage = 3;
        int frameSpeed = 5;
        int seq[] = new int[frameSpeed * framesPerImage];
        for (int i = 0; i < seq.length; i++) {
            seq[i] = i / frameSpeed;
        }

        int spriteWidth = 25;
        int spriteHeight = 30;
        player = new Character(images, seq, spriteWidth, spriteHeight, 35, 35);

        player.defineCollisionRectangle(5, spriteHeight / 2, spriteWidth - 10,
                spriteHeight / 2);

        dead = false;
        deadCnt = 0;
    }

    public Character getPlayer() {
        return player;
    }

    public void moveWith(int dx, int dy) {
        if (!dead) {
            player.moveWith(dx, dy);
        }
    }

    public void changeDirection(int direction) {
        if (!dead) {
            player.changeDirection(direction);
        }
    }

    public boolean collidesWith(Layer obj) {
        return player.collidesWith(obj);
    }

    void setPosition(int lastx, int lasty) {
        player.setPosition(lastx, lasty);
    }

    public int getX() {
        return player.getX();
    }

    public void callIfDie() {
        if (player.getFrame() < 8) {
            deadCnt++;
            if (deadCnt % 50 == 0) {
                player.nextFrame();
            }
        } else {
            player.setVisible(false);
        }
    }

    public int getY() {
        return player.getY();
    }
    boolean dead;
    int deadCnt;

    void kill() {
        try {
            dead = true;
            player.setImage(Image.createImage("/dead.png"), 30, 30);
            int seq[] = new int[9];
            for (int i = 0; i < 9; i++) {
                seq[i] = i;
            }
            player.setFrameSequence(seq);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
