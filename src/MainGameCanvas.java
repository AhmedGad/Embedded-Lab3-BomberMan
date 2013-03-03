
import java.io.IOException;
import java.util.Random;
import java.util.Vector;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.GameCanvas;
import javax.microedition.lcdui.game.LayerManager;
import javax.microedition.lcdui.game.Sprite;

public class MainGameCanvas extends GameCanvas implements Runnable {

    public boolean running;
    public static Player player;
    public static Monster[] mobs;
    public static Map backgroundLayer;
    private LayerManager layerManager;
    private Image backgroundTilesImage;
    int direction = 0;
    int spriteWidth = 25, spriteHeight = 30;
    boolean init;
    int lastx = 0, lasty = 0;
    int speed;
    int width = 14;
    int height = 14;
    int numberOfBombs;
    BombPool bp;
    Digits d[];
    Sprite winner;
    int lev;
    int wrongCnt = 0;

    public MainGameCanvas(int lev) throws IOException {
        super(true);
        this.lev = lev;
        if (lev == 1) {
            spirit = 3;
            score = 0;
        }
        mobSlowness = 4 - lev;
        generateEquation();
        this.numberOfBombs = lev;
        init = true;
        speed = 1;

        winner = new Sprite(Image.createImage("/winner.png"), 90, 62);

        layerManager = new LayerManager();
        layerManager.append(winner);
        winner.setVisible(false);

        player = new Player(lev);
        layerManager.append(player.getPlayer());
        try {
            backgroundTilesImage = Image.createImage("/tiles.png");
        } catch (Exception ioe) {
            System.out.println("unable to load image");
        }
        backgroundLayer = new Map(backgroundTilesImage, width, height);
        addMonsters(lev == 1 ? 5 : lev == 2 ? 7 : 10);
        bp = new BombPool(lev);
        for (int i = 0; i < lev; i++) {
            Bomb b = bp.GetBomb();
            for (int j = 0; j < 5; j++) {
                layerManager.append(b.getArray()[j]);
            }
        }
        bp.release();

        layerManager.append(backgroundLayer);
        addDigits();

    }
    Main main;

    public void addMonsters(int NumOfMonsters) {
        mobs = new Monster[NumOfMonsters];
        int cnt = 0;
        for (int i = 0; i < width && cnt < NumOfMonsters; i++) {
            for (int j = 0; j < height && cnt < NumOfMonsters; j++) {
                if (i < 4 && j < 4) {
                    continue;
                }
                if (backgroundLayer.isFreeCell(i, j)) {
                    mobs[cnt] = new Monster(i * 30, j * 30);
                    layerManager.append(mobs[cnt++].getMonster());
                }
            }
        }
    }

    public void start() throws IOException {
        running = true;
        Thread t = new Thread(this);
        t.start();
    }
    final int RIGHT = 1;
    final int LEFT = 3;
    final int UP = 0;
    final int DOWN = 2;
    private int mobSlowness = 4, mobCnt = 0;
    boolean dead = false;
    static int cnt = 0;

    public void run() {
        Graphics g = getGraphics();
        while (running) {
            dead = player.dead;
            if (!dead) {
                int keyStates = getKeyStates();
                int dx = 0;
                int dy = 0;
                if ((keyStates & 4) != 0) {
                    dx -= speed;
                    direction = LEFT;
                } else if ((keyStates & 0x20) != 0) {
                    dx += speed;
                    direction = RIGHT;
                } else if ((keyStates & 2) != 0) {
                    dy -= speed;
                    direction = DOWN;
                } else if ((keyStates & 0x40) != 0) {
                    dy += speed;
                    direction = UP;
                }
                cnt++;
                if ((keyStates & GAME_A_PRESSED) != 0
                        && bp.canGetBomb(player.getX() + spriteWidth / 2,
                        player.getY() + spriteHeight / 2)) {
                    Bomb b = bp.GetBomb();
                    b.initBomb(player.getX() + spriteWidth / 2, player.getY()
                            + spriteHeight / 2, 15);
                }
                if ((keyStates & FIRE_PRESSED) != 0) {
                    for (int i = 0; i < d.length; i++) {
                        if (player.collidesWith(d[i])) {
                            d[i].setVisible(false);
                            if (i == winDigit) {
                                running = false;
                                winner.setVisible(true);
                                winner.setPosition(player.getX(), player.getY());
                            } else {
                                wrongCnt++;
                                if (wrongCnt == 3) {
                                    player.kill();
                                }
                            }
                        }
                    }


                }

                bp.tickAllTaken();

                boolean movePlayer = true;

                int dx1 = 0;
                int dy1 = 0;
                if (moveScreen()) {
                    moveBckGrnd(-dx, -dy);
                    dx1 = -dx;
                    dy1 = -dy;
                    movePlayer = false;
                }

                mobCnt++;
                System.out.println(mobSlowness);
                if (mobCnt % mobSlowness == 0) {
                    for (int i = 0; i < mobs.length; i++) {
                        mobs[i].move();
                    }
                    mobCnt %= 10000;
                }

                if (movePlayer) {

                    player.moveWith(dx, dy);
                } else if (dx != 0 || dy != 0) {
                    player.getPlayer().nextFrame();
                }
                if (dx == dy && dx == 0) {
                    player.getPlayer().setFrame(0);
                }

                if (player.collidesWith(backgroundLayer)) {

                    if (movePlayer) {
                        player.setPosition(lastx, lasty);
                    } else {
                        moveBckGrnd(-dx1, -dy1);
                    }

                } else {
                    lastx = player.getX();
                    lasty = player.getY();
                }

                for (int i = 0; i < mobs.length; i++) {
                    if (mobs[i].getMonster().isVisible()
                            && player.collidesWith(mobs[i].getMonster())) {
                        player.kill();
                        dead = true;
                    }
                }

            } else {
                player.callIfDie();
            }

            player.changeDirection(direction);
            g.setColor(255, 255, 255);
            g.fillRect(0, 0, getWidth(), getHeight());
            setHeader(g);
            layerManager.paint(g, 0, 20);
            flushGraphics();

            try {
                if (!running) {
                    Thread.sleep(2000);
                    main.nextLevel(lev);
                }
                Thread.sleep(5);

            } catch (Exception ie) {
                ie.printStackTrace();
            }
        }
    }

    boolean moveScreen() {
        int halfW = getWidth() / 2;
        int halfH = getHeight() / 2;
        return ((player.getX() > halfW
                && (direction == RIGHT && backgroundLayer.getWidth()
                - player.getX() + backgroundLayer.getX() > halfW) || (player.getX() < halfW && direction == LEFT && (player.getX() - backgroundLayer.getX()) > halfW)))
                || ((player.getY() > halfH
                && (direction == UP && backgroundLayer.getHeight()
                - player.getY() + backgroundLayer.getY() > halfH) || (player.getY() < halfH && direction == DOWN && (player.getY() - backgroundLayer.getY()) > halfH)));
    }
    static int score, spirit, winDigit;
    String eq;

    void generateEquation() {
        Random r = new Random(System.currentTimeMillis());
        int d1 = r.nextInt(10), d2 = r.nextInt(10), d3 = r.nextInt(10), d4 = r.nextInt(10);
        while (d1 == 0) {
            d1 = r.nextInt(10);
        }
        while (d3 == 0) {
            d3 = r.nextInt(10);
        }
        String op = "+-*";
        char oper = op.charAt(r.nextInt(3));
        int n1 = d1 * 10 + d2, n2 = d3 * 10 + d4;
        String tmp = n1 + "" + oper + ""
                + n2 + "=";
        int res = 0;
        if (oper == '+') {
            res = n1 + n2;
        } else if (oper == '-') {
            res = n1 - n2;
        } else if (oper == '*') {
            res = n1 * n2;
        }
        tmp += res;
        int hide = r.nextInt(5);
        while (hide == 2) {
            hide = r.nextInt(5);
        }
        char[] arr = tmp.toCharArray();
        winDigit = arr[hide] - '0';
        arr[hide] = '?';
        eq = new String(arr);
    }

    private void moveBckGrnd(int dx, int dy) {

        backgroundLayer.move(dx, dy);
        for (int i = 0; i < mobs.length; i++) {
            mobs[i].move(dx, dy);
        }
        for (int i = 0; i < d.length; i++) {
            d[i].setPosition(d[i].getX() + dx, d[i].getY() + dy);
        }
        bp.moveAll(dx, dy);

    }

    private void setHeader(Graphics g) {
        g.setColor(0, 0, 0);
        g.fillRect(0, 0, getWidth(), 20);
        g.setColor(165, 176, 100);
        g.setFont(Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_BOLD | Font.STYLE_UNDERLINED | Font.STYLE_ITALIC, Font.SIZE_LARGE));
        g.drawString("SC:" + score + " " + eq + " " + "SP:" + spirit, 0, 0, 0);
    }

    private void addDigits() {
        try {
            d = new Digits[10];
            for (int i = 0; i < d.length; i++) {

                d[i] = new Digits(i);
                layerManager.append(d[i]);
            }

            int[][] map = backgroundLayer.map;
            int cnt = 0;
            Vector v = new Vector();
            for (int i = 0; i < map.length; i++) {
                for (int j = 0; j < map[0].length; j++) {
                    if (map[i][j] == Map.breakable) {
                        v.addElement(new pair(i * backgroundLayer.getCellHeight(), j * backgroundLayer.getCellWidth()));
                    }
                }
            }

            int randomize[] = new int[v.size()];
            for (int i = 0; i < randomize.length; i++) {
                randomize[i] = i;
            }
            Random r = new Random(System.currentTimeMillis());
            for (int i = 0; i < randomize.length; i++) {
                int ii = i + r.nextInt(randomize.length - i);
                int tmp = randomize[i];
                randomize[i] = randomize[ii];
                randomize[ii] = tmp;
            }
            for (int i = 0; i < d.length; i++) {
                d[i].setPosition(((pair) v.elementAt(randomize[i])).x + 5, ((pair) v.elementAt(randomize[i])).y + 5);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}

class pair {

    int x, y;

    public pair(int x, int y) {
        this.x = x;
        this.y = y;
    }
}