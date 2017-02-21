package email.com.gmail.ttsai0509.mustache.tokenizer;

import email.com.gmail.ttsai0509.mustache.common.Math;
import email.com.gmail.ttsai0509.mustache.common.PushbackUtils;
import email.com.gmail.ttsai0509.mustache.consumer.*;
import email.com.gmail.ttsai0509.mustache.token.Token;
import email.com.gmail.ttsai0509.mustache.token.Type;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.Reader;
import java.util.Arrays;

public class Tokenizer {

    private static final int[] WHITESPACE = new int[] {' ', '\t'};
    private static final int[] NEWLINE = new int[] {'\r', '\n'};
    private static final int[] NONTEXT = new int[] {'{', ' ', '\n', '\t', '\r', -1, 65535};
    private static final int[] DELIMITER_START = new int[] {'{', '{'};
    private static final int[] DELIMITER_END = new int[] {'}', '}'};
    private static final int[] VARIABLE = "_abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".chars().map(c -> c).toArray();
    static {
        // Consumer uses Array.binarySearch
        Arrays.sort(NONTEXT);
        Arrays.sort(VARIABLE);
    }

    private int pushback;
    private final PushbackReader in;
    private final Consumer consumer;

    public Tokenizer(Reader in) {
        this.pushback = Math.max(2, DELIMITER_START.length, DELIMITER_END.length);
        this.in = new PushbackReader(in, pushback);
        this.consumer = new Consumer(this.in, Integer.MAX_VALUE);
    }

    public Token next(boolean asPlaintext) throws IOException {
        int[] peek = PushbackUtils.peek(in, pushback);

        // Whitespace & EOF
        switch (peek[0]) {
            case -1: return new Token(Type.EOF, consumer.consumeChar(-1));
            case 65535: return new Token(Type.EOF, consumer.consumeChar(65535));
            case ' ': return new Token(Type.WHITESPACE, consumer.consumeAll(WHITESPACE));
            case '\t': return new Token(Type.WHITESPACE, consumer.consumeAll(WHITESPACE));
            case '\n': return new Token(Type.NEWLINE, consumer.consumeChar('\n'));
            case '\r': return (peek[1] == '\n')
                    ? new Token(Type.NEWLINE, consumer.consumeSeq(NEWLINE))
                    : new Token(Type.NEWLINE, consumer.consumeChar('\r'));
        }

        // Delimiter
        if (Arrays.equals(peek, DELIMITER_START))
            return new Token(Type.DELIMITER_START, consumer.consumeSeq(DELIMITER_START));
        if (Arrays.equals(peek, DELIMITER_END))
            return new Token(Type.DELIMITER_END, consumer.consumeSeq(DELIMITER_END));

        // Plaintext
        if (asPlaintext)
            return new Token(Type.PLAINTEXT, consumer.consumeTil(NONTEXT));

        // Tags
        switch (peek[0]) {
            case '.': return new Token(Type.DOT, consumer.consumeChar('.'));
            case '$': return new Token(Type.SELF, consumer.consumeChar('$'));
            case '/': return new Token(Type.TAG_CLOSE, consumer.consumeChar('/'));
            case ')': return new Token(Type.PARAM_END, consumer.consumeChar(')'));
            case ',': return new Token(Type.PARAM_NEXT, consumer.consumeChar(','));
            case '#': return new Token(Type.TAG_SECTION, consumer.consumeChar('#'));
            case '^': return new Token(Type.TAG_INVERSE, consumer.consumeChar('^'));
            case '!': return new Token(Type.TAG_COMMENT, consumer.consumeChar('!'));
            case '(': return new Token(Type.PARAM_START, consumer.consumeChar('('));
        }

        // Variables
        return new Token(Type.VARIABLE, consumer.consumeAll(VARIABLE));
    }

}
