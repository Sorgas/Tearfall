package stonering.enums;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import stonering.utils.global.FileLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Alexander on 02.11.2018.
 */
public class WorkbenchesListMap {
    private static WorkbenchesListMap instance;
    private HashMap<String, List<String>> lists;

    private WorkbenchesListMap() {
        lists = new HashMap<>();
        loadLists();
    }

    public static WorkbenchesListMap getInstance() {
        if (instance == null)
            instance = new WorkbenchesListMap();
        return instance;
    }

    private void loadLists() {
        System.out.println("loading crafting lists");
        Json json = new Json();
        json.setOutputType(JsonWriter.OutputType.json);
        ArrayList<WorkbenchList> elements = json.fromJson(ArrayList.class, WorkbenchList.class, FileLoader.getItemsFile());
        for (WorkbenchList list : elements) {
            lists.put(list.getWorkbench(), list.getRecipes());
        }
    }

    private class WorkbenchList {
        private String workbench;
        private List<String> recipes;

        public String getWorkbench() {
            return workbench;
        }

        public void setWorkbench(String workbench) {
            this.workbench = workbench;
        }

        public List<String> getRecipes() {
            return recipes;
        }

        public void setRecipes(List<String> recipes) {
            this.recipes = recipes;
        }
    }

    public List<String> getRecipes(String workbench) {
        return lists.get(workbench);
    }

    public boolean listExists(String workbench) {
        return lists.keySet().contains(workbench);
    }
}
