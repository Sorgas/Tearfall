package stonering.enums.buildings.blueprint;

import stonering.enums.items.recipe.Ingredient;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * Blueprint describes how building are built. Stores materials and amount, button place in menu hierarchy,
 * placing requirements, required skill and job.
 */
public class Blueprint {
    public final String name; // blueprint id.
    public final String building; //building id
    public final String title; // button name
    public final String placing; // maps to position validator for place selecting and task checking.
    public final List<String> menuPath; // button path in toolbar
    public final LinkedHashMap<String, Ingredient> parts; // components mapped to building parts

    public Blueprint(RawBlueprint rawBlueprint) {
        name = rawBlueprint.name;
        building = rawBlueprint.building;
        title = rawBlueprint.title;
        placing = rawBlueprint.placing;
        menuPath = rawBlueprint.menuPath;
        parts = new LinkedHashMap<>();
    }
}
