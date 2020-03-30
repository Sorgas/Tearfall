package stonering.stage.building;

import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Tree;
import stonering.util.global.StaticSkin;

/**
 * {@link Tree.Node} for all items of certain type.
 * On checking, checks all its sub nodes (allows whole type for building).
 *
 * @author Alexander on 25.03.2020
 */
public class TypeNode extends Tree.Node {
    private final Table table;
    private String itemType;
    private CheckBox checkbox;
    private int number;

    public TypeNode(String itemType, int number) {
        super(new Table());
        table = (Table) getActor();
        table.add(new Label(itemType, StaticSkin.getSkin()));
        table.add(new Label(number + "", StaticSkin.getSkin()));
        table.add(checkbox = new CheckBox("", StaticSkin.getSkin())).growX().right();
        checkbox.setProgrammaticChangeEvents(false); // this checkbox is updated by children nodes, and should not fire recursive event
    }

    public void toggleAll(boolean enable) {
        getChildren().forEach(node -> ((MaterialTypeNode) node).set(enable));
    }

    public void update() {
        checkbox.setChecked(true);
        getChildren().forEach(node -> {
            if(!((MaterialTypeNode) node).checkbox.isChecked()) checkbox.setChecked(false);
        });
    }
}
