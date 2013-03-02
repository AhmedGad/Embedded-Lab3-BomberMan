import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Layer;

public class Monster {

    public Monster(int map[][]) {
        this.map = map;
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
        monster = new Character(images, seq, spriteWidth, spriteHeight, 10, 10);
    }

    public void changeDirection(int direction) {
        monster.changeDirection(direction);
    }

    public boolean collidesWith(Layer obj) {
        return monster.collidesWith(obj);
    }
    private Character monster;
    private int map[][];
}
