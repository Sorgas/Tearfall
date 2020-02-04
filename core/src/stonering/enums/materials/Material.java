package stonering.enums.materials;

import com.badlogic.gdx.graphics.Color;
import stonering.enums.items.ItemTagEnum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Descriptor of material. Tags are copied to item on creation.
 *
 * @author Alexander Kuzyakov on 04.06.2017.
 */

public class Material {
    public final int id;
    public final String name;
    public final List<ItemTagEnum> tags;
    public final float density;
    public final HashMap<String, ArrayList<Object>> reactions; // other aspects
    public final int value;
    public final byte atlasY;
    public final Color color;

    public Material(RawMaterial raw) {
        id = raw.id;
        name = raw.name;
        tags = raw.tags.stream().map(ItemTagEnum::get).collect(Collectors.toList());
        density = raw.density;
        reactions = raw.reactions;
        value = raw.value;
        atlasY = raw.atlasY;
        color = Color.valueOf(raw.color);
    }

    @Override
    public String toString() {
        return "material[" + name + "]";
    }
}