package stonering.game.view.render.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import stonering.entity.environment.GameCalendar;
import stonering.enums.blocks.BlockTypesEnum;
import stonering.game.GameMvc;
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
    private byte blockType;

    private Label date;
    private Label coordinates;
    private Label cellType;

    public TileStatusBar() {
        super();
        createTable();
    }

    private void createTable() {
        align(Align.bottomLeft);
        defaults().align(Align.left);
        add(new Label("Date: ", StaticSkin.getSkin()));
        add(date = new Label("", StaticSkin.getSkin())).row();
        add(new Label("Material: ", StaticSkin.getSkin()));
        add(cellType = new Label("", StaticSkin.getSkin())).row();
        add(new Label("Coordinates: ", StaticSkin.getSkin()));
        add(coordinates = new Label("", StaticSkin.getSkin())).row();
    }

    public void setData(Position camera, String material, int area, int blocktype) {
        x = camera.x;
        y = camera.y;
        z = camera.z;
        this.area = area;
        this.material = material;
        this.blockType = (byte) blocktype;
        updateLabels();
    }

    private void updateLabels() {
        coordinates.setText("(" + x + ", " + y + ", " + z + ") " + BlockTypesEnum.getType(blockType));
        cellType.setText(material);
        date.setText(GameMvc.instance().getModel().get(GameCalendar.class).getCurrentDate());
    }
}