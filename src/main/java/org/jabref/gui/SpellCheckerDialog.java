package org.jabref.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import org.jabref.logic.l10n.Localization;
import org.jabref.model.entry.BibEntry;
import org.jabref.model.util.SpellCheckerRecord;

import com.swabunga.spell.engine.Word;

/**
 * Dialog that gives a Graphic Interface to the user for applying the methods of the selected SpellCheckAbstract implementation
 */

public class SpellCheckerDialog extends JDialog {

    private static final long serialVersionUID = 1L;
    private final JPanel contentPanel = new JPanel();
    JList<String> list;
    JLabel misspelledWord;
    JLabel localCurrentField;
    List<SpellCheckerRecord> wordsToCheck;
    int currentWord;
    BasePanel panel;
    BibEntry entry;
    JLabel lblNewLabel;

    public JLabel getLblNewLabel() {
        return lblNewLabel;
    }

    public JList<String> getList() {
        return list;
    }

    public void setList(JList<String> list) {
        this.list = list;
    }

    public int getCurrentWord() {
        return currentWord;
    }

    /**
    * Method that helps us loading values into the Dialog to be displayed to the user
    * @param spellCheckerRecord: ArrayList containing the words that the engine from the JazzySpellChecker class was able to detect
    */
    public void populateDialog(SpellCheckerRecord spellCheckerRecord) {
        // Updates the label that displays the current field under inspection
        this.localCurrentField.setText(spellCheckerRecord.getFieldName());
        // Updates the label that displays the current word under inspection with the word  previously detected by the Spell Check Engine
        this.misspelledWord.setText(spellCheckerRecord.getWord());
        // List that helps updating the list of options to replace the word with
        DefaultListModel<String> listModel = new DefaultListModel<>();
        // Check the list is not empty
        if ((spellCheckerRecord.getSuggestedWords() != null) && (spellCheckerRecord.getSuggestedWords().size() > 0)) {
            // Iterate over the list to check the words
            for (Word word : spellCheckerRecord.getSuggestedWords()) {
                // Add the words to the list
                listModel.addElement(word.getWord());
            }
        }
        // Set the new list of words to the JList
        list.setModel(listModel);
        // Is the list is not empty, select the first word
        if (listModel.size() > 0) {
            list.setSelectedIndex(0);
        }
    }

    /**
     *
     * @param frame: Parent od the Dialog
     * @param panel: Panel that contains the current Entry being edited
     * @param entry: Entry being edited, object from where we pull the fields containing the text to be analyzed
     * @param wordsToCheck: List with objects of type SpellCheckerRecord containing the information needed to display on the GUI
     */
    public SpellCheckerDialog(JFrame frame, BasePanel panel, BibEntry entry, List<SpellCheckerRecord> wordsToCheck) {
        // This could also be replaced for a JabRefDialog type of object, this creates problems at the moment of performing test cases
        super(frame);
        this.wordsToCheck = wordsToCheck;
        this.entry = entry;
        this.panel = panel;
        // Code for creating the Dialog
        setTitle(Localization.lang("Entry_Fields_Spell_Check"));
        setBounds(100, 100, 365, 262);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(null);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
        getContentPane().add(buttonPane, BorderLayout.SOUTH);
        // Label displaying the current field title
        lblNewLabel = new JLabel(Localization.lang("Current_Field_Spell_Check"));
        lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 13));
        lblNewLabel.setBounds(24, 81, 120, 16);
        contentPanel.add(lblNewLabel);
        if (wordsToCheck.size() != 0) {

            // ScrollPane containing the list of suggestions
            JScrollPane scrollPane = new JScrollPane();
            scrollPane.setBounds(158, 21, 165, 140);
            contentPanel.add(scrollPane);
            // List that contains the list of suggestions
            list = new JList<>();
            scrollPane.setViewportView(list);

            // Label for displaying the original field from the list
            JLabel lblNewLabel_1 = new JLabel(Localization.lang("Misspelled_Word_Spell_Check"));
            lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 13));
            lblNewLabel_1.setBounds(24, 23, 106, 16);
            contentPanel.add(lblNewLabel_1);
            // Label displaying the current word title
            misspelledWord = new JLabel();
            misspelledWord.setBounds(24, 52, 146, 16);
            contentPanel.add(misspelledWord);
            // Label for displaying the original word from the list
            localCurrentField = new JLabel();
            localCurrentField.setBounds(24, 110, 146, 16);
            contentPanel.add(localCurrentField);

            {
                JButton replaceButton = new JButton(Localization.lang("Ignore_Spell_Check_Word"));
                replaceButton.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent arg0) {
                        ignoreWord();
                    }
                });
                replaceButton.setActionCommand("Ignore");
                buttonPane.add(replaceButton);
                getRootPane().setDefaultButton(replaceButton);
            }
            {
                JButton replaceButton = new JButton(Localization.lang("Replace_Spell_Check_Word"));
                replaceButton.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent arg0) {
                        replaceWord();
                    }
                });
                replaceButton.setActionCommand("Replace");
                buttonPane.add(replaceButton);
                getRootPane().setDefaultButton(replaceButton);
            }
        } else {
            lblNewLabel.setText(Localization.lang("No_Errors_Spell_Check_Word"));
        }
        buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
        getContentPane().add(buttonPane, BorderLayout.SOUTH);
        {
            JButton cancelButton = new JButton(Localization.lang("Cancel_Spell_Check_Word"));
            cancelButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent arg0) {
                    closeDialog();
                }
            });
            cancelButton.setActionCommand("Cancel");
            buttonPane.add(cancelButton);
        }
        setLocationRelativeTo(frame);
        loadNextWordInList();

    }

    /**
     * Loads the next word in queue to the dialog
     */
    public void loadNextWordInList() {
        if (currentWord < wordsToCheck.size()) {
            this.populateDialog(wordsToCheck.get(currentWord));
        } else {
            closeDialog();
        }
    }

    /**
     * Ignores the current word for correction and jumps to the next
     */
    public void ignoreWord() {
        currentWord++;
        loadNextWordInList();
    }

    /**
     * Replaces the word on the field of the entry by the word selected on the list
     */
    public void replaceWord() {
        correctWordInPosition();
        currentWord++;
        loadNextWordInList();
    }

    /**
     * Replace the text on the field of the Entry for the selected word form the JList
     */
    public void correctWordInPosition() {
        String[] correctedValueArray = entry.getField(this.wordsToCheck.get(currentWord).getFieldName()).get().split(" ");
        correctedValueArray[this.wordsToCheck.get(currentWord).getPositionInArray()] = list.getSelectedValue();
        String correctedValue = Arrays.stream(correctedValueArray).collect(Collectors.joining(" "));
        entry.setField(this.wordsToCheck.get(currentWord).getFieldName(), correctedValue);

    }

    /**
     * Close the Dialog
     */
    public void closeDialog() {
        this.dispose();
    }

}
