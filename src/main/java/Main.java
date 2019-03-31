import extracting.NElementsSelector;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        Map<String, Float> oko = new HashMap<>();
        oko.put("a",1f);
        oko.put("b",2f);
        oko.put("c",3f);
        oko.put("d",4f);
        oko.put("e",1f);
        oko.put("f",3f);
        oko.put("g",2f);
        List<String> e = NElementsSelector.selectN(oko,3);
        e.forEach(System.out::println);
        System.out.println("Hello");
    }
}
