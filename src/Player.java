
import java.io.IOException;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Layer;

public class Player {

    private Character player;

    public Player() {
        Image images[] = new Image[4];
        try {
            images[0] = Image.createImage("/1.png");
            images[1] = Image.createImage("/2.png");
            images[2] = Image.createImage("/3.png");
            images[3] = Image.createImage("/4.png");
        } catch (Exception ioe) {
            System.out.println("unable to load image");
        }
        int framesPerImage = 10;
        int frameSpeed = 5;
        int seq[] = new int[frameSpeed * framesPerImage];
        for (int i = 0; i < seq.length; i++) {
            seq[i] = i / frameSpeed;
        }

        int spriteWidth = 25;
        int spriteHeight = 30;
        player = new Character(images, seq, spriteWidth, spriteHeight, 35, 35);

        player.defineCollisionRectangle(5, spriteHeight / 2, spriteWidth - 10, spriteHeight / 2);

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
            System.out.println(deadCnt);
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

    void die() {
        try {
            dead = true;
            player.setImage(Image.createImage("/dead.png"), 30, 30);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
