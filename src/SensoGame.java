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
                if (userPlays == false)
                    return;
                int x = e.getX()*2/getWidth();
                int y = e.getY()*2/getHeight();
                int pos = x+y*2;
                switchLight (pos, 300);
                input.add(pos);
                if (input.size() == mission.size()) {
                    if (!input.toString().contentEquals(mission.toString()))
                    {
                        ClipHandler.play (4);
                        System.out.println("fail");
                    }
                    input.clear();
                    sleep(1000);
                    userPlays = false;
                }
            }
        });

        new Thread(() -> {
        for (;;) {
            if (userPlays == true)
            {
                sleep (1);
                continue;
            }
            // computer plays
            mission.add(Math.abs(rnd.nextInt()%4));
            System.out.println(mission);
            for (Integer i : mission)
            {
                switchLight (i, 300);
                sleep (300);
            }
            userPlays = true;
        }
        }).start();
    }

    private void sleep (int x)
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
        ClipHandler.play (pos);
        offGraphics.drawImage (imgLight, 0,0, null);
        paint();

        sleep (delay);
        offGraphics.setClip(null);
        offGraphics.drawImage (imgMain, 0,0, null);
        paint();
    }

    @Override
    public void paint(Graphics g) {
        if (g == null)
            return;
        g.drawImage (offImage, 0,0, getWidth(), getHeight(), this);
    }

    private void paint() {
        paint(getGraphics());
    }

    @Override
    public void update(Graphics g) {
    }

    private void prepareGraphic (String face, String light) throws Exception {
        imgMain = ImageIO.read (Objects.requireNonNull (Utils.getResource (face)));
        imgLight = ImageIO.read (Objects.requireNonNull (Utils.getResource (light)));

        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gs = ge.getDefaultScreenDevice();
        GraphicsConfiguration gc = gs.getDefaultConfiguration();
        offImage =  gc.createCompatibleImage(imgMain.getWidth(), imgMain.getHeight(), Transparency.OPAQUE);

        offGraphics = offImage.createGraphics();
        offGraphics.drawImage (imgMain, 0,0, this);
    }

}
