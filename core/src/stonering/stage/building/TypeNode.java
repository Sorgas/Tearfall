package stonering.stage.building;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

/**
 * {@link Tree.Node} for all items of certain type.
 * On checking, checks all its sub nodes (allows whole type for building).
 *
 * @author Alexander on 25.03.2020
 */
public class TypeNode extends ItemSelectionNode {
    private final String type;

    public TypeNode(String itemType, int number) {
        super(itemType, number + "");
        type = itemType;
        checkbox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                toggleAll(checkbox.isChecked());
            }
        });
    }

    /**
     * Toggles all children of this node.
     */
    public void toggleAll(boolean enable) {
        getChildren().forEach(node -> ((MaterialTypeNode) node).set(enable));
    }

    /**
     * Updates state of this node, based on states of its children.
     */
    public void update() {
        checkbox.setChecked(true);
        getChildren().forEach(node -> {
            if(!((MaterialTypeNode) node).checkbox.isChecked()) checkbox.setChecked(false);
        });
    }
}
