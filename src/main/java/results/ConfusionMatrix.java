package results;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfusionMatrix {
    public static void calculate(List<String> tags, List<String> corrects, List<String> results) {
        Map<String, Map<String, Integer>> matrix = new HashMap<>();
        for (String tag : tags) {
            matrix.put(tag, new HashMap<>());
            for (String tag2 : tags) {
                matrix.get(tag).put(tag2, 0);
            }
        }

        for (int i = 0; i < corrects.size(); i++) {
            matrix.get(corrects.get(i)).replace(results.get(i), matrix.get(corrects.get(i)).get(results.get(i)) + 1);
        }

        printConfusionMatrix(tags, matrix);
    }

    private static String adjustLength(String word, int maxLength) {
        StringBuilder wordBuilder = new StringBuilder(word);
        while (wordBuilder.length() < maxLength) {
            wordBuilder.insert(0, " ");
        }
        word = wordBuilder.toString();
        return word + " ";
    }

    private static void printLine(int lineLength) {
        for(int i = 0; i < lineLength; i++) {
            System.out.print("-");
        }
        System.out.println();
    }

    private static void printConfusionMatrix(List<String> tags, Map<String, Map<String, Integer>> matrix) {
        int maxLength = 0;
        for (String tag : tags) {
            if (tag.length() > maxLength) {
                maxLength = tag.length();
            }
        }
        maxLength++;
        int lineLenght =  (maxLength+1) * (tags.size()+1) + tags.size() + 2;

        System.out.println("Confusion matrix:");
        printLine(lineLenght);
        System.out.print("|");
        System.out.print(adjustLength("", maxLength));
        System.out.print("|");
        for (String tag : tags) {
            System.out.print(adjustLength(tag, maxLength) + "|");
        }
        System.out.println();
        printLine(lineLenght);
        for (String tag : tags) {
            System.out.print("|");
            System.out.print(adjustLength(tag, maxLength));
            System.out.print("|");
            for (String tag2 : tags) {
                System.out.print(adjustLength(matrix.get(tag).get(tag2).toString(), maxLength) + "|");
            }
            System.out.println();
            printLine(lineLenght);
        }
    }
}
