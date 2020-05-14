package stonering.widget;

import java.util.Optional;
import java.util.function.Consumer;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Tree;
import com.badlogic.gdx.utils.Array;

import stonering.enums.ControlActionsEnum;

/**
 * @author Alexander on 04.05.2020
 */
public class NavigableTree<T extends Tree.Node<T, String, Actor>> extends Tree<T, String> {
    private Consumer<Node> selectionConsumer;

    public NavigableTree(Skin skin) {
        super(skin);
        getSelection().setMultiple(false);
        selectionConsumer = node -> {}; // placeholder consumer
    }

    public void accept(ControlActionsEnum action) {
        if (getRootNodes().isEmpty()) return; // no handling for empty tree
        if (getSelection().isEmpty()) getSelection().set(getRootNodes().get(0)); // init selection
        switch (action) {
            case UP:
                navigate(-1);
                break;
            case DOWN:
                navigate(1);
                break;
            case LEFT:
                navigateLevel(-1);
                break;
            case RIGHT:
                navigateLevel(1);
                break;
            case SELECT:
                Optional.ofNullable(ensureSelection()).ifPresent(selectionConsumer);
        }
    }

    public void navigate(int delta) {
        T node = ensureSelection();
        if (node == null) return; // tree is empty
        if (delta > 0) {
            getSelection().set(getNextNode(node));
        } else {
            getSelection().set(getPreviousNode(node));
        }
    }

    public void navigateLevel(int delta) {
        T node = ensureSelection();
        if (node == null) return; // tree is empty
        if (delta > 0) {
            if (node.getChildren().isEmpty()) {
                navigate(1); // go to next node
            } else {
                if (node.isExpanded()) {
                    getSelection().set(node.getChildren().get(0)); // go to first child
                } else {
                    node.setExpanded(true); // expand
                }
            }
        } else {
            if (!node.getChildren().isEmpty() && node.isExpanded()) {
                node.setExpanded(false); // collapse
            } else {
                getSelection().set(node.getParent() != null ? node.getParent() : getRootNodes().get(0));
            }
        }
    }

    private int getNodeIndex(Array<T> array, T node) {
        for (int i = 0; i < array.size; i++) {
            if (node == array.get(i)) return i;
        }
        return -1;
    }

    private T getNextNode(T node) {
        if (node.isExpanded()) {
            return node.getChildren().get(0);
        } else {
            T node2 = node;
            while (node2 != null) {
                Array<T> array = getSiblings(node2);
                int index = getNodeIndex(array, node2);
                if (index < array.size - 1) return array.get(index + 1); // return next sibling
                node2 = node2.getParent(); // 1 level up
            }
            return node; // node was last
        }
    }

    private T getPreviousNode(T node) {
        Array<T> array = getSiblings(node);
        int index = getNodeIndex(array, node);
        if (index == 0) {
            return node.getParent() != null ? node.getParent() : node; // return parent or first node
        } else {
            T targetNode = array.get(index - 1);
            while (targetNode.isExpanded()) {
                Array<T> children = targetNode.getChildren();
                targetNode = children.get(children.size - 1);
            }
            return targetNode;
        }
    }

    private Array<T> getSiblings(T node) {
        return node.getParent() != null ? node.getParent().getChildren() : getRootNodes();
    }

    public T getSelectedNode() {
        return getSelection().getLastSelected();
    }

    private T ensureSelection() {
        if (getRootNodes().isEmpty()) return null;
        if (!getSelection().isEmpty()) return getSelection().getLastSelected();
        getSelection().set(getRootNodes().get(0));
        return getRootNodes().get(0);
    }

    public void setSelectionConsumer(Consumer<Node> consumer) {
        if(consumer != null) selectionConsumer = consumer;
    }
}
