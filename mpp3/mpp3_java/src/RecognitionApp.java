import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class RecognitionApp extends JFrame {
    private JPanel mainPanel;
    private JTextField textField1;
    private JPanel RightPanel;
    private JButton button1;
    private JLabel languageLabel;
    private JTextArea enterTextHereTextArea;

    public RecognitionApp(List<Perceptron> perceptrons) {
        this.setTitle("recognition");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(800, 600);
//        this.setLocationRelativeTo(null);
//        this.setResizable(false);
        this.setVisible(true);
        this.add(mainPanel);

        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                languageLabel.setText("Language: " + Main.kasyfikacjaInputUsera(perceptrons, enterTextHereTextArea.getText()));
            }
        });
    }

}
