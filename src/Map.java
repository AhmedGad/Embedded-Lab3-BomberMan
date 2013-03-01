import java.util.Random;

import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.TiledLayer;

import com.sun.midp.io.j2me.storage.File;

public class Map extends TiledLayer {
	public static final int breakable = 1;
	public static final int nonbreak = 2;
	private static Random r = new Random();
	private int[][] map;
	private int maxMobs; // maximum number of monsters for this level

	public Map(Image tiles, int width, int height, int level) {
		super(width / (tiles.getWidth()/2), height / tiles.getHeight(), tiles,
				tiles.getWidth()/2, tiles.getHeight());
		map = new int[width / (tiles.getWidth()/2)][height / tiles.getHeight()];
		// make number of mobs depend on the size too?!
		maxMobs = level * 4;

		// generating map
		fillMap();

		// filling the TiledLayer
		for (int i = 0; i < map.length; i++)
			for (int j = 0; j < map[0].length; j++)
				setCell(i, j, map[i][j]);
	}

	private void fillMap() {
		for (int i = 0; i < map.length; i++) {
//			System.out.println(i +" i");
			for (int j = 0; j < map[0].length; j++) {
//				System.out.println(j+ " j");
				if (i == 0 && j == 0) {
					// starting cell
					map[i][j] = 0;
					continue;
				}
				if (i % 2 == 1 && j % 2 == 1) {
					map[i][j] = nonbreak;
				} else {
					if (r.nextInt() > -1000000) {
						map[i][j] = breakable;
					} else {
						map[i][j] = 0;
					}
				}
			}
		}
	}

	public boolean clearCell(int i, int j) {
		if (map[i][j] == breakable) {
			map[i][j] = 0;
			return true;
		} else {
			return false;
		}
	}

}
