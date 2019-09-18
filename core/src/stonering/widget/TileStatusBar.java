package stonering.widget;

import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import stonering.game.model.system.GameCalendar;
import stonering.enums.blocks.BlockTypesEnum;
import stonering.game.GameMvc;
import stonering.util.global.StaticSkin;
import stonering.util.geometry.Position;

/**
 * Renders information about tile under camera position.
 *
 * @author Alexander Kuzyakov on 12.10.2017.
 */
public class TileStatusBar extends Container<Table> {
    private Label date;
    private Label coordinates;
    private Label cellType;

    public TileStatusBar() {
        super();
        setActor(createTable());
        setFillParent(true);
        align(Align.bottomRight);
    }

    private Table createTable() {
        Table table = new Table();
        table.defaults().align(Align.left);
        table.add(new Label("Date: ", StaticSkin.getSkin()));
        table.add(date = new Label("", StaticSkin.getSkin())).row();
        table.add(new Label("Material: ", StaticSkin.getSkin()));
        table.add(cellType = new Label("", StaticSkin.getSkin())).row();
        table.add(new Label("Coordinates: ", StaticSkin.getSkin()));
        table.add(coordinates = new Label("", StaticSkin.getSkin())).row();
        setDebug(true, true);
        return table;
    }

    public void setData(Position camera, String material, int area, int blockType) {
        coordinates.setText("(" + camera.x + ", " + camera.y + ", " + camera.z + ") " + BlockTypesEnum.getType((byte) blockType));
        cellType.setText(material);
        date.setText(GameMvc.instance().getModel().get(GameCalendar.class).getCurrentDate());
    }
}