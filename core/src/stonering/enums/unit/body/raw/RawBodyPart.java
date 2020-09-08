package stonering.enums.unit.body.raw;

import java.util.ArrayList;
import java.util.List;

/**
 * Raw body part with strings red from body_templates.json.
 *
 * @author Alexander_Kuzyakov on 03.07.2019.
 */
public class RawBodyPart implements Cloneable {
    public String name;
    public String root;
    public boolean mirrored; // bi for left and right, quadro for front left, rear right, etc todo
    public final List<String> layers;
    public final List<String> internal;
    public final List<String> external;
    public final List<String> tags;

    // for json reader
    public RawBodyPart() {
        internal = new ArrayList<>();
        external = new ArrayList<>();
        tags = new ArrayList<>();
        layers = new ArrayList<>();
    }

    @Override
    protected Object clone() {
        RawBodyPart clone = new RawBodyPart();
        clone.name = name;
        clone.root = root;
        clone.mirrored = mirrored;
        clone.layers.addAll(layers);
        clone.tags.addAll(tags);
        clone.internal.addAll(internal);
        clone.external.addAll(external);
        return clone;
    }
}

