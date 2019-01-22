package stonering.test_chamber.screen.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import stonering.test_chamber.model.TestModel;
import stonering.util.global.StaticSkin;

public class TestSelectTable extends Table {
    private TestModel testModel;
    
    public TestSelectTable(TestModel testModel) {
        this.testModel = testModel;
        initTable();
    }

    private void initTable() {
        TextButton resetButton = new TextButton("reset", StaticSkin.getSkin());
//        resetButton.addListener(new ChangeListener() {
//            @Override
//            public void changed(ChangeEvent event, Actor actor) {
//                testModel.reset();
//            }
//        })
//        add(new TextButton())
    }
}
