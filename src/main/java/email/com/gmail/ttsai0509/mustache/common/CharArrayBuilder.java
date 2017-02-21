package email.com.gmail.ttsai0509.mustache.common;

public class CharArrayBuilder {

    private int length;
    private char[] data;

    public CharArrayBuilder() {
        reset();
    }

    public CharArrayBuilder append(char c) {
        if (length >= data.length)
            data = resize(data, length * 2);
        data[length++] = c;
        return this;
    }

    public char[] toCharArray() {
        return resize(data, length);
    }

    public void reset() {
        this.length = 0;
        this.data = new char[8];
    }

    public int size() {
        return length;
    }

    private static char[] resize(char[] data, int newLength) {
        int toCopy = (data.length < newLength) ? data.length : newLength;
        char[] newData = new char[newLength];
        System.arraycopy(data, 0, newData, 0, toCopy);
        return newData;
    }

}
