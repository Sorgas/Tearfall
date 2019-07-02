package stonering.entity.local.item.selectors;

import stonering.enums.materials.MaterialMap;
import stonering.entity.local.item.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Selects item by their name and material.
 * //TODO handle amount of item.
 *
 * @author Alexander Kuzyakov on 21.07.2018.
 */
public class SimpleItemSelector extends ItemSelector {
    private String title;         // item type name
    private String material;      // material name
    private int materialId;
    private int amount;
    private String displayedTitle;// overrides toString if set

    public SimpleItemSelector(String title, String material, int amount) {
        super();
        this.title = title;
        setMaterial(material);
        this.amount = amount;
    }

    public SimpleItemSelector(String displayedTitle) {
        this.displayedTitle = displayedTitle;
    }

    @Override
    public boolean check(List<Item> items) {
        for (int i = 0; i < items.size(); i++) {
            if (checkItem(items.get(i)))
                return true;
        }
        return false;
    }

    @Override
    public List<Item> selectItems(List<Item> items) {
        ArrayList<Item> selectedItems = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            if (checkItem(items.get(i))) {
                selectedItems.add(items.get(i));
                return selectedItems;
            }
        }
        return selectedItems;
    }

    private boolean checkItem(Item item) {
        return item.getTitle().equals(title) && item.getMaterial() == materialId;
    }

    @Override
    public String toString() {
        if (displayedTitle != null) return displayedTitle;
        return  material + " " + title + " " + (amount != 0 ? amount : "");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SimpleItemSelector that = (SimpleItemSelector) o;
        return amount == that.amount &&
                title.equals(that.title) &&
                material.equals(that.material);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, material, amount);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
        materialId = MaterialMap.getInstance().getId(material);
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
