package org.jabref.gui.spellchecker;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.jabref.gui.SpellCheckerDialog;

import com.swabunga.spell.engine.SpellDictionaryHashMap;
import com.swabunga.spell.engine.Word;
import com.swabunga.spell.event.SpellChecker;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SpellCheckerDialogTest {

    SpellCheckerDialog dialog;
    SpellDictionaryHashMap dictionary = null;
    SpellChecker spellChecker = null;
    Map<String, Map<String, List<Word>>> mapFields;

    @Before
    public void setUp() throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/english.0")));
        dictionary = new SpellDictionaryHashMap(reader);
        spellChecker = new SpellChecker(dictionary);

        mapFields = new LinkedHashMap<>();
        Map<String, List<Word>> author = new LinkedHashMap<>();
        author.put("teh", spellChecker.getSuggestions("teh", 1));
        mapFields.put("Author", author);

        dialog = new SpellCheckerDialog(null, null, mapFields);

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
