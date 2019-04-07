package results;

import java.util.ArrayList;
import java.util.List;

public class PrecisionAndRecallForTags {
    public static void show(List<String> tags, List<String> correctLabels, List<String> resultLabels) {
        List<Float> precisions = new ArrayList<>();
        List<Float> recalls = new ArrayList<>();
        for (String tag : tags) {
            precisions.add(Precision.calculate(tag, correctLabels, resultLabels));
            recalls.add(Recall.calculate(tag, correctLabels, resultLabels));
        }
        List<Integer> max = new ArrayList<>();
        max.add("Label".length());
        max.add("Precision".length());
        max.add("Recall".length());
        for (int i = 0; i < tags.size(); i++) {
            if (tags.get(i).length() > max.get(0)) max.set(0, tags.get(i).length());
            if (precisions.get(i).toString().length() > max.get(1)) max.set(1, precisions.get(i).toString().length());
            if (recalls.get(i).toString().length() > max.get(2)) max.set(2, recalls.get(i).toString().length());
        }

        int overallLength = 4 + 6;
        overallLength += max.get(0);
        overallLength += max.get(1);
        overallLength += max.get(2);
        System.out.println("Precision and recall for each label");
        printLine(overallLength);
        System.out.print("|");
        System.out.print(adjustLength("Label", max.get(0)));
        System.out.print("|");
        System.out.print(adjustLength("Precision", max.get(1)));
        System.out.print("|");
        System.out.print(adjustLength("Recall", max.get(2)));
        System.out.println("|");
        printLine(overallLength);
        for (int i = 0; i < tags.size(); i++) {
            System.out.print("|");
            System.out.print(adjustLength(tags.get(i), max.get(0)));
            System.out.print("|");
            System.out.print(adjustLength(precisions.get(i).toString(), max.get(1)));
            System.out.print("|");
            System.out.print(adjustLength(recalls.get(i).toString(), max.get(2)));
            System.out.println("|");
            printLine(overallLength);
        }
    }

    private static void printLine(int lineLength) {
        for (int i = 0; i < lineLength; i++) {
            System.out.print("-");
        }
        System.out.println();
    }

    private static String adjustLength(String word, int maxLength) {
        StringBuilder wordBuilder = new StringBuilder(word);
        while (wordBuilder.length() < maxLength + 1) {
            wordBuilder.insert(0, " ");
        }
        word = wordBuilder.toString();
        return word + " ";
    }
}
