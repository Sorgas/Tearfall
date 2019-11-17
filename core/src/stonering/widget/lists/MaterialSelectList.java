package stonering.widget.lists;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import stonering.entity.crafting.BuildingComponent;
import stonering.game.GameMvc;
import stonering.game.controller.controllers.toolbar.DesignationsController;
import stonering.game.model.system.item.ItemContainer;
import stonering.util.global.StaticSkin;
import stonering.widget.Hideable;
import stonering.entity.item.Item;
import stonering.util.geometry.Position;

import java.util.List;

/**
 * List that shows items, appropriate for building.
 * If no items found for building, shows indication line which cannot be selected.
 * Takes information from {@link DesignationsController}.
 * Uses {@link ItemCardButton}.
 *
 * @author Alexander on 03.07.2018.
 */
public class MaterialSelectList extends ItemsCountList implements Hideable {
    public boolean active = true; // prevents clicking when no items found

    public MaterialSelectList() {
        super();
        createHighlightHandler();
    }

    /**
     * Fills this list for given crafting or building step.
     */
    public void fillForCraftingStep(BuildingComponent step, Position position) {
        clearChildren();
        List<Item> items = GameMvc.instance().model().get(ItemContainer.class).util.getAvailableMaterialsForBuildingStep(step, position);
        if (items.isEmpty()) { // items not found
            addActor(new Label("No item available.", StaticSkin.getSkin()));
            active = false;
        } else {
            addItems(items);
            setSelectedIndex(-1); //change event is not fired without this.
            setSelectedIndex(0);
        }
    }

    private void createHighlightHandler() {
        highlightHandler = new HighlightHandler(this) {
            @Override
            public void handle(boolean value) {
                for (Actor child : getChildren()) {
                    child.setColor(Color.LIGHT_GRAY);
                }
                getSelectedElement().setColor(Color.RED);
            }
        };
    }

    @Override
    public void show() {
        GameMvc.instance().getView().mainUiStage.toolbar.addMenu(this);
    }

    @Override
    public void hide() {
        GameMvc.instance().getView().mainUiStage.toolbar.hideMenu(this);
    }
}
