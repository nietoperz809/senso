import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Objects;

public class SensoGame extends JPanel {
    public final ArrayList<Integer> mission = new ArrayList<>();
    public final ArrayList<Integer> input = new ArrayList<>();
    private final Lock lock = new Lock();
    public BufferedImage imgMain;
    public BufferedImage imgLight;
    private BufferedImage offImage;
    private int offx, offy, offxhalf, offyhalf;
    private Graphics offGraphics;
    private boolean userPlays = false;
    private boolean running = false;
    private InputPanel inputPanel;

    public SensoGame() throws Exception {
        super();
        prepareGraphic();
        setPreferredSize(new Dimension(500, 500));
        setToolTipText("1-yellow, 2-blue, 3-red, 4-green");

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (!userPlays || !running)
                    return;
                int x = e.getX() * 2 / getWidth();
                int y = e.getY() * 2 / getHeight();
                userAction(x + y * 2);
            }
        });

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (!userPlays || !running)
                    return;
                int pos = Math.abs(e.getKeyChar()-'1');
                if (pos < 4)
                    userAction(pos);
            }
        });

        new Thread(() -> {
            for (; ; ) {
                if (userPlays || !running) {
                    lock.lock();
                }
                int rnd = (int) (Math.random() * 4);
                mission.add(rnd);
                inputPanel.setSeq(mission.size());
                for (Integer i : mission) {
                    switchLight(i);
                    sleep(300);
                }
                grabFocus();
                userPlays = true;
            }
        }).start();
    }

    public void setInputPane(InputPanel pp) {
        inputPanel = pp;
    }

    public void start() {
        input.clear();
        mission.clear();
        running = true;
        lock.unlock();
    }

    private void sleep(int x) {
        try {
            Thread.sleep(x);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private synchronized void switchLight(int pos) { // 0...3
        switch (pos) {
            case 0:
                offGraphics.setClip(0, 0, offxhalf, offyhalf);
                break;
            case 1:
                offGraphics.setClip(offxhalf, 0, offx, offyhalf);
                break;
            case 2:
                offGraphics.setClip(0, offyhalf, offxhalf, offy);
                break;
            case 3:
                offGraphics.setClip(offxhalf, offyhalf, offx, offy);
                break;
        }
        ClipHandler.play(pos);
        offGraphics.drawImage(imgLight, 0, 0, null);
        paint();

        sleep(300);
        offGraphics.setClip(null);
        offGraphics.drawImage(imgMain, 0, 0, null);
        paint();
    }

    @Override
    public void paint(Graphics g) {
        if (g != null)
            g.drawImage(offImage, 0, 0, getWidth(), getHeight(), this);
    }

    private void paint() {
        paint(getGraphics());
    }

    @Override
    public void update(Graphics g) {
    }

    private void prepareGraphic() throws Exception {
        imgMain = ImageIO.read(Objects.requireNonNull(Utils.getResource("main.png")));
        imgLight = ImageIO.read(Objects.requireNonNull(Utils.getResource("light.png")));

        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gs = ge.getDefaultScreenDevice();
        GraphicsConfiguration gc = gs.getDefaultConfiguration();
        offImage = gc.createCompatibleImage(imgMain.getWidth(), imgMain.getHeight(), Transparency.OPAQUE);
        offx = offImage.getWidth();
        offxhalf = offx / 2;
        offy = offImage.getHeight();
        offyhalf = offy / 2;

        offGraphics = offImage.createGraphics();
        offGraphics.drawImage(imgMain, 0, 0, this);
    }

    private void userAction(int pos) {
        switchLight(pos);
        input.add(pos);
        int test = mission.get(input.size() - 1);
        if (test != pos) {
            ClipHandler.play(4);
            JOptionPane.showMessageDialog(SensoGame.this,
                    "You'd lose at seq #" + mission.size() +
                            "\nHit START to play again ...",
                    "Bang!",
                    JOptionPane.WARNING_MESSAGE);
            running = false;
            return;
        }
        if (input.size() == mission.size()) {
            input.clear();
            sleep(1000);
            userPlays = false;
            lock.unlock();
        }
    }
}
