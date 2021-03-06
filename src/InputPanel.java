import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InputPanel {

    private JButton startButton;
    public JPanel thePane;
    private JLabel seq;
    private final SensoGame _game;

    public void setSeq (int x) {
        seq.setText ("Sequence: " + x);
    }

    public InputPanel (SensoGame game) {
        _game = game;
        $$$setupUI$$$ ();
        startButton.addActionListener (e -> _game.start ());
    }

    private void createUIComponents () {
        thePane = new JPanel ();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$ () {
        createUIComponents ();
        thePane.setLayout (new FlowLayout (FlowLayout.CENTER, 5, 5));
        startButton = new JButton ();
        startButton.setText ("Start");
        thePane.add (startButton);
        seq = new JLabel ();
        seq.setPreferredSize (new Dimension (80, 16));
        seq.setText ("Sequence: 0");
        thePane.add (seq);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$ () {
        return thePane;
    }

}
