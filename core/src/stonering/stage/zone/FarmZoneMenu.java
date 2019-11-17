package stonering.stage.zone;

import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import stonering.entity.zone.FarmZone;
import stonering.enums.plants.PlantTypeMap;
import stonering.enums.plants.PlantType;
import stonering.game.GameMvc;
import stonering.game.model.system.ZonesContainer;
import stonering.widget.lists.NavigableList;
import stonering.widget.HintedActor;
import stonering.util.global.StaticSkin;

import java.util.ArrayList;
import java.util.List;

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
    private PlantTypeSelectList disabledPlants;
    private Label selectedPlantLabel;
    private Label monthsLabel;
    private Label hintLabel;
    private FarmZone farmZone;

    public FarmZoneMenu(FarmZone farmZone) {
        super(farmZone.getName(), StaticSkin.getSkin());
        this.farmZone = farmZone;
        createTable();
        fillList();
        setLabels();
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        Actor focused = getStage().getKeyboardFocus();
        hintLabel.setText((focused instanceof HintedActor) ? ((HintedActor) focused).getHint() : "");
    }

    private void createTable() {
        setDebug(true, true);
        add(new Label("All plants:", StaticSkin.getSkin()));
        add(new Label("Selected:", StaticSkin.getSkin())).row();
        add(disabledPlants = createList()).prefWidth(Value.percentWidth(0.5f, this)).prefHeight(Value.percentHeight(0.5f, this)).fill();
        VerticalGroup verticalGroup = new VerticalGroup();
        verticalGroup.addActor(selectedPlantLabel = new Label("", StaticSkin.getSkin()));
        verticalGroup.addActor(monthsLabel = new Label("", StaticSkin.getSkin()));
        add(verticalGroup).prefWidth(Value.percentWidth(0.5f, this)).prefHeight(Value.percentHeight(0.5f, this)).fill().row();
        add(hintLabel = new Label("", StaticSkin.getSkin())).fillX().colspan(2).row();
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
        disabledPlants.clearItems();
        List<PlantType> allTypes = new ArrayList<>(PlantTypeMap.getInstance().getDomesticTypes());
        PlantType selectedType = farmZone.getPlantType();
        allTypes.stream().filter(type -> !type.equals(selectedType)).forEach(type -> disabledPlants.getItems().add(type));
    }

    private PlantTypeSelectList createList() {
        PlantTypeSelectList list = new PlantTypeSelectList(this);
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
        GameMvc.instance().model().get(ZonesContainer.class).deleteZone(farmZone);
    }

    public void close() {
        GameMvc.instance().getView().removeStage(getStage());
    }

    public NavigableList<PlantType> getDisabledPlants() {
        return disabledPlants;
    }
}
