package stonering.widget;

import java.util.Optional;

import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import stonering.enums.blocks.BlockTypeEnum;
import stonering.entity.material.Material;
import stonering.enums.materials.MaterialMap;
import stonering.game.GameMvc;
import stonering.game.model.GameModel;
import stonering.game.model.local_map.LocalMap;
import stonering.game.model.entity_selector.EntitySelectorSystem;
import stonering.game.model.system.GameTime;
import stonering.game.model.system.liquid.LiquidContainer;
import stonering.util.lang.StaticSkin;
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
    private Label liquid;

    public TileStatusBar() {
        super();
        setActor(createTable());
        setFillParent(true);
        align(Align.topRight);
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
        table.add(area = new Label("", StaticSkin.getSkin())).row();
        table.add(new Label("Liquid: ", StaticSkin.getSkin()));
        table.add(liquid = new Label("", StaticSkin.getSkin()));
        setDebug(true, true);
        return table;
    }

    public void update() {
        GameModel gameModel = GameMvc.model();
        Position focus = gameModel.get(EntitySelectorSystem.class).selector.position;
        Material material = MaterialMap.getMaterial(gameModel.get(LocalMap.class).blockType.getMaterial(focus));
        setData(focus,
                material != null ? material.name : "",
                gameModel.get(LocalMap.class).passageMap != null ? gameModel.get(LocalMap.class).passageMap.area.get(focus) : 0,
                gameModel.get(LocalMap.class).blockType.get(focus));
    }

    public void setData(Position camera, String material, int area, int blockType) {
        coordinates.setText("(" + camera.x + ", " + camera.y + ", " + camera.z + ") " + BlockTypeEnum.getType((byte) blockType));
        cellType.setText(material);
        GameTime time = GameMvc.model().gameTime; 
        date.setText(time.day.progress + " : " + time.hour.progress);
        this.area.setText(area);
        Optional.ofNullable(GameMvc.model().get(LiquidContainer.class).getTile(camera))
                .ifPresentOrElse(tile -> liquid.setText(tile.amount + " stable: " + tile.tpStable),
                        () -> liquid.setText(""));
    }
}