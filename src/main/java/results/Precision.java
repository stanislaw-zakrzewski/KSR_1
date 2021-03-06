package results;

import java.util.List;

public class Precision {
    public static float calculate(String tag, List<String> correctLabels, List<String> resultLabels) {
        int truePositives = 0;
        int falsePositives = 0;

        for (int i = 0; i < correctLabels.size(); i++) {
            if (resultLabels.get(i).equals(tag)) {
                if (correctLabels.get(i).equals(tag)) {
                    truePositives++;
                } else {
                    falsePositives++;
                }
            }
        }
        return ((float) truePositives) / ((float) (falsePositives + truePositives));
    }
}
