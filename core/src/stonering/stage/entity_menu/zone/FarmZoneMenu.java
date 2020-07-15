package stonering.stage.entity_menu.zone;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import stonering.entity.zone.Zone;
import stonering.entity.zone.aspect.FarmAspect;
import stonering.game.GameMvc;
import stonering.game.model.system.ZoneContainer;
import stonering.stage.util.SingleActorStage;
import stonering.widget.ButtonMenu;
import stonering.widget.ConfirmationDialogue;
import stonering.util.global.StaticSkin;
import stonering.widget.util.KeyNotifierListener;

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
public class FarmZoneMenu extends Container<Table> {
    private Zone zone;
    private FarmAspect farm;
    private final Table table = new Table();
    private FarmPlantSelectionTab selectionTab;
    FarmPlantDetailsTab detailsTab;
    private Button deleteButton;
    private Button closeButton;
    private ButtonMenu buttonMenu;
    
    public FarmZoneMenu(Zone zone) {
        this.zone = zone;
        setActor(table);
        farm = zone.get(FarmAspect.class);
        createLayout();
        createListeners();
    }

    private void createLayout() {
        setDebug(true, true);
        table.add(selectionTab = new FarmPlantSelectionTab(this, farm)).width(300).growY();
        table.add(detailsTab = new FarmPlantDetailsTab()).width(600).growY().row();
        table.add(buttonMenu = createButtonMenu());
        table.add(deleteButton = createDeleteButton()).size(120, 60).pad(10).expandX();
        table.add(deleteButton = createDeleteButton()).size(120, 60).pad(10).expandX();
        size(800, 900); // size table
    }

    private ButtonMenu createButtonMenu() {
        ButtonMenu menu = new ButtonMenu();
        return null;
    }

    private TextButton createDeleteButton() {
        TextButton button = new TextButton("Delete farm", StaticSkin.skin());
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ConfirmationDialogue dialogue = new ConfirmationDialogue("Deleate farm " + zone.name + "?");
                dialogue.confirmAction = () -> {
                    GameMvc.model().get(ZoneContainer.class).deleteZone(zone);
                    GameMvc.view().removeStage(getStage());
                };
                GameMvc.view().addStage(new SingleActorStage<>(dialogue, true));
            }
        });
        return button;
    }
    
    private TextButton createCloseButton() {
        TextButton button = new TextButton("Delete farm", StaticSkin.skin());
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ConfirmationDialogue dialogue = new ConfirmationDialogue("Deleate farm " + zone.name + "?");
                dialogue.confirmAction = () -> {
                    GameMvc.model().get(ZoneContainer.class).deleteZone(zone);
                    GameMvc.view().removeStage(getStage());
                };
                GameMvc.view().addStage(new SingleActorStage<>(dialogue, true));
            }
        });
        return button;
    }
    
    private void createListeners() {
        // for pressing buttons
        addListener(new InputListener() {
            @Override
            public boolean keyUp(InputEvent event, int keycode) {
                switch(keycode) {
                    case Input.Keys.Q: // close this menu
                        GameMvc.view().removeStage(getStage());
                        return true;
                    case Input.Keys.X: // delete zone
                        deleteButton.toggle();
                        return true;
                }
                return false;
            }
        });
        // for navigating and selecting plants
        addListener(new KeyNotifierListener(() -> selectionTab));
    }
}
