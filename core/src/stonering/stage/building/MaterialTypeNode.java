package stonering.stage.building;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import stonering.enums.materials.MaterialMap;

/**
 * {@link Tree.Node} for allowing items of certain type and material to be used in construction.
 *
 * @author Alexander on 26.03.2020
 */
public class MaterialTypeNode extends ItemSelectionNode {
    public final int material; // id

    public MaterialTypeNode(String type, int material, int required, int all) {
        super(MaterialMap.instance().getMaterial(material).name + " " + type, required + "/" + all);
        this.material = material;
        checkbox.addListener(new ChangeListener() {

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ((TypeNode) getParent()).update();
            }
        });
    }

    public void set(boolean enable) {
        checkbox.setChecked(enable);
    }
}
