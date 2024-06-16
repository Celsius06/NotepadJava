/* Name: Nguyen Minh Thuan – ITCSIU22269
Purpose: This class implements the keyboard shortcut combination for the convenient user experience of the application
*/

// Libraries
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

// Class declaration
public class KeyHandler implements KeyListener {
    // Variables declaration
    GUI gui;

    // Constructor
    public KeyHandler(GUI gui) {
        this.gui = gui;
    }

    // Methods
    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // Combination with Ctrl
        if (e.isControlDown()) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_O:
                    // Ctrl + O: Open a previously saved note
                    gui.file.open();
                    break;
                case KeyEvent.VK_S:
                    if (e.isShiftDown()) {
                        // Ctrl + Shift + S: Save As
                        gui.file.saveAs();
                    } else {
                        // Ctrl + S: Save
                        gui.file.save();
                    }
                    break;
                case KeyEvent.VK_N:
                    // Ctrl + N: Open a New Blank Page
                    gui.file.newFile();
                    break;
                case KeyEvent.VK_Z:
                    // Ctrl + Z: Undo
                    gui.edit.undo();
                    break;
                case KeyEvent.VK_X:
                    // Ctrl + X: Cut
                    gui.textArea.cut();
                    break;
                case KeyEvent.VK_V:
                    // Ctrl + V: Paste
                    gui.textArea.paste();
                    break;
                case KeyEvent.VK_C:
                    // Ctrl + C: Copy
                    gui.textArea.copy();
                    break;
                case KeyEvent.VK_F:
                    // Ctrl + F: Open Find
                    gui.view.openFindDialog();
                    break;
                case KeyEvent.VK_H:
                    // Ctrl + H: Open Replace
                    gui.view.openReplaceDialog();
                    break;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
