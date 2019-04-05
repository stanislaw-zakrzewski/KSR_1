package results;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Precision {
    public static float calculate(List<String> tags, List<String> correctLabels, List<String> resultLabels) {
        Map<String, Integer> truePositives = new HashMap<>();
        Map<String, Integer> falsePositives = new HashMap<>();
        for(String tag : tags) {
            truePositives.put(tag, 0);
            falsePositives.put(tag, 0);
        }
        for(int i  = 0; i < correctLabels.size(); i++) {
            String correct = correctLabels.get(i);
            if(correctLabels.get(i).equals(resultLabels.get(i))) {
                truePositives.replace(correct, truePositives.get(correct) + 1);
            } else {
                falsePositives.replace(correct, falsePositives.get(correct) + 1);
            }
        }
        int sumTP = 0;
        int sumFP = 0;
        for(String tag : tags) {
            sumTP += truePositives.get(tag);
            sumFP += falsePositives.get(tag);
        }
        return ((float)sumTP)/((float)(sumFP + sumTP));
    }
}
