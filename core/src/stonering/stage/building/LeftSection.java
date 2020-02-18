package stonering.stage.building;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import stonering.enums.buildings.blueprint.Blueprint;
import stonering.enums.items.recipe.Ingredient;
import stonering.util.geometry.Position;
import stonering.util.global.StaticSkin;
import stonering.widget.NavigableVerticalGroup;
import stonering.widget.item.SelectedMaterialsWidget;

import java.util.ArrayList;
import java.util.List;

/**
 * Shows what building are designated and how many.
 * Shows material categories required and selected items.
 *
 * @author Alexander on 17.02.2020
 */
public class LeftSection extends Table {
    private BuildingMaterialListMenu menu;
    List<SelectedMaterialsWidget> list;
    NavigableVerticalGroup<SelectedMaterialsWidget> group;

    public LeftSection(BuildingMaterialListMenu menu, Blueprint blueprint, int number) {
        this.menu = menu;
        list = new ArrayList<>();
        defaults().align(Align.topLeft).expandX().fillX();
        add(new Label(createTitle(blueprint, number), StaticSkin.getSkin())).row();
        add(group = createIngredientGroup(blueprint, number));
        align(Align.topLeft);
    }

    private String createTitle(Blueprint blueprint, int number) {
        return "Building " + (number > 1 ? number + " " + blueprint.title + "s" : blueprint.title);
    }

    /**
     * Creates widgets for each part of a building.
     */
    private NavigableVerticalGroup<SelectedMaterialsWidget> createIngredientGroup(Blueprint blueprint, int number) {
        NavigableVerticalGroup<SelectedMaterialsWidget> group = new NavigableVerticalGroup<>();
        group.grow();
        blueprint.parts.keySet().forEach(part -> {
            Ingredient ingredient = blueprint.parts.get(part);
            SelectedMaterialsWidget widget = new SelectedMaterialsWidget(ingredient, ingredient.quantity * number, part);
            list.add(widget);
            group.addActor(widget);
            widget.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    setSelected(ingredient, menu.position);
                }
            });
        });
        return group;
    }

    void setSelected(Ingredient ingredient, Position position) {
        menu.rightSection.fillForIngredient(ingredient, position);
        //TODO highlight
    }

    void setSelected(int index) {
        setSelected(group.getChildAtIndex(index).ingredient, menu.position);
    }
}
