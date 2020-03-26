package stonering.stage.building;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import stonering.enums.buildings.blueprint.Blueprint;
import stonering.game.GameMvc;
import stonering.game.model.system.item.ItemContainer;
import stonering.util.geometry.Position;
import stonering.util.global.StaticSkin;

/**
 * Allows selection of item for building.
 * Has columns for each building part. Materials are shown in list, unique item shown in greed.
 * Player can tick rows of material items selecting specific material(oak, iron) or category(wood, stone).
 * Player can mark unique items as allowed during current designation.
 * Option 'any' is also available for both material and unique items.
 *
 * @author Alexander on 25.03.2020
 */
public class BuildingMaterialTab extends Table {

    public BuildingMaterialTab(Blueprint blueprint, Position position) {
        blueprint.parts.keySet().forEach(part -> {
//            blueprint.parts.get(part).
            this.add();
        });
    }
}
