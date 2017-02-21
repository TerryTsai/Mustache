package email.com.gmail.ttsai0509.mustache.consumer;

import email.com.gmail.ttsai0509.mustache.common.CharArrayBuilder;

import java.io.IOException;
import java.io.PushbackReader;
import java.util.Arrays;

@SuppressWarnings("unused")
public class Consumer {

    private final int limit;
    private final PushbackReader in;
    private final CharArrayBuilder out;

    public Consumer(PushbackReader in) {
        this(in, Integer.MAX_VALUE);
    }

    public Consumer(PushbackReader in, int limit) {
        this.limit = limit;
        this.in = in;
        this.out = new CharArrayBuilder();
    }

    public char[] consumeChar(int val) throws IOException, ConsumerException {
        out.reset();
        int read = in.read();
        if (read != val) {
            throw new ConsumerException();
        }
        return out.append((char) read).toCharArray();
    }

    public char[] consumeAny(int[] vals) throws IOException, ConsumerException {
        out.reset();
        int read = in.read();
        for (int val : vals) {
            if (read == val) {
                return out.append((char) read).toCharArray();
            }
        }
        throw new ConsumerException();
    }

    public char[] consumeSeq(int[] vals) throws IOException, ConsumerException {
        if (vals.length > limit) {
            throw new ConsumerException();
        }

        out.reset();
        int read = in.read();
        for (int val : vals) {
            if (read != val) {
                throw new ConsumerException();
            }
            out.append((char) read);
            read = in.read();
        }
        in.unread(read);
        return out.toCharArray();
    }

    public char[] consumeAll(int[] vals) throws IOException, ConsumerException {
        out.reset();
        int read = in.read();
        if (Arrays.binarySearch(vals, read) < 0) {
            throw new ConsumerException();
        }
        while (Arrays.binarySearch(vals, read) >= 0 && out.size() < limit) {
            out.append((char) read);
            read = in.read();
        }
        in.unread(read);
        return out.toCharArray();
    }

    public char[] consumeTil(int[] vals) throws IOException, ConsumerException {
        out.reset();
        int read = in.read();
        if (Arrays.binarySearch(vals, read) >= 0) {
            throw new ConsumerException();
        }
        while (Arrays.binarySearch(vals, read) < 0 && out.size() < limit) {
            out.append((char) read);
            read = in.read();
        }
        in.unread(read);
        return out.toCharArray();
    }

}
