package stonering.stage.building;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import stonering.enums.buildings.blueprint.Blueprint;
import stonering.enums.items.recipe.Ingredient;
import stonering.util.global.Pair;
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
    private BuildingMaterialSelectMenu menu;
    final List<SelectedMaterialsWidget> widgets;
    NavigableVerticalGroup<SelectedMaterialsWidget> group;
    SelectedMaterialsWidget selectedWidget;

    public LeftSection(BuildingMaterialSelectMenu menu, Blueprint blueprint, int number) {
        this.menu = menu;
        widgets = new ArrayList<>();
        defaults().align(Align.topLeft).expandX().fillX();
        add(new Label(createTitle(blueprint, number), StaticSkin.getSkin())).colspan(2).row();
        add(group = createIngredientGroup(blueprint, number)).expandY().colspan(2).row();
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
            System.out.println("creating widget for building part ");
            Ingredient ingredient = blueprint.parts.get(part);
            SelectedMaterialsWidget widget = new SelectedMaterialsWidget(ingredient, ingredient.quantity * number, part);
            widgets.add(widget);
            group.addActor(widget);
            widget.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    setSelected(widget);
                }
            });
        });
        return group;
    }

    void setSelected(SelectedMaterialsWidget widget) {
        menu.rightSection.fill(widget.ingredient, menu.position);
        menu.hintLabel.setText("Selecting items for " + widget.partName);
        selectedWidget = widget;
    }

    /**
     * Selects next unfilled widget for filling, or enables confirmation button.
     */
    public void updateState() {
        SelectedMaterialsWidget nextWidget = widgets.stream()
                .filter(widget -> widget.targetNumber <= widget.number)
                .findFirst().orElse(null);
        if(nextWidget == null) {
            // enable confirmation
        } else {
            setSelected(nextWidget);
        }
    }
}
