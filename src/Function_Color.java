/* Name: Nguyen Minh Thuan – ITCSIU22269
Purpose: This class illustrates the color changing feature
*/

// Libraries
import java.awt.Color;

// Class declaration
public class Function_Color {
	// Variables declaration
	GUI gui;

	// Constructor
	public Function_Color(GUI gui) {
		this.gui = gui;
	}

	// Methods
	public void changeColor(String color) {
		switch (color) {
			case "White":
				gui.window.getContentPane().setBackground(Color.white);
				gui.textArea.setBackground(Color.white);
				gui.textArea.setForeground(Color.black);
				break;
			case "Black":
				gui.window.getContentPane().setBackground(Color.black);
				gui.textArea.setBackground(Color.black);
				gui.textArea.setForeground(Color.white);
				break;
			case "Blue":
				gui.window.getContentPane().setBackground(Color.blue);
				gui.textArea.setBackground(Color.blue);
				gui.textArea.setForeground(Color.white);
				break;
			case "Cyan":
				gui.window.getContentPane().setBackground(new Color(0, 204, 204)); // RGB color code
				gui.textArea.setBackground(new Color(0, 204, 204));
				gui.textArea.setForeground(Color.white);
				break;
		}
	}
}
