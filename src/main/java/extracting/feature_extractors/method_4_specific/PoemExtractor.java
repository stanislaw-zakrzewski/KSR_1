package extracting.feature_extractors.method_4_specific;

import data_management.Article;
import data_management.StanfordLemmatizer;

import java.util.*;

public class PoemExtractor {
    private static PoemExtractor instance;

    public static PoemExtractor getInstance() {
        if (instance == null) {
            instance = new PoemExtractor();
        }
        return instance;
    }

    private PoemExtractor() {

    }

    public List<Float> calculateVector(Object element) {
        List<Float> vector = new ArrayList<>();
        List<String> lines = Arrays.asList(((Article) element).getContent().split("\n"));
        lines.remove("");
        List<List<String>> wordLines = new ArrayList<>();
        for (String line : lines) {
            wordLines.add(StanfordLemmatizer.getInstance().lemmatize(line));
        }

        //First 4 elements of vector represents different endings count of all lines for different length of endings
        List<String> endings = new ArrayList<>();
        for (String line : lines) {
            if (line.length() >= 4) {
                int i = 0;
                String pom = line;
                while (!Character.isLetter(pom.charAt(pom.length() - 1))) {
                    pom = pom.substring(0, pom.length() - 1);
                }
                endings.add(pom.substring(pom.length() - 4));
            }
        }
        Map<String, Integer> endingsCount1 = new HashMap<>();
        Map<String, Integer> endingsCount2 = new HashMap<>();
        Map<String, Integer> endingsCount3 = new HashMap<>();
        Map<String, Integer> endingsCount4 = new HashMap<>();
        for (String ending : endings) {
            checkCount(ending.substring(ending.length() - 1), endingsCount1);
            checkCount(ending.substring(ending.length() - 2), endingsCount2);
            checkCount(ending.substring(ending.length() - 3), endingsCount3);
            checkCount(ending, endingsCount4);
        }
        vector.add((float) endingsCount1.size());
        vector.add((float) endingsCount2.size());
        vector.add((float) endingsCount3.size());
        vector.add((float) endingsCount4.size());

        //Fifth element is syllables max difference count
        List<Integer> syllabesCount = new ArrayList<>();
        for (int i = 0; i < wordLines.size(); i++) {
            syllabesCount.add(0);
            for (String word : wordLines.get(i)) {
                syllabesCount.set(i, syllabesCount.get(i) + syllablesIn(word));
            }
        }
        vector.add((float) (Collections.max(syllabesCount) - Collections.min(syllabesCount)));

        //Sixth element is max line length difference
        List<Integer> lineLengths = new ArrayList<>();
        for (String line : lines) {
            lineLengths.add(line.length());
        }
        vector.add((float) (Collections.max(lineLengths) - Collections.min(lineLengths)));

        return vector;
    }

    private void checkCount(String key, Map<String, Integer> map) {
        if (!map.containsKey(key)) {
            map.put(key, 1);
        } else {
            map.replace(key, map.get(key) + 1);
        }
    }

    private int syllablesIn(String word) {
        int numSyllables = 0;
        //Set everything to upper case
        String upperCaseWord = word.toUpperCase();
        //The loop will run from 1 to the character before the last
        for (int i = 1; i < upperCaseWord.length() - 1; i++) {
            char ch = upperCaseWord.charAt(i);
            char c = (upperCaseWord.charAt(i - 1));
            //Only adds if the char is in the index AND if there is no
            //other letter in the index fore i
            if ("AEIOUY".indexOf(ch) >= 0 && "AEIOUY".indexOf(c) == -1) {
                numSyllables++;
            }

        }
        //Check the first character
        char a = upperCaseWord.charAt(0);
        //Check the last character
        char b = upperCaseWord.charAt(upperCaseWord.length() - 1);

        //Add if the last char is not 'E'
        if ("AIOUY".indexOf(b) >= 0) {
            numSyllables++;
        }
        //Add if the first character is in the index
        if ("AEIOUY".indexOf(a) >= 0) {
            numSyllables++;
        }
        //There must be atleast one syllable
        if (numSyllables <= 0) {
            numSyllables = 1;
        }
        return numSyllables;
    }
}
