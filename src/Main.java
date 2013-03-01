import javax.microedition.lcdui.Display;
import javax.microedition.midlet.MIDlet;

public class Main extends MIDlet
{

    public Main()
    {
    }

    public void startApp()
    {
        AnotherGameCanvas myCanvas = new AnotherGameCanvas();
        Display.getDisplay(this).setCurrent(myCanvas);
        myCanvas.start();
    }

    public void pauseApp()
    {
    }

    public void destroyApp(boolean flag)
    {
    }
}
