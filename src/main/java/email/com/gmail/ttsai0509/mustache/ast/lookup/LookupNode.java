package email.com.gmail.ttsai0509.mustache.ast.lookup;

import email.com.gmail.ttsai0509.mustache.ast.ASTNode;

public class LookupNode extends ASTNode {
    protected char[] name;
    protected LookupNode then;

    public LookupNode(ASTNode parent, char[] name) {
        super(parent);
        this.name = name;
        this.then = null;
    }

    protected void then(LookupNode then) {
        this.then = then;
    }

}
