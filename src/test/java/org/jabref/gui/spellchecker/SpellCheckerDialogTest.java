package org.jabref.gui.spellchecker;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import org.jabref.gui.SpellCheckerDialog;
import org.jabref.model.util.SpellCheckerRecord;

import com.swabunga.spell.engine.SpellDictionaryHashMap;
import com.swabunga.spell.event.SpellChecker;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SpellCheckerDialogTest {

    SpellCheckerDialog dialog;
    SpellDictionaryHashMap dictionary = null;
    SpellChecker spellChecker = null;
    List<SpellCheckerRecord> wordsToCheck;
    JFrame jFrame;

    @Before
    public void setUp() throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/english.0")));
        dictionary = new SpellDictionaryHashMap(reader);
        spellChecker = new SpellChecker(dictionary);
        wordsToCheck = new ArrayList<>();
        wordsToCheck.add(new SpellCheckerRecord("author", "teh", 0, spellChecker.getSuggestions("teh", 1)));
        wordsToCheck.add(new SpellCheckerRecord("author", "pensil", 0, spellChecker.getSuggestions("pensil", 1)));
        dialog = new SpellCheckerDialog(new JFrame(), null, null, wordsToCheck);
        //dialog.setVisible(true);
    }

    @After
    public void tearDown() throws Exception {
        dialog.dispose();
    }

    @Test
    public void loadNextWordInListTest() {
        dialog.loadNextWordInList();
        //Verify the currentWord variable remains 0
        Assert.assertEquals(0, dialog.getCurrentWord());
    }

    @Test
    public void firstWordOfListSelected() {
        dialog.loadNextWordInList();
        Assert.assertEquals("tea", dialog.getList().getSelectedValue());
        dialog.ignoreWord();
        Assert.assertEquals("pencil", dialog.getList().getSelectedValue());
    }

}
