package stonering.widget;

import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import stonering.enums.blocks.BlockTypeEnum;
import stonering.enums.materials.Material;
import stonering.enums.materials.MaterialMap;
import stonering.game.GameMvc;
import stonering.game.model.GameModel;
import stonering.game.model.local_map.LocalMap;
import stonering.game.model.system.EntitySelectorSystem;
import stonering.util.global.StaticSkin;
import stonering.util.geometry.Position;

/**
 * Renders information about tile under camera position.
 *
 * @author Alexander Kuzyakov on 12.10.2017.
 */
public class TileStatusBar extends Container<Table> {
    private Label date;
    private Label cellType;
    private Label coordinates;
    private Label area;

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
        table.add(new Label("Area: ", StaticSkin.getSkin()));
        table.add(area = new Label("", StaticSkin.getSkin()));
        setDebug(true, true);
        return table;
    }

    public void update() {
        GameModel gameModel = GameMvc.instance().model();
        Position focus = gameModel.get(EntitySelectorSystem.class).selector.position;
        Material material = MaterialMap.instance().getMaterial(gameModel.get(LocalMap.class).getMaterial(focus));
        setData(focus,
                material != null ? material.name : "",
                gameModel.get(LocalMap.class).passageMap != null ? gameModel.get(LocalMap.class).passageMap.area.get(focus) : 0,
                gameModel.get(LocalMap.class).getBlockType(focus));
    }

    public void setData(Position camera, String material, int area, int blockType) {
        coordinates.setText("(" + camera.x + ", " + camera.y + ", " + camera.z + ") " + BlockTypeEnum.getType((byte) blockType));
        cellType.setText(material);
        date.setText(GameMvc.instance().model().getCalendar().getCurrentTime());
        this.area.setText(area);
    }
}