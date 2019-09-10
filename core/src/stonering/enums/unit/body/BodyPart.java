package stonering.enums.unit.body;

import stonering.entity.unit.aspects.body.BodyAspect;
import stonering.enums.unit.body.raw.RawBodyPart;

import java.util.List;

/**
 * Class of creature body part. stored in {@link BodyTemplate}. {@link BodyAspect} does not store body parts.
 *
 * @author Alexander_Kuzyakov on 02.07.2019.
 */
public class BodyPart {
    //determine wear items, that can be equipped (slot name = side + type)
    public final String name; //

    public BodyPart root; // each body part points to one it`s connected to
    public List<String> layers; // tissue layers
    public List<String> external;
    public List<String> internal;
    public List<String> tags;

    public BodyPart(RawBodyPart rawBodyPart) {
        name = rawBodyPart.name;
        layers = rawBodyPart.layers;
        internal = rawBodyPart.internal;
        external = rawBodyPart.internal;
        tags = rawBodyPart.tags;
    }
}
