package stonering.stage.entity_menu.item;

import java.util.Optional;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import stonering.entity.item.Item;
import stonering.entity.item.aspects.FoodItemAspect;
import stonering.entity.item.aspects.SeedAspect;
import stonering.enums.materials.MaterialMap;
import stonering.enums.plants.PlantTypeMap;
import stonering.stage.entity_menu.building.MenuSection;
import stonering.util.lang.StaticSkin;

/**
 * Section for showing state of item's aspects.
 *
 * @author Alexander on 08.06.2020.
 */
public class ItemAspectSection extends MenuSection {
    private Item item;
    private boolean detailsShown = false;

    public ItemAspectSection(Item item) {
        super("Details");
        this.item = item;
        if (!item.parts.keySet().stream().allMatch(item.type.name::equals)) addItemParts();
        addItemAspects();
        if (!detailsShown) add(new Label("Nothing to show here.", StaticSkin.skin()));
    }

    private void addItemParts() {
        Table table = new Table();
        table.add(new Label("Item parts:", StaticSkin.skin())).colspan(2).row();
        item.parts.values().forEach(part -> {
            table.add(new Label(part.name, StaticSkin.skin())).padLeft(20).left().expandX();
            table.add(new Label(MaterialMap.getMaterial(part.material).name, StaticSkin.skin())).right().row();
        });
        add(table);
        detailsShown = true;
    }

    private void addItemAspects() {
        Optional.ofNullable(item.get(SeedAspect.class))
                .map(seedAspect -> PlantTypeMap.getPlantType(seedAspect.specimen))
                .map(plantType -> plantType.title)
                .ifPresent(title -> {
                    Table table = new Table();
                    table.defaults().left();
                    table.add(new Label("Seed:", StaticSkin.skin())).row();
                    table.add(new Label("Is seed for " + title, StaticSkin.skin())).padLeft(20);
                    add(table);
                    detailsShown = true;
                });
        Optional.ofNullable(item.get(FoodItemAspect.class))
                .ifPresent(foodAspect -> {
                    Table table = new Table();
                    table.defaults().left();
                    table.add(new Label("Edible item:", StaticSkin.skin())).row();
                    table.add(new Label("Nutrition: " + foodAspect.nutrition, StaticSkin.skin())).padLeft(20);
                    add(table);
                    detailsShown = true;
                });
    }

    @Override
    public String getHint() {
        return null;
    }
}
