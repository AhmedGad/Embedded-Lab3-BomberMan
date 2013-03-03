
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.midlet.MIDlet;

public class Main extends MIDlet implements CommandListener {

    public Main() {
    }
    Command restart;

    public void startApp() {
        restart = new Command("restart", Command.SCREEN, 0);

        MainGameCanvas myCanvas = new MainGameCanvas(3);
        myCanvas.addCommand(restart);
        myCanvas.setCommandListener(this);
        Display.getDisplay(this).setCurrent(myCanvas);
        myCanvas.start();
    }

    public void pauseApp() {
    }

    public void destroyApp(boolean flag) {
    }

    public void commandAction(Command c, Displayable d) {
        MainGameCanvas myCanvas = new MainGameCanvas(3);
        myCanvas.addCommand(restart);
        myCanvas.setCommandListener(this);
        Display.getDisplay(this).setCurrent(myCanvas);
        myCanvas.start();
    }
}
