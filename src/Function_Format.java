/* Name: Nguyen Minh Thuan – ITCSIU22269
Purpose: This class implements the format features (word wrap, font and font size change)
*/

// Libraries
import java.awt.Font;

// Class declaration
public class Function_Format {
	// Variables declaration
	GUI gui;
	Font arial, comicSansMS, timesNewRoman;
	String selectedFont;

	// Constructor
	public Function_Format(GUI gui) {
		this.gui = gui;
	}

	// Methods
	public void wordWrap() {
		if (gui.wordWrapOn == false) {
			gui.wordWrapOn = true;
			gui.textArea.setLineWrap(true);
			gui.textArea.setWrapStyleWord(true);
			gui.iWrap.setText("Word Wrap: On");
		} else if (gui.wordWrapOn == true) {
			gui.wordWrapOn = false;
			gui.textArea.setLineWrap(false);
			gui.textArea.setWrapStyleWord(false);
			gui.iWrap.setText("Word Wrap: Off");
		}
	}

	public void createFont(int fontSize) {
		arial = new Font("Arial", Font.PLAIN, fontSize);
		comicSansMS = new Font("Comic Sans MS", Font.PLAIN, fontSize);
		timesNewRoman = new Font("Times New Roman", Font.PLAIN, fontSize);
		setFont(selectedFont);
	}

	public void setFont(String font) {
		selectedFont = font;
		switch (selectedFont) {
			case "Arial":
				gui.textArea.setFont(arial);
				break;
			case "Comic Sans MS":
				gui.textArea.setFont(comicSansMS);
				break;
			case "Times New Roman":
				gui.textArea.setFont(timesNewRoman);
				break;
		}
	}
}
