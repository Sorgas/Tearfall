package stonering.stage.entity_menu.building.workbench.details;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.StringBuilder;
import stonering.entity.crafting.IngredientOrder;
import stonering.enums.items.ItemTagEnum;
import stonering.util.lang.StaticSkin;

import java.util.List;

/**
 * Shows item part name and ingredient properties.
 * TODO allow to select ingredient items for it.
 *
 * @author Alexander on 30.08.2019.
 */
public class ItemPartRow extends Table {
    private final OrderDetailsSection section;
    private final IngredientOrder ingredientOrder;
    public Label name;
    public final Label description;

    public ItemPartRow(IngredientOrder ingredientOrder, String title, OrderDetailsSection section) {
        this.section = section;
        this.ingredientOrder = ingredientOrder;
        if (title != null) add(name = new Label(title, StaticSkin.getSkin())).align(Align.left).row();
        add(description = new Label(getDescriptionText(), StaticSkin.getSkin())).align(Align.left).padLeft(20);
        setDebug(true, true);
        //TODO add dropdown with ingredientOrder.options.
    }

    private String getDescriptionText() {
        String text = "any " + (ingredientOrder.ingredient.tag.isDisplayable() ? ingredientOrder.ingredient.tag.toString().toLowerCase() : "");
        if(ingredientOrder.ingredient.itemTypes.contains("any")) {
            return text + (ingredientOrder.ingredient.tag != ItemTagEnum.WATER ? " item" : ""); // TODO move condition to TagEnum
        } else {
            return text + " " + enumerateList(ingredientOrder.ingredient.itemTypes, ", ", " or ");
        }
    }

    /**
     * Collects strings from list to single string with format: [item1], [item2], [item3] [lastSeparator] [item4].
     */
    private String enumerateList(List<String> list, String separator, String lastSeparator) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            builder.append(list.get(i));
            if (i < list.size() - 2) builder.append(separator);
            if (i == list.size() - 2) builder.append(lastSeparator);
        }
        return builder.toString();
    }
}
