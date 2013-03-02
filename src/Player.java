
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
    }

    public Character getPlayer() {
        return player;
    }

    public void moveWith(int dx, int dy) {
        player.moveWith(dx, dy);
    }

    public void changeDirection(int direction) {
        player.changeDirection(direction);
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

    public int getY() {
        return player.getY();
    }
}
