package stonering.util.saving;


import stonering.game.GameMvc;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

public class GameSaver {

    public static void loadGame() {
        //TODO
    }

    public static void saveGame() {
        try {
            File file = new File("saves/" + makeSaveName() + "/game.dat");
            file.getParentFile().mkdirs();
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file.getPath());
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(GameMvc.model());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String makeSaveName() {
        File root = new File("saves");
        if (!root.exists()) {
            root.mkdirs();
        }
        Set<String> set = new HashSet<>();
        for (File file : root.listFiles()) {
            if (file.getName().startsWith("save") && !file.getName().contains(".")) {
                set.add(file.getName());
            }
        }
        int i = 1;
        while (set.contains("save" + i)) {
            i++;
        }
        return "save" + i;
    }
}
