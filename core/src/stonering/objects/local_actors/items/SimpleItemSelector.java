package stonering.objects.local_actors.items;

import stonering.enums.materials.MaterialMap;

import java.util.ArrayList;

/**
 * @author Alexander on 21.07.2018.
 */
public class SimpleItemSelector extends ItemSelector {
    private String title;
    private int material;
    private int amount;

    public SimpleItemSelector(String title, int material, int amount) {
        super();
        this.title = title;
        this.material = material;
        this.amount = amount;
    }

    public SimpleItemSelector(String title, String material, int amount) {
        super();
        this.title = title;
        setMaterial(material);
        this.amount = amount;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getMaterial() {
        return material;
    }

    public void setMaterial(int material) {
        this.material = material;
    }

    public void setMaterial(String material) {
        this.material = MaterialMap.getInstance().getId(material);
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public boolean check(ArrayList<Item> items) {
        for (int i = 0; i < items.size(); i++) {
            if (checkItem(items.get(i)))
                return true;
        }
        return false;
    }

    private boolean checkItem(Item item) {
        return item.getTitle().equals(title) && item.getMaterial() == material;
    }

    @Override
    public ArrayList<Item> selectItems(ArrayList<Item> items) {
        for (int i = 0; i < items.size(); i++) {
            if (checkItem(items.get(i))) {
                ArrayList<Item> selectedItems = new ArrayList<>();
                selectedItems.add(items.get(i));
                return selectedItems;
            }
        }
        return null;
    }
}