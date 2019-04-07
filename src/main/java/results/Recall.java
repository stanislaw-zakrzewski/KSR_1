package results;

import java.util.List;

public class Recall {
    public static float calculate(String tag, List<String> correctLabels, List<String> resultLabels) {
        int truePositives = 0;
        int falseNegatives = 0;

        for (int i = 0; i < correctLabels.size(); i++) {
            if (correctLabels.get(i).equals(tag)) {
                if (resultLabels.get(i).equals(tag)) {
                    truePositives++;
                } else {
                    falseNegatives++;
                }
            }
        }
        return ((float) truePositives) / ((float) (falseNegatives + truePositives));
    }
}
