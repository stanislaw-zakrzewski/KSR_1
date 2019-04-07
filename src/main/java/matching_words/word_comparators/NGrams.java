package matching_words.word_comparators;

public class NGrams implements WordComparator {
    //TODO change name of this class and implement it

    @Override
    public float similarity(Object o1, Object o2) {
        int N = 0;
        String s1 = (String)o1;
        String s2 = (String)o2;
        s1 = s1.toLowerCase();
        s2 = s2.toLowerCase();

        if (s1.length() > s2.length()) N = s1.length();
        else N = s2.length();

        int n = 3;
        int h = 0;

        for (int i=0;i<s1.length()-n;i++) {
            if (s2.contains((s1.substring(i, i+n)))) h++;
        }

        return ((float)h/((float)N-(float)n+1));
    }
}
