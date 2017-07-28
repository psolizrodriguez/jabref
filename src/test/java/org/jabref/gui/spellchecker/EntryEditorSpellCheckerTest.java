package org.jabref.gui.spellchecker;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.jabref.model.util.JazzySpellChecker;
import org.jabref.model.util.SpellCheckAbstract;

import com.swabunga.spell.engine.SpellDictionaryHashMap;
import com.swabunga.spell.engine.Word;
import com.swabunga.spell.event.SpellChecker;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class EntryEditorSpellCheckerTest {

    SpellCheckAbstract jazzySpellChecker;
    Map<String, String> currentFields;
    SpellDictionaryHashMap dictionary = null;
    SpellChecker spellChecker = null;
    BufferedReader reader;

    @Before
    public void setUp() throws Exception {
        reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/english.0")));
        dictionary = new SpellDictionaryHashMap(reader);
        spellChecker = new SpellChecker(dictionary);
        jazzySpellChecker = new JazzySpellChecker();
        currentFields = new LinkedHashMap<>();
        currentFields.put("author", "teh");
    }

    @Test
    public void getSuggestionsReturnsCorrectListOfWords() {
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

    @Test
    public void dictionaryFromFileIsNotNull() throws Exception {
        //Verify the reader was able to load the dictionary file
        Assert.assertNotNull(reader);
        //Verify the file has something to read
        Assert.assertTrue(reader.readLine(), true);
    }

    @Test
    public void wordIsInSuggestionVerifiesWordIsInList() throws Exception {
        //Verify the word "the" is actually on the list
        Assert.assertTrue(jazzySpellChecker.wordIsInSuggestion(spellChecker.getSuggestions("teh", 1), "the"));
        //Verify the word "pensil" is not on the list
        Assert.assertFalse(jazzySpellChecker.wordIsInSuggestion(spellChecker.getSuggestions("pensil", 1), "pensil"));
    }

}
