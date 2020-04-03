package stonering.stage.building;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

/**
 * {@link Tree.Node} for allowing items of certain type and material to be used in construction.
 *
 * @author Alexander on 26.03.2020
 */
public class MaterialTypeNode extends ItemSelectionNode {

    public MaterialTypeNode(String type, String material, int required, int all) {
        super(material + " " + type, required + "/" + all);
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
