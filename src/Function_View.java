/* Name: Nguyen Minh Thuan – ITCSIU22269
Purpose: This class implements the view features (zoom in/out, search and replace)
*/

// Libraries
import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import java.util.LinkedList;
import java.util.Queue;
// import java.util.Stack;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

// Class declaration
public class Function_View {
    // Variables declaration
    GUI gui;

    // Constructor
    public Function_View(GUI gui) {
        this.gui = gui;
    }

    // Methods
    public void zoomIn() {
        float currentSize = gui.textArea.getFont().getSize();
        float newSize = currentSize * 1.2f; // Increase font size by a factor (1.2)
        gui.textArea.setFont(gui.textArea.getFont().deriveFont(newSize));
    }

    public void zoomOut() {
        float currentSize = gui.textArea.getFont().getSize();
        float newSize = currentSize * 0.8f; // Increase font size by a factor (0.8)
        gui.textArea.setFont(gui.textArea.getFont().deriveFont(newSize));
    }

    public void searchAndHighlight(String word) {
        Queue<Integer> positions = new LinkedList<>();
        // Stack<Integer> positionsStack = new Stack<>();
        String content = gui.textArea.getText();
        int index = content.indexOf(word);
        int count = 0; // Counter for occurrences
        while (index >= 0) {
            positions.add(index);
            // positionsStack.push(index);
            index = content.indexOf(word, index + 1);
            count++;
        }
        Highlighter highlighter = gui.textArea.getHighlighter();
        Highlighter.HighlightPainter painter = new DefaultHighlighter.DefaultHighlightPainter(Color.YELLOW);
        highlighter.removeAllHighlights();
        while (!positions.isEmpty()) {
            int pos = positions.poll();
            try {
                // Highlight the found word
                highlighter.addHighlight(pos, pos + word.length(), painter);
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
        }
        if (count > 0) {
            JOptionPane.showMessageDialog(null, "Total occurrences of '" + word + "': " + count, "Word Search Result",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Word not found.", "Word Search Result",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void openFindDialog() {
        String word = JOptionPane.showInputDialog(gui.window, "Enter word to search:");
        if (word != null && !word.isEmpty()) {
            searchAndHighlight(word);
        }
    }

    public void openReplaceDialog() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 2));

        JTextField findField = new JTextField(10);
        JTextField replaceField = new JTextField(10);

        panel.add(new JLabel("Find: "));
        panel.add(findField);
        panel.add(new JLabel("Replace with: "));
        panel.add(replaceField);

        int result = JOptionPane.showConfirmDialog(gui.window, panel, "Find and Replace", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String findWord = findField.getText();
            String replaceWord = replaceField.getText();
            if (!findWord.isEmpty() && !replaceWord.isEmpty()) {
                int changes = replaceWord(findWord, replaceWord);
                JOptionPane.showMessageDialog(gui.window, "Total changes made: " + changes, "Replace Result",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private int replaceWord(String findWord, String replaceWord) {
        String content = gui.textArea.getText();
        String newContent = content.replace(findWord, replaceWord);
        gui.textArea.setText(newContent);
        // Count occurrences of the replaced word
        int changes = (content.length() - newContent.length()) / findWord.length();
        return changes;
    }
}
