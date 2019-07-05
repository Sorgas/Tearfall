package stonering.game.view.render.ui.menus.zone;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import stonering.entity.zone.FarmZone;
import stonering.enums.ControlActionsEnum;
import stonering.enums.plants.PlantTypeMap;
import stonering.enums.plants.PlantType;
import stonering.game.GameMvc;
import stonering.game.model.lists.ZonesContainer;
import stonering.game.view.render.ui.lists.NavigableList;
import stonering.game.view.render.ui.menus.util.Highlightable;
import stonering.util.global.Logger;
import stonering.util.global.StaticSkin;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * Menu for managing farms. Plants for growing are configured from here.
 * Contains list of enabled plants and list of disabled ones.
 * Only these lists get stage focus and have similar input handlers.
 * <p>
 * Controls:
 * E select/deselect plant for planting(depends on active list).
 * Q close menu
 * X delete zone
 * WS fetch lists
 * AD switch active list
 *
 * @author Alexander on 20.03.2019.
 */
public class FarmZoneMenu extends Window {
    private NavigableList<PlantType> disabledPlants;
    private HorizontalGroup bottomButtons;
    private Label hintLabel;
    private Label selectedPlantLabel;
    private Label monthsLabel;

    private FarmZone farmZone;

    public FarmZoneMenu(FarmZone farmZone) {
        super(farmZone.getName(), StaticSkin.getSkin());
        this.farmZone = farmZone;
        createTable();
        createDefaultListener();
    }

    private void createTable() {
        setDebug(true, true);
        fillLists();
        add(new Label("All plants:", StaticSkin.getSkin()));
        add(new Label("Selected:", StaticSkin.getSkin())).row();
        add(disabledPlants = createList()).prefWidth(Value.percentWidth(0.5f, this)).prefHeight(Value.percentHeight(0.5f, this)).fill();
        VerticalGroup verticalGroup = new VerticalGroup();
        verticalGroup.addActor(selectedPlantLabel = new Label("", StaticSkin.getSkin()));
        verticalGroup.addActor(monthsLabel = new Label("", StaticSkin.getSkin()));
        add(verticalGroup).prefWidth(Value.percentWidth(0.5f, this)).prefHeight(Value.percentHeight(0.5f, this)).fill().row();
        hintLabel = new Label("", StaticSkin.getSkin());
        add(hintLabel).fillX().colspan(2).row();
        bottomButtons = new HorizontalGroup();
        TextButton quitButton = new TextButton("Quit", StaticSkin.getSkin());
        quitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                close();
            }
        });
        bottomButtons.addActor(quitButton);
        TextButton deleteButton = new TextButton("Remove Zone", StaticSkin.getSkin());
        deleteButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                deleteZone();
                close();
            }
        });
        bottomButtons.addActor(deleteButton);
        add(bottomButtons).colspan(2);
        setWidth(800);
        setHeight(600);
    }

    private void createDefaultListener() {
        addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                Logger.UI.logDebug(keycode + " on farm menu");
                switch (ControlActionsEnum.getAction(keycode)) {
                    case SELECT:
                    case UP:
                    case DOWN:
                    case LEFT:
                        switchList(disabledPlants);
                        return true;
                    case CANCEL:
                        close();
                        return true;
                    case DELETE:
                        deleteZone();
                        return true;
                    case RIGHT:
                        switchList(enabledPlants);
                        return true;
                }
                return false;
            }
        });
    }

    private void fillLists() {
        List<PlantType> allTypes = new ArrayList<>(PlantTypeMap.getInstance().getDomesticTypes());
        if (farmZone.getPlantType() != null) {
            enabledPlants.getItems().add(farmZone.getPlantType());
            allTypes.remove(farmZone.getPlantType());
        }
        allTypes.forEach(type -> disabledPlants.getItems().add(type));
    }

    private NavigableList<PlantType> createList() {
        NavigableList<PlantType> list = new NavigableList<>();
        list.setSize(150, 300);
        list.setHighlightHandler(new Highlightable.CheckHighlightHandler() {
            @Override
            public void handle() {
                list.setColor(value ? Color.BLUE : Color.RED);
            }
        });
        ListInputHandler handler = new ListInputHandler(list);
        list.getListeners().clear(); // to replace standard listener.
        list.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                event.stop(); // to prevent handling on menu level.
                return handler.test(keycode);
            }
        });
        return list;
    }

    /**
     * Picks selected plantType from given list, and moves it to another list.
     * Moves plant to another list, selecting or deselecting it.
     */
    private void select(NavigableList<PlantType> list) {
        PlantType type = list.getSelected();
        int index = list.getSelectedIndex();
        if (type == null) return;
        list.getItems().removeValue(type, true);
        getAnotherList(list).getItems().add(type);
        list.setSelectedIndex(Math.min(index, list.getItems().size - 1));
        farmZone.setPlant(type, list == disabledPlants);
    }

    /**
     * Switches focus to another list and sets selection to first item, if needed.
     * If another list is empty, no switching happens.
     */
    private void switchList(NavigableList<PlantType> list) {
        NavigableList<PlantType> targetList = getAnotherList(list);
        if (targetList.getItems().isEmpty()) return;
        getStage().setKeyboardFocus(targetList);
        targetList.setSelectedIndex(Math.max(0, targetList.getSelectedIndex()));
    }

    private NavigableList<PlantType> getAnotherList(NavigableList<PlantType> list) {
        return list == enabledPlants ? disabledPlants : enabledPlants;
    }

    private void deleteZone() {
        GameMvc.instance().getModel().get(ZonesContainer.class).deleteZone(farmZone);
    }

    private void close() {
        GameMvc.instance().getView().removeStage(getStage());
    }

    /**
     * Handles input for both lists.
     */
    private class ListInputHandler implements Predicate<Integer> {
        private NavigableList<PlantType> list;

        public ListInputHandler(NavigableList<PlantType> list) {
            this.list = list;
        }

        @Override
        public boolean test(Integer keycode) {
            Logger.UI.logDebug(keycode + " on plant list");
            switch (ControlActionsEnum.getAction(keycode)) {
                case SELECT:
                    select(list);
                    break;
                case CANCEL:
                    close();
                    break;
                case DELETE:
                    deleteZone();
                    break;
                case UP:
                    list.up();
                    break;
                case DOWN:
                    list.down();
                    break;
                case LEFT:
                case RIGHT:
                    switchList(list);
            }
            return true;
        }
    }
}
