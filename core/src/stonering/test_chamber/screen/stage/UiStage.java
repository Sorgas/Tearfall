package stonering.test_chamber.screen.stage;

import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import stonering.game.core.view.render.ui.lists.ListSelectBox;
import stonering.test_chamber.model.SingleTreeModel;
import stonering.test_chamber.model.TestModel;
import stonering.util.global.StaticSkin;

import java.util.ArrayList;
import java.util.List;

public class UiStage extends Stage {
    private ViewStage modelStage;
    private ListSelectBox<TestModel> selectBox;

    public UiStage() {
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
        table.add(selectBox = new ListSelectBox<>(StaticSkin.getSkin())).row();
        selectBox.setItems(fillModels());
        selectBox.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println(selectBox.getSelected().toString());
                super.clicked(event, x, y);
            }
        });
        table.add(new TextButton("asd", StaticSkin.getSkin()));
        return table;
    }

    private List<TestModel> fillModels() {
        List<TestModel> testModels = new ArrayList<>();
        testModels.add(new SingleTreeModel());
        return testModels;
    }

    @Override
    public void draw() {
        super.draw();
    }
}
