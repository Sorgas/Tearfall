package stonering.stage.workbench.recipelist;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Tree;
import com.badlogic.gdx.utils.Align;

import stonering.util.global.StaticSkin;
import stonering.widget.BackgroundGenerator;

/**
 * @author Alexander on 25.04.2020
 */
public class RecipeCategoryNode extends Tree.Node {
    private final int NODE_WIDTH = 270;

    public RecipeCategoryNode(String category) {
        super(new Table());
        fillTable((Table) getActor(), category);
    }

    private void fillTable(Table table, String category) {
        Label label = new Label(category, StaticSkin.getSkin());
        label.setAlignment(Align.left);
        table.add(label).size(NODE_WIDTH, 50).padLeft(20);
        table.setBackground(new BackgroundGenerator().generate(1,1,1,1));
    }
}
