package email.com.gmail.ttsai0509.mustache.ast.context;

import email.com.gmail.ttsai0509.mustache.ast.ASTNode;
import email.com.gmail.ttsai0509.mustache.ast.resolve.ResolveNode;

import java.util.ArrayList;
import java.util.List;

public abstract class ContextNode extends ASTNode {

    protected ResolveNode context;
    protected List<ASTNode> children;

    public ContextNode(ASTNode parent, ResolveNode context) {
        super(parent);
        this.context = context;
        this.children = new ArrayList<>();
    }

    public void add(ASTNode child) {
        children.add(child);
    }

}
