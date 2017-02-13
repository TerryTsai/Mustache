package email.com.gmail.ttsai0509.mustache;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.Reader;

class Tokenizer {

    private static final char TAG_OPEN = '{';
    private static final char TAG_CLOSE = '}';
    private static final char TAG_END = '/';
    private static final char TAG_SECTION = '#';
    private static final char TAG_INVERSE = '^';
    private static final char TAG_COMMENT = '!';
    private static final int TAG_DEFAULT = 16;
    private static final int TEXT_LIMIT = 100;
    private static final char[] COMMENT_CONTENT = new char[0];
    private static final char[] NEWLINE_DOS_CONTENT = new char[] {'\r', '\n'};
    private static final char[] NEWLINE_UNIX_CONTENT = new char[] {'\n'};

    private final PushbackReader pb;

    Tokenizer(Reader pb) {
        this.pb = new PushbackReader(pb, 2);
    }

    Token readToken() throws IOException {
        int a = pb.read();
        if (a == -1 || a == 65535) {
            return Token.EOF;
        } else if (a == '\n') {
            return Token.NEWLINE(NEWLINE_UNIX_CONTENT);
        } else if (a != '\r' && a != TAG_OPEN) {
            return Token.TEXT(consumeText(a));
        }

        int b = pb.read();
        if (b == -1 || b == 65535) {
            pb.unread(b);
            return Token.TEXT(a);
        } else if (a == '\r' && b == '\n') {
            return Token.NEWLINE(NEWLINE_DOS_CONTENT);
        } else if (b != TAG_OPEN) {
            return Token.TEXT(consumeText(a, b));
        }

        int c = pb.read();
        if (c == -1 || c == 65535) {
            pb.unread(c);
            return Token.TEXT(a, b);
        } else if (c == TAG_OPEN || c == TAG_CLOSE) {
            return Token.TEXT(consumeText(a, b, c));
        }

        // Next Token Must Be A Tag
        switch (c) {
            case TAG_END: return Token.END(consumeTag());
            case TAG_SECTION: return Token.SECTION(consumeTag());
            case TAG_INVERSE: return Token.INVERSE(consumeTag());
            case TAG_COMMENT: return Token.COMMENT(consumeComment());
            default: pb.unread(c); return Token.VARIABLE(consumeTag());
        }
    }

    private char[] consumeTag() throws IOException {
        int length = 0;
        char[] buffer = new char[TAG_DEFAULT];
        int a = pb.read();

        // Consume tag variable (no limits here)
        while (a != TAG_CLOSE && a != -1 && a != 65535 && a != '\n' && a != '\r') {
            if (length == buffer.length)
                buffer = ArrayResize.resize(buffer, buffer.length * 2);
            buffer[length++] = (char) a;
            a = pb.read();
        }

        // Already decided this is a tag
        int d = pb.read();
        if (d != TAG_CLOSE) {
            throw new IOException(String.format("Missing closing tag after %s", new String(buffer)));
        }

        return ArrayResize.resize(buffer, length);
    }

    private char[] consumeText(int... prefix) throws IOException {
        // Handle already consumed text
        int length = 0;
        char[] buffer = new char[TEXT_LIMIT];
        for (int a : prefix) buffer[length++] = (char) a;

        // Read up to EOF, EOL, start tag, or text limit
        int a = pb.read();
        while (a != TAG_OPEN && a != -1 && a != 65535 && a != '\n' && a != '\r' && length < TEXT_LIMIT) {
            buffer[length++] = (char) a;
            a = pb.read();
        }

        // Unread EOF, start tag, or text limit
        pb.unread(a);

        return ArrayResize.resize(buffer, length);
    }

    private char[] consumeComment() throws IOException {
        // Read up to EOF, start tag, or text limit
        int a = pb.read();
        while (a != TAG_CLOSE && a != -1 && a != 65535) {
            a = pb.read();
        }

        // Already decided this is a tag
        int b = pb.read();
        if (b != TAG_CLOSE) {
            throw new IOException("Missing closing tag after comment");
        }

        // Comment content is just discarded
        return COMMENT_CONTENT;
    }

}