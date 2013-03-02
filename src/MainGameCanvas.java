import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.GameCanvas;
import javax.microedition.lcdui.game.LayerManager;

public class MainGameCanvas extends GameCanvas implements Runnable {

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
		backgroundLayer = new Map(backgroundTilesImage, 16, 16, 1);
		layerManager.append(backgroundLayer);

		layerManager.setViewWindow(0, 0, getWidth(), getHeight());
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
							- player.getX() + backgroundLayer.getX() > halfW) || (player
					.getX() < halfW && direction == LEFT && (player.getX() - backgroundLayer
					.getX()) > halfW))) {
				backgroundLayer.move(-dx, 0);
				movePlayer = false;
				dx1 = -dx;
				dy1 = 0;
			} else if ((player.getY() > halfH
					&& (direction == UP && backgroundLayer.getHeight()
							- player.getY() + backgroundLayer.getY() > halfH) || (player
					.getY() < halfH && direction == DOWN && (player.getY() - backgroundLayer
					.getY()) > halfH))) {
				backgroundLayer.move(0, -dy);
				dx1 = 0;
				dy1 = -dy;
				movePlayer = false;
			}

			if (movePlayer) {
				player.moveWith(dx, dy);
			} else {
				player.getPlayer().nextFrame();
			}

			if (player.collidesWith(backgroundLayer)) {
				if (movePlayer)
					player.setPosition(lastx, lasty);
				else
					backgroundLayer.move(-dx1, -dy1);
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