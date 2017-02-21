package email.com.gmail.ttsai0509.mustache.token;

public enum Type {

    EOF,
    PLAINTEXT,
    WHITESPACE,
    NEWLINE,

    DELIMITER_START,
    DELIMITER_END,

    TAG_CLOSE,
    TAG_COMMENT,
    TAG_SECTION,
    TAG_INVERSE,

    SELF,
    VARIABLE,
    DOT,
    PARAM_START,
    PARAM_END,
    PARAM_NEXT

}
