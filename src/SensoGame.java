import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class SensoGame extends JPanel {
    public BufferedImage imgMain;
    public BufferedImage imgLight;
    private BufferedImage offImage;
    private Graphics offGraphics;
    private boolean clickAllow = true;
    public ArrayList<Integer> mission = new ArrayList<>();
    public ArrayList<Integer> input = new ArrayList<>();

    public SensoGame() throws Exception {
        super();
        prepareGraphic("main.png","light.png");
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (clickAllow == false)
                    return;

                int x = e.getX()*2/getWidth();
                int y = e.getY()*2/getHeight();
                int pos = x+y*2;
                switchLight (pos);
                input.add(pos);
            }
        });
    }

    private void switchLight(int pos) { // 0...3
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
        clickAllow = false;

        new Timer().schedule(new TimerTask() {
            @Override public void run() {
                offGraphics.setClip(null);
                offGraphics.drawImage (imgMain, 0,0, null);
                repaint();
                clickAllow = true;
            }
        }, 300);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage (offImage, 0,0, getWidth(), getHeight(), this);
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
