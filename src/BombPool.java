public class BombPool {
	private Bomb[] pool;
	private boolean[] taken;
	private int available;

	public BombPool(int size) {
		pool = new Bomb[size];
		for (int i = 0; i < size; i++)
			pool[i] = new Bomb();
		taken = new boolean[size];
		available = size;
	}

	public boolean canGetBomb(int x, int y) {
		if (available <= 0)
			return false;
		x /= 30;
		y /= 30;
		for (int i = 0; i < pool.length; i++) {
			if (taken[i]) {
				if (pool[i].getArray()[0].getX() / 30 == x
						&& pool[i].getArray()[0].getY() / 30 == y)
					
					return false;
			}
		}
		return true;
	}

	public Bomb GetBomb() {
		for (int i = 0; i < pool.length; i++) {
			if (!taken[i]) {
				taken[i] = true;
				available--;
				return pool[i];
			}
		}
		return null;
	}

	public void moveAll(int dx, int dy) {
		for (int i = 0; i < pool.length; i++)
			if (taken[i])
				pool[i].move(dx, dy);

	}

	public void release() {
		for (int i = 0; i < pool.length; i++) {
			taken[i] = false;
			available++;
		}
	}

	public void tickAllTaken() {
		for (int i = 0; i < pool.length; i++) {
			if (taken[i]) {
				if (pool[i].tick()) {
					taken[i] = false;
					available++;
				}
			}
		}
	}
}