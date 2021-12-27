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
        InputPanel ipane = new InputPanel(imgs);
        imgs.setInputPane(ipane);
        mainPanel.add (imgs, BorderLayout.CENTER);
        mainPanel.add (ipane.thePane, BorderLayout.NORTH);
        System.out.println (imgs.imgMain.getWidth() + "/" + imgs.imgMain.getHeight());
    }

    public static void main(String[] args) throws Exception {
        MainForm frame = new MainForm();
        frame.setContentPane(frame.mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
