package org.jabref.model.util;

import java.util.List;
import java.util.Map;

import com.swabunga.spell.engine.Word;

public interface SpellCheckAbstract {

    /**
     * Method to perform the spell check over current fields of the Entry
     * @param currentFields: map containing information from the Entry, the id of the field and the text written by the user on the entry
     * @return List<SpellCheckerRecord> : LinkedList containing the words and the suggestions given by Jazzy
     */
    public List<SpellCheckerRecord> performSpellCheck(Map<String, String> currentFields);

    /**
     * Method that helps correct issues for certain words during the spell check
     * @param possibleWords : Array containing the suggested words given by the Jazzy method spellChecker.getSuggestions()
     * @param string : original word
     * @return boolean : result of the comparison
     */
    public default boolean wordIsInSuggestion(List<Word> possibleWords, String string) {
        if ((possibleWords != null) && (possibleWords.size() > 0)) {
            for (Word word : possibleWords) {
                if (word.getWord().equalsIgnoreCase(string)) {
                    return true;
                }
            }
        }
        return false;
    }
}
