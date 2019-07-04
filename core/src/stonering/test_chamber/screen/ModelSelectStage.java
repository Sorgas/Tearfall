package stonering.test_chamber.screen;

import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import stonering.game.GameMvc;
import stonering.game.model.GameModel;
import stonering.game.view.render.stages.UiStage;
import stonering.game.view.render.ui.lists.PlaceHolderSelectBox;
import stonering.test_chamber.TestChamberGame;
import stonering.test_chamber.model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ModelSelectStage extends UiStage {
    private TestChamberGame testChamberGame;
    private PlaceHolderSelectBox<String> selectBox;
    private Map<String, Class> classMap;

    public ModelSelectStage(TestChamberGame testChamberGame) {
        this.testChamberGame = testChamberGame;
        fillModels();
        createStage();
    }

    private void createStage() {
        Container container = new Container();
        container.setFillParent(true);
        container.align(Align.topLeft);
        container.setActor(createTable());
        addActor(container);
        container.setDebug(true, true);
    }

    private Table createTable() {
        Table table = new Table();
        table.defaults().pad(10);
        table.setWidth(300);
        table.setHeight(300);
        table.add(selectBox = new PlaceHolderSelectBox<>("Select Model")).row();
        selectBox.setItems(new ArrayList<>(classMap.keySet()));
        selectBox.getSelection().setProgrammaticChangeEvents(false);
        selectBox.setSelectListener(e -> {
            switchToModel(selectBox.getSelected());
            return true;
        });
        setKeyboardFocus(selectBox);
        return table;
    }

    private void switchToModel(String name) {
        GameMvc gameMvc = GameMvc.createInstance(getInstance(name));
        gameMvc.createViewAndController();
        gameMvc.init();
        testChamberGame.setScreen(GameMvc.instance().getView());
        gameMvc.getModel().setPaused(false);
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
        classMap.put(SingleUnitModel.class.getSimpleName(), SingleUnitModel.class);
        classMap.put(FarmModel.class.getSimpleName(), FarmModel.class);
        classMap.put(LightingModel.class.getSimpleName(), LightingModel.class);
    }

    private GameModel getInstance(String name) {
        try {
            return (GameModel) classMap.get(name).newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }
}
