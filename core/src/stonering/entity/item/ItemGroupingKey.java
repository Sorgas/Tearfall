package stonering.entity.item;

import java.util.List;
import java.util.Objects;

/**
 * Contains item type and material. Used as map key, for grouping several items into one list.
 *
 * @author Alexander on 25.02.2020.
 */
public class ItemGroupingKey {
    public int material;
    public String type;

    public ItemGroupingKey(Item item) {
        this.material = item.material;
        this.type = item.type.name;
    }

    public ItemGroupingKey(List<Item> items) {
        this(items.get(0));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemGroupingKey that = (ItemGroupingKey) o;
        return material == that.material &&
                Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(material, type);
    }

    @Override
    public String toString() {
        return "ItemGroupingKey{" +
                "material=" + material +
                ", type='" + type + '\'' +
                '}';
    }
}
