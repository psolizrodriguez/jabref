package org.jabref.logic.sharelatex;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.TreeMap;

import org.jabref.logic.importer.ImportFormatPreferences;
import org.jabref.logic.importer.ParseException;
import org.jabref.logic.importer.fileformat.BibtexParser;
import org.jabref.model.entry.BibEntry;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bitbucket.cowwoc.diffmatchpatch.DiffMatchPatch;
import org.bitbucket.cowwoc.diffmatchpatch.DiffMatchPatch.Diff;
import org.bitbucket.cowwoc.diffmatchpatch.DiffMatchPatch.Operation;

public class ShareLatexParser {

    private final JsonParser parser = new JsonParser();

    public int getVersionFromBibTexJsonString(String content) {
        JsonArray array = parseFirstPartOfMessageAsArray(content);
        return array.get(2).getAsInt();
    }

    public List<BibEntry> parseBibEntryFromJsonMessageString(String message, ImportFormatPreferences prefs)
            throws ParseException {
        return parseBibEntryFromJsonArray(parseFirstPartOfMessageAsArray(message), prefs);
    }

    public String getBibTexStringFromJsonMessage(String message) {
        return getBibTexStringFromJsonArray(parseFirstPartOfMessageAsArray(message));
    }

    public String getOtErrorMessageContent(String otUpdateError) {

        JsonObject obj = parseFirstPartOfMessageAsObject(otUpdateError);
        return obj.get("args").getAsJsonArray().get(0).getAsString();
    }

    public Map<String, String> getBibTexDatabasesNameWithId(String json) {
        Map<String, String> bibFileWithId = new TreeMap<>();

        JsonObject obj = parseFirstPartOfMessageAsArray(json).get(1).getAsJsonObject();
        JsonArray arr = obj.get("rootFolder").getAsJsonArray();

        Optional<JsonArray> docs = arr.get(0).getAsJsonObject().entrySet().stream()
                .filter(entry -> entry.getKey().equals("docs")).map(v -> v.getValue().getAsJsonArray()).findFirst();

        docs.ifPresent(jsonArray -> {
            for (JsonElement doc : jsonArray) {
                String name = doc.getAsJsonObject().get("name").getAsString();
                String id = doc.getAsJsonObject().get("_id").getAsString();

                if (name.endsWith(".bib")) {
                    bibFileWithId.put(name, id);
                }

            }
        });

        return bibFileWithId;
    }

    public List<SharelatexDoc> generateDiffs(String before, String after) {
        DiffMatchPatch patch = new DiffMatchPatch();

        LinkedList<Diff> diffs = patch.diffMain(before, after);
        patch.diffCleanupSemantic(diffs);

        int pos = 0;

        List<SharelatexDoc> docsWithChanges = new ArrayList<>();

        for (Diff d : diffs) {

            if (d.operation == Operation.INSERT) {
                SharelatexDoc doc = new SharelatexDoc();
                doc.setPosition(pos);
                doc.setContent(d.text);
                doc.setOperation("i");
                docsWithChanges.add(doc);
                pos += d.text.length();
            } else if (d.operation == Operation.DELETE) {
                SharelatexDoc doc = new SharelatexDoc();
                doc.setPosition(pos);
                doc.setContent(d.text);
                doc.setOperation("d");

                docsWithChanges.add(doc);

            } else if (d.operation == Operation.EQUAL) {
                pos += d.text.length();
            }

        }
        return docsWithChanges;

    }

    private List<BibEntry> parseBibEntryFromJsonArray(JsonArray arr, ImportFormatPreferences prefs)
            throws ParseException {

        String bibtexString = getBibTexStringFromJsonArray(arr);
        BibtexParser parser = new BibtexParser(prefs);
        return parser.parseEntries(bibtexString);
    }

    private JsonArray parseFirstPartOfMessageAsArray(String documentToParse) {
        String jsonToRead = documentToParse.substring(documentToParse.indexOf("+") + 1, documentToParse.length());
        JsonArray arr = parser.parse(jsonToRead).getAsJsonArray();
        return arr;
    }

    private JsonObject parseFirstPartOfMessageAsObject(String documentToParse) {
        String jsonToRead = documentToParse.substring(documentToParse.indexOf("{"), documentToParse.length());
        return parser.parse(jsonToRead).getAsJsonObject();
    }

    private String getBibTexStringFromJsonArray(JsonArray arr) {

        JsonArray stringArr = arr.get(1).getAsJsonArray();

        StringJoiner joiner = new StringJoiner("\n");

        for (JsonElement elem : stringArr) {
            joiner.add(elem.getAsString());
        }

        return joiner.toString();
    }

    /**
     * Fixes wrongly encoded UTF-8 strings which were encoded into ISO-8859-1
     * Workaround for server side bug
     * @param wrongEncoded The wrongly encoded string
     * @return The correct UTF-8 string
     */
    public String fixUTF8Strings(String wrongEncoded) {
        String str = new String(wrongEncoded.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);
        return new String(str.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
    }

}
