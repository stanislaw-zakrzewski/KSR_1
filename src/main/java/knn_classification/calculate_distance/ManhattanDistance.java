package knn_classification.calculate_distance;

import java.util.List;

public class ManhattanDistance implements Distance {
    @Override
    public float calculate(List<Float> vector1, List<Float> vector2) {
        float sum = 0;
        for (int i=0;i<vector1.size();i++) {
            sum += Math.abs(vector1.get(i) - vector2.get(i));
        }
        return sum;
    }
}
