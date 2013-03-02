import java.io.IOException;

import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;

public class Bomb {

	private int x, y, time; // time is the number of cycles (Thread sleeps)
	private int cyclesPerImage = 10;
	private int cntCycles = 0;
	private Sprite[] bomb = new Sprite[5];
	private int spriteWidth = 20;
	private int spriteHeight = 25;
	private boolean active;
	private Image imgs[] = new Image[2];
	private int[] seq1 = { 2, 5, 1, 4, 0, 3 };
	private int[] seq2 = { 0, 1, 6, 7 };

	public Bomb() {
		try {
			imgs[0] = Image.createImage("/b1.png");
			imgs[1] = Image.createImage("/b2.png");
		} catch (IOException e) {

		}
		bomb[0] = new Sprite(imgs[0], spriteWidth, spriteHeight);
		bomb[0].setVisible(false);
		bomb[0].setFrameSequence(seq1);
		for (int i = 1; i < 5; i++) {
			bomb[i] = new Sprite(imgs[1], 30, 30);
			bomb[i].setVisible(false);
			bomb[i].setFrameSequence(seq2);
		}
	}

	public void initBomb(int x, int y, int time) {
		this.x = 30 * (x / 30)+5;
		this.y = 30 * (y / 30)+2;
		this.time = time;
		bomb[0].setPosition(this.x, this.y);
		bomb[0].setVisible(true);
		bomb[0].setImage(imgs[0], spriteWidth, spriteHeight);
		active = true;
		done = false;
	}

	public Sprite[] getArray() {
		return bomb;
	}

	private int[] dx = { 1, -1, 0, 0 };
	private int[] dy = { 0, 0, 1, -1 };

	private void explode() {
		active = false;
		for (int i = 1; i < 5; i++)
			bomb[i].setVisible(true);
		bomb[0].setImage(imgs[1], 30, 30);
		bomb[0].setFrameSequence(seq2);
		int px = x / 30;
		int py = y / 30;
		for (int i = 0; i < 4; i++) {
			int nx = px + dx[i];
			int ny = py + dy[i];
			if (MainGameCanvas.backgroundLayer.clearCell(nx, ny)) {
				bomb[1 + i].setPosition(nx * 30, ny * 30);
			} else {
				bomb[1 + i].setVisible(false);
			}
		}
	}

	boolean done;

	public boolean tick() {
		cntCycles++;
		if (cntCycles % cyclesPerImage == 0) {
			time--;
			if (active)
				bomb[0].nextFrame();
			else
				for (int i = 0; i < 5; i++)
					bomb[i].nextFrame();
			cntCycles = 0;
		}
		if (time == 0 && active) {
			explode();
			time = 6;
		} else if (time == 0) {
			for (int i = 0; i < 5; i++)
				bomb[i].setVisible(false);
			done = true;
		}
		return done;
	}
}
