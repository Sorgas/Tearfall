package stonering.stage.entity_menu.building;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Tree;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import stonering.enums.buildings.blueprint.MaterialSelectionConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link Tree.Node} for all items of certain type.
 * On checking, checks all its sub nodes (allows whole type for building).
 *
 * @author Alexander on 25.03.2020
 */
public class TypeNode extends ItemSelectionNode {
    public final String type;
    public final List<MaterialTypeNode> nodes = new ArrayList<>();

    public TypeNode(String type, int number, MaterialSelectionConfig config) {
        super(type, number + "", config);
        this.type = type;
        checkbox.setChecked(config.types.contains(type));
        checkbox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                set(checkbox.isChecked());
            }
        });
    }

    @Override
    public void set(boolean state) {
        setThis(state);
        for (Tree.Node node : getChildren()) {
            ((MaterialTypeNode) node).setThis(state);
        }
    }

    public void addMaterialNode(MaterialTypeNode node) {
        add(node);
        nodes.add(node);
    }

    public void setThis(boolean state) {
        checkbox.setChecked(state);
        if(state) {
            config.types.add(type);
        } else {
            config.types.remove(type);
        }
    }

    /**
     * Updates state of this node, based on states of its children.
     */
    public void update() {
        boolean state = true;
        for (Tree.Node node : getChildren()) {
            if(!((MaterialTypeNode) node).checked()) {
                state = false;
                break;
            }
        }
        setThis(state);
    }
}
