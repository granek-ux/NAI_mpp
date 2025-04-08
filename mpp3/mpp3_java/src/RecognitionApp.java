import javax.swing.*;
import java.util.List;

public class RecognitionApp extends JFrame {
    private JPanel mainPanel;
    private JPanel RightPanel;
    private JButton button1;
    private JLabel languageLabel;
    private JTextArea enterTextHereTextArea;

    public RecognitionApp(List<Perceptron> perceptrons) {
        this.setTitle("recognition");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(800, 600);
        this.setVisible(true);
        this.add(mainPanel);

        button1.addActionListener(e -> {
            languageLabel.setText("Language: " + Main.klasyfikacjaInputUsera(perceptrons, enterTextHereTextArea.getText()));
        });
    }

}
