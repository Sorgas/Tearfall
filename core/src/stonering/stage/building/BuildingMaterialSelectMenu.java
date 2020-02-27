package stonering.stage.building;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import stonering.entity.building.BuildingOrder;
import stonering.entity.crafting.IngredientOrder;
import stonering.enums.buildings.blueprint.Blueprint;
import stonering.game.GameMvc;
import stonering.game.model.system.task.DesignationSystem;
import stonering.game.model.system.task.TaskContainer;
import stonering.util.geometry.Position;
import stonering.util.global.StaticSkin;

import java.util.List;

/**
 * Menu for selecting material items for building.
 * Has left section with number and type(one) designated buildings, required and selected materials,
 * and right section with available materials, filled for each building part.
 * When widget is selected, right part of menu is filled with appropriate items and allows to select them for this part.
 * <p>
 * When all part are filled, confirm button is unlocked, and {@link BuildingOrder} can be created.
 * On order creation, fetches items from a left section widgets and assigns them to {@link IngredientOrder}s of building order,
 * and then submits orders to {@link TaskContainer}.
 *
 * @author Alexander on 17.02.2020
 */
public class BuildingMaterialSelectMenu extends Window {
    public final LeftSection leftSection;
    public final RightSection rightSection;
    public final Label hintLabel;
    public final TextButton cancelButton;
    public final TextButton confirmButton;

    List<Position> positions;

    public BuildingMaterialSelectMenu(Blueprint blueprint, List<Position> positions) {
        super("", StaticSkin.getSkin());
        this.positions = positions;
        defaults().fill().expand();
        add(leftSection = new LeftSection(this, blueprint, positions.size(), positions.get(0))).size(300, 800);
        add(rightSection = new RightSection(this)).height(800).fill().expand().row();
        add(hintLabel = new Label("", StaticSkin.getSkin())).colspan(2).expandX().fillX().row();
        HorizontalGroup buttonGroup = new HorizontalGroup();
        buttonGroup.addActor(confirmButton = new TextButton("Confirm", StaticSkin.getSkin()));
        buttonGroup.addActor(cancelButton = new TextButton("Cancel", StaticSkin.getSkin()));
        add(buttonGroup).colspan(2);
        cancelButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GameMvc.view().removeStage(getStage());
            }
        });
        confirmButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GameMvc.view().removeStage(getStage());
                DesignationSystem system = GameMvc.model().get(TaskContainer.class).designationSystem;
                for (Position position : positions) {
                    system.submitBuildingDesignation(createOrder(blueprint, position), 1);
                }
                // TODO create building designations
            }
        });
        leftSection.group.selectListener = event -> {
            rightSection.fill(leftSection.group.getSelectedElement().ingredient, positions.get(0));
            return true;
        };
        leftSection.group.setSelectedIndex(0);
        setDebug(true, true);
    }

    private BuildingOrder createOrder(Blueprint blueprint, Position position) {
        BuildingOrder order = new BuildingOrder(blueprint, position);
        order.parts
                .forEach((partName, ingredientOrder) -> ingredientOrder.items.addAll(leftSection.widgetMap.get(partName).removeItemsFromButtons(ingredientOrder.ingredient.quantity)));
        return order;
    }
}
