package stonering.stage.entity_menu.building;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import stonering.enums.buildings.blueprint.MaterialSelectionConfig;
import stonering.enums.materials.MaterialMap;

import java.util.HashSet;

/**
 * {@link Tree.Node} for allowing items of certain type and material to be used in construction.
 *
 * @author Alexander on 26.03.2020
 */
public class MaterialTypeNode extends ItemSelectionNode {
    public final int material; // id

    public MaterialTypeNode(String type, int material, int required, int all, MaterialSelectionConfig config) {
        super(MaterialMap.getMaterial(material).name + " " + type, required + "/" + all, config);
        this.material = material;
        checkbox.addListener(new ChangeListener() {

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                set(checkbox.isChecked());
            }
        });
    }

    @Override
    public void set(boolean state) {
        TypeNode parent = (TypeNode) getParent();
        setThis(state);
        parent.update();
    }

    public void setThis(boolean state) {
        checkbox.setChecked(state);
        TypeNode parent = (TypeNode) getParent();
        if(state) {
            config.map.putIfAbsent(parent.type, new HashSet<>());
            config.map.get(parent.type).add(material);
        } else {
            config.map.get(parent.type).remove(material);
        }
    }
}
