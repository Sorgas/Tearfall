package stonering.entity.material;

import com.badlogic.gdx.graphics.Color;

import stonering.entity.Entity;
import stonering.enums.items.ItemTagEnum;
import stonering.enums.materials.RawMaterial;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Descriptor of material. Tags are copied to item on creation.
 * Ids of materials are set on loading.
 *
 * @author Alexander Kuzyakov on 04.06.2017.
 */

public class Material extends Entity {
    public final int id;
    public final String name;
    public final List<ItemTagEnum> tags;
    public final float density;
    public final HashMap<String, ArrayList<String>> reactions; // other aspects
    public final int value;
    public final byte atlasY;
    public final Color color;
    public final float workAmountModifier; // changes time of building and crafting
    
    public Material(int id, RawMaterial raw) {
        super();
        this.id = id;
        name = raw.name;
        tags = raw.tags.stream()
                .map(ItemTagEnum::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        density = raw.density;
        reactions = raw.reactions;
        value = raw.value;
        atlasY = raw.atlasY;
        color = Color.valueOf(raw.color);
        workAmountModifier = raw.workAmountModifier;
    }

    @Override
    public String toString() {
        return "material[" + name + "]";
    }
}