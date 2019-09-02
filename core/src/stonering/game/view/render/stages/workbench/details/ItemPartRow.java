package stonering.game.view.render.stages.workbench.details;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import stonering.entity.crafting.IngredientOrder;
import stonering.util.global.StaticSkin;

/**
 * Shows item part name and ingredient properties.
 * TODO allow to select ingredient items for it.
 *
 * @author Alexander on 30.08.2019.
 */
public class ItemPartRow extends Table {
    private final OrderDetailsSection section;
    private final IngredientOrder ingredientOrder;
    public final Label name;
    public final Label description;

    public ItemPartRow(IngredientOrder ingredientOrder, String title, OrderDetailsSection section) {
        this.section = section;
        this.ingredientOrder = ingredientOrder;
        add(name = new Label(title, StaticSkin.getSkin())).row();
        add(description = new Label(ingredientOrder.ingredient.tag + " " + ingredientOrder.itemType, StaticSkin.getSkin()));
        //TODO add dropdown with ingredientOrder.options.
    }
}
