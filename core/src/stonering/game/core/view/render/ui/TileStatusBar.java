package stonering.game.core.view.render.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import stonering.util.global.StaticSkin;
import stonering.util.geometry.Position;

/**
 * Renders information about tile under camera position.
 *
 * @author Alexander Kuzyakov on 12.10.2017.
 */
public class TileStatusBar extends Table {
    private int x;
    private int y;
    private int z;
    private int area;
    private String material;
    private int flooding;

    private Label coordinates;
    private Label cellType;

    public TileStatusBar() {
        super();
        createTable();
    }

    private void createTable() {
        align(Align.bottomLeft);
        defaults().align(Align.left);

        cellType = new Label("", StaticSkin.getSkin());
        add(new Label("Material: ", StaticSkin.getSkin()));
        add(cellType).row();

        coordinates = new Label("", StaticSkin.getSkin());
        add(new Label("Coordinates: ", StaticSkin.getSkin()));
        add(coordinates).row();
    }

    public void setData(Position camera, String material, int area, int flooding) {
        x = camera.getX();
        y = camera.getY();
        z = camera.getZ();
        this.area = area;
        this.material = material;
        this.flooding = flooding;
        updateLabels();
    }

    private void updateLabels() {
        coordinates.setText("(" + x + ", " + y + ", " + z + ") " + flooding);
        cellType.setText(material);
    }
}