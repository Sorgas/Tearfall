package stonering.stage.workbench;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import stonering.entity.item.aspects.ItemContainerAspect;
import stonering.util.global.StaticSkin;

/**
 * Menu for observing items stored in {@link ItemContainerAspect}.
 *
 * @author Alexander on 5/13/2020
 */
public class ContainerMenu extends Table {

    public ContainerMenu(ItemContainerAspect containerAspect) {
        add(new Label("This is container menu.", StaticSkin.getSkin()));
    }
}

