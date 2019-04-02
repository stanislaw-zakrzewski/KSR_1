package extracting;

import parsing.Article;

import java.text.BreakIterator;
import java.util.*;

//TODO Fix this garbage
public class Converter {
    public static List<String> contentToWords(String text) {
        List<String> words = new ArrayList<>();
        BreakIterator breakIterator = BreakIterator.getWordInstance();
        breakIterator.setText(text);
        int lastIndex = breakIterator.first();
        while (BreakIterator.DONE != lastIndex) {
            int firstIndex = lastIndex;
            lastIndex = breakIterator.next();
            if (lastIndex != BreakIterator.DONE && Character.isLetterOrDigit(text.charAt(firstIndex))) {
                words.add(text.substring(firstIndex, lastIndex).toLowerCase());
            }
        }

        return words;
    }

    public static Map<Object, Float> articlesToVector(List<Object> articles) {
        Map<Object, Float> vector = new HashMap<>();
        for(Object article : articles) {
            List<String> wordsInArticles = contentToWords(((Article)article).getContent());
            for(String word : wordsInArticles) {
                if(!vector.containsKey(word)) {
                    vector.put(word, 0f);
                }
            }
        }
        return vector;
    }
}
