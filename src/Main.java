
import java.io.IOException;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.midlet.MIDlet;

public class Main extends MIDlet implements CommandListener {

    public Main() {
    }
    Command restart;
    int currentLevel = 1;

    public void startApp() {
        try {
            restart = new Command("restart", Command.SCREEN, 0);
            nextLevel(0);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void pauseApp() {
    }

    public void destroyApp(boolean flag) {
    }

    public void commandAction(Command c, Displayable d) {
        try {
            nextLevel(0);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    MainGameCanvas myCanvas;

    public void nextLevel(int lev) throws IOException {
        if (lev < 3) {
            if (myCanvas != null) {
                myCanvas.running = false;
            }
            lev++;
            myCanvas = new MainGameCanvas(lev);
            myCanvas.main = this;
            myCanvas.addCommand(restart);
            myCanvas.setCommandListener(this);
            Display.getDisplay(this).setCurrent(myCanvas);
            myCanvas.start();
        }
    }
}
