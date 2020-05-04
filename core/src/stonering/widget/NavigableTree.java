package stonering.widget;

import java.util.function.Consumer;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Tree;
import com.badlogic.gdx.utils.Array;

import stonering.enums.ControlActionsEnum;

/**
 * @author Alexander on 04.05.2020
 */
public class NavigableTree extends Tree {
    public Consumer<Node> selectionConsumer;

    public NavigableTree(Skin skin) {
        super(skin);
        getSelection().setMultiple(false);
    }

    public void accept(ControlActionsEnum action) {
        if(getRootNodes().isEmpty()) return; // no handling for empty tree
        if(getSelection().isEmpty()) getSelection().set(getRootNodes().get(0)); // init selection
        Node node = getSelection().first();
        switch (action) {
            case UP:
                navigate(-1);
                break;
            case DOWN:
                navigate(1);
                break;
            case LEFT:
                if (node.isExpanded()) {
                    node.setExpanded(false); // collapse expanded node
                } else if (node.getParent() != null) {
                    getSelection().set(node.getParent()); // navigate to parent
                }
                break;
            case RIGHT:
                if (node.isExpanded() || node.getChildren().isEmpty()) {
                    navigate(1);
                } else {
                    node.setExpanded(true);
                }
                break;
            case SELECT:
                if(selectionConsumer != null) selectionConsumer.accept(getSelection().first());
        }
    }

    public void navigate(int delta) {
        Node node = getSelection().first();
        if (delta > 0) {
            getSelection().set(getNextNode(node));
        } else {
            getSelection().set(getPreviousNode(node));
        }
    }

    private int getNodeIndex(Array<Node> array, Node node) {
        for (int i = 0; i < array.size; i++) {
            if (node == array.get(i)) return i;
        }
        return -1;
    }

    private Node getNextNode(Node node) {
        if (node.isExpanded()) {
            return node.getChildren().get(0);
        } else {
            Node node2 = node;
            while (node2 != null) {
                Array<Node> array = getSiblings(node2);
                int index = getNodeIndex(array, node2);
                if (index < array.size - 1) return array.get(index + 1); // return next sibling
                node2 = node2.getParent(); // 1 level up
            }
            return node; // node was last
        }
    }

    private Node getPreviousNode(Node node) {
        Array<Node> array = getSiblings(node);
        int index = getNodeIndex(array, node);
        if (index == 0) {
            return node.getParent() != null ? node.getParent() : node; // return parent or first node
        } else {
            Node targetNode = array.get(index - 1);
            while (targetNode.isExpanded()) {
                Array<Node> children = targetNode.getChildren();
                targetNode = children.get(children.size - 1);
            }
            return targetNode;
        }
    }

    private Array<Node> getSiblings(Node node) {
        return node.getParent() != null ? node.getParent().getChildren() : getRootNodes();
    }
}
