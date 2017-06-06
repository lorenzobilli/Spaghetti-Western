import javax.swing.*;
import java.security.InvalidParameterException;

/**
 * MainWindow class
 */
public class MainWindow {

    private JFrame window;

    public MainWindow(String title) {
        if (title == null) {
            throw new InvalidParameterException("Window title unspecified");
        }
        window = new JFrame(title);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setSize(800, 600);
        window.setVisible(true);
    }
}
