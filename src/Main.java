import javax.microedition.lcdui.Display;
import javax.microedition.midlet.MIDlet;

public class Main extends MIDlet {

	public Main() {
	}

	public void startApp() {
		MainGameCanvas myCanvas = new MainGameCanvas(1);
		Display.getDisplay(this).setCurrent(myCanvas);
		myCanvas.start();
	}

	public void pauseApp() {
	}

	public void destroyApp(boolean flag) {
	}
}
