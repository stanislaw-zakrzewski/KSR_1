package matching_words.word_comparators;

public class NGrams implements WordComparator{

    //Uogulniona miara NGranow

    @Override
    public float similarity(Object o1, Object o2) {
        String s1 = (String)o1;
        String s2 = (String)o2;
        s1 = s1.toLowerCase();
        s2 = s2.toLowerCase();
        int N = 0;
        int h = 0;

        if (s1.length() > s2.length()) N = s1.length();
        else N = s2.length();

        for (int i=1;i<=s1.length();i++) {
            for (int j=0;j<(s1.length()-i+1);j++) {
                if (s2.contains((s1.substring(j, j+i)))) h++;
            }
        }

        return (h * (2f/((N*N)+N)));
    }
}
