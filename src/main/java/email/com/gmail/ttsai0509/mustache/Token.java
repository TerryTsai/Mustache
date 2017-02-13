package email.com.gmail.ttsai0509.mustache;

@SuppressWarnings({"UnusedParameters", "SimplifiableIfStatement"})
class Token {

    enum Type {EOF, TEXT, COMMENT, VARIABLE, SECTION, INVERSE, END, NEWLINE}

    static boolean matches(Token a, Token b) {
        if (a.type == Type.SECTION && b.type == Type.END)
            return matches(a.content, b.content);
        if (a.type == Type.INVERSE && b.type == Type.END)
            return matches(a.content, b.content);
        if (b.type == Type.SECTION && a.type == Type.END)
            return matches(b.content, a.content);
        if (b.type == Type.INVERSE && a.type == Type.END)
            return matches(b.content, a.content);
        return false;
    }

    private static boolean matches(char[] start, char[] end) {
        if (start == end) return true;
        if (start == null) return false;
        if (end == null) return false;

        // Auto-matching tags
        if (end.length == 0) return true;

        int length = start.length;
        if (length != end.length) return false;
        for (int i = 0; i < length; i++)
            if (start[i] != end[i]) return false;

        return true;
    }

    static final Token EOF = new Token(Type.EOF, new char[0]);

    static Token NEWLINE(char[] name) {
        return new Token(Type.NEWLINE, name);
    }

    static Token COMMENT(char[] name) {
        return new Token(Type.COMMENT, name);
    }

    static Token VARIABLE(char[] name) {
        return new Token(Type.VARIABLE, name);
    }

    static Token SECTION(char[] name) {
        return new Token(Type.SECTION, name);
    }

    static Token INVERSE(char[] name) {
        return new Token(Type.INVERSE, name);
    }

    static Token END(char[] name) {
        return new Token(Type.END, name);
    }

    static Token TEXT(char[] text) {
        return new Token(Type.TEXT, text);
    }

    static Token TEXT(int... texts) {
        StringBuilder sb = new StringBuilder();
        for (int text : texts) sb.append((char) text);
        return new Token(Type.TEXT, sb.toString().toCharArray());
    }

    final Type type;
    final char[] content;

    private Token(Type type, char[] content) {
        this.type = type;
        this.content = content;
    }

    boolean isWhitespace() {
        for (char c : content)
            if (c != ' ' && c != '\t')
                return false;
        return true;
    }

    @Override
    public String toString() {
        return String.format("%-10s : %s", type.name(),
                new String(content).replaceAll("\n", "n").replaceAll("\r", "r"));
    }
}