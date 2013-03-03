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
		System.out.println(available);
		return available > 0;
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