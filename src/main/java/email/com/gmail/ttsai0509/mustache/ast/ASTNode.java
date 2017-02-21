package email.com.gmail.ttsai0509.mustache.ast;

public abstract class ASTNode {

    protected ASTNode parent;

    public ASTNode(ASTNode parent) {
        this.parent = parent;
    }

}
