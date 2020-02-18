package stonering.stage;

import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import stonering.stage.building.BuildingMaterialListMenu;

/**
 * Stage for displaying one ui window in the center of the screen. 
 * 
 * @author Alexander on 18.02.2020.
 */
public class SingleWindowStage<T extends Window> extends UiStage {
    private T menu;

    public SingleWindowStage(T menu) {
        this.menu = menu;
        Container<T> container = new Container<>();
        container.setActor(menu);
        container.setFillParent(true);
        addActor(container);
    }
}
