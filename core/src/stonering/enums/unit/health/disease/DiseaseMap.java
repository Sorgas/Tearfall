package stonering.enums.unit.health.disease;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;

import stonering.enums.unit.health.HealthFunctionEnum;
import stonering.util.global.FileUtil;
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
        FileUtil.iterate(FileUtil.ITEMS_PATH, file -> {
            List<RawDiseaseType> elements = json.fromJson(ArrayList.class, RawDiseaseType.class, file);
            elements.stream()
                    .map(this::processDisease)
                    .forEach(disease -> diseases.put(disease.name, disease));
            Logger.LOADING.logDebug(elements.size() + " loaded from " + file.path());
        });
    }

    private DiseaseType processDisease(RawDiseaseType raw) {
        DiseaseType disease = new DiseaseType(raw);
        for (RawDiseaseStage rawStage : raw.stages) {
            DiseaseStage stage = new DiseaseStage(rawStage);
            for (String s : rawStage.effects) {
                String[] parts = s.split("/");
                if (parts.length != 2)
                    return Logger.LOADING.logError("Invalid disease effect string " + s + " in disease " + raw.name, null);
                try {
                    stage.effectsMap.put(HealthFunctionEnum.valueOf(parts[0]), Float.valueOf(parts[1]));
                } catch (NumberFormatException e) {
                    return Logger.LOADING.logError("Invalid health function delta number " + s + " in disease " + raw.name, null);
                } catch (IllegalArgumentException e) {
                    return Logger.LOADING.logError("Invalid health function name " + s + " in disease " + raw.name, null);
                }
            }
            disease.stages.put(stage.name, stage);
        }
        return disease;
    }

    public static DiseaseType get(String name) {
        return instance().diseases.get(name);
    }
}
