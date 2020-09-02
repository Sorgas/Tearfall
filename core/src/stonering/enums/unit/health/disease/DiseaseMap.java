package stonering.enums.unit.health.disease;

import java.util.*;
import java.util.stream.Collectors;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;

import stonering.enums.unit.health.CreatureAttributeEnum;
import stonering.enums.unit.health.HealthFunctionEnum;
import stonering.enums.unit.health.GameplayStatEnum;
import stonering.util.lang.FileUtil;
import stonering.util.logging.Logger;

/**
 * @author Alexander on 8/14/2020
 */
public class DiseaseMap {
    private static DiseaseMap instance;
    private final HashMap<String, DiseaseType> diseases;

    private DiseaseMap() {
        diseases = new HashMap<>();
        loadItemTypes();
    }

    public static DiseaseMap instance() {
        if (instance == null)
            instance = new DiseaseMap();
        return instance;
    }

    private void loadItemTypes() {
        Logger.LOADING.logDebug("loading diseases");
        Json json = new Json();
        json.setOutputType(JsonWriter.OutputType.json);
        FileUtil.iterate(FileUtil.DISEASES_PATH, file -> {
            List<RawDiseaseType> elements = json.fromJson(ArrayList.class, RawDiseaseType.class, file);
            elements.stream()
                    .map(this::processDisease)
                    .forEach(disease -> diseases.put(disease.name, disease));
            Logger.LOADING.logDebug(elements.size() + " loaded from " + file.path());
        });
    }

    private DiseaseType processDisease(RawDiseaseType raw) {
        DiseaseType diseaseType = new DiseaseType(raw);
        for (RawDiseaseStage rawStage : raw.stages) {
            DiseaseStage stage = new DiseaseStage(rawStage);
            for (String s : rawStage.effects) {
                String[] parts = s.split("/");
                if (parts.length != 2)
                    return Logger.LOADING.logError("Invalid disease effect string " + s + " in disease " + raw.name, null);
                try {
                    String name = parts[0];
                    Float delta = Float.valueOf(parts[1]);
                    Optional.ofNullable(HealthFunctionEnum.map.get(name)).ifPresent(function -> stage.functionEffects.put(function, delta));
                    Optional.ofNullable(CreatureAttributeEnum.map.get(name)).ifPresent(attribute -> stage.attributeEffects.put(attribute, delta.intValue()));
                    Optional.ofNullable(GameplayStatEnum.map.get(name)).ifPresent(property -> stage.statEffects.put(property, delta));
                } catch (NumberFormatException e) {
                    return Logger.LOADING.logError("Invalid health function delta number " + s + " in disease " + raw.name, null);
                }
            }
            diseaseType.stages.put(stage.name, stage);
        }
        List<DiseaseStage> sortedStages = diseaseType.stages.values().stream()
                .sorted(Comparator.comparingDouble(stage -> stage.range.min)) // sort stages by start
                .collect(Collectors.toList());
        for (int i = 0; i < sortedStages.size(); i++) {
            DiseaseStage stage = sortedStages.get(i);
            stage.range.max = i < sortedStages.size() - 1 ? sortedStages.get(i + 1).range.min : 1f; // complete ranges
        }
        return diseaseType;
    }

    public static DiseaseType get(String name) {
        return instance().diseases.get(name);
    }
}
