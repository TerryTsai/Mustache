package email.com.gmail.ttsai0509.mustache.ast;

public class TextNode extends ASTNode {

    private final char[] text;

    public TextNode(ASTNode parent, char[] text) {
        super(parent);
        this.text = text;
    }

    public char[] getText() {
        return text;
    }

}
