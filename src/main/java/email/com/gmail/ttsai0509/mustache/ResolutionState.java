package email.com.gmail.ttsai0509.mustache;

class ResolutionState {

    private static final int DEFAULT_SIZE = 4;

    private int length;
    private int position;
    private String[] array;

    ResolutionState() {
        this.length = 0;
        this.position = 0;
        this.array = new String[DEFAULT_SIZE];
    }

    void add(String variable) {
        if (length == array.length)
            array = ArrayResize.resize(array, length * 2);

        array[length++] = variable;
    }

    void reset() {
        position = 0;
    }

    String next() {
        return array[position++];
    }

    boolean hasNext() {
        return position < length;
    }

}
