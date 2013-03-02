import java.util.Random;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.TiledLayer;

public class Map extends TiledLayer {

	public static final int breakable = 1;
	public static final int nonbreak = 2;
	private static Random r = new Random();
	private int[][] map;
	// private int maxMobs; // maximum number of monsters for this level
	private int x, y;

	public Map(Image tiles, int width, int height) {
		super(width, height, tiles, tiles.getWidth() / 2, tiles.getHeight());

		x = y = 0;

		map = new int[width][height];
		// make number of mobs depend on the size too?!
		// maxMobs = level * 4;

		// generating map
		fillMap();

		// filling the TiledLayer
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[0].length; j++) {
				setCell(i, j, map[i][j]);
			}
		}
	}

	private void fillMap() {
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[0].length; j++) {
				if (j == 0 || j == map[0].length - 1 || i == 0
						|| i == map.length - 1) {
					map[i][j] = nonbreak;
				} else if (i < 3 && j < 3) {
					// starting cell
					map[i][j] = 0;
				} else if (i % 2 == 1 && j % 2 == 1) {
					map[i][j] = nonbreak;
				} else {
					if (r.nextInt() > 1000000000) {
						map[i][j] = breakable;
					} else {
						map[i][j] = 0;
					}
				}
			}
		}
	}

	public void move(int dx, int dy) {
		x += dx;
		y += dy;
		super.setPosition(x, y);
	}

	public boolean clearCell(int i, int j) {
		if (map[i][j] == breakable) {
			map[i][j] = 0;
			return true;
		} else {
			return false;
		}
	}

	public boolean isFreeCell(int i, int j) {
		return map[i][j] == 0;
	}
}
