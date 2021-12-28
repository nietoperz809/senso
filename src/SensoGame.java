import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class SensoGame extends JPanel {
    public BufferedImage imgMain;
    public BufferedImage imgLight;
    public ArrayList<Integer> mission = new ArrayList<>();
    public ArrayList<Integer> input = new ArrayList<>();
    private BufferedImage offImage;
    private Graphics offGraphics;
    private Random rnd = new Random();
    private boolean userPlays = false;
    private Lock lock = new Lock();
    private boolean running = false;
    private InputPanel inputPanel;

    public SensoGame() throws Exception {
        super();
        prepareGraphic("main.png", "light.png");
        setPreferredSize(new Dimension(500, 500));
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                e.consume();
                if (userPlays == false || running == false)
                    return;
                int x = e.getX() * 2 / getWidth();
                int y = e.getY() * 2 / getHeight();
                int pos = x + y * 2;
                switchLight(pos, 300);
                input.add(pos);
                int test = mission.get(input.size() - 1);
                //System.out.println(pos + "--"+ test);
                if (test != pos) {
                    ClipHandler.play(4);
                    JOptionPane.showMessageDialog(SensoGame.this,
                            "You'd lose at seq #"+mission.size() +
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
        });

        new Thread(() -> {
            for (; ; ) {
                if (userPlays == true || running == false) {
                    lock.lock();
                }
                mission.add(Math.abs(rnd.nextInt() % 4));
                inputPanel.setSeq(mission.size());
                for (Integer i : mission) {
                    switchLight(i, 300);
                    sleep(300);
                }
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

    private synchronized void switchLight(int pos, int delay) { // 0...3
        switch (pos) {
            case 0:
                offGraphics.setClip(0, 0,
                        offImage.getWidth() / 2,
                        offImage.getHeight() / 2);
                break;
            case 1:
                offGraphics.setClip(offImage.getWidth() / 2, 0,
                        offImage.getWidth(),
                        offImage.getHeight() / 2);
                break;
            case 2:
                offGraphics.setClip(0, offImage.getHeight() / 2,
                        offImage.getWidth() / 2,
                        offImage.getHeight());
                break;
            case 3:
                offGraphics.setClip(offImage.getWidth() / 2, offImage.getHeight() / 2,
                        offImage.getWidth(),
                        offImage.getHeight());
                break;
        }
        ClipHandler.play(pos);
        offGraphics.drawImage(imgLight, 0, 0, null);
        paint();

        sleep(delay);
        offGraphics.setClip(null);
        offGraphics.drawImage(imgMain, 0, 0, null);
        paint();
    }

    @Override
    public void paint(Graphics g) {
        if (g == null)
            return;
        g.drawImage(offImage, 0, 0, getWidth(), getHeight(), this);
    }

    private void paint() {
        paint(getGraphics());
    }

    @Override
    public void update(Graphics g) {
    }

    private void prepareGraphic(String face, String light) throws Exception {
        imgMain = ImageIO.read(Objects.requireNonNull(Utils.getResource(face)));
        imgLight = ImageIO.read(Objects.requireNonNull(Utils.getResource(light)));

        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gs = ge.getDefaultScreenDevice();
        GraphicsConfiguration gc = gs.getDefaultConfiguration();
        offImage = gc.createCompatibleImage(imgMain.getWidth(), imgMain.getHeight(), Transparency.OPAQUE);

        offGraphics = offImage.createGraphics();
        offGraphics.drawImage(imgMain, 0, 0, this);
    }

}
