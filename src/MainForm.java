import javax.swing.*;
import java.awt.*;

public class MainForm extends JFrame{
    private final JPanel mainPanel;

    MainForm() throws Exception {
        super ("title");
        mainPanel = new JPanel();
        mainPanel.setLayout (new BorderLayout());
        SensoGame game = new SensoGame();
        InputPanel ipane = new InputPanel(game);
        game.setInputPane(ipane);
        mainPanel.add (game, BorderLayout.CENTER);
        mainPanel.add (ipane.thePane, BorderLayout.NORTH);
       // System.out.println (game.imgMain.getWidth() + "/" + game.imgMain.getHeight());
    }

    public static void main(String[] args) throws Exception {
        MainForm frame = new MainForm();
        frame.setTitle("The wonderful Senso!");
        frame.setContentPane(frame.mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
