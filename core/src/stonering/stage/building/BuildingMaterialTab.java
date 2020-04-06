package stonering.stage.building;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import stonering.enums.buildings.blueprint.Blueprint;
import stonering.enums.items.ItemTagEnum;
import stonering.enums.items.type.ItemTypeMap;
import stonering.util.global.StaticSkin;

import java.util.HashMap;
import java.util.Map;

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
    public final Map<String, Actor> sectionMap = new HashMap<>();

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
            ItemsSelectSection section;
            if(blueprint.configMap.containsKey(part)) { // create section from config
                section = new MaterialItemsSelectSection(ingredient, part, blueprint.configMap.get(part));
            } else { // create section for unique items
                section = new UniqueItemsSelectSection(ingredient, part);
                section.setAllEnabled(true);
            }
            sectionsTable.add(section);
            sectionMap.put(part, section);
        });
    }

    public void initBlueprint(Blueprint blueprint) {

    }
}
