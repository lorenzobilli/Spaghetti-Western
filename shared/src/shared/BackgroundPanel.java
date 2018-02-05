package shared;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * shared.BackgroundPanel class
 */
public class BackgroundPanel extends JPanel {

    private Image panelBackground;

    public BackgroundPanel(String backgroundImage) throws IOException {
        panelBackground = ImageIO.read(new File(backgroundImage));
    }

    public void updateBackground(String backgroundImage) throws IOException {
        panelBackground = ImageIO.read(new File(backgroundImage));
    }

    public void paintComponent(Graphics graph) {
        super.paintComponent(graph);
        graph.drawImage(panelBackground, 0, 0, this);
    }
}
