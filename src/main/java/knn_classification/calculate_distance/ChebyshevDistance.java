package knn_classification.calculate_distance;

import java.util.List;

public class ChebyshevDistance implements Distance {
    @Override
    public float calculate(List<Float> vector1, List<Float> vector2) {
        float mx = Math.abs(vector1.get(0) - vector2.get(0));
        for (int i=0;i<vector1.size();i++) {
            if (Math.abs(vector1.get(i) - vector2.get(i)) > mx) mx = Math.abs(vector1.get(i) - vector2.get(i));
        }
        return mx;
    }
}
