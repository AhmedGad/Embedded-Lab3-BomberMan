
import javax.microedition.lcdui.game.Sprite;

public class Bomb implements Runnable {

    private int x, y, time; // time is the number of cycles (Thread sleeps)
    // before exploding
    private Sprite Bomb;

    public Bomb(int x, int y, int time) {
        super();
        this.x = x;
        this.y = y;
        this.time = time;
        new Thread(this).start();
    }

    private void explode() {
    }

    public void run() {
        while (true) {
            if (time == 0) {
                explode();
                break;
            }

            time--;

            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
