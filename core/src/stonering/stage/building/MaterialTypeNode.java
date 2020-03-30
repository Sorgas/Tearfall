package stonering.stage.building;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Tree;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import stonering.util.global.StaticSkin;

/**
 * {@link Tree.Node} for allowing items of certain type and material to be used in construction.
 *
 * @author Alexander on 26.03.2020
 */
public class MaterialTypeNode extends Tree.Node {
    private final Table table;
    private String type;
    private String material;
    private int current;
    private int all;
    private Label numberLabel;
    public CheckBox checkbox;

    public MaterialTypeNode(String type, String material) {
        super(new Table());
        table = (Table) getActor();
        table.add(new Label(material + " " + type, StaticSkin.getSkin()));
        table.add(numberLabel = new Label(current + "/" + all, StaticSkin.getSkin()));
        table.add(checkbox = new CheckBox("", StaticSkin.getSkin())).growX().right();
        checkbox.setProgrammaticChangeEvents(false); // this checkbox is updated by parent node, and should not fire recursive event
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
