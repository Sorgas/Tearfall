package stonering.game.core.view;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import stonering.utils.global.StaticSkin;
import stonering.global.utils.Position;

/**
 * Created by Alexander on 12.10.2017.
 *
 * Renders information about focused tile
 */
public class TileStatusBar extends Table {
    private int x;
    private int y;
    private int z;
    private String material;

    private Label coordinates;
    private Label cellType;

    public TileStatusBar() {
        super();
        createTable();
    }

    private void createTable() {
        this.left().bottom();
        this.pad(10);
        cellType = new Label("", StaticSkin.getSkin());
        this.add(new Label("Material: ", StaticSkin.getSkin()));
        this.add(cellType).row();
        coordinates = new Label("", StaticSkin.getSkin());
        this.add(new Label("Coordinates: ", StaticSkin.getSkin()));
        this.add(coordinates).row();
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
