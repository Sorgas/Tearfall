package stonering.test_chamber.screen;

import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.utils.Align;
import stonering.game.GameMvc;
import stonering.game.model.GameModel;
import stonering.game.model.local_map.LocalMap;
import stonering.game.model.entity_selector.EntitySelectorSystem;
import stonering.stage.util.UiStage;
import stonering.widget.lists.PlaceHolderSelectBox;
import stonering.test_chamber.TestChamberGame;
import stonering.test_chamber.model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class TestModelSelectStage extends UiStage {
    private TestChamberGame testChamberGame;
    private PlaceHolderSelectBox<String> selectBox;
    private Map<String, Supplier<GameModel>> classMap;

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
        GameMvc gameMvc = GameMvc.createInstance(classMap.get(name).get());
        gameMvc.createViewAndController();
        gameMvc.init();
        GameMvc.view().localWorldStage.getCamera().centerCameraToPosition(GameMvc.model().get(EntitySelectorSystem.class).selector.position.clone());
        testChamberGame.setScreen(GameMvc.view());
        GameMvc.model().get(LocalMap.class).initAreas();
        GameMvc.model().gameTime.setPaused(false);
    }

    private void fillModels() {
        classMap = new HashMap<>();
        classMap.put(SingleTreeModel.class.getSimpleName(), SingleTreeModel::new);
        classMap.put(SingleTreeModel.class.getSimpleName(), SingleTreeModel::new);
        classMap.put(SinglePlantModel.class.getSimpleName(), SinglePlantModel::new);
        classMap.put(PondPlantsModel.class.getSimpleName(), PondPlantsModel::new);
        classMap.put(PassageModel.class.getSimpleName(), PassageModel::new);
        classMap.put(CameraModel.class.getSimpleName(), CameraModel::new);
        classMap.put(WorkbenchModel.class.getSimpleName(), WorkbenchModel::new);
        classMap.put(FarmModel.class.getSimpleName(), FarmModel::new);
//        classMap.put(LightingModel.class.getSimpleName(), LightingModel::new);
        classMap.put(DiggingModel.class.getSimpleName(), DiggingModel::new);
//        classMap.put(FurnitureModel.class.getSimpleName(), FurnitureModel::new);
        classMap.put(ConstructionBuildingPlaytestModel.class.getSimpleName(), ConstructionBuildingPlaytestModel::new);
        classMap.put(MeltingOrePlayTestModel.class.getSimpleName(), MeltingOrePlayTestModel::new);
        classMap.put(FoodPlayTestModel.class.getSimpleName(), FoodPlayTestModel::new);
        classMap.put(LiquidFlowPlaytestModel.class.getSimpleName(), LiquidFlowPlaytestModel::new);
        classMap.put(DrinkingPlayTestModel.class.getSimpleName(), DrinkingPlayTestModel::new);
        classMap.put(AttackPlayTestModel.class.getSimpleName(), AttackPlayTestModel::new);
    }
}
