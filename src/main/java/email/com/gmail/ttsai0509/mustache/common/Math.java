package email.com.gmail.ttsai0509.mustache.common;

public enum Math {

    ;

    public static int max(int ... vals) {
        int max = vals[0];
        for (int val : vals)
            if (val > max)
                max = val;
        return max;
    }
}
