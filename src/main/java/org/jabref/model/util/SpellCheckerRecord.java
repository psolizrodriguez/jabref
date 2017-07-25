package org.jabref.model.util;

import java.util.List;

import com.swabunga.spell.engine.Word;

public class SpellCheckerRecord {

    String fieldName;
    String word;
    Integer positionInArray;
    List<Word> suggestedWords;

    public SpellCheckerRecord(String fieldName,
            String word, Integer positionInArray,
            List<Word> suggestedWords) {
        this.fieldName = fieldName;
        this.word = word;
        this.positionInArray = positionInArray;
        this.suggestedWords = suggestedWords;

    }

    public Integer getPositionInArray() {
        return positionInArray;
    }

    public void setPositionInArray(Integer positionInArray) {
        this.positionInArray = positionInArray;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public List<Word> getSuggestedWords() {
        return suggestedWords;
    }

    public void setSuggestedWords(List<Word> suggestedWords) {
        this.suggestedWords = suggestedWords;
    }

}