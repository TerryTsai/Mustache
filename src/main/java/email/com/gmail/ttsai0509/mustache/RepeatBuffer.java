package email.com.gmail.ttsai0509.mustache;

class RepeatBuffer {

    private static final int DEFAULT_CACHE_SIZE = 32;

    private int length;
    private int position;
    private Token[] array;

    RepeatBuffer() {
        clear();
    }

    void clear() {
        this.position = 0;
        this.array = new Token[DEFAULT_CACHE_SIZE];
        this.length = 0;
    }

    Token next() {
        return array[position++];
    }

    boolean hasNext() {
        return position < length;
    }

    int addToken(Token token) {
        // Position stays at end of buffer until manually set.
        // This doesn't seem like the place for that though.
        if (length == position)
            position++;

        if (length == array.length)
            array = ArrayResize.resize(array, length * 2);

        array[length] = token;
        return length++;
    }

    Token getToken(int location) {
        return (location >= 0 && location < length) ? array[location] : null;
    }

    int position() {
        return this.position - 1;
    }

    void moveTo(int position) {
        this.position = position;
    }

}
