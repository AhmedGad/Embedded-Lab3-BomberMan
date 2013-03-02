
import java.io.IOException;

import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;

public class Bomb {

    private int x, y, time; // time is the number of cycles (Thread sleeps)
    private int cyclesPerImage = 10;
    private int cntCycles = 0;
    private Sprite bomb;
    private int spriteWidth = 20;
    private int spriteHeight = 25;
    private boolean active;

    public Bomb(int x, int y, int time) {
        this.x = x;
        this.y = y;
        this.time = time;
        Image imgs[] = new Image[2];
        try {
            imgs[0] = Image.createImage("/b1.png");
            imgs[1] = Image.createImage("/b2.png");
        } catch (IOException e) {
        }
        bomb = new Sprite(imgs[0], spriteWidth, spriteHeight);
        bomb.setFrameSequence(new int[]{0, 1, 2, 3, 4, 5});
        bomb.setPosition(x, y);
        active = true;
    }

    public Sprite getBomb() {
        return bomb;
    }

    private void explode() {

        active = false;
    }

    public void tick() {
        if (!active) {
            return;
        }
        cntCycles++;
        if (cntCycles % cyclesPerImage == 0) {
            time--;
//			bomb.setPosition(bomb.getX()+1, bomb.getY());
            bomb.nextFrame();
            cntCycles = 0;
        }
//		if (time == 0)
//			explode();
    }
}
