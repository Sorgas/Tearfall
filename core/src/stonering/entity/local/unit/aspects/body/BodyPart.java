package stonering.entity.local.unit.aspects.body;

import stonering.enums.unit.BodyTemplate;
import stonering.enums.unit.RawBodyTemplate;

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
    public String[] layers; // tissue layers
    public String[] organs;
    public String[] tags;
    public List<Wound> wounds;

    public int size;
    public int weight; // TODO add calculation.

    public BodyPart(RawBodyTemplate.RawBodyPart rawBodyPart) {
        name = rawBodyPart.name;
        type = rawBodyPart.type;
        layers =rawBodyPart.layers;
        organs = rawBodyPart.internal;
        tags = rawBodyPart.tags;
        size = rawBodyPart.size;
        wounds = new ArrayList<>();
    }

    public BodyPart(String name) {
        this.name = name;
    }

}
