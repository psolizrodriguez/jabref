package org.jabref.model.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jabref.gui.preftabs.PreferencesDialog;

import com.swabunga.spell.engine.SpellDictionaryHashMap;
import com.swabunga.spell.engine.Word;
import com.swabunga.spell.event.SpellChecker;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Helps us implementing and using the Jazzy methods inside of the fields of the current Entry as we require
 * Mainly focus on testing and implementing possible problems with the Jazzy Library
 * Jazzy could also be replaced in this class by a different Spell Check Engine
 */
public class JazzySpellChecker implements SpellCheckAbstract {

    private static final Log LOGGER = LogFactory.getLog(PreferencesDialog.class);
    SpellDictionaryHashMap dictionary = null;
    SpellChecker spellChecker = null;

    public JazzySpellChecker() {
        try {
            // Buffer the dictionary
            BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/english.0")));
            // Load values for Dictionary from the internal Jazzy Jar
            dictionary = new SpellDictionaryHashMap(reader);
            // Create spell check object
            spellChecker = new SpellChecker(dictionary);
        } catch (IOException ioe) {
            //Verify we are able to find the file of the dictionary
            LOGGER.warn(ioe.getMessage(), ioe);
        }
    }

    /**
     * Method to perform the spell check over current fields of the Entry
     * @param currentFields: map containing information from the Entry, the id of the field and the text written by the user on the entry
     * @return List<SpellCheckerRecord> : LinkedList containing the words and the suggestions given by Jazzy
     */

    @Override
    public List<SpellCheckerRecord> performSpellCheck(Map<String, String> currentFields) {
        List<SpellCheckerRecord> allErrors = new ArrayList<>();
        // Verify the fields have content
        if (!currentFields.isEmpty()) {
            // Iterate over current fields
            for (Map.Entry<String, String> currentField : currentFields.entrySet()) {
                // Divide the String into words
                String[] arrayOfWords = currentField.getValue().split(" ");
                if (arrayOfWords.length > 0) {
                    // Iterate over the array of words we just created
                    for (int i = 0; i < arrayOfWords.length; i++) {
                        // Obtain the list of suggested replacements by the Jazzy lib (word, threshold)
                        List<Word> possibleWords = spellChecker.getSuggestions(arrayOfWords[i], 1);
                        // Verify the word is not present into the suggestions
                        if ((possibleWords != null) && (possibleWords.size() > 0) && !wordIsInSuggestion(possibleWords, arrayOfWords[i])) {
                            // Add word to a list to be processed as we require later on
                            allErrors.add(new SpellCheckerRecord(currentField.getKey(), arrayOfWords[i], i, possibleWords));
                        }
                    }
                }
            }

        }
        return allErrors;
    }

}
