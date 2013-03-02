
import java.util.Random;

import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Layer;

public class Monster {

    private int x, y;
    private Random r = new Random();

    public Monster(int x, int y) {
        this.x = x;
        this.y = y;
        Image images[] = new Image[1];
        try {
            images[0] = Image.createImage("/monster.png");
        } catch (Exception ioe) {
            System.out.println("unable to load image");
        }
        int framesPerImage = 3;
        int frameSpeed = 5;
        int seq[] = new int[frameSpeed * framesPerImage];
        for (int i = 0; i < seq.length; i++) {
            seq[i] = i / frameSpeed;
        }

        int spriteWidth = 30;
        int spriteHeight = 30;
        monster = new Character(images, seq, spriteWidth, spriteHeight, x, y);
        monster.defineCollisionRectangle(5, spriteHeight / 4, spriteWidth - 10, 2 * spriteHeight / 3);
    }

    public void changeDirection(int direction) {
        monster.changeDirection(direction);
    }

    public int getNextDir() {
        /*	0 right
         *  1 up
         *  2 left
         *  3 down
         */
        double p = (r.nextInt() - 1.0 * Integer.MIN_VALUE)
                / (Integer.MAX_VALUE - Integer.MIN_VALUE);
        if (p > 0.8) {
            return (((dir - 2) % 4) + 4) % 4;
        } else if (p > 0.40) {
            // left
            return (dir + 1) % 4;
        } else {
            // right
            return (((dir - 1) % 4) + 4) % 4;
        }
    }

    public void move(int dx, int dy) {
        monster.setPosition(x + dx, y + dy);
        if (!collidesWith(MainGameCanvas.backgroundLayer)) {
            x += dx;
            y += dy;
        } else {
            monster.setPosition(x - dx, y - dy);
        }
    }
    
    private int[] dx = {1, -1, 0, 0};
    private int[] dy = {0, 0, 1, -1};
    boolean moved;

    public void move() {

        monster.moveWith(dx[dir], dy[dir]);
        moved = true;

        if (collidesWith(MainGameCanvas.backgroundLayer)) {
            monster.setPosition(x, y);
            dir = getNextDir();
            moved = false;
        } else {
            // change cur direction?
            double p = (r.nextInt() * 1.0 - 1.0 * Integer.MIN_VALUE)
                    / (1.0 * Integer.MAX_VALUE - Integer.MIN_VALUE);
            if (p > 0.999) {
                dir = getNextDir();
            } else {
                x += dx[dir];
                y += dy[dir];
            }
        }

    }

    private boolean collidesWith(Layer obj) {
        return monster.collidesWith(obj);
    }

    public Character getMonster() {
        return monster;
    }
    private int dir = 0;
    private Character monster;
}
