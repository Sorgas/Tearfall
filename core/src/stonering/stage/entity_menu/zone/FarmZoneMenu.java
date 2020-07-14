package stonering.stage.entity_menu.zone;

import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import stonering.entity.zone.FarmZone;
import stonering.entity.zone.aspect.FarmAspect;
import stonering.enums.plants.PlantType;
import stonering.game.GameMvc;
import stonering.game.model.system.ZoneContainer;
import stonering.widget.HintedActor;
import stonering.util.global.StaticSkin;

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
    public FarmPlantSelectionTab disabledPlants;
    private Label selectedPlantLabel;
    private Label monthsLabel;
    private Label hintLabel;
    private FarmZone farmZone;
    private FarmAspect farm;
    
    public FarmZoneMenu(FarmZone farmZone) {
        super(farmZone.name, StaticSkin.getSkin());
        this.farmZone = farmZone;
        farm = farmZone.get(FarmAspect.class);
        createLayout();
        fillList();
        setLabels();
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        Actor focused = getStage().getKeyboardFocus();
        hintLabel.setText((focused instanceof HintedActor) ? ((HintedActor) focused).getHint() : "");
    }

    private void createLayout() {
        setDebug(true, true);
        add(new FarmPlantSelectionTab(this, farm));
        add(createButtonsGroup()).colspan(2);
        setWidth(800);
        setHeight(600);
    }

    private HorizontalGroup createButtonsGroup() {
        HorizontalGroup group = new HorizontalGroup();
        group.addActor(createButton("Quit", new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                close();
            }
        }));
        group.addActor(createButton("Delete zone", new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                deleteZone();
                close();
            }
        }));
        return group;
    }

    private TextButton createButton(String text, ChangeListener listener) {
        TextButton button = new TextButton(text, StaticSkin.getSkin());
        button.addListener(listener);
        return button;
    }

    private void fillList() {
//        disabledPlants.clearItems();
//        List<PlantType> allTypes = new ArrayList<>(PlantTypeMap.instance().domesticTypes.values());
//        PlantType selectedType = farmZone.getPlantType();
//        allTypes.stream().filter(type -> !type.equals(selectedType)).forEach(type -> disabledPlants.getItems().add(type));
    }

    private FarmPlantSelectionTab createList() {
        FarmPlantSelectionTab list = new FarmPlantSelectionTab(this, farm);
        list.setSize(150, 300);
        return list;
    }

    public void select(PlantType type) {
        farmZone.setPlant(type);
        fillList();
        setLabels();
    }

    /**
     * Updates texts in labels for plant and months.
     */
    private void setLabels() {
        PlantType type = farmZone.getPlantType();
        selectedPlantLabel.setText(type != null ? type.title : "none");
        monthsLabel.setText(type != null ? type.plantingStart.toString() : "none");
    }

    public void deleteZone() {
        GameMvc.model().get(ZoneContainer.class).deleteZone(farmZone);
    }

    public void close() {
        GameMvc.view().removeStage(getStage());
    }
}
