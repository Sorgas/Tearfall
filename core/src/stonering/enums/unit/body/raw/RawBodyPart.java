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
    public String type;
    public String root;
    public String mirrored; // bi for left and right, quadro for front left, rear right, etc
    public List<String> layers;
    public List<String> internal;
    public List<String> external;
    public List<String> tags;

    // for json reader
    public RawBodyPart() {
        internal = new ArrayList<>();
        external = new ArrayList<>();
        tags = new ArrayList<>();
        layers = new ArrayList<>();
    }

    public RawBodyPart(RawBodyPart rawBodyPart) {
        type = rawBodyPart.type;
        root = rawBodyPart.root;
        internal = rawBodyPart.internal;
        external = rawBodyPart.external;
        tags = rawBodyPart.tags;
        layers = rawBodyPart.layers;
        mirrored = rawBodyPart.mirrored;
    }

    @Override
    protected Object clone() {
        return new RawBodyPart(this);
    }
}

