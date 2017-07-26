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

import org.jabref.model.entry.BibEntry;
import org.jabref.model.util.SpellCheckerRecord;

import com.swabunga.spell.engine.Word;

public class SpellCheckerDialog extends JDialog {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private final JPanel contentPanel = new JPanel();
    JList<String> list;
    JLabel misspelledWord;
    JLabel localCurrentField;
    List<SpellCheckerRecord> wordsToCheck;
    int currentWord;
    BasePanel panel;
    BibEntry entry;

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
     * Populate the Dialog
     */

    public void populateDialog(SpellCheckerRecord spellCheckerRecord) {
        this.localCurrentField.setText(spellCheckerRecord.getFieldName());
        this.misspelledWord.setText(spellCheckerRecord.getWord());
        DefaultListModel<String> listModel = new DefaultListModel<>();
        if ((spellCheckerRecord.getSuggestedWords() != null) && (spellCheckerRecord.getSuggestedWords().size() > 0)) {
            for (Word word : spellCheckerRecord.getSuggestedWords()) {
                listModel.addElement(word.getWord());
            }
        }
        list.setModel(listModel);
        if (listModel.size() > 0) {
            list.setSelectedIndex(0);
        }
    }

    /**
     * Create the dialog.
     */
    public SpellCheckerDialog(JFrame frame, BasePanel panel, BibEntry entry, List<SpellCheckerRecord> wordsToCheck) {
        super(frame, "Spell Checker", true);
        this.wordsToCheck = wordsToCheck;
        this.entry = entry;
        this.panel = panel;

        //setModal(true);
        setTitle("Entry Fields Spell Checker");
        setBounds(100, 100, 365, 262);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(null);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(158, 21, 165, 140);
        contentPanel.add(scrollPane);

        list = new JList<>();
        scrollPane.setViewportView(list);

        JLabel lblNewLabel = new JLabel("Current Field");
        lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 13));
        lblNewLabel.setBounds(24, 81, 96, 16);
        contentPanel.add(lblNewLabel);

        JLabel lblNewLabel_1 = new JLabel("Misspelled Word");
        lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 13));
        lblNewLabel_1.setBounds(24, 23, 106, 16);
        contentPanel.add(lblNewLabel_1);

        misspelledWord = new JLabel("Misspelled Word");
        misspelledWord.setBounds(24, 52, 146, 16);
        contentPanel.add(misspelledWord);

        localCurrentField = new JLabel("Misspelled Word");
        localCurrentField.setBounds(24, 110, 146, 16);
        contentPanel.add(localCurrentField);
        {
            JPanel buttonPane = new JPanel();
            buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
            getContentPane().add(buttonPane, BorderLayout.SOUTH);
            {
                JButton replaceButton = new JButton("Ignore");
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
                JButton replaceButton = new JButton("Replace");
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
            {
                JButton cancelButton = new JButton("Cancel");
                cancelButton.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent arg0) {
                        closeDialog();
                    }
                });
                cancelButton.setActionCommand("Cancel");
                buttonPane.add(cancelButton);
            }
        }
        setLocationRelativeTo(frame);
        loadNextWordInList();

    }

    public void loadNextWordInList() {
        if (currentWord < wordsToCheck.size()) {
            this.populateDialog(wordsToCheck.get(currentWord));
        } else {
            closeDialog();
        }
    }

    public void ignoreWord() {
        currentWord++;
        loadNextWordInList();
    }

    public void replaceWord() {
        correctWordInPosition();
        currentWord++;
        loadNextWordInList();
    }

    public void correctWordInPosition() {
        String[] correctedValueArray = entry.getField(this.wordsToCheck.get(currentWord).getFieldName()).get().split(" ");
        correctedValueArray[this.wordsToCheck.get(currentWord).getPositionInArray()] = list.getSelectedValue();
        String correctedValue = Arrays.stream(correctedValueArray).collect(Collectors.joining(" "));
        entry.setField(this.wordsToCheck.get(currentWord).getFieldName(), correctedValue);

    }

    public void closeDialog() {
        this.dispose();
    }

}
