package stonering.enums.plants;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import stonering.enums.plants.raw.RawPlantTypeProcessor;
import stonering.enums.plants.raw.RawPlantType;
import stonering.exceptions.DescriptionNotFoundException;
import stonering.util.global.FileLoader;
import stonering.util.global.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Load all {@link PlantType}s from jsons, and inits them.
 */
public class PlantTypeMap {
    private static PlantTypeMap instance;
    private Map<String, PlantType> plantTypes;
    private Map<String, PlantType> treeTypes;
    private Map<String, PlantType> substrateTypes;
    private Map<String, PlantType> domesticTypes;
    private Json json;

    private PlantTypeMap() {
        plantTypes = new HashMap<>();
        treeTypes = new HashMap<>();
        substrateTypes = new HashMap<>();
        json = new Json();
        json.setOutputType(JsonWriter.OutputType.json);
        Logger.LOADING.log("plant types");
        //TODO add json validation
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
        List<RawPlantType> elements = json.fromJson(ArrayList.class, RawPlantType.class, FileLoader.getFile(filePath));
        RawPlantTypeProcessor processor = new RawPlantTypeProcessor();
        elements.forEach(rawType -> map.put(rawType.name, processor.processRawType(rawType)));
        Logger.LOADING.logDebug(map.keySet().size() + " loaded from " + filePath);
    }

    private void fillDomesticTypes() {
        domesticTypes = new HashMap<>();
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

    public Collection<PlantType> getDomesticTypes() {
        return domesticTypes.values();
    }

    public Map<String, PlantType> getPlantTypes() {
        return plantTypes;
    }

    public Map<String, PlantType> getTreeTypes() {
        return treeTypes;
    }

    public Map<String, PlantType> getSubstrateTypes() {
        return substrateTypes;
    }
}
