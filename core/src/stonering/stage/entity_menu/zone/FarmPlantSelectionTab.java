package stonering.stage.entity_menu.zone;

import java.util.HashSet;
import java.util.Set;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;

import stonering.entity.zone.aspect.FarmAspect;
import stonering.game.GameMvc;
import stonering.game.model.PlayerSettlementProperties;
import stonering.util.lang.StaticSkin;

/**
 * List for selecting plant type in {@link FarmZoneMenu}.
 * Just enables or
 *
 * @author Alexander on 06.07.2019.
 */
public class FarmPlantSelectionTab extends Table {
    private final FarmZoneMenu menu;
    private final FarmAspect farm;
    private final Table listTable;
    private final ScrollPane pane;
    private final Set<CheckBox> checkBoxes;
    
    public FarmPlantSelectionTab(FarmZoneMenu menu, FarmAspect farm) {
        this.menu = menu;
        this.farm = farm;
        checkBoxes = new HashSet<>();
        add(new Label("Enabled plants.", StaticSkin.skin())).height(80).growX().row();
        listTable = new Table().align(Align.topLeft);
        listTable.defaults().height(25).pad(5);
        add(pane = new ScrollPane(listTable)).grow();
        if(farm.plantType != null) createRowForPlant(farm.plantType, true);
        GameMvc.model().get(PlayerSettlementProperties.class).availablePlants.stream()
                .filter(plant -> !plant.equals(farm.plantType))
                .forEach(jobName -> createRowForPlant(jobName, false));
        top();
        setDebug(true, true);
    }

    private void createRowForPlant(String plantName, boolean enabled) {
        if (plantName == null) return;
        Table rowTable = new Table();
        rowTable.add(new Label(plantName, StaticSkin.skin())).growX();
        CheckBox checkBox = new CheckBox(null, StaticSkin.getSkin());
        checkBox.setChecked(enabled);

        checkBox.getStyle().checkboxOff.setMinWidth(25);
        checkBox.getStyle().checkboxOff.setMinHeight(25);
        checkBox.getStyle().checkboxOn.setMinWidth(25);
        checkBox.getStyle().checkboxOn.setMinHeight(25);

        checkBox.addListener(new ChangeListener() {

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // uncheck others
                checkBoxes.stream()
                        .filter(box -> box != checkBox)
                        .forEach(box -> {
                            box.setProgrammaticChangeEvents(false);
                            box.setChecked(false);
                            box.setProgrammaticChangeEvents(true);
                        });
                if (checkBox.isChecked()) {
                    farm.plantType = plantName;
                } else {
                    farm.plantType = null;
                }
            }
        });
        checkBoxes.add(checkBox);
        rowTable.add(checkBox).width(25);
        rowTable.setBackground(StaticSkin.generator.generate(StaticSkin.backgroundFocused));
        listTable.add(rowTable).growX().row();
    }
}
