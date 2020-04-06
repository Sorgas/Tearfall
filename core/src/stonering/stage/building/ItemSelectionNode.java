package stonering.stage.building;

import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Scaling;
import stonering.util.global.StaticSkin;

/**
 * {@link Tree.Node} with two {@link Label}s and a {@link CheckBox}.
 *
 * @author Alexander on 03.04.2020
 */
public class ItemSelectionNode extends Tree.Node {
    private final Container<Table> container;
    public final Table table;
    public final Label leftLabel;
    public final Label rightLabel;
    public final CheckBox checkbox;

    public ItemSelectionNode(String leftText, String rightText) {
        super(new Container<Table>().fill().height(22));
        container = (Container<Table>) getActor();
        container.setActor(table = new Table());
        table.add(leftLabel = new Label(leftText, StaticSkin.getSkin()));
        table.add(rightLabel = new Label(rightText, StaticSkin.getSkin())).expandX().right().padRight(5);
        table.add(checkbox = new CheckBox("", StaticSkin.getSkin()));
        checkbox.getImageCell().size(22, 22);
        checkbox.getImage().setScaling(Scaling.fill);
        checkbox.setProgrammaticChangeEvents(false); // this checkbox is updated by parent node, and should not fire recursive event
    }

    public boolean checked() {
        return checkbox.isChecked();
    }

    public void width(float width) {
        container.width(width);
    }
}
