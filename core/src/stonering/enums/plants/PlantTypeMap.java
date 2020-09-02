package stonering.enums.plants;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;

import stonering.enums.plants.raw.RawPlantTypeProcessor;
import stonering.enums.plants.raw.RawPlantType;
import stonering.util.lang.FileUtil;
import stonering.util.logging.Logger;

import java.util.*;

/**
 * Load all {@link PlantType}s from jsons.
 * //TODO add json validation
 */
public class PlantTypeMap {
    private static PlantTypeMap instance;
    public final Map<String, PlantType> plantTypes;
    public final Map<String, PlantType> treeTypes;
    public final Map<String, PlantType> substrateTypes;
    public final Map<String, PlantType> domesticTypes;
    private Json json;
    private RawPlantTypeProcessor processor = new RawPlantTypeProcessor();

    private PlantTypeMap() {
        Logger.LOADING.log("plant types");
        json = new Json();
        json.setOutputType(JsonWriter.OutputType.json);
        plantTypes = new HashMap<>();
        treeTypes = new HashMap<>();
        substrateTypes = new HashMap<>();
        domesticTypes = new HashMap<>();
                
        loadTypesFileToMap(FileUtil.PLANTS_PATH, plantTypes);
        loadTypesFileToMap(FileUtil.TREES_PATH, treeTypes);
        loadTypesFileToMap(FileUtil.SUBSTRATES_PATH, substrateTypes);
        fillDomesticTypes();
    }

    public static PlantTypeMap instance() {
        return instance == null ? instance = new PlantTypeMap() : instance;
    }

    /**
     * Loads {@link PlantType} from given file into given file.
     */
    private void loadTypesFileToMap(String filePath, Map<String, PlantType> map) {
        FileUtil.iterate(filePath, file -> {
            List<RawPlantType> elements = json.fromJson(ArrayList.class, RawPlantType.class, file);
            elements.stream()
                    .filter(type -> !type.lifeStages.isEmpty())
                    .map(processor::process)
                    .peek(type -> type.atlasName = file.nameWithoutExtension())
                    .forEach(type -> map.put(type.name, type));
        });
        Logger.LOADING.logDebug(map.keySet().size() + " loaded from " + filePath);
    }

    private void fillDomesticTypes() {
        plantTypes.values().stream().filter(type -> type.isPlant).forEach(type -> domesticTypes.put(type.name, type));
    }

    public static PlantType getPlantType(String specimen) {
        return instance().plantTypes.get(specimen);
    }

    public static PlantType getTreeType(String specimen) {
        return !instance().treeTypes.containsKey(specimen)
                ? Logger.LOADING.logError("Tree type with name " + specimen + " not found.", null)
                : instance().treeTypes.get(specimen);
    }

    public static PlantType getSubstrateType(String specimen) {
        return !instance().substrateTypes.containsKey(specimen)
                ? Logger.LOADING.logError("Substrate type with name " + specimen + " not found.", null)
                : instance().substrateTypes.get(specimen);
    }
}
