/* Name: Nguyen Minh Thuan – ITCSIU22269
Purpose: This class implements the Graphical User Interface of the application
*/

// Libraries
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.undo.UndoManager;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.BadLocationException;

// Class declaration
public class GUI implements ActionListener {
	// Variables declaration
	JFrame window;
	// Text area
	JTextArea textArea;
	JScrollPane scrollPane;
	boolean wordWrapOn = false;
	// Top menu bar
	JMenuBar menuBar;
	JMenu menuFile, menuEdit, menuFormat, menuColor, menuView;
	// Function: File menu
	JMenuItem iNew, iOpen, iSave, iSaveAs, iExit;
	// Function: Edit menu
	JMenuItem iUndo, iRedo;
	// Function: Format menu
	JMenuItem iWrap, iFontArial, iFontCSMS, iFontTNR, iFontInter, iFontBarlow;
	JMenuItem iFontSize8, iFontSize12, iFontSize16, iFontSize20, iFontSize24, iFontSize28;
	JMenu menuFont, menuFontSize;
	// Function: Color menu
	JMenuItem iColor1, iColor2, iColor3, iColor4;
	// Function: View menu
	JMenuItem iZoomIn, iZoomOut, iSearch;
	// Status bar
	private int currentZoomPercentage = 100;

	boolean isTextModified = false;
	private boolean isFindDialogOpen = false;

	Function_File file = new Function_File(this);
	Function_Format format = new Function_Format(this);
	Function_Color color = new Function_Color(this);
	Function_Edit edit = new Function_Edit(this);
	KeyHandler kHandler = new KeyHandler(this);
	Function_View view = new Function_View(this);
	StatusBar statusBar = new StatusBar(this);
	UndoManager um = new UndoManager();

	// Constructor
	public GUI() {
		createWindow();
		createTextArea();
		createMenuBar();
		createFileMenu();
		createEditMenu();
		createFormatMenu();
		createColorMenu();
		createViewMenu();

		// Set the font for the text editor
		format.selectedFont = "Arial"; // Set default font
		format.createFont(16); // Set default font size
		format.wordWrap();
		color.changeColor("White"); // Set default color

		// Set the font of the menu bar
		Font menuFont = new Font("Segoe UI", Font.PLAIN, 12);
		setMenuBarFont(menuBar, menuFont);
		setComponentForeground(menuBar, Color.BLACK); // Set the foreground color to black

		// Set the font of the status bar
		Font statusBarFont = new Font("Segoe UI", Font.PLAIN, 12);
		statusBar.setFont(statusBarFont);
		statusBar.setForeground(Color.BLACK); // Set the foreground color to black

		window.add(statusBar.getStatusBar(), BorderLayout.SOUTH);
		window.setVisible(true);
	}

	// Methods
	public void createWindow() {
		window = new JFrame("Notepad - New Text File 1");
		window.setSize(800, 600); // Dimensions of the application window: 800 * 600 pixels
		window.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		window.setLocationRelativeTo(null);	// Center the application window on the screen
		window.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
			    // Check if the Find dialog is open and the text area is empty
			    if (isFindDialogOpen && textArea.getText().isEmpty()) {
			        window.dispose();
			        return;
			    } else {
			        // Check if the file is saved and not modified
			        if (file.isFileSaved && !file.isModified()) {
			            window.dispose();
			        } else {
			            // Check if the text is modified and the file is not saved
			            if (file.isFileJustOpened && !textArea.getText().isEmpty() && file.isModified() && !file.isFileSaved) {
			                int choice = JOptionPane.showConfirmDialog(window,
			                        "Do you want to save changes before exiting/exit without modifying the text?", "Save Changes",
			                        JOptionPane.YES_NO_CANCEL_OPTION);
			                if (choice == JOptionPane.YES_OPTION) {
			                    file.save();
			                    JOptionPane.showMessageDialog(window, "File has been saved.", "File Saved",
			                            JOptionPane.INFORMATION_MESSAGE);
			                    window.dispose();
			                } else if (choice == JOptionPane.NO_OPTION) {
			                    window.dispose();
			                    return;
			                }			               
			            } 
			         // Check if user opens an existing text file and exit without modifying anything
//			            else if (file.isFileJustOpened && !file.isTextModified()) {
//			                int choice = JOptionPane.showConfirmDialog(window,
//			                        "Do you want to exit without modifying the text file?", "Exit Without",
//			                        JOptionPane.YES_NO_CANCEL_OPTION);
//			                if (choice == JOptionPane.YES_OPTION) {
//			                    window.dispose();
//			                } else if (choice == JOptionPane.NO_OPTION || choice == JOptionPane.CANCEL_OPTION) {
//			                    return;
//			                }
//			            } 
		                // Check if the app is launched, something is typed, and the user wants to exit directly
			            else if (!file.isFileJustOpened && !file.isFileSaved) {
			                int choice = JOptionPane.showConfirmDialog(window,
			                        "Do you want to save the new text before exiting?", "Save Changes",
			                        JOptionPane.YES_NO_CANCEL_OPTION);
			                if (choice == JOptionPane.YES_OPTION) {
			                    file.saveAs();
			                    if (!file.isFileSaved) {
			                        JOptionPane.showMessageDialog(window, "File has not been saved. The application will be closed.", "File Not Saved", JOptionPane.WARNING_MESSAGE);
			                    } else {
			                        JOptionPane.showMessageDialog(window, "File has been saved.", "File Saved", JOptionPane.INFORMATION_MESSAGE);
			                    }
			                    window.dispose();
			                } else if (choice == JOptionPane.NO_OPTION) {
			                    window.dispose();
			                } else {
			                    // If Cancel option is chosen, do nothing and return
			                    return;
			                }
			            } else {
			                // Default case to handle unsaved changes for other scenarios
			                int choice = JOptionPane.showConfirmDialog(window,
			                        "Do you want to save changes before exiting?", "Save Changes",
			                        JOptionPane.YES_NO_CANCEL_OPTION);
			                if (choice == JOptionPane.YES_OPTION) {
			                    file.save();
			                    JOptionPane.showMessageDialog(window, "File has been saved.", "File Saved",
			                            JOptionPane.INFORMATION_MESSAGE);
			                    window.dispose();
			                } else if (choice == JOptionPane.NO_OPTION) {
			                    window.dispose();
			                } else {
			                    // If Cancel option is chosen, do nothing and return
			                    return;
			                }
			            }
			        }
			    }
			}
		});
		
		try {
			File imgFile = new File("img\\notepad.png");
			Image img = ImageIO.read(imgFile);
			window.setIconImage(img);
		} catch (IOException e) {
			System.err.println("Error loading image: " + e.getMessage());
		}
		window.setVisible(true);
	}

	public void createTextArea() {
		textArea = new JTextArea();
		textArea.setFont(format.arial);
		textArea.addKeyListener(kHandler);
		textArea.getDocument().addUndoableEditListener(
				new UndoableEditListener() {
					public void undoableEditHappened(UndoableEditEvent e) {
						um.addEdit(e.getEdit());
					}
				});
		scrollPane = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setPreferredSize(new Dimension(430, 520));
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		window.add(scrollPane);
		// window.add(textArea);

		textArea.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				isTextModified = true;
				file.isFileSaved = false;
				updateStatusBar();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				isTextModified = true;
				file.isFileSaved = false;
				updateStatusBar();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				// Plain text components don't fire these events
			}
		});
	}

	public void createMenuBar() {
		menuBar = new JMenuBar();
		window.setJMenuBar(menuBar);

		// Functions for the menu bar
		menuFile = new JMenu("File");
		menuBar.add(menuFile);
		menuEdit = new JMenu("Edit");
		menuBar.add(menuEdit);
		menuFormat = new JMenu("Format");
		menuBar.add(menuFormat);
		menuColor = new JMenu("Color");
		menuBar.add(menuColor);
	}

	public void createFileMenu() {
		iNew = new JMenuItem("New");
		iNew.addActionListener(this);
		iNew.setActionCommand("New");
		menuFile.add(iNew);

		iOpen = new JMenuItem("Open");
		iOpen.addActionListener(this);
		iOpen.setActionCommand("Open");
		menuFile.add(iOpen);

		iSave = new JMenuItem("Save");
		iSave.addActionListener(this);
		iSave.setActionCommand("Save");
		menuFile.add(iSave);

		iSaveAs = new JMenuItem("Save As");
		iSaveAs.addActionListener(this);
		iSaveAs.setActionCommand("Save As");
		menuFile.add(iSaveAs);
	}

	public void createEditMenu() {
		iUndo = new JMenuItem("Undo");
		iUndo.addActionListener(this);
		iUndo.setActionCommand("Undo");
		menuEdit.add(iUndo);

		iRedo = new JMenuItem("Redo");
		iRedo.addActionListener(this);
		iRedo.setActionCommand("Redo");
		menuEdit.add(iRedo);

		iExit = new JMenuItem("Exit");
		iExit.addActionListener(this);
		iExit.setActionCommand("Exit");
		menuFile.add(iExit);
	}

	public void createFormatMenu() {
		iWrap = new JMenuItem("Word Wrap: Off");
		iWrap.addActionListener(this);
		iWrap.setActionCommand("Word Wrap");
		menuFormat.add(iWrap);

		menuFont = new JMenu("Font");
		menuFormat.add(menuFont);

		iFontArial = new JMenuItem("Arial");
		iFontArial.addActionListener(this);
		iFontArial.setActionCommand("Arial");
		menuFont.add(iFontArial);

		iFontCSMS = new JMenuItem("Comic Sans MS");
		iFontCSMS.addActionListener(this);
		iFontCSMS.setActionCommand("Comic Sans MS");
		menuFont.add(iFontCSMS);

		iFontTNR = new JMenuItem("Times New Roman");
		iFontTNR.addActionListener(this);
		iFontTNR.setActionCommand("Times New Roman");
		menuFont.add(iFontTNR);
		
		iFontInter = new JMenuItem("Inter");
		iFontInter.addActionListener(this);
		iFontInter.setActionCommand("Inter");
		menuFont.add(iFontInter);
		
		iFontBarlow = new JMenuItem("Barlow");
		iFontBarlow.addActionListener(this);
		iFontBarlow.setActionCommand("Barlow");
		menuFont.add(iFontBarlow);

		menuFontSize = new JMenu("Font Size ");
		menuFormat.add(menuFontSize);

		iFontSize8 = new JMenuItem("8");
		iFontSize8.addActionListener(this);
		iFontSize8.setActionCommand("8");
		menuFontSize.add(iFontSize8);

		iFontSize12 = new JMenuItem("12");
		iFontSize12.addActionListener(this);
		iFontSize12.setActionCommand("12");
		menuFontSize.add(iFontSize12);

		iFontSize16 = new JMenuItem("16");
		iFontSize16.addActionListener(this);
		iFontSize16.setActionCommand("16");
		menuFontSize.add(iFontSize16);

		iFontSize20 = new JMenuItem("20");
		iFontSize20.addActionListener(this);
		iFontSize20.setActionCommand("20");
		menuFontSize.add(iFontSize20);

		iFontSize24 = new JMenuItem("24");
		iFontSize24.addActionListener(this);
		iFontSize24.setActionCommand("24");
		menuFontSize.add(iFontSize24);

		iFontSize28 = new JMenuItem("28");
		iFontSize28.addActionListener(this);
		iFontSize28.setActionCommand("28");
		menuFontSize.add(iFontSize28);
	}

	public void createColorMenu() {
		iColor1 = new JMenuItem("White");
		iColor1.addActionListener(this);
		iColor1.setActionCommand("White");
		menuColor.add(iColor1);

		iColor2 = new JMenuItem("Black");
		iColor2.addActionListener(this);
		iColor2.setActionCommand("Black");
		menuColor.add(iColor2);

		iColor3 = new JMenuItem("Blue");
		iColor3.addActionListener(this);
		iColor3.setActionCommand("Blue");
		menuColor.add(iColor3);

		iColor4 = new JMenuItem("Cyan");
		iColor4.addActionListener(this);
		iColor4.setActionCommand("Cyan");
		menuColor.add(iColor4);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		switch (command) {
			case "New":
				file.newFile();
				break;
			case "Open":
				file.open();
				break;
			case "Save":
				file.save();
				break;
			case "Save As":
				file.saveAs();
				break;
			case "Exit":
				file.exit();
				break;
			case "Undo":
				edit.undo();
				break;
			case "Redo":
				edit.redo();
				break;

			case "Word Wrap":
				format.wordWrap();
				break;
			case "Arial":
				format.setFont(command);
				break;
			case "Comic Sans MS":
				format.setFont(command);
				break;
			case "Times New Roman":
				format.setFont(command);
				break;
			case "Inter":
				format.setFont(command);
				break;
			case "Barlow":
				format.setFont(command);
				break;

			case "8":
				format.createFont(8);
				break;
			case "12":
				format.createFont(12);
				break;
			case "16":
				format.createFont(16);
				break;
			case "20":
				format.createFont(20);
				break;
			case "24":
				format.createFont(24);
				break;
			case "28":
				format.createFont(28);
				break;

			case "White":
				color.changeColor(command);
				break;
			case "Black":
				color.changeColor(command);
				break;
			case "Blue":
				color.changeColor(command);
				break;
			case "Cyan":
				color.changeColor(command);
				break;

			case "ZoomIn":
				view.zoomIn();
				if (currentZoomPercentage < 200) {
					int newZoom = currentZoomPercentage + 20; // Increase zoom by 20%
					if (newZoom > 200) {
						newZoom = 200; // Limit zoom to 200%
					}
					setZoomLevel(newZoom);
				}
				break;
			case "ZoomOut":
				view.zoomOut();
				if (currentZoomPercentage > 50) {
					int newZoom = currentZoomPercentage - 20; // Decrease zoom by 20%
					if (newZoom < 50) {
						newZoom = 50; // Limit zoom to 50%
					}
					setZoomLevel(newZoom);
				}
				break;
			case "Search":
				isFindDialogOpen = true;
				String word = JOptionPane.showInputDialog(window, "Enter word to search:");
				if (word != null && !word.isEmpty()) {
					view.searchAndHighlight(word);
				}
				isFindDialogOpen = false;
				break;
		}
	}

	public void createViewMenu() {
		// Create View menu
		menuView = new JMenu("View");
		menuBar.add(menuView);

		// Create Zoom In menu item
		iZoomIn = new JMenuItem("Zoom In");
		iZoomIn.addActionListener(this);
		iZoomIn.setActionCommand("ZoomIn");
		menuView.add(iZoomIn);

		// Create Zoom Out menu item
		iZoomOut = new JMenuItem("Zoom Out");
		iZoomOut.addActionListener(this);
		iZoomOut.setActionCommand("ZoomOut");
		menuView.add(iZoomOut);

		// Create Search menu item
		iSearch = new JMenuItem("Search");
		iSearch.addActionListener(this);
		iSearch.setActionCommand("Search");
		menuView.add(iSearch);
	}

	private void updateStatusBar() {
		// Update line and column
		int caretPosition = textArea.getCaretPosition();
		int line, column;
		try {
			line = textArea.getLineOfOffset(caretPosition) + 1;
			column = caretPosition - textArea.getLineStartOffset(line - 1) + 1;
		} catch (BadLocationException e) {
			line = 1;
			column = 1;
		}
		// Update total characters
		int totalCharacters = textArea.getText().length();
		statusBar.updateCursorPosition(line, column);
		statusBar.updateTotalCharacters(totalCharacters);
	}

	public void setZoomLevel(int percentage) {
		// Update the text area font size
		int newFontSize = (int) (textArea.getFont().getSize() * (percentage / 100.0));
		Font newFont = textArea.getFont().deriveFont((float) newFontSize);
		textArea.setFont(newFont);
	}

	public void setFontsAndColors() {
		// Set the font of the menu bar
		Font menuFont = new Font("Segoe UI", Font.PLAIN, 12);
		setMenuBarFont(menuBar, menuFont);
		// Set the font and color of each option in each menu
		setMenuItemFontAndColor(menuFile, menuFont, Color.BLACK);
		setMenuItemFontAndColor(menuEdit, menuFont, Color.BLACK);
		setMenuItemFontAndColor(menuFormat, menuFont, Color.BLACK);
		setMenuItemFontAndColor(menuColor, menuFont, Color.BLACK);
		setMenuItemFontAndColor(menuView, menuFont, Color.BLACK);
	}

	private void setMenuBarFont(JMenuBar menuBar, Font font) {
		for (int i = 0; i < menuBar.getMenuCount(); i++) {
			JMenu menu = menuBar.getMenu(i);
			setComponentFont(menu, font);
		}
	}

	private void setMenuItemFontAndColor(JMenu menu, Font font, Color color) {
		for (int i = 0; i < menu.getItemCount(); i++) {
			JMenuItem menuItem = menu.getItem(i);
			setComponentFontAndColor(menuItem, font, color);
		}
	}

	private void setComponentFontAndColor(Component component, Font font, Color color) {
		if (component != null) {
			component.setFont(font);
			if (component instanceof JMenuItem) {
				((JMenuItem) component).setForeground(color);
			}
		}
	}

	public void setComponentFont(Component component, Font font) {
		if (component != null) {
			component.setFont(font);
			if (component instanceof JMenu) {
				JMenu menu = (JMenu) component;
				for (int i = 0; i < menu.getMenuComponentCount(); i++) {
					setComponentFont(menu.getMenuComponent(i), font);
				}
			} else if (component instanceof JMenuItem) {
				JMenuItem menuItem = (JMenuItem) component;
				menuItem.setFont(font);
			}
		}
	}

	public void setComponentForeground(JMenuBar menuBar, Color color) {
		if (menuBar != null) {
			for (int i = 0; i < menuBar.getMenuCount(); i++) {
				JMenu menu = menuBar.getMenu(i);
				setMenuItemForeground(menu, color);
			}
		}
	}

	private void setMenuItemForeground(JMenu menu, Color color) {
		if (menu != null) {
			for (int i = 0; i < menu.getItemCount(); i++) {
				JMenuItem menuItem = menu.getItem(i);
				if (menuItem != null) {
					// Set the foreground color of the menu item's text
					menuItem.setForeground(color);

					// Set the foreground color of the menu item's submenu recursively
					setMenuForeground(menuItem, color);
				}
			}
		}
	}

	private void setMenuForeground(Component menuComponent, Color color) {
		if (menuComponent instanceof JMenu) {
			JMenu submenu = (JMenu) menuComponent;
			for (int j = 0; j < submenu.getItemCount(); j++) {
				JMenuItem submenuItem = submenu.getItem(j);
				if (submenuItem != null) {
					submenuItem.setForeground(color);
					setMenuForeground(submenuItem, color);
				}
			}
		}
	}
}
