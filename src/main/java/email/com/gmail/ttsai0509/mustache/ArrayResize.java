package email.com.gmail.ttsai0509.mustache;

enum ArrayResize {

    ;

    static char[] resize(char[] array, int length) {
        if (length == array.length)
            return array;

        int toCopy = (length < array.length) ? length : array.length;
        char[] newArray = new char[length];
        System.arraycopy(array, 0, newArray, 0, toCopy);
        return newArray;
    }

    static Token[] resize(Token[] array, int length) {
        if (length == array.length)
            return array;

        int toCopy = (length < array.length) ? length : array.length;
        Token[] newArray = new Token[length];
        System.arraycopy(array, 0, newArray, 0, toCopy);
        return newArray;
    }

    static String[] resize(String[] array, int length) {
        if (length == array.length)
            return array;

        int toCopy = (length < array.length) ? length : array.length;
        String[] newArray = new String[length];
        System.arraycopy(array, 0, newArray, 0, toCopy);
        return newArray;
    }

    static ContextFrame[] resize(ContextFrame[] array, int length) {
        if (length == array.length)
            return array;

        int toCopy = (length < array.length) ? length : array.length;
        ContextFrame[] newArray = new ContextFrame[length];
        System.arraycopy(array, 0, newArray, 0, toCopy);
        return newArray;
    }

    // Prefer type-safe versions above.
    @SuppressWarnings({"unchecked", "unused"})
    static <T> T[] resize(T[] array, int length) {
        if (length == array.length)
            return array;

        int toCopy = (length < array.length) ? length : array.length;
        T[] newArray = (T[]) new Object[length];
        System.arraycopy(array, 0, newArray, 0, toCopy);
        return newArray;
    }

}
