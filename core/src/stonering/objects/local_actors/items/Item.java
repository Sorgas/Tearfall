package stonering.objects.local_actors.items;

import stonering.global.utils.Position;
import stonering.objects.local_actors.AspectHolder;

/**
 * Created by Alexander on 09.12.2017.
 * <p>
 * In game item.
 */
public class Item extends AspectHolder {
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

    @Override
    public String toString() {
        return "title: " + title +
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
