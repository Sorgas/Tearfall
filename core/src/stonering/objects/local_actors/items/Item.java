package stonering.objects.local_actors.items;

import stonering.enums.items.ItemTypesEnum;
import stonering.global.utils.Position;
import stonering.objects.local_actors.AspectHolder;

/**
 * Created by Alexander on 09.12.2017.
 * <p>
 * In game item.
 */
public class Item extends AspectHolder {
    private ItemTypesEnum type;
    private String title;
    private int material;
    private int weight;
    private int volume;

    public Item(Position position) {
        super(position);
    }

    public void turn() {

    }

    public int getMaterial() {
        return material;
    }

    public void setMaterial(int material) {
        this.material = material;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public ItemTypesEnum getType() {
        return type;
    }

    public void setType(ItemTypesEnum type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "title: " + title +
                "type: " + type +
                "position: " + position +
                "material: " + material +
                "weight: " + weight +
                "volume: " + volume;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
