package email.com.gmail.ttsai0509.mustache.ast;

import java.util.ArrayList;
import java.util.List;

public class RootNode extends ASTNode {

    private List<ASTNode> children;

    public RootNode(ASTNode parent) {
        super(parent);
        this.children = new ArrayList<>();
    }
}
