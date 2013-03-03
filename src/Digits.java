
import java.io.IOException;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;
import javax.microedition.lcdui.game.TiledLayer;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Ahmed
 */
public class Digits extends Sprite {

    public Digits(int d) throws IOException {
        super(Image.createImage("/numbers.png"), 20, 20);
        
        this.setFrameSequence(new int[]{d});
    }
}
