import javax.swing.*;
import java.awt.*;

public class MainForm extends JFrame{
    private JPanel mainPanel;
    private SensoGame imgs;

    MainForm() throws Exception {
        super ("title");
        mainPanel = new JPanel();
        mainPanel.setLayout (new BorderLayout());
        imgs = new SensoGame();
        mainPanel.add (imgs, BorderLayout.CENTER);
        System.out.println (imgs.imgMain.getWidth() + "/" + imgs.imgMain.getHeight());
    }

    public static void main(String[] args) throws Exception {
        MainForm frame = new MainForm();
        frame.setContentPane(frame.mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500,500);
        frame.setVisible(true);
    }
}
