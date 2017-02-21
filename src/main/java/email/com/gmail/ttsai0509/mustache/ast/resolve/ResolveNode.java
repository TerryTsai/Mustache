package email.com.gmail.ttsai0509.mustache.ast.resolve;

import email.com.gmail.ttsai0509.mustache.ast.ASTNode;

public abstract class ResolveNode extends ASTNode {

    protected char[] name;
    protected ResolveNode then;

    public ResolveNode(ASTNode parent, char[] name) {
        super(parent);
        this.name = name;
        this.then = null;
    }

    protected void then(ResolveNode then) {
        this.then = then;
    }

}
