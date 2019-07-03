package stonering.enums.unit;

import stonering.entity.local.unit.aspects.body.BodyAspect;
import stonering.entity.local.unit.aspects.body.Wound;
import stonering.enums.unit.raw.RawBodyPart;

import java.util.ArrayList;
import java.util.List;

/**
 * Class of creature body part. stored in {@link BodyTemplate}. {@link BodyAspect} does not store body parts.
 *
 * @author Alexander_Kuzyakov on 02.07.2019.
 */
public class BodyPart {
    public String name;
    public BodyPart root; // each body part points to one it`s connected to
    public String type; // determines wear items, that can be equipped.
    public List<String> layers; // tissue layers
    public List<String> external;
    public List<String> internal;
    public List<String> tags;

    public int size;
    public int weight; // TODO add calculation.

    public BodyPart() {
        internal = new ArrayList<>();
        external = new ArrayList<>();
        tags = new ArrayList<>();
        layers = new ArrayList<>();
    }

    public BodyPart(RawBodyPart rawBodyPart) {
        name = rawBodyPart.name;
        type = rawBodyPart.type;
        layers = rawBodyPart.layers;
        internal = rawBodyPart.internal;
        external = rawBodyPart.internal;
        tags = rawBodyPart.tags;
        size = rawBodyPart.size;
    }

    public BodyPart(String name) {
        this.name = name;
    }
}
