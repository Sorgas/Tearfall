package stonering.enums.unit.body;

import stonering.entity.unit.aspects.body.BodyAspect;
import stonering.enums.unit.body.raw.RawBodyPart;

import java.util.ArrayList;
import java.util.List;

/**
 * Class of creature body part. stored in {@link BodyTemplate}. {@link BodyAspect} does not store body parts.
 *
 * @author Alexander_Kuzyakov on 02.07.2019.
 */
public class BodyPart implements Cloneable {
    //determine wear items, that can be equipped (slot name = side + type)
    public String name; //

    public BodyPart root; // each body part points to one it`s connected to
    public final List<String> layers = new ArrayList<>(); // tissue layers
    public final List<String> external = new ArrayList<>();
    public final List<String> internal = new ArrayList<>();
    public final List<String> tags = new ArrayList<>();

    public BodyPart(RawBodyPart rawBodyPart) {
        name = rawBodyPart.name;
        layers.addAll(rawBodyPart.layers);
        internal.addAll(rawBodyPart.internal);
        external.addAll(rawBodyPart.internal);
        tags.addAll(rawBodyPart.tags);
    }

    public BodyPart() {}
    
    @Override
    public BodyPart clone() {
        BodyPart bodyPart = new BodyPart();
        bodyPart.name = name;
        bodyPart.root = root;
        bodyPart.tags.addAll(tags);
        bodyPart.layers.addAll(layers);
        bodyPart.internal.addAll(internal);
        bodyPart.external.addAll(external);
        return bodyPart;
    }
}
