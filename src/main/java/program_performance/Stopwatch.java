package program_performance;

public class Stopwatch {
    private long startTime;

    public Stopwatch() {
        startTime = System.nanoTime();
    }

    public String getTime() {
        long endTime = System.nanoTime();
        String s = Long.toString((endTime - startTime) / 1000000);
        while (s.length() < 3) {
            s = "0" + s;
        }
        return (endTime - startTime) / 1000000000 + "." + s;
    }
}
