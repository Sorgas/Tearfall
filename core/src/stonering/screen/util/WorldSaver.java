package stonering.screen.util;

import stonering.entity.world.World;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Alexander Kuzyakov on 14.05.2017.
 */
public class WorldSaver {

    public World loadWorld(String name) {
        try {
            if (name == null) {
                return null;
            }
            File file = new File("saves/" + name + "/world.dat");
            FileInputStream fis = new FileInputStream(file.getPath());
            ObjectInputStream ois = new ObjectInputStream(fis);
            return (World) ois.readObject();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void saveWorld(World world) {
        try {
            File file = new File("saves/" + makeSaveName() + "/world.dat");
            file.getParentFile().mkdirs();
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file.getPath());
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(world);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String makeSaveName() {
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
