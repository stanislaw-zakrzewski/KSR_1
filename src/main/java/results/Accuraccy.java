package results;

import java.util.List;

public class Accuraccy {
    public static float calculate(String tag, List<String> correctLabels, List<String> resultLabels) {
        int truePositives = 0;
        int falseNegatives = 0;
        int trueNegatives = 0;
        int falsePositives = 0;

        for(int i = 0; i < correctLabels.size(); i++) {
            if(correctLabels.get(i).equals(tag)) {
                if(resultLabels.get(i).equals(tag)) {
                    truePositives ++;
                } else {
                    falseNegatives ++;
                }
            } else {
                if(resultLabels.get(i).equals(tag)) {
                    falsePositives ++;
                } else {
                    trueNegatives ++;
                }
            }
        }

        return ((float) truePositives + trueNegatives) / ((float) (falseNegatives + truePositives + trueNegatives + falsePositives));
    }
}
