package stonering.stage.building;

import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Tree;
import stonering.enums.items.recipe.Ingredient;
import stonering.util.global.StaticSkin;

/**
 * Part of {@link BuildingMaterialTab}.
 * Collects all items for given ingredient, groups them by type and material, and shows in a tree structure.
 *
 * @author Alexander on 25.03.2020
 */
public class MaterialItemsSelectSection extends Container<Tree> {
    private Tree tree;

    public MaterialItemsSelectSection(Ingredient ingredient) {
        tree = new Tree(StaticSkin.getSkin());
        ingredient.itemTypes.forEach(type -> {

        });
    }
}
