package email.com.gmail.ttsai0509.mustache.common;

import java.io.IOException;
import java.io.PushbackReader;

public enum PushbackUtils {

    ;

    public static int[] peek(PushbackReader in, int n) throws IOException {
        int[] peek = new int[n];
        for (int i = 0; i < n; i++)
            peek[i] = in.read();
        for (int j = 0; j < n; j++)
            in.unread(peek[n - j - 1]);
        return peek;
    }

}
