package email.com.gmail.ttsai0509.mustache.token;

public class Token {

    private final Type type;
    private final char[] data;

    public Token(Type type, char[] data) {
        this.type = type;
        this.data = data;
    }

    public boolean hasType(Type type) {
        return this.type == type;
    }

    public Type getType() {
        return type;
    }

    public char[] getData() {
        return data;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (char d: data) {
            switch (d) {
                case '\n': sb.append("\\n"); break;
                case '\r': sb.append("\\r"); break;
                case ' ' : sb.append("\\s"); break;
                case '\t': sb.append("\\t"); break;
                default  : sb.append(d); break;
            }
        }

        return String.format("%-16s = %s", type, sb.toString());
    }
}
