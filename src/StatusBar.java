// Libraries
import javax.swing.*;
import java.awt.*;

// Class declaration
public class StatusBar {
	// Variables declaration
    private JPanel statusBar;
    private JLabel positionLabel;
    private JLabel totalCharactersLabel;

    // Constructor
    public StatusBar(GUI gui) {
        statusBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        positionLabel = new JLabel("Line: 1 | Column: 1"); // Default value
        totalCharactersLabel = new JLabel("0 characters"); // Default value

        statusBar.add(positionLabel);
        statusBar.add(Box.createHorizontalGlue()); 
        statusBar.add(Box.createHorizontalStrut(50)); 
        statusBar.add(totalCharactersLabel);
    }

    // Getter
    public JPanel getStatusBar() {
        return statusBar;
    }

    // Methods
    public void updateCursorPosition(int line, int column) {
        positionLabel.setText(String.format("Line: %d | Column: %d", line, column));
    }
    
    public void updateTotalCharacters(int total) {
        totalCharactersLabel.setText(String.format("%d characters", total));
    }
    
    public void setFont(Font statusBarFont) {
        positionLabel.setFont(statusBarFont);
        totalCharactersLabel.setFont(statusBarFont);
    }
    
    public void setForeground(Color color) {
        positionLabel.setForeground(color);
        totalCharactersLabel.setForeground(color);
    }
}
