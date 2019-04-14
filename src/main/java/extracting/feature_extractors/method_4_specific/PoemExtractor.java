package extracting.feature_extractors.method_4_specific;

import data_management.Article;
import data_management.StanfordLemmatizer;

import java.util.*;

public class PoemExtractor {
    private int maxSyllabeCount = 0;
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
        maxSyllabeCount = 0;
        List<Float> vector = new ArrayList<>();
        String content = ((Article) element).getContent();
        List<String> lines = Arrays.asList(content.split("\n"));
        content = content.replaceAll("[,.:;\"']", "");
        content = content.replaceAll("-", "");


        List<List<String>> wordLines = new ArrayList<>();
        for (String line : lines) {
            String[] words = line.split(" ");

            wordLines.add(new ArrayList<>());
            for (String word : words) {
                if (!word.equals("")) {
                    wordLines.get(wordLines.size() - 1).add(word);
                }
            }
        }

        //First 4 elements of vector represents different endings count of all lines for different length of endings
        List<String> endings = new ArrayList<>();
        for (String line : lines) {
            if (line.length() >= 10) {
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
        vector.add((float) endingsCount1.size() / (float) lines.size());
        vector.add((float) endingsCount2.size() / (float) lines.size());
        vector.add((float) endingsCount3.size() / (float) lines.size());
        vector.add((float) endingsCount4.size() / (float) lines.size());

        //Fifth element is syllables max difference count
        List<Integer> syllabesCount = new ArrayList<>();
        for (int i = 0; i < wordLines.size(); i++) {
            syllabesCount.add(0);
            for (String word : wordLines.get(i)) {
                syllabesCount.set(i, syllabesCount.get(i) + syllablesIn(word));
            }
        }
        int maxLineLength = 0;
        for (String line : lines) {
            if (maxLineLength < line.length()) {
                maxLineLength = line.length();
            }
        }
        vector.add((float) (Collections.max(syllabesCount) - Collections.min(syllabesCount)) / (float) maxSyllabeCount);

        //Sixth element is max line length difference
        List<Integer> lineLengths = new ArrayList<>();
        for (String line : lines) {
            lineLengths.add(line.length());
        }
        vector.add((float) (Collections.max(lineLengths) - Collections.min(lineLengths)) / (float) maxLineLength);

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
        if(numSyllables > maxSyllabeCount) {
            maxSyllabeCount = numSyllables;
        }
        return numSyllables;
    }
}
