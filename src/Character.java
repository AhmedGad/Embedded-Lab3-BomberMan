import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.*;

public class Character extends Sprite
{

    public Character(Image imgs[], int seq[], int spriteWidth, int spriteHeight)
    {
        super(imgs[0], spriteWidth, spriteHeight);
        direction = 0;
        super.setFrameSequence(seq);
        this.seq = seq;
        this.imgs = imgs;
        x = y = 35;
        width = spriteWidth;
        height = spriteHeight;
        super.setPosition(x, y);
        super.defineCollisionRectangle(5, spriteHeight / 2, spriteWidth - 10, spriteHeight / 2);
    }

    public void changeDirection(int direction)
    {
        if(direction != this.direction)
        {
            super.setImage(imgs[direction], width, height);
            this.direction = direction;
        }
    }

    public void moveWith(int dx, int dy)
    {
        if(dx != 0 || dy != 0)
        {
            x += dx;
            y += dy;
            super.nextFrame();
            super.setPosition(x, y);
        }
    }

    public boolean collidesWith(Layer obj)
    {
        if(obj instanceof TiledLayer)
            return super.collidesWith((TiledLayer)obj, false);
        else
            return super.collidesWith((Sprite)obj, false);
    }

    public void setPosition(int x, int y)
    {
        this.x = x;
        this.y = y;
        super.setPosition(x, y);
    }

    private int width;
    private int height;
    private int x;
    private int y;
    private int seq[];
    private Image imgs[];
    int direction;
}
