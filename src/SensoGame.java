import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.*;
import java.util.Timer;

public class SensoGame extends JPanel {
    public BufferedImage imgMain;
    public BufferedImage imgLight;
    private BufferedImage offImage;
    private Graphics offGraphics;
    private boolean clickAllowed = true;
    private Random rnd = new Random();
    public ArrayList<Integer> mission = new ArrayList<>();
    public ArrayList<Integer> input = new ArrayList<>();
    private boolean userPlays = false;

    public SensoGame() throws Exception {
        super();
        prepareGraphic("main.png","light.png");
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                e.consume();
                if (clickAllowed == false)
                    return;
                if (userPlays == false)
                    return;
                int x = e.getX()*2/getWidth();
                int y = e.getY()*2/getHeight();
                int pos = x+y*2;
                switchLight (pos, 300);
                input.add(pos);
                //System.out.println(input.size()+ " -- "+ mission.size());
                if (input.size() == mission.size()) {
                    if (!input.toString().contentEquals(mission.toString()))
                    {
                        System.out.println("fail");
                    }
                    input.clear();
                    userPlays = false;
                }
            }
        });

        new Thread(() -> {
        for (;;) {
            if (userPlays == true)
            {
                wait (2000);
                continue;
            }
            // computer plays
            mission.add(Math.abs(rnd.nextInt()%4));
            System.out.println(mission);
            for (Integer i : mission)
            {
                switchLight (i, 1000);
                wait (1000);
            }
            userPlays = true;
        }
        }).start();
    }

    private void wait (int x)
    {
        try {
            Thread.sleep(x);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private synchronized void switchLight(int pos, int delay) { // 0...3
        switch (pos) {
            case 0:
                offGraphics.setClip(0, 0,
                        offImage.getWidth()/2,
                        offImage.getHeight()/2);
                break;
            case 1:
                offGraphics.setClip(offImage.getWidth()/2, 0,
                        offImage.getWidth(),
                        offImage.getHeight()/2);
                break;
            case 2:
                offGraphics.setClip(0, offImage.getHeight()/2,
                        offImage.getWidth()/2,
                        offImage.getHeight());
                break;
            case 3:
                offGraphics.setClip(offImage.getWidth()/2, offImage.getHeight()/2,
                        offImage.getWidth(),
                        offImage.getHeight());
                break;
        }
        offGraphics.drawImage (imgLight, 0,0, null);
        repaint();
        clickAllowed = false;

        new Timer().schedule(new TimerTask() {
            @Override public void run() {
                offGraphics.setClip(null);
                offGraphics.drawImage (imgMain, 0,0, null);
                repaint();
                clickAllowed = true;
            }
        }, delay);
    }

    @Override
    public void paint(Graphics g) {
        //super.paintComponent(g);
        g.drawImage (offImage, 0,0, getWidth(), getHeight(), this);
    }

    @Override
    public void update(Graphics g) {
    }


    private void prepareGraphic (String face, String light) throws Exception {
        imgMain = ImageIO.read (Objects.requireNonNull (getResource (face)));
        imgLight = ImageIO.read (Objects.requireNonNull (getResource (light)));

        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gs = ge.getDefaultScreenDevice();
        GraphicsConfiguration gc = gs.getDefaultConfiguration();
        offImage =  gc.createCompatibleImage(imgMain.getWidth(), imgMain.getHeight(), Transparency.OPAQUE);

        offGraphics = offImage.createGraphics();
        offGraphics.drawImage (imgMain, 0,0, this);
    }

    /////////////////////////////////////////////////////////////////////////

    public static InputStream getResource (String name)
    {
        InputStream is = ClassLoader.getSystemResourceAsStream (name);
        if (is == null)
        {
            System.out.println ("could not load: "+name);
            return null;
        }
        return new BufferedInputStream(is);
    }
}
