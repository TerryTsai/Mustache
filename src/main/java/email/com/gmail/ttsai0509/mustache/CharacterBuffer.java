package email.com.gmail.ttsai0509.mustache;

class CharacterBuffer {

    private int position;
    private char[] array;

    CharacterBuffer() {
        set(new char[0]);
    }

    int read(char[] cbuf, int off, int len) {
        len = Math.min(array.length - position, len);
        System.arraycopy(array, position, cbuf, off, len);
        position += len;
        return len;
    }

    void set(char[] buffer) {
        this.position = 0;
        this.array = buffer;
    }

    boolean hasNext() {
        return position < array.length;
    }

}
