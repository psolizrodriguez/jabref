package org.jabref.gui.spellchecker;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import org.jabref.Globals;
import org.jabref.gui.SpellCheckerDialog;
import org.jabref.model.util.SpellCheckerRecord;
import org.jabref.preferences.JabRefPreferences;

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
        Globals.prefs = JabRefPreferences.getInstance();
        BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/english.0")));
        dictionary = new SpellDictionaryHashMap(reader);
        spellChecker = new SpellChecker(dictionary);
        wordsToCheck = new ArrayList<>();
        wordsToCheck.add(new SpellCheckerRecord("author", "teh", 0, spellChecker.getSuggestions("teh", 1)));
        wordsToCheck.add(new SpellCheckerRecord("author", "pensil", 0, spellChecker.getSuggestions("pensil", 1)));
        dialog = new SpellCheckerDialog(null, null, null, wordsToCheck);
        //dialog.setVisible(true);
    }

    @After
    public void tearDown() throws Exception {
        dialog.dispose();
    }

    @Test
    public void loadNextWordInListDoesNotAffectCounter() {
        dialog.loadNextWordInList();
        dialog.loadNextWordInList();
        //Verify the currentWord variable remains 0
        Assert.assertEquals(0, dialog.getCurrentWord());
        dialog.loadNextWordInList();
        Assert.assertEquals(0, dialog.getCurrentWord());
    }

    @Test
    public void ignoreNextWordInListIncreasesCounter() {
        dialog.ignoreWord();
        //After Ignoring one word, It jumps to the next
        Assert.assertEquals(1, dialog.getCurrentWord());
    }

    @Test
    public void loadNextWordInListFirstWordOfListIsSelected() {
        dialog.loadNextWordInList();
        Assert.assertEquals("tea", dialog.getList().getSelectedValue());
        dialog.ignoreWord();
        Assert.assertEquals("pencil", dialog.getList().getSelectedValue());
    }

    @Test
    public void textMessageNoErrorFoundsWhenEmptyList() {
        wordsToCheck = new ArrayList<>();
        // Verifies the List is empty
        Assert.assertEquals(0, wordsToCheck.size());
        dialog = new SpellCheckerDialog(null, null, null, wordsToCheck);
        // Verifies the message displayed is correct Assert.assertEquals(Localization.lang("No_Errors_Spell_Check_Word"), dialog.getLblNewLabel().getText());
    }

}
