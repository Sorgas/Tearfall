package stonering.stage.building;

import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import stonering.enums.buildings.blueprint.Blueprint;
import stonering.enums.items.ItemTagEnum;
import stonering.enums.items.type.ItemTypeMap;
import stonering.util.global.StaticSkin;

/**
 * Allows selection of item for building.
 * Has columns for each building part. Materials are shown in list, unique items shown in grid.
 * Player can tick rows of material items selecting specific material(oak, iron) or category(wood, stone).
 * Player can mark unique items as allowed during current designation.
 *
 * @author Alexander on 25.03.2020
 */
public class BuildingMaterialTab extends Container<Table> {
    private Table sectionsTable;

    public BuildingMaterialTab() {
        Table table;
        setActor(table = new Table());
        table.add(new Label("Select items for building parts", StaticSkin.getSkin())).growX().row();
        table.add(sectionsTable = new Table()).growY().left();
        sectionsTable.left();
        sectionsTable.defaults().growY().left();
        size(800, 500);
        fill();
        sectionsTable.setDebug(true, true);
        table.setDebug(true, true);
    }

    public void fillFor(Blueprint blueprint) {
        sectionsTable.clearChildren();
        blueprint.parts.forEach((part, ingredient) -> {
            boolean allItemTypesAreMaterial = ingredient.itemTypes.stream()
                    .map(typeName -> ItemTypeMap.instance().getItemType(typeName))
                    .allMatch(type -> type.tags.contains(ItemTagEnum.BUILDING_MATERIAL));
            if (allItemTypesAreMaterial) {
                sectionsTable.add(new MaterialItemsSelectSection(ingredient, part));
            } else {
                sectionsTable.add(new UniqueItemsSelectSection(ingredient));
            }
        });
    }
}
