package org.jabref.gui.spellchecker;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.jabref.gui.JabRefFrame;
import org.jabref.model.util.JazzySpellChecker;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class EntryEditorSpellCheckerTest {

    JazzySpellChecker jazzySpellChecker;
    Map<String, String> currentFields;
    SpellDictionaryHashMap dictionary = null;
    SpellChecker spellChecker = null;
    BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/english.0")));

    @Before
    public void setUp() throws Exception {

        dictionary = new SpellDictionaryHashMap(reader);
        spellChecker = new SpellChecker(dictionary);
        jazzySpellChecker = new JazzySpellChecker();
        currentFields = new LinkedHashMap<>();
        currentFields.put("author", "teh");
    }

    @Test
    public void dictionaryTest() {
        // tests to make sure a dictionary has text and an exception is not thrown by the BufferedReader
        Assert.assertTrue(reader.readLine(), true);
        Assert.assertNotNull(reader);
    }

    @Test
    public void mapLoadedTest() {
        // Will check to make sure the map will work for required fields with text input
        Assert.assertEquals("author", currentFields.get("author"));
        Assert.assertEquals("publisher", currentFields.get("publisher"));
        Assert.assertEquals("title", currentFields.get("title"));
        Assert.assertEquals("editor", currentFields.get("editor"));
    }

    @Test
    public void simpleSpellCheckTest() {
        String[] expectedValues = {"tea", "the", "ten"};
        List<Word> vectorSuggestedValues = spellChecker.getSuggestions("teh", 1);
        String[] suggestedValues = new String[vectorSuggestedValues.size()];
        for (int i = 0; i < vectorSuggestedValues.size(); i++) {
            suggestedValues[i] = vectorSuggestedValues.get(i).getWord();
        }
        //Verifying size of suggestions
        Assert.assertEquals(expectedValues.length, suggestedValues.length);
        //Verifying content of suggestions
        Assert.assertArrayEquals(expectedValues, suggestedValues);
    }


}
