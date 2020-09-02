package stonering.stage.entity_menu.container;

import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import stonering.util.lang.StaticSkin;

/**
 * Shows grid of items icons in a grid table.
 *
 * @author Alexander on 03.09.2019.
 */
public class ContainerGrid extends Container {
    private Table mainTable;
    private Table gridTable;
    private Label capacityLabel;

    public ContainerGrid(int capacity, int width, int heigth) {
        mainTable = new Table();
        mainTable.add(capacityLabel = new Label("", StaticSkin.getSkin()));
        gridTable = new Table();
        gridTable.defaults().size(64).space(10);
        mainTable.add(gridTable);
        setActor(mainTable);
    }
}
