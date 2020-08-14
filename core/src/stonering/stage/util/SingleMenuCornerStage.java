package stonering.stage.util;

/**
 * Stage for showing menus in the right bottom of the screen.
 * 
 * @author Alexander on 14.08.2020.
 */

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;

public class SingleMenuCornerStage<T extends Actor> extends SingleActorStage<T>{

    public SingleMenuCornerStage(T actor, boolean interceptInput) {
        super(actor, interceptInput);
        container.align(Align.bottomRight);
    }
}
