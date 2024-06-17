/* Name: Nguyen Minh Thuan – ITCSIU22269
Purpose: This class implements some features like create a new text document, open an existing file, save, save as and exit the application.
*/

// Libraries
import java.awt.FileDialog;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;

import javax.swing.JOptionPane;

//Class declaration
public class Function_File {
	// Variables declaration
	GUI gui;
	int newFileCounter = 1;
	String fileName;
	String fileAddress;
	boolean isFileSaved = true;
	boolean isFileJustOpened = false;
	boolean isTextModified = true;

	// Constructor
	public Function_File(GUI gui) {
		this.gui = gui;
	}

	// Methods
	public void newFile() {
		gui.textArea.setText("");
		newFileCounter++;
		gui.window.setTitle("New Text File " + newFileCounter);
		fileName = null; // Reset
		fileAddress = null; // Reset
		isFileSaved = true;
	}

	public void open() {
		FileDialog fd = new FileDialog(gui.window, "Open", FileDialog.LOAD);
		fd.setVisible(true);
		if (fd.getFile() != null) {
			fileName = fd.getFile();
			fileAddress = fd.getDirectory();
			gui.window.setTitle(fileName);
		}
		System.out.println("Opened chosen file with file address and file name: " + fileAddress + fileName); // Print out the file directory 
																											 // and file name in the terminal
		try {
			BufferedReader br = new BufferedReader(new FileReader(fileAddress + fileName)); // Link directory and file
																							// name to open the text
			gui.textArea.setText("");
			String line = null;
			while ((line = br.readLine()) != null) {
				gui.textArea.append(line + "\n");
			}
			br.close();
		} catch (Exception e) {
			System.out.println("File not opened!");
			e.printStackTrace();
		}
		isFileJustOpened = true;
		// isFileJustOpened = false;
	}

	public void save() {
	    // Check if the text area is empty initially
	    if (gui.textArea.getText().isEmpty() && fileName == null) {
	        JOptionPane.showMessageDialog(gui.window, "Cannot save an empty file.", "Warning", JOptionPane.WARNING_MESSAGE);
	        return;
	    }
		if (fileName == null) {
			saveAs();
		} else {
			try {
				FileWriter fw = new FileWriter(fileAddress + fileName);
				fw.write(gui.textArea.getText());
				gui.window.setTitle(fileName);
				fw.close();
				isFileSaved = true; // Set the flag to true after saving
			} catch (Exception e) {
				System.out.println("Something went wrong with the file.");
			}
		}
	}

	public void saveAs() {
	    // Check if the text area is empty initially
	    if (gui.textArea.getText().isEmpty() && fileName == null) {
	        JOptionPane.showMessageDialog(gui.window, "Cannot save an empty file.", "Warning", JOptionPane.WARNING_MESSAGE);
	        return;
	    }
		FileDialog fd = new FileDialog(gui.window, "Save As", FileDialog.SAVE);
		fd.setVisible(true);
		if (fd.getFile() != null) {
			fileName = fd.getFile();
			fileAddress = fd.getDirectory();
			gui.window.setTitle(fileName);
			System.out.println("Saved file as the file address and file name: " + fileAddress + fileName);
			try {
				FileWriter fw = new FileWriter(fileAddress + fileName);
				fw.write(gui.textArea.getText());
				fw.close();
			} catch (Exception e) {
				System.out.println("Something went wrong with the file.");
			}
		} else {
			System.out.println("Save dialog canceled. No file saved.");
			isFileSaved = false;
		}
	}

	public boolean isModified() {
		return !isFileSaved;
	}

	public boolean isTextModified() {
		return isTextModified;
	}

	public void exit() {
		if (gui.textArea.getText().isEmpty()) {
			System.exit(0);
		}
	}
}