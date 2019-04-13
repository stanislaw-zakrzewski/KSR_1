package matching_words.word_comparators;

public class OurComparator implements WordComparator{
    @Override
    public float similarity(Object o1, Object o2) {
        String s1 = (String)o1;
        String s2 = (String)o2;
        if(s1.charAt(0) == s2.charAt(0) && s1.charAt(1) == s2.charAt(2)) {
            return (float)1/(1+ Math.abs(Integer.parseInt(s1.substring(2))-Integer.parseInt(s2.substring(2))));
        }
        return 0;
    }
}
