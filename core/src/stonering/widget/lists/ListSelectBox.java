package stonering.widget.lists;

import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import java.util.List;

/**
 * {@link SelectBox} extention that can accept {@link List}.
 */
public class ListSelectBox<T> extends SelectBox<T> {

    public ListSelectBox(Skin skin) {
        super(skin);
    }

    public ListSelectBox(Skin skin, String styleName) {
        super(skin, styleName);
    }

    public ListSelectBox(SelectBoxStyle style) {
        super(style);
    }

    public void setItems(List<T> items) {
        super.setItems((T[]) items.toArray());
    }

    @Override
    public void showList() {
        super.showList();
        getList().toFront(); // ensures that list will be visible.
    }
}
