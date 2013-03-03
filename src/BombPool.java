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

	public boolean canGetBomb() {
		return available > 0;
	}

	public Bomb GetBomb() {
		for (int i = 0; i < pool.length; i++) {
			if (taken[i] == false) {
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

	public void release(Bomb b) {
		for (int i = 0; i < pool.length; i++) {
			if (taken[i]) {
				taken[i] = false;
				available++;
				pool[i] = b;
			}
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