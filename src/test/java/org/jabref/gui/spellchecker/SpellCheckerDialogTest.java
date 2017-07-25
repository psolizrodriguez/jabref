package org.jabref.gui.spellchecker;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.jabref.gui.SpellCheckerDialog;
import org.jabref.model.util.SpellCheckerRecord;

import com.swabunga.spell.engine.SpellDictionaryHashMap;
import com.swabunga.spell.event.SpellChecker;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SpellCheckerDialogTest {

    SpellCheckerDialog dialog;
    SpellDictionaryHashMap dictionary = null;
    SpellChecker spellChecker = null;
    List<SpellCheckerRecord> wordsToCheck;

    @Before
    public void setUp() throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/english.0")));
        dictionary = new SpellDictionaryHashMap(reader);
        spellChecker = new SpellChecker(dictionary);
        wordsToCheck = new ArrayList<>();
        wordsToCheck.add(new SpellCheckerRecord("author", "teh", 0, spellChecker.getSuggestions("teh", 1)));

        dialog = new SpellCheckerDialog(null, null, null, wordsToCheck);

        dialog.setVisible(true);

    }

    @After
    public void tearDown() throws Exception {
        dialog.dispose();
    }

    @Test
    public void populateDialogTest() {
        dialog.loadNextWordInList();
    }

}
