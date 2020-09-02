package stonering.util.lang;

import com.badlogic.gdx.utils.Array;

import java.util.List;

/**
 * Provides constructor from {@link List}.
 *
 * @author Alexander on 07.02.2019.
 */
public class CompatibleArray<T> extends Array<T> {

    public CompatibleArray() {
        super();
    }

    public CompatibleArray(List<T> list) {
        super();
        addAll((T[]) list.toArray());
    }
}
