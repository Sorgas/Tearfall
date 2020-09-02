package stonering.stage.entity_menu.building;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import stonering.entity.building.BuildingOrder;
import stonering.entity.crafting.IngredientOrder;
import stonering.enums.buildings.blueprint.Blueprint;
import stonering.game.GameMvc;
import stonering.game.model.system.task.DesignationSystem;
import stonering.game.model.system.task.TaskContainer;
import stonering.util.geometry.Position;
import stonering.util.lang.StaticSkin;

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
        Table buttonTable = new Table();
        buttonTable.center().pad(10);
        buttonTable.add(confirmButton = new TextButton("Confirm", StaticSkin.getSkin())).size(200, 20);
        buttonTable.add(cancelButton = new TextButton("Q: Cancel", StaticSkin.getSkin())).size(100, 20);
        add(buttonTable).colspan(2).fill().center();
        cancelButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GameMvc.view().removeStage(getStage());
                leftSection.widgetMap.values().stream() // free all selected items
                        .flatMap(value -> value.getButtonMap().values().stream())
                        .flatMap(stackedItemSquareButton -> stackedItemSquareButton.items.stream())
                        .forEach(item -> item.locked = false);
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
            }
        });
        leftSection.group.selectListener = actor -> {
            rightSection.clearGrid();
            rightSection.fillGrid(leftSection.group.getSelectedElement().ingredient, positions.get(0));
        };
        leftSection.group.setSelectedIndex(0);
        setDebug(true, true);
        addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                switch(keycode) {
                    case Input.Keys.Q: {
                        cancelButton.toggle();
                        return true;
                    }
                }
                return false;
            }
        });
    }

    private BuildingOrder createOrder(Blueprint blueprint, Position position) {
        BuildingOrder order = new BuildingOrder(blueprint, position);
        order.ingredientOrders.forEach((partName, ingredientOrder) -> ingredientOrder.items.addAll(leftSection.widgetMap.get(partName).removeItemsFromButtons(ingredientOrder.ingredient.quantity)));
        return order;
    }
}
