package stonering.widget.util;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Widgets with this can be rescaled by global setting.
 *
 * @author Alexander_Kuzyakov on 16.08.2019.
 */
public interface Scalable {

    void rescale(float scale);

    default Vector2 getSize() {
        if (this instanceof Actor) {
            new Vector2(((Actor) this).getWidth(), ((Actor) this).getHeight());
        }
        return new Vector2(0, 0);
    }
}
