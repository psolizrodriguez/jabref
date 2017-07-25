package org.jabref.model.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.swabunga.spell.engine.SpellDictionaryHashMap;
import com.swabunga.spell.engine.Word;
import com.swabunga.spell.event.SpellChecker;

public class JazzySpellChecker {

    SpellDictionaryHashMap dictionary = null;
    SpellChecker spellChecker = null;

    public JazzySpellChecker() {
        try {
            // Load values for Dictionary
            BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/english.0")));
            dictionary = new SpellDictionaryHashMap(reader);
            spellChecker = new SpellChecker(dictionary);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<SpellCheckerRecord> performSpellCheck(Map<String, String> currentFields) {
        List<SpellCheckerRecord> allErrors = new ArrayList<>();
        if (!currentFields.isEmpty()) {
            // 2. Iterate over current fields
            for (Map.Entry<String, String> currentField : currentFields.entrySet()) {
                String[] arrayOfWords = currentField.getValue().split(" ");
                if (arrayOfWords.length > 0) {
                    for (int i = 0; i < arrayOfWords.length; i++) {
                        List<Word> possibleWords = spellChecker.getSuggestions(arrayOfWords[i], 1);
                        if ((possibleWords != null) && (possibleWords.size() > 0)) {
                            allErrors.add(new SpellCheckerRecord(currentField.getKey(), arrayOfWords[i], i, possibleWords));
                        }
                    }
                }
            }

        }
        System.out.println("Errors found : " + allErrors.toString());
        return allErrors;
    }
}
