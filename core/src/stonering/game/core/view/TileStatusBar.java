package stonering.game.core.view;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import stonering.global.StaticSkin;
import stonering.utils.Position;

/**
 * Created by Alexander on 12.10.2017.
 */
public class TileStatusBar {

    private Table table;
    private int x;
    private int y;
    private int z;
    private String material;

    Label coordinates;
    Label cellType;

    public TileStatusBar() {
        createTable();
    }

    private void createTable() {
        table = new Table();
        table.left().bottom();
        table.pad(10);
        cellType = new Label("", StaticSkin.getSkin());
        table.add(cellType).row();
        coordinates = new Label("", StaticSkin.getSkin());
        table.add(coordinates).row();
    }

    public void setData(Position camera, String material) {
        x = camera.getX();
        y = camera.getY();
        z = camera.getZ();
        this.material = material;
    }

    private void updateLabels() {
        coordinates.setText("(" + x + ", " + y + ", " + z + ")");
        cellType.setText(material);
    }

}
