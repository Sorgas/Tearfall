package stonering.game.view.render.ui.menus.zone;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import stonering.entity.local.zone.FarmZone;
import stonering.enums.plants.PlantMap;
import stonering.enums.plants.PlantType;
import stonering.game.GameMvc;
import stonering.game.model.lists.ZonesContainer;
import stonering.game.view.render.ui.lists.NavigableList;
import stonering.util.global.StaticSkin;
import stonering.util.global.TagLoggersEnum;

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
    private NavigableList<PlantType> enabledPlants;
    private NavigableList<PlantType> disabledPlants;
    private HorizontalGroup bottomButtons;
    private Label hintLabel;

    private FarmZone farmZone;

    public FarmZoneMenu(FarmZone farmZone) {
        super(farmZone.getName(), StaticSkin.getSkin());
        this.farmZone = farmZone;
        createTable();
        createDefaultListener();
    }

    private void createTable() {
        setDebug(true, true);
        disabledPlants = createList();
        enabledPlants = createList();
        fillLists();
        add(new Label("All plants:", StaticSkin.getSkin()));
        add(new Label("Selected plants:", StaticSkin.getSkin())).row();
        add(disabledPlants).prefWidth(Value.percentWidth(0.5f, this)).prefHeight(Value.percentHeight(0.5f, this)).row();
        add(enabledPlants).prefWidth(Value.percentWidth(0.5f, this)).prefHeight(Value.percentHeight(0.5f, this));
        hintLabel = new Label("", StaticSkin.getSkin());
        add(hintLabel).fillX().colspan(2);
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
        this.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                TagLoggersEnum.UI.logDebug(keycode + " on farm menu");
                switch (keycode) {
                    case Input.Keys.E:
                    case Input.Keys.W:
                    case Input.Keys.S:
                    case Input.Keys.A:
                        switchList(disabledPlants);
                        return true;
                    case Input.Keys.Q:
                        close();
                        return true;
                    case Input.Keys.X:
                        deleteZone();
                        return true;
                    case Input.Keys.D:
                        switchList(enabledPlants);
                        return true;
                }
                return false;
            }
        });
    }

    private void fillLists() {
        farmZone.getPlants().forEach(type -> enabledPlants.getItems().add(type));
        List<PlantType> allTypes = new ArrayList<>(PlantMap.getInstance().getDomesticTypes());
        allTypes.removeAll(farmZone.getPlants());
        allTypes.forEach(type -> disabledPlants.getItems().add(type));
    }

    private NavigableList<PlantType> createList() {
        NavigableList<PlantType> list = new NavigableList<>();
        list.setSize(150, 300);
        list.setHighlightHandler(focused -> {
            list.setColor(focused ? Color.BLUE : Color.RED);
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
        if (type == null) return;
        Array<PlantType> listItems = list.getItems();
        listItems.removeValue(type, true);
        list.setItems(listItems);
        NavigableList<PlantType> list2 = getAnotherList(list);
        listItems = list2.getItems();
        listItems.add(type);
        list2.setItems(listItems);
        farmZone.setPlant(type, list == disabledPlants);
    }

    private void switchList(NavigableList<PlantType> list) {
        getStage().setKeyboardFocus(getAnotherList(list));
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
            TagLoggersEnum.UI.logDebug(keycode + " on plant list");
            switch (keycode) {
                case Input.Keys.E:
                    select(list);
                    break;
                case Input.Keys.Q:
                    close();
                    break;
                case Input.Keys.X:
                    deleteZone();
                    break;
                case Input.Keys.W:
                    list.up();
                    break;
                case Input.Keys.S:
                    list.down();
                    break;
                case Input.Keys.A:
                case Input.Keys.D:
                    switchList(list);
            }
            return true;
        }
    }
}
