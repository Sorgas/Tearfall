package stonering.stage.entity_menu.zone;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.ui.*;

import stonering.entity.zone.Zone;
import stonering.entity.zone.aspect.FarmAspect;
import stonering.game.GameMvc;
import stonering.game.model.system.ZoneContainer;
import stonering.stage.util.SingleActorStage;
import stonering.widget.ButtonMenu;
import stonering.widget.ConfirmationDialogue;
import stonering.util.lang.StaticSkin;
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
        table.add(buttonMenu = createButtonMenu()).height(60).left().colspan(2);
        table.setBackground(StaticSkin.getColorDrawable(StaticSkin.background));
        size(900, 900); // size table
    }

    private ButtonMenu createButtonMenu() {
        ButtonMenu menu = new ButtonMenu(false);
        menu.defaults().pad(5, 5, 5, 0);
        menu.addButton("Delete farm", Input.Keys.X, () -> {
            ConfirmationDialogue dialogue = new ConfirmationDialogue("Deleate farm " + zone.name + "?");
            dialogue.confirmAction = () -> {
                GameMvc.model().get(ZoneContainer.class).deleteZone(zone);
                GameMvc.view().removeStage(getStage());
            };
            GameMvc.view().addStage(new SingleActorStage<>(dialogue, true));
        });
        menu.addButton("Close", Input.Keys.Q, () -> GameMvc.view().removeStage(getStage()));
        return menu;
    }

    private void createListeners() {
        addListener(new KeyNotifierListener(() -> selectionTab)); // for navigating and selecting plants
        addListener(new KeyNotifierListener(() -> buttonMenu)); // for pressing buttons
    }
}
