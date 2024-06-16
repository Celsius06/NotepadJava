/* Name: Nguyen Minh Thuan – ITCSIU22269
Purpose: This class implements the undo and redo function
*/

// Libraries
import javax.swing.JMenuItem;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

// Class declaration
public class Function_Edit {
    // Variables declaration
    GUI gui;
    JMenuItem iRedo;

    // Constructor
    public Function_Edit(GUI gui) {
        this.gui = gui;
    }

    // Methods
    public void undo() {
        try {
            gui.um.undo(); // Attempt to undo the last edit
        } catch (CannotUndoException ex) {
            System.out.println("Cannot undo: " + ex);
        }
    }

    public void redo() {
        try {
            gui.um.redo(); // Attempt to redo the last edit
        } catch (CannotRedoException ex) {
            System.out.println("Cannot redo: " + ex);
        }
    }
}