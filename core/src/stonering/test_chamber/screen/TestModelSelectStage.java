package stonering.test_chamber.screen;

import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.utils.Align;
import stonering.game.GameMvc;
import stonering.game.model.GameModel;
import stonering.game.model.local_map.LocalMap;
import stonering.game.model.entity_selector.EntitySelectorSystem;
import stonering.stage.UiStage;
import stonering.widget.lists.PlaceHolderSelectBox;
import stonering.test_chamber.TestChamberGame;
import stonering.test_chamber.model.*;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TestModelSelectStage extends UiStage {
    private TestChamberGame testChamberGame;
    private PlaceHolderSelectBox<String> selectBox;
    private Map<String, Class<? extends GameModel>> classMap;

    public TestModelSelectStage(TestChamberGame testChamberGame) {
        this.testChamberGame = testChamberGame;
        fillModels();
        createStage();
    }

    private void createStage() {
        Container<PlaceHolderSelectBox> container = new Container<>();
        container.align(Align.topLeft);
        container.setActor(createTable());
        container.setFillParent(true);
        addActor(container);
        container.setDebug(true, true);
    }

    private PlaceHolderSelectBox<String> createTable() {
        selectBox = new PlaceHolderSelectBox<>("Select Model");
        selectBox.setItems(new ArrayList<>(classMap.keySet()));
        selectBox.getSelection().setProgrammaticChangeEvents(false);
        selectBox.setSelectListener(e -> {
            switchToModel(selectBox.getSelected());
            return true;
        });
        setKeyboardFocus(selectBox);
        return selectBox;
    }

    private void switchToModel(String name) {
        GameMvc gameMvc = GameMvc.createInstance(getInstance(name));
        gameMvc.createViewAndController();
        gameMvc.init();
        GameMvc.view().localWorldStage.getCamera().centerCameraToPosition(GameMvc.model().get(EntitySelectorSystem.class).selector.position.clone());
        testChamberGame.setScreen(GameMvc.view());
        GameMvc.model().get(LocalMap.class).initAreas();
        GameMvc.model().setPaused(false);
    }

    private GameModel getInstance(String name) {
        try {
            return classMap.get(name).getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void fillModels() {
        classMap = new HashMap<>();
        classMap.put(SingleTreeModel.class.getSimpleName(), SingleTreeModel.class);
        classMap.put(SingleTreeModel.class.getSimpleName(), SingleTreeModel.class);
        classMap.put(SinglePlantModel.class.getSimpleName(), SinglePlantModel.class);
        classMap.put(PondPlantsModel.class.getSimpleName(), PondPlantsModel.class);
        classMap.put(PassageModel.class.getSimpleName(), PassageModel.class);
        classMap.put(CameraModel.class.getSimpleName(), CameraModel.class);
        classMap.put(WorkbenchModel.class.getSimpleName(), WorkbenchModel.class);
        classMap.put(FarmModel.class.getSimpleName(), FarmModel.class);
        classMap.put(LightingModel.class.getSimpleName(), LightingModel.class);
        classMap.put(DiggingModel.class.getSimpleName(), DiggingModel.class);
        classMap.put(FurnitureModel.class.getSimpleName(), FurnitureModel.class);
        classMap.put(ConstructionBuildingPlaytestModel.class.getSimpleName(), ConstructionBuildingPlaytestModel.class);
    }
}