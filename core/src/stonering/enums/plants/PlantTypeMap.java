package stonering.enums.plants;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import stonering.enums.plants.raw.RawPlantTypeProcessor;
import stonering.enums.plants.raw.RawPlantType;
import stonering.exceptions.DescriptionNotFoundException;
import stonering.util.global.FileLoader;
import stonering.util.global.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private PlantTypeMap() {
        Logger.LOADING.log("plant types");
        json = new Json();
        json.setOutputType(JsonWriter.OutputType.json);
        plantTypes = new HashMap<>();
        treeTypes = new HashMap<>();
        substrateTypes = new HashMap<>();
        domesticTypes = new HashMap<>();
        loadTypesFileToMap(FileLoader.PLANTS_PATH, plantTypes);
        loadTypesFileToMap(FileLoader.TREES_PATH, treeTypes);
        loadTypesFileToMap(FileLoader.SUBSTRATES_PATH, substrateTypes);
        fillDomesticTypes();
    }

    public static PlantTypeMap getInstance() {
        if (instance == null)
            instance = new PlantTypeMap();
        return instance;
    }

    /**
     * Loads {@link PlantType} from given file into given file.
     */
    private void loadTypesFileToMap(String filePath, Map<String, PlantType> map) {
        List<RawPlantType> elements = json.fromJson(ArrayList.class, RawPlantType.class, FileLoader.get(filePath));
        RawPlantTypeProcessor processor = new RawPlantTypeProcessor();
        elements.forEach(rawType -> map.put(rawType.name, processor.processRawType(rawType)));
        Logger.LOADING.logDebug(map.keySet().size() + " loaded from " + filePath);
    }

    private void fillDomesticTypes() {
        plantTypes.values().stream().filter(type -> type.isPlant).forEach(type -> domesticTypes.put(type.name, type));
    }

    public PlantType getPlantType(String specimen) throws DescriptionNotFoundException {
        if (!plantTypes.containsKey(specimen))
            throw new DescriptionNotFoundException("Plant type with name " + specimen + " not found.");
        return plantTypes.get(specimen);
    }

    public PlantType getTreeType(String specimen) throws DescriptionNotFoundException {
        if (!treeTypes.containsKey(specimen))
            throw new DescriptionNotFoundException("Plant type with name " + specimen + " not found.");
        return treeTypes.get(specimen);
    }

    public PlantType getSubstrateType(String specimen) throws DescriptionNotFoundException {
        if (!substrateTypes.containsKey(specimen))
            throw new DescriptionNotFoundException("Plant type with name " + specimen + " not found.");
        return substrateTypes.get(specimen);
    }
}
