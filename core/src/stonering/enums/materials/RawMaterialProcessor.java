package stonering.enums.materials;

import java.util.Arrays;
import java.util.List;

import stonering.entity.Aspect;
import stonering.entity.material.Material;
import stonering.util.logging.Logger;
import stonering.util.lang.Pair;

/**
 * Processor for creating {@link Material}
 *
 * @author Alexander on 09.09.2019.
 */
public class RawMaterialProcessor {

    public Material process(int id, RawMaterial raw) {
        Material material = new Material(id, raw);
        return addAspectsFromRawType(material, raw);
    }

    private Material addAspectsFromRawType(Material material, RawMaterial raw) {
        raw.aspects.stream()
                .map(string -> createAspect(string, material))
                .forEach(material::add);
        return material;
    }

    private Aspect createAspect(String aspectString, Material material) {
        Pair<String, List<String>> pair = parseAspectString(aspectString);
        switch (pair.key) {
            case "ore"  : {

            }
            default:
                return Logger.LOADING.logWarn("Material aspect with name " + material.name, null);
        }
    }

    private Pair<String, List<String>> parseAspectString(String aspectString) {
        String[] aspectParts = aspectString.replace(")", "").split("\\(");
        return new Pair<>(aspectParts[0], aspectParts.length > 1 ? Arrays.asList(aspectParts[1].split(",")) : null);
    }
}
