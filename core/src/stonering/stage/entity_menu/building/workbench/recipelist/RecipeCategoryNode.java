package stonering.stage.entity_menu.building.workbench.recipelist;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Tree;
import com.badlogic.gdx.utils.Align;

import stonering.util.lang.StaticSkin;
import stonering.widget.BackgroundGenerator;

/**
 * @author Alexander on 25.04.2020
 */
public class RecipeCategoryNode extends Tree.Node {
    public final String category;

    public RecipeCategoryNode(String category, float width) {
        super(new Table());
        this.category = category;
        Label label = new Label(category, StaticSkin.getSkin());
        label.setAlignment(Align.left);
        Table table = (Table) getActor();
        table.add(label).size(width - 20, 50).padLeft(20);
        table.setBackground(new BackgroundGenerator().generate(1,1,1,1));
    }
}
