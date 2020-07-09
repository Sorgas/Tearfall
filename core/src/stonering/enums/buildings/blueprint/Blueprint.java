package stonering.enums.buildings.blueprint;

import stonering.enums.items.ItemTagEnum;
import stonering.enums.items.recipe.Ingredient;
import stonering.enums.items.type.ItemTypeMap;

import java.util.*;

/**
 * Blueprint describes how building are built.
 * Stores materials and amount, button place in menu hierarchy,
 * placing requirements, required skill and job.
 *
 * Material items for building are described in {@link Ingredient}.
 * Items selected by player are stored in {@link MaterialSelectionConfig} (for each blueprint).
 * When blueprint is loaded, only first item type is fully enabled in config.
 * When player selects place for building, he can change allowed materials for building part. These changes are kept in config between ordering of building.
 * Complex items like anvils and saws are selected individually and not stored in config.
 *
 * @author Alexander
 */
public class Blueprint {
    public final String name; // blueprint id.
    public final String building; //building id, points to BuildingType
    public final String title; // button name
    public final String placing; // maps to position validator for place selecting and task checking.
    public final List<String> menuPath; // button path in toolbar
    public final Map<String, Ingredient> ingredients = new HashMap<>();
    public final Map<String, MaterialSelectionConfig> configMap;
    public final boolean construction; // blueprint has no building type
    public final String icon;

    public Blueprint(RawBlueprint rawBlueprint) {
        name = rawBlueprint.name;
        building = rawBlueprint.building;
        title = rawBlueprint.title;
        placing = rawBlueprint.placing;
        menuPath = rawBlueprint.menuPath;
        configMap = new HashMap<>();
        construction = "construction".equals(placing);
        icon = rawBlueprint.icon;
    }
}
