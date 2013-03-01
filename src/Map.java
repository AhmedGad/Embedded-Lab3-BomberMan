import java.util.Random;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.TiledLayer;

public class Map extends TiledLayer
{

    public Map(Image tiles, int width, int height, int level)
    {
        super(width, height, tiles, tiles.getWidth() / 2, tiles.getHeight());
        map = new int[width][height];
        maxMobs = level * 4;
        fillMap();
        for(int i = 0; i < map.length; i++)
        {
            for(int j = 0; j < map[0].length; j++)
                setCell(i, j, map[i][j]);

        }

    }

    private void fillMap()
    {
        for(int i = 0; i < map.length; i++)
        {
            for(int j = 0; j < map[0].length; j++)
            {
                if(i == 0 && j == 0)
                {
                    map[i][j] = 0;
                    continue;
                }
                if(i % 2 == 1 && j % 2 == 1)
                {
                    map[i][j] = 2;
                    continue;
                }
                if(r.nextInt() > 0x3b9aca00)
                    map[i][j] = 1;
                else
                    map[i][j] = 0;
            }

        }

    }

    public boolean clearCell(int i, int j)
    {
        if(map[i][j] == 1)
        {
            map[i][j] = 0;
            return true;
        } else
        {
            return false;
        }
    }

    public static final int breakable = 1;
    public static final int nonbreak = 2;
    private static Random r = new Random();
    private int map[][];
    private int maxMobs;

}
