package extracting;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.stream.Collectors;

public class NElementsSelector {
    public static List<String> selectN(Map<String, Float> vector, int n) {
        List<Entry<String, Float>> greatest = findGreatest(vector, n);
        return greatest.stream().map(Entry::getKey).collect(Collectors.toList());
    }

    private static <K, V extends Comparable<? super V>> List<Entry<K, V>>
    findGreatest(Map<K, V> map, int n)
    {
        Comparator<? super Entry<K, V>> comparator =
                (Comparator<Entry<K, V>>) (e0, e1) -> {
                    V v0 = e0.getValue();
                    V v1 = e1.getValue();
                    return v0.compareTo(v1);
                };
        PriorityQueue<Entry<K, V>> highest =
                new PriorityQueue<>(n, comparator);
        for (Entry<K, V> entry : map.entrySet())
        {
            highest.offer(entry);
            while (highest.size() > n)
            {
                highest.poll();
            }
        }

        List<Entry<K, V>> result = new ArrayList<>();
        while (highest.size() > 0)
        {
            result.add(highest.poll());
        }
        return result;
    }
}
