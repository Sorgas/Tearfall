package stonering.entity.item.selectors;

import stonering.enums.materials.MaterialMap;
import stonering.entity.item.Item;

import java.util.Objects;

/**
 * Selects item by their name and material of main part.
 * //TODO handle amount of item.
 *
 * @author Alexander Kuzyakov on 21.07.2018.
 */
public class SimpleItemSelector extends ItemSelector {
    public final String title;         // item type name
    private int material;
    private int amount;
    private String materialName;

    public SimpleItemSelector(String title, int material, int amount) {
        this.title = title;
        this.material = material;
        this.materialName = MaterialMap.instance().getMaterial(material).name;
        this.amount = amount;
    }

    public boolean checkItem(Item item) {
        return item.type.name.equals(title) && item.material == material;
    }

    @Override
    public String toString() {
        return  materialName + " " + title + " " + (amount != 0 ? amount : "");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SimpleItemSelector that = (SimpleItemSelector) o;
        return amount == that.amount &&
                title.equals(that.title) &&
                materialName.equals(that.materialName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, materialName, amount);
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
